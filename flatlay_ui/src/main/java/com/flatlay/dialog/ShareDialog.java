package com.flatlay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.Product;
import com.flatlaylib.utils.AlertUtils;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.BranchShortLinkBuilder;


public class ShareDialog extends Dialog{
	private Button otherButton;
	private HomeActivity homeActivity;
	private Context mContext;
	private Product product;
	private String imageUrl;
	private TextView cancelicon;
	private Inspiration inspiration;
	private String shareimagename;
//	private TextView searchText;

//	public ShareDialog(Context mContext, HomeActivity homeActivity,Product product) {
//		super(mContext, R.style.AdvanceDialogTheme);
//		this.homeActivity = homeActivity;
//		this.mContext=mContext;
//		this.product = product;
//		init();
//	}
//
//	public ShareDialog(Context mContext, HomeActivity homeActivity,Inspiration inspiration) {
//		super(mContext, R.style.AdvanceDialogTheme);
//		this.mContext=mContext;
//		this.inspiration = inspiration;
//		init();
//	}

	public ShareDialog(Context mContext, HomeActivity homeActivity,String imageUrl,String shareimagename) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.homeActivity = homeActivity;
		this.mContext=mContext;
		this.imageUrl = imageUrl;
		this.shareimagename=shareimagename;
		share();
	}

	public ShareDialog(Context mContext, String imageUrl,String shareimagename) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.mContext=mContext;
		this.imageUrl = imageUrl;
		this.shareimagename=shareimagename;
		share();
	}

	private void share() {
		setContentView(R.layout.dialog_share);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
		otherButton = (Button) findViewById(R.id.otherButton);
		otherButton.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
		cancelicon = (TextView) findViewById(R.id.cancellayout);
		cancelicon.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
//		searchText = (TextView)findViewById(R.id.searchText) ;
		otherButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkInternet()) {
					shareImage(imageUrl,true,shareimagename);
					dismiss();
				}
			}
		});

		cancelicon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
	}


//	public ShareDialog(Context context, int theme) {
//		super(context, R.style.AdvanceDialogTheme);
//		init();
//	}

//	private void init() {
//		setContentView(R.layout.dialog_share);
//		setCancelable(true);
//		WindowManager.LayoutParams lp = getWindow().getAttributes();
//		lp.dimAmount = 0.9f;
//		getWindow().setAttributes(lp);
//		setCanceledOnTouchOutside(true);
//		otherButton = (Button) findViewById(R.id.otherButton);
//		otherButton.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
//		cancelicon = (TextView) findViewById(R.id.cancellayout);
//		cancelicon.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
////		searchText = (TextView)findViewById(R.id.searchText) ;
//        otherButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkInternet()) {
//                    shareProduct(product,true);
//                    dismiss();
//                }
//            }
//        });
//
//		cancelicon.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				dismiss();
//			}
//		});
//    }

	public boolean checkInternet() {
		if (CommonUtility.isOnline(mContext)) {
			return true;
		} else {
			CommonUtility.showNoInternetAlert(mContext);
			return false;
		}
	}

//	public void shareProduct(final Product product, final boolean isOther) {
//		AlertUtils.showToast(mContext, "Please wait...");
//		BranchShortLinkBuilder shortUrlBuilder = new BranchShortLinkBuilder(mContext)
//				.addParameters("id", product.getId())
//				.addParameters("productname", product.getProductname())
//				.addParameters("skunumber", product.getSkunumber())
//				.addParameters("primarycategory", product.getPrimarycategory())
//				.addParameters("secondarycategory", product.getSecondarycategory())
//				.addParameters("product_url", product.getProducturl())
//				.addParameters("productimageurl", product.getProductimageurl())
//				.addParameters("shortproductdesc", product.getShortproductdesc())
//				.addParameters("longproductdesc", product.getLongproductdesc())
//				.addParameters("discount", product.getDiscount())
//				.addParameters("discounttype", product.getDiscounttype())
//				.addParameters("saleprice", product.getSaleprice())
//				.addParameters("retailprice", product.getRetailprice())
//				//.addParameters("brand", product.getBrand())
//				.addParameters("shippingcost", product.getShippingcost())
//				.addParameters("keywords", product.getKeywords())
//				.addParameters("manufacturename", product.getManufacturename())
//				.addParameters("availability", product.getAvailability())
//				.addParameters("shippinginfo", product.getShippinginfo())
//				.addParameters("pixel", product.getPixel())
//				.addParameters("merchantid", product.getMerchantid())
//				.addParameters("merchantname", product.getMerchantname())
//				.addParameters("quantity", product.getQuantity())
//				.addParameters("color", product.getColor())
//				.addParameters("cart_id", product.getCart_id())
//				.addParameters("from_user_id", product.getFrom_user_id())
//				.addParameters("from_collection_id", product.getFrom_collection_id())
//				.addParameters("productcart_id", product.getProductcart_id())
//				.addParameters("tbl_product_id", product.getTbl_product_id())
//				.addParameters("size", product.getSize())
//				.addParameters("selected_size", product.getSelected_size())
//				.addParameters("selected_color", product.getSelected_color())
//				.addParameters("brand_image", product.getBrand_image())
//				.addParameters("like_count", product.getLike_info().getLike_count())
//				.addParameters("like_id", product.getLike_info().getLike_id())
//				.addParameters("affiliateurl", product.getAffiliateurlforsharing())
//				.addParameters("option", product.getOption())
//				.addParameters("fit", product.getFit())
//				.addParameters("siteId", product.getSiteId())
//				.addParameters("$og_title", product.getProductname())
//				.addParameters("$og_description", product.getShortproductdesc())
//				.addParameters("$og_image_url", product.getProductimageurl());
//		// Get URL Asynchronously
//		shortUrlBuilder.generateShortUrl(new Branch.BranchLinkCreateListener() {
//
//			@Override
//			public void onLinkCreate(String url, BranchError error) {
//				if (error != null) {
//					Log.e("Branch Error", "Branch create short url failed. Caused by -" + error.getMessage());
//					Intent intent = new Intent(Intent.ACTION_SEND);
//					intent.setType("image/*");
//					String link = AppConstants.SHARE_MESSAGE + " " + AppConstants.APP_LINK + "\nItem: " + product.getProducturl();
//					intent.putExtra(Intent.EXTRA_TEXT, link);
//					if (isOther)
//						mContext.startActivity(Intent.createChooser(intent, "Share"));
//					else {
//
//					}
//				} else {
//					Log.i("Branch", "Got a Branch URL 2 " + url);
//					Intent intent = new Intent(Intent.ACTION_SEND);
//					intent.setType("text/plain");
//					String link = "Check out the " + product.getProductname() + " on #Flatlay  " + url;
//					Log.w("link: ", "" + link);
//					intent.putExtra(Intent.EXTRA_TEXT, link);
//					if (isOther)
//						mContext.startActivity(Intent.createChooser(intent, "Share"));
//					else {
////
//					}
//				}
//			}
//		});
//	}

	public void shareImage(final String imageUrl, final boolean isOther, final String shareimagename) {
		AlertUtils.showToast(mContext, "Please wait...");
		BranchShortLinkBuilder shortUrlBuilder = new BranchShortLinkBuilder(mContext)

				.addParameters("product_url", imageUrl);
		Log.w("temp", "CREATING BRANCH LINK!" + imageUrl);
		Log.w("temp", "CREATING BRANCH LINK!" + shareimagename);

		// Get URL Asynchronously
		shortUrlBuilder.generateShortUrl(new Branch.BranchLinkCreateListener() {

			@Override
			public void onLinkCreate(String url, BranchError error) {
				if (error != null) {
					Log.e("Branch Error", "Branch create short url failed. Caused by -" + error.getMessage());
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("image/*");
					String link = AppConstants.SHARE_MESSAGE + " " + AppConstants.APP_LINK + "\nItem: ";
					intent.putExtra(Intent.EXTRA_TEXT, link);
					if (isOther)
						mContext.startActivity(Intent.createChooser(intent, "Share"));
					else {
//
					}
				} else {
					Log.i("Branch", "Got a Branch URL 3 " + url);
					Log.w("shareimagename", "" + shareimagename);
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("text/plain");
					String link = shareimagename;
					intent.putExtra(Intent.EXTRA_TEXT, link);
					if (isOther) {
						Log.w("inside if", "inside if" + link);
						mContext.startActivity(Intent.createChooser(intent, "Share"));
					} else {
						Log.w("inside else", "inside else");
//							loginPinterest(link, url, product.getProductimageurl());
						Log.w("inside else", "" + link);
						Log.w("inside else", "" + url);
						Log.w("inside else", "" + imageUrl);

					}
				}
			}
		});
	}

}
