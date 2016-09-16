package com.kikr.fragment;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.dialog.ColourDialog;
import com.kikr.dialog.DialogCallback;
import com.kikr.dialog.FitDialog;
import com.kikr.dialog.OptionDialog;
import com.kikr.dialog.ProductMainOptionDialog;
import com.kikr.dialog.QuantityDialog;
import com.kikr.dialog.RemoveProductFromCartDialog;
import com.kikr.dialog.SizeDialog;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CallBack;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.CartApi;
import com.kikrlib.api.TwoTapApi;
import com.kikrlib.api.UpdateCartProductApi;
import com.kikrlib.api.UpdateProductInCartApi;
import com.kikrlib.bean.Product;
import com.kikrlib.bean.ProductChildOption;
import com.kikrlib.bean.ProductMainOption;
import com.kikrlib.bean.ProductRequiredOption;
import com.kikrlib.bean.TwoTapProductDetails;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.CartRes;
import com.kikrlib.service.res.CommonRes;
import com.kikrlib.service.res.UpdateProductInCartRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.StringUtils;
import com.kikrlib.utils.Syso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FragmentEditPurchaseItem extends BaseFragment implements OnClickListener, DialogCallback {
    private View mainView;
    private ImageView productImage, sizeArrow, colorArrow;
    private TextView productName, viewDetailsText, removeFromCartText;
    private Product product;
    private LinearLayout productSize, productColor, productQuantity, productOption, productFit;
    private ProgressBarDialog mProgressBarDialog;
    private FragmentEditPurchaseItem editPurchaseItem;
    private TextView colorText, sizeText, quantityText, optionText, fitText;
    private QuantityDialog quantityDialog;
    private ColourDialog colourDialog;
    private SizeDialog sizeDialog;
    private OptionDialog optionDialog;
    private FitDialog fitDialog;
    FragmentUserCart fragmentUserCart;
    View sizeView, colorView;

    enum TYPE {Quantity, Size, Color, option, fit}

    ;
    TYPE type;
    boolean isProductUpdated = false;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int i = 0;
    private String cartid;
    private List<String> optionList = new ArrayList<String>();
    private List<String> fitList = new ArrayList<String>();
    private List<String> colors = new ArrayList<String>();
    private List<String> sizearray = new ArrayList<String>();
    private List<String> sizeForSelectedColor = new ArrayList<String>();
    private List<String> colorForSelectedOption = new ArrayList<String>();
    private List<JSONArray> sizes = new ArrayList<JSONArray>();
    private List<JSONArray> colorsArray = new ArrayList<JSONArray>();
    private View optionView, fitView;
    private TwoTapProductDetails twoTapProductDetails;
    private boolean isoption = false, isfit = false, issize = false, iscolor = false;
    private LinearLayout ok_layout;
    private TextView doneBtn;
    private TextView kikr_learn_more;
    private FragmentDiscoverDetail discoverDetail;
    private LinearLayout optionMainLayout;
    List<ProductRequiredOption> selectedList = new ArrayList<ProductRequiredOption>();
    boolean isReloadData = true, isFromBuy;
    boolean isFromArcMenu = false;

    public FragmentEditPurchaseItem(Product productList, FragmentUserCart fragmentUserCart) {
        this.product = productList;
        this.fragmentUserCart = fragmentUserCart;
        isFromArcMenu = false;
    }

    public FragmentEditPurchaseItem(Product productList, FragmentDiscoverDetail discoverDetail, boolean isFromBuy) {
        this.product = productList;
        this.discoverDetail = discoverDetail;
        this.isFromBuy = isFromBuy;
        isFromArcMenu = false;
    }

    public FragmentEditPurchaseItem(Product productList, boolean isFromBuy) {
        this.product = productList;

        this.isFromArcMenu = isFromBuy;
    }

    public FragmentEditPurchaseItem(FragmentUserCart fragmentUserCart, TwoTapProductDetails twoTapProductDetails, Product productList) {
        this.twoTapProductDetails = twoTapProductDetails;
        this.product = productList;
        this.fragmentUserCart = fragmentUserCart;
        isFromArcMenu = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_edit_purchase_item, null);
        editPurchaseItem = this;
        return mainView;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        productImage = (ImageView) mainView.findViewById(R.id.productImage);
        sizeArrow = (ImageView) mainView.findViewById(R.id.sizeArrow);
        colorArrow = (ImageView) mainView.findViewById(R.id.colorArrow);
        productSize = (LinearLayout) mainView.findViewById(R.id.productSize);
        productColor = (LinearLayout) mainView.findViewById(R.id.productColor);
        productQuantity = (LinearLayout) mainView.findViewById(R.id.productQuantity);
        productOption = (LinearLayout) mainView.findViewById(R.id.productOption);
        productFit = (LinearLayout) mainView.findViewById(R.id.productFit);
        productName = (TextView) mainView.findViewById(R.id.productName);
        viewDetailsText = (TextView) mainView.findViewById(R.id.viewDetailsText);
        removeFromCartText = (TextView) mainView.findViewById(R.id.removeFromCartText);
        colorText = (TextView) mainView.findViewById(R.id.colorText);
        sizeText = (TextView) mainView.findViewById(R.id.sizeText);
        quantityText = (TextView) mainView.findViewById(R.id.quantityText);
        fitText = (TextView) mainView.findViewById(R.id.fitText);
        optionText = (TextView) mainView.findViewById(R.id.optionText);
        sizeView = (View) mainView.findViewById(R.id.sizeView);
        colorView = (View) mainView.findViewById(R.id.colorView);
        optionView = (View) mainView.findViewById(R.id.optionView);
        fitView = (View) mainView.findViewById(R.id.fitView);
        doneBtn = (TextView) mainView.findViewById(R.id.doneBtn);
        ok_layout = (LinearLayout) mainView.findViewById(R.id.ok_layout);
        optionMainLayout = (LinearLayout) mainView.findViewById(R.id.optionMainLayout);
        kikr_learn_more = (TextView) mainView.findViewById(R.id.kikr_learn_more);
        kikr_learn_more.setPaintFlags(kikr_learn_more.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        viewDetailsText.setOnClickListener(this);
        removeFromCartText.setOnClickListener(this);
        productColor.setOnClickListener(this);
        productSize.setOnClickListener(this);
        productQuantity.setOnClickListener(this);
        productFit.setOnClickListener(this);
        doneBtn.setOnClickListener(this);
        productOption.setOnClickListener(this);
        kikr_learn_more.setOnClickListener(this);
    }

    @Override
    public void setData(Bundle bundle) {
        setDaynamicData();

        productName.setText(product.getProductname());
        CommonUtility.setImage(mContext, product.getProductimageurl(), productImage, R.drawable.dum_list_item_product);
    }

    private void setDaynamicData() {
        if (product != null) {
            List<ProductMainOption> mainOptions = product.getProductMainOptionList();
            setNewOption(mainOptions);
        }
    }

    private void setNewOption(List<ProductMainOption> mainOptions) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        List<String> nameList;
        for (int i = 0; i < mainOptions.size(); i++) {
            ProductMainOption mainOption = mainOptions.get(i);
            String name = mainOptions.get(i).getName();
            View v = inflater.inflate(R.layout.edit_product_item, null);
            TextView nameTextView = (TextView) v.findViewById(R.id.optionName);
            final TextView optionTextValue = (TextView) v.findViewById(R.id.optionText);
            nameTextView.setText(StringUtils.FirstLetterInUpperCase(name));
            LinearLayout productOptionLayout = (LinearLayout) v.findViewById(R.id.productOptionLayout);
            productOptionLayout.setTag(mainOptions.get(i));
            productOptionLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    isReloadData = false;
                    ProductMainOption mainOption = (ProductMainOption) v.getTag();
                    ProductMainOptionDialog optionDialog = new ProductMainOptionDialog(mContext, editPurchaseItem, mainOption, product.getOption(), optionTextValue);
                    optionDialog.show();
                }
            });
            nameList = getNameList(mainOptions);
            Syso.info("List>>>>>>>" + nameList);
            for (int k = 0; k < nameList.size(); k++) {
                for (int j = 0; j < optionMainLayout.getChildCount(); j++) {
                    TextView textView = (TextView) optionMainLayout.getChildAt(j).findViewById(R.id.optionName);
                    String textName = textView.getText().toString();
                    Syso.info("Name>>>>>>>" + textName);
                    if (textName.equalsIgnoreCase(nameList.get(k))) {
                        optionMainLayout.removeViewAt(j);
                    }
                }
            }
            optionMainLayout.addView(v);
            if (isReloadData) {
                if (product.getSelected_values() != null) {
                    for (int j = 0; j < product.getSelected_values().size(); j++) {
                        if (product.getSelected_values().get(j).getName().equalsIgnoreCase(name)) {
                            String value = product.getSelected_values().get(j).getValue();
                            optionTextValue.setText(value);
                            for (int k = 0; k < mainOption.getOptionList().size(); k++) {
                                if (mainOption.getOptionList().get(k).getText().equalsIgnoreCase(value)) {
                                    addOption(mainOption.getOptionList().get(k));
                                }
                            }
                        }
                    }
                }
            }
        }
        checkButtonLayout();
    }

    private void checkButtonLayout() {
        if (validateOption(false)) {
            ok_layout.setBackgroundColor(getResources().getColor(R.color.header_background));
        } else {
            ok_layout.setBackgroundColor(getResources().getColor(R.color.gray));
        }
    }

    private List<String> getNameList(List<ProductMainOption> mainOptions) {
        List<String> nameList = new ArrayList<String>();
        for (int i = 0; i < mainOptions.size(); i++) {
            String finalName = mainOptions.get(i).getName();
            if (!nameList.contains(finalName))
                nameList.add(finalName);
            for (int j = 0; j < mainOptions.get(i).getOptionList().size(); j++) {
                List<ProductMainOption> list = mainOptions.get(i).getOptionList().get(j).getDep();
                List<String> nameNew = getNameList(list);
                for (int m = 0; m < nameNew.size(); m++) {
                    if (!nameList.contains(nameNew.get(m)))
                        nameList.add(nameNew.get(m));
                }
            }
        }
        return nameList;
    }

    private void getCartId() {
        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();
        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("success:   " + object);
                try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    cartid = (String) jsonObject.get("cart_id");
                    runnable = new Runnable() {

                        @Override
                        public void run() {
                            if (i <= 1) {
                                getStatus(cartid);
                            }
                        }
                    };
                    handler.postDelayed(runnable, 3000);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {

            }
        });
        List<String> products = new ArrayList<String>();
        products.add(product.getProducturl());
        twoTapApi.getCartId(products);
        twoTapApi.execute();
    }

    private void getStatus(String cart_id) {
        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("success:   " + object);
                getData(object);
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                mProgressBarDialog.dismiss();
            }
        });
        twoTapApi.getCartStatus(cart_id);
        twoTapApi.execute();
    }

    protected void getData(Object object) {
        try {
            JSONObject jsonObject = new JSONObject(object.toString());
            JSONObject add_to_cart = null;
            JSONObject sites = jsonObject.getJSONObject("sites");
            Iterator keys = sites.keys();

            while (keys.hasNext()) {
                // loop to get the dynamic key
                String currentDynamicKey = (String) keys.next();

                // get the value of the dynamic key
                JSONObject currentDynamicValue = sites.getJSONObject(currentDynamicKey);

                // do something here with the value...
                try {
                    add_to_cart = currentDynamicValue.getJSONObject("add_to_cart");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Iterator data = add_to_cart.keys();
                while (data.hasNext()) {
                    // loop to get the dynamic key
                    String currentKey = (String) data.next();
                    Syso.info("currentKey   " + currentKey);

                    // get the value of the dynamic key
                    JSONObject currentValue = add_to_cart.getJSONObject(currentKey);
                    Syso.info("currentValue   " + currentValue);
                    // do something here with the value...
                    JSONObject required_field_values = currentValue.getJSONObject("required_field_values");
                    Syso.info("required_field_values  " + required_field_values);
                    if (required_field_values != null) {
                        try {
                            JSONArray color = required_field_values
                                    .getJSONArray("color");
                            colors.clear();
                            if (color != null) {
                                for (int i = 0; i < color.length(); i++) {
                                    colors.add(color.getJSONObject(i).getString(
                                            "text"));
                                }
                                for (int i = 0; i < colors.size(); i++) {
                                    sizes.add(color.getJSONObject(i)
                                            .getJSONObject("dep")
                                            .getJSONArray("size"));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (colors.size() == 0 && required_field_values != null) {
                                JSONArray size = required_field_values
                                        .getJSONArray("size");
                                sizearray.clear();
                                if (size != null) {
                                    for (int i = 0; i < size.length(); i++) {
                                        sizearray.add(size.getJSONObject(i)
                                                .getString("text"));
                                    }
                                }
                            }
                        } catch (Exception e2) {
                            // TODO Auto-generated catch block
                            e2.printStackTrace();
                        }
                        try {
                            if (colors.size() == 0 && sizearray.size() == 0 && required_field_values != null) {
                                JSONArray option = required_field_values
                                        .getJSONArray("option");
                                optionList.clear();
                                colorsArray.clear();
                                if (option != null) {
                                    for (int i = 0; i < option.length(); i++) {
                                        optionList.add(option.getJSONObject(i)
                                                .getString("text"));
                                    }
                                    for (int k = 0; k < optionList.size(); k++) {
                                        colorsArray.add(option.getJSONObject(k)
                                                .getJSONObject("dep")
                                                .getJSONArray("color"));
                                    }
                                }
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        try {
                            if (colors.size() == 0 && sizearray.size() == 0 && optionList.size() == 0 && required_field_values != null) {
                                JSONArray fit = required_field_values
                                        .getJSONArray("fit");
                                fitList.clear();
                                if (fit != null) {
                                    for (int i = 0; i < fit.length(); i++) {
                                        fitList.add(fit.getJSONObject(i)
                                                .getString("text"));
                                    }
                                    List<JSONArray> color = new ArrayList<JSONArray>();
                                    for (int i = 0; i < fit.length(); i++) {
                                        color.add(fit.getJSONObject(i)
                                                .getJSONObject("dep")
                                                .getJSONArray("color"));
                                    }
                                    for (int i = 0; i < color.size(); i++) {
                                        colors.add(color.get(i).getJSONObject(i).getString("text"));
                                        sizes.add(color.get(i).getJSONObject(i)
                                                .getJSONObject("dep")
                                                .getJSONArray("size"));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setData();
    }

    private void setData() {
        if (colors.size() == 0 && i == 0 && sizearray.size() == 0 && optionList.size() == 0 && fitList.size() == 0) {
            i = 1;
            handler.postDelayed(runnable, 3000);
        } else {
            mProgressBarDialog.dismiss();
            if (colors.size() > 0) {
                setSizeForSelectedColor(0);
            }
            if (optionList.size() > 0) {
                setColorForSelectedOption(0);
            }
            if (optionList.size() > 0) {
                productOption.setVisibility(View.VISIBLE);
                optionView.setVisibility(View.VISIBLE);
//			optionText.setText(optionList.get(0));
//			type=TYPE.option;
//			updateProductValue(optionList.get(0));
            }
            if (fitList.size() > 0) {
                productFit.setVisibility(View.VISIBLE);
                fitView.setVisibility(View.VISIBLE);
//			fitText.setText(fitList.get(0));
//			type=TYPE.fit;
//			updateProductValue(fitList.get(0));
            }
            if (colors.size() == 0 && colorsArray.size() == 0) {
                productColor.setVisibility(View.GONE);
                colorView.setVisibility(View.GONE);
            } else {
                productColor.setVisibility(View.VISIBLE);
                colorView.setVisibility(View.VISIBLE);
//			colorText.setText(colors.get(0));
//			type=TYPE.Color;
//			updateValue(colors.get(0));
            }
            if (sizeForSelectedColor.size() == 0 && sizearray.size() == 0) {
                productSize.setVisibility(View.GONE);
            } else {
                productSize.setVisibility(View.VISIBLE);
                sizeView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setSizeForSelectedColor(int position) {
        sizeForSelectedColor.clear();
        if (twoTapProductDetails != null && twoTapProductDetails.getSizeArray() != null && twoTapProductDetails.getSizeArray().size() > position) {
            for (int i = 0; i < twoTapProductDetails.getSizeArray().get(position).length(); i++) {
                try {
                    sizeForSelectedColor.add(twoTapProductDetails.getSizeArray().get(position).getJSONObject(i).getString("text"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (sizes != null && sizes.size() > position) {
            for (int i = 0; i < sizes.get(position).length(); i++) {
                try {
                    sizeForSelectedColor.add(sizes.get(position).getJSONObject(i).getString("text"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setColorForSelectedOption(int position) {
        colorForSelectedOption.clear();
        if (twoTapProductDetails != null && twoTapProductDetails.getColorsArray() != null && twoTapProductDetails.getColorsArray().size() > position) {
            for (int i = 0; i < twoTapProductDetails.getColorsArray().get(position).length(); i++) {
                try {
                    colorForSelectedOption.add(twoTapProductDetails.getColorsArray().get(position).getJSONObject(i).getString("text"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (colorsArray != null && colorsArray.size() > position) {
            for (int i = 0; i < colorsArray.get(position).length(); i++) {
                try {
                    colorForSelectedOption.add(colorsArray.get(position).getJSONObject(i).getString("text"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewDetailsText:
                addFragment(new FragmentProductDetailWebView(product.getProducturl(), product));
                break;
            case R.id.removeFromCartText:
                RemoveProductFromCartDialog removeProductFromCartDialog = new RemoveProductFromCartDialog(mContext, product.getProductcart_id(), fragmentUserCart, new CallBack() {

                    @Override
                    public void onSuccess() {
                        fragmentUserCart.getCartList();
                        ((HomeActivity) mContext).onBackPressed();
                        UserPreference.getInstance().decCartCount();
                        ((HomeActivity) mContext).refreshCartCount();

                    }

                    @Override
                    public void onFail() {
                        // TODO Auto-generated method stub

                    }
                });
                removeProductFromCartDialog.show();
                break;
            case R.id.productFit:
                if (twoTapProductDetails != null) {
                    fitDialog = new FitDialog(mContext, editPurchaseItem, twoTapProductDetails.getFitList(), product.getFit());
                    fitDialog.show();
                } else {
                    fitDialog = new FitDialog(mContext, editPurchaseItem, fitList, product.getFit());
                    fitDialog.show();
                }
                break;
            case R.id.productOption:
                if (twoTapProductDetails != null) {
                    optionDialog = new OptionDialog(mContext, editPurchaseItem, twoTapProductDetails.getOptionList(), product.getOption());
                    optionDialog.show();
                } else {
                    optionDialog = new OptionDialog(mContext, editPurchaseItem, optionList, product.getOption());
                    optionDialog.show();
                }
                break;
            case R.id.productColor:
                if (colorForSelectedOption.size() > 0) {
                    colourDialog = new ColourDialog(mContext, editPurchaseItem, colorForSelectedOption, product.getSelected_color());
                    colourDialog.show();
                } else if (twoTapProductDetails != null) {
                    colourDialog = new ColourDialog(mContext, editPurchaseItem, twoTapProductDetails.getColorList(), product.getSelected_color());
                    colourDialog.show();
                } else {
                    colourDialog = new ColourDialog(mContext, editPurchaseItem, colors, product.getSelected_color());
                    colourDialog.show();
                }
                break;
            case R.id.productQuantity:
                quantityDialog = new QuantityDialog(mContext, editPurchaseItem, product, product.getQuantity());
                quantityDialog.show();
                break;
            case R.id.productSize:
                if (sizeForSelectedColor.size() > 0) {
                    sizeDialog = new SizeDialog(mContext, editPurchaseItem, sizeForSelectedColor, product.getSelected_size());
                    sizeDialog.show();
                } else {
                    if (twoTapProductDetails != null) {
                        sizeDialog = new SizeDialog(mContext, editPurchaseItem, twoTapProductDetails.getSizeList(), product.getSelected_size());
                        sizeDialog.show();
                    } else {
                        sizeDialog = new SizeDialog(mContext, editPurchaseItem, sizearray, product.getSelected_size());
                        sizeDialog.show();
                    }
                }
                break;
            case R.id.kikr_learn_more:
                this.addFragment(new FragmentLearnMoreOutsideUS());
                break;
            case R.id.doneBtn:
                if (validateOption(true)) {
                    if (selectedList.size() > 0) {
                        if (checkInternet()) {
//						callWebservicehere
                            if (discoverDetail != null) {
                                product.setSelected_values(selectedList);

                                discoverDetail.addToCartFromEdit(isFromBuy);

                                ((HomeActivity) mContext).onBackPressed();
                            } else if (isFromArcMenu) {
                                product.setSelected_values(selectedList);

                                ((HomeActivity) mContext).addProductToCart(product);
                                ((HomeActivity) mContext).onBackPressed();
                            } else {
                                Syso.print("Selected list>>>" + selectedList);
                                updateProductOptionValue();
                            }
                        }
                    } else {
                        ((HomeActivity) mContext).onBackPressed();
                    }

                }
//			if (!iscolor && !issize && !isfit && !isoption) {
//				((HomeActivity)mContext).onBackPressed();
//			}else if (iscolor) {
//				AlertUtils.showToast(mContext, "Please select Color");
//			}else if (issize) {
//				AlertUtils.showToast(mContext, "Please select Size");
//			}else if (isoption) {
//				AlertUtils.showToast(mContext, "Please select Option");
//			}else if (isfit) {
//				AlertUtils.showToast(mContext, "Please select Fit");
//			}
        }
    }

    private boolean validateOption(boolean showMessage) {
        selectedList.clear();
        for (int i = 0; i < optionMainLayout.getChildCount(); i++) {
            View v = optionMainLayout.getChildAt(i);
            TextView nameTextView = (TextView) v.findViewById(R.id.optionName);
            TextView optionTextValue = (TextView) v.findViewById(R.id.optionText);
            String name = nameTextView.getText().toString().trim();
            String value = optionTextValue.getText().toString().trim();
            if (TextUtils.isEmpty(value)) {
                if (showMessage)
                    AlertUtils.showToast(mContext, "Please select " + name);
                return false;
            } else {
                ProductRequiredOption option = new ProductRequiredOption();
                option.setName(name.toLowerCase());
                option.setValue(value);
                selectedList.add(option);
            }
        }
        return true;
    }

    public void setColor(String color, int position) {
        type = TYPE.Color;
//		colorText.setText(color);
        colourDialog.dismiss();
        updateProductValue(color);
        setSizeForSelectedColor(position);
    }

    public void setSize(String size) {
        type = TYPE.Size;
//		sizeText.setText(size);
        sizeDialog.dismiss();
        updateProductValue(size);
    }

    public void setQuantity(String quantity) {
        type = TYPE.Quantity;
//		quantityText.setText(quantity);
        quantityDialog.dismiss();
        updateProductValue(quantity);
    }

    private void updateProductValue(final String value) {
        System.out.println("Type :" + type);
//		mProgressBarDialog = new ProgressBarDialog(mContext);
//		mProgressBarDialog.show();

        final UpdateProductInCartApi cartApi = new UpdateProductInCartApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (object != null) {
                    UpdateProductInCartRes response = (UpdateProductInCartRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, "Updated successfully");
                }
                changeUi(value);
//				mProgressBarDialog.dismiss();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
//				mProgressBarDialog.dismiss();
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    UpdateProductInCartRes response = (UpdateProductInCartRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
//		mProgressBarDialog.setCancelable(false);
        if (type.equals(TYPE.Quantity)) {
            cartApi.updateQuantity(UserPreference.getInstance().getUserID(), product.getProductcart_id(), value);
        } else if (type.equals(TYPE.Size)) {
            cartApi.updateSize(UserPreference.getInstance().getUserID(), product.getProductcart_id(), value);
        } else if (type.equals(TYPE.Color)) {
            cartApi.updateColor(UserPreference.getInstance().getUserID(), product.getProductcart_id(), value);
        }
        cartApi.execute();
    }

    protected void changeUi(String value) {
        isProductUpdated = true;
        if (type.equals(TYPE.Quantity)) {
            quantityText.setText(value);
            product.setQuantity(value);
        } else if (type.equals(TYPE.Size)) {
            sizeText.setText(value);
            product.setSelected_size(value);
        } else if (type.equals(TYPE.Color)) {
            colorText.setText(value);
            product.setSelected_color(value);
        } else if (type.equals(TYPE.option)) {
            optionText.setText(value);
            product.setOption(value);
        } else if (type.equals(TYPE.fit)) {
            fitText.setText(value);
            product.setFit(value);
        }
        validate();
    }

    private void validate() {
        if (iscolor) {
            if (!TextUtils.isEmpty(product.getSelected_color())) {
                iscolor = false;
            }
        }
        if (issize) {
            if (!TextUtils.isEmpty(product.getSelected_size())) {
                issize = false;
            }
        }
        if (isoption) {
            if (!TextUtils.isEmpty(product.getOption())) {
                isoption = false;
            }
        }
        if (isfit) {
            if (!TextUtils.isEmpty(product.getFit())) {
                isfit = false;
            }
        }
        if (!iscolor && !issize && !isfit && !isoption) {
            product.setSelectdetails(false);
            ok_layout.setBackgroundColor(getResources().getColor(R.color.header_background));
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
//		if(isProductUpdated)
//			fragmentUserCart.getCartList();
    }

    @Override
    public void setOption(String option) {
        type = TYPE.option;
//		sizeText.setText(size);
        optionDialog.dismiss();
        updateValue(option);
    }

    @Override
    public void setOption(String option, int position) {
        type = TYPE.option;
//		sizeText.setText(size);
        optionDialog.dismiss();
        updateValue(option);
        setColorForSelectedOption(position);
    }

    @Override
    public void setFit(String fit) {
        type = TYPE.fit;
//		sizeText.setText(size);
        fitDialog.dismiss();
        updateValue(fit);
    }

    private void updateValue(final String value) {
        System.out.println("Type :" + type);
//		mProgressBarDialog = new ProgressBarDialog(mContext);
//		mProgressBarDialog.show();

        final CartApi cartApi = new CartApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (object != null) {
                    CartRes response = (CartRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, "Updated successfully");
                }
                changeUi(value);
//				mProgressBarDialog.dismiss();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
//				mProgressBarDialog.dismiss();
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    CartRes response = (CartRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
//		mProgressBarDialog.setCancelable(false);
        if (type.equals(TYPE.option)) {
            cartApi.updateproductoption(UserPreference.getInstance().getUserID(), product.getProductcart_id(), value);
        } else if (type.equals(TYPE.fit)) {
            cartApi.updateproductfit(UserPreference.getInstance().getUserID(), product.getProductcart_id(), value);
        }
        cartApi.execute();
    }

    @Override
    public void addOption(ProductChildOption child) {
        if (child != null && child.getDep() != null && child.getDep().size() > 0) {
            List<ProductMainOption> mainOptions = child.getDep();
            setNewOption(mainOptions);
        }
        checkButtonLayout();
    }

    private void updateProductOptionValue() {
        System.out.println("Type :" + type);
        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();

        final UpdateCartProductApi cartApi = new UpdateCartProductApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (object != null) {
                    CommonRes response = (CommonRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, "Updated successfully");
                }
                product.setSelected_values(selectedList);
                product.setSelectdetails(false);
                mProgressBarDialog.dismiss();
                ((HomeActivity) mContext).onBackPressed();
                fragmentUserCart.refreshCart();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                mProgressBarDialog.dismiss();
                Syso.info("In handleOnFailure>>" + object);
                try {


                    if (object != null) {

                        UpdateProductInCartRes response = (UpdateProductInCartRes) object;
                        AlertUtils.showToast(mContext, response.getMessage());
                    } else {
                        AlertUtils.showToast(mContext, R.string.invalid_response);
                    }
                } catch (Exception e) {

                }
            }
        });
        mProgressBarDialog.setCancelable(false);
        cartApi.updateProductValues(UserPreference.getInstance().getUserID(), product.getProductcart_id(), selectedList);
        cartApi.execute();
    }


}
