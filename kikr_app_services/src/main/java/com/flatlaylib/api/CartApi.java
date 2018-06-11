package com.flatlaylib.api;

import com.flatlaylib.bean.ProductRequiredOption;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.res.CartRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartApi extends AbsService {

    boolean isGetCartList = false;
    private String requestValue;
    private String requestType;

    public CartApi(ServiceCallback serviceCallback) {
        super();
        this.mServiceCallback = serviceCallback;
    }

    //	cartApi.addToCart(UserPreference.getInstance().getUserID(), product.getId(), quantityTextView.getText().toString(),UserPreference.getInstance().getCartID(),fromUserId,fromCollection,selectedSize,selectedColor);
    public void addToCart(String user_id, String product_id, String quantity, String cartId, String from_user_id, String from_collection_id, String size, String color, List<ProductRequiredOption> list) {
        this.METHOD_NAME = WebConstants.HOST_FILE + "addtocart";
        isGetCartList = false;
        requestType = WebConstants.HTTP_METHOD_POST;
        Map<String, String> comment = new HashMap<String, String>();
        comment.put("user_id", user_id);
        comment.put("product_id", product_id);
        comment.put("quantity", quantity);
        comment.put("cart_id", cartId);
        comment.put("from_user_id", from_user_id);
        comment.put("from_collection_id", from_collection_id);
        comment.put("color", color);
        comment.put("size", size);
//		comment.put("selected_values", getValues(list));
        Map[] maps = new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(maps);
        try {
            if (list != null) {
                JSONArray array = new JSONArray(requestValue);
                array.getJSONObject(0).put("selected_values", new JSONArray(getValues(list)));
                requestValue = array.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getValues(List<ProductRequiredOption> list) {
        String value = "[";
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                value += "{\"name\":\""
                        + list.get(i).getName() + "\","
                        + "\"value\":\""
                        + list.get(i).getValue() + "\"}";
                if (i != list.size() - 1)
                    value += ",";
            }
            value += "]";
        }
        return value.length() > 1 ? value : null;
    }

    public void getCartList(String user_id) {
        this.METHOD_NAME = WebConstants.HOST_FILE + "getcartlist";
        isGetCartList = true;
        requestType = WebConstants.HTTP_METHOD_POST;
        Map<String, String> comment = new HashMap<String, String>();
        comment.put("user_id", user_id);
        Map[] maps = new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(maps);
    }

    public void removeFromCart(String user_id, String productcart_id) {
        this.METHOD_NAME = WebConstants.HOST_FILE + "removefromcart";
        isGetCartList = false;
        requestType = WebConstants.HTTP_METHOD_POST;
        Map<String, String> comment = new HashMap<String, String>();
        comment.put("productcart_id", productcart_id);
        comment.put("user_id", user_id);
        Map[] maps = new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(maps);
    }

    public void emptyTheCart(String user_id) {
        this.METHOD_NAME = WebConstants.HOST_FILE + "emptythecart";
        requestType = WebConstants.HTTP_METHOD_POST;
        isGetCartList = false;
        Map<String, String> comment = new HashMap<String, String>();
        comment.put("user_id", user_id);
        Map[] maps = new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(maps);
    }

    public void createCart(String user_id) {
        this.METHOD_NAME = WebConstants.HOST_FILE + "createcart";
        isGetCartList = false;
        requestType = WebConstants.HTTP_METHOD_POST;
        Map<String, String> comment = new HashMap<String, String>();
        comment.put("user_id", user_id);
        Map[] maps = new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(maps);
    }

    public void getTotalInfo(String user_id, String address_id) {
        this.METHOD_NAME = WebConstants.HOST_FILE + "gettotalinfo";
        requestType = WebConstants.HTTP_METHOD_POST;
        isGetCartList = false;
        Map<String, String> comment = new HashMap<String, String>();
        comment.put("user_id", user_id);
        comment.put("address_id", address_id);
        Map[] maps = new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(maps);
    }

    public void updateproductoption(String user_id, String productcart_id, String option) {
        this.METHOD_NAME = WebConstants.HOST_FILE + "updateproductoption";
        isGetCartList = false;
        requestType = WebConstants.HTTP_METHOD_POST;
        Map<String, String> comment = new HashMap<String, String>();
        comment.put("user_id", user_id);
        comment.put("productcart_id", productcart_id);
        comment.put("option", option);
        Map[] maps = new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(maps);
    }

    public void updateproductfit(String user_id, String productcart_id, String fit) {
        this.METHOD_NAME = WebConstants.HOST_FILE + "updateproductfit";
        requestType = WebConstants.HTTP_METHOD_POST;
        isGetCartList = false;
        Map<String, String> comment = new HashMap<String, String>();
        comment.put("user_id", user_id);
        comment.put("productcart_id", productcart_id);
        comment.put("fit", fit);
        Map[] maps = new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(maps);
    }

    public void updateproductPrice(String user_id, String productcart_id, String price, String shiping, String tax, String md5, String siteId) {
        this.METHOD_NAME = WebConstants.HOST_FILE + "updateproductprice";
        isGetCartList = false;
        requestType = WebConstants.HTTP_METHOD_POST;
        Map<String, String> comment = new HashMap<String, String>();
        comment.put("user_id", user_id);
        comment.put("productcart_id", productcart_id);
        comment.put("price", price);
        comment.put("shiping", shiping);
        comment.put("tax", tax);
        comment.put("md5", md5);
        comment.put("siteid", siteId);

        Map[] maps = new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(maps);
    }

    @Override
    public String getActionName() {
        return METHOD_NAME;
    }

    @Override
    public List<NameValuePair> getNameValueRequest() {
        return null;
    }

    @Override
    public String getMethod() {
        return requestType;
    }

    @Override
    public String getHeader() {
        return "Bearer " + UserPreference.getInstance().getAccessToken();
    }

    @Override
    public String getJsonRequest() {
        try {
            JSONArray array = new JSONArray(requestValue);
            return array.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            JSONArray array = new JSONArray();
            return array.toString();
        }
    }

    @Override
    protected void processResponse(String response) {
        Syso.info("In RegisterUserApi processResponse9>>" + response);
        try {
            if (isGetCartList) {
                JSONObject object = null;
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    JSONArray array = json.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        object = array.getJSONObject(i);

                        try {
                            object.getJSONArray("selected_values");

                        } catch (Exception ex) {
                            Syso.info(ex.getMessage());
                            object.put("selected_values", new JSONArray());
                        }
                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                }
                if (json != null)
                    response = json.toString();
            }

            CartRes userResponse = JsonUtils.fromJson(response, CartRes.class);
            if (userResponse.getCode().equals(WebConstants.SUCCESS_CODE)) {
                isValidResponse = true;
            }
            serviceResponse = userResponse;
        } catch (JsonParseException e) {
            Syso.error(e);
            isValidResponse = false;
        } catch (NullPointerException e) {
            Syso.error(e);
            isValidResponse = false;
        }
    }
}
