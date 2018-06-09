package com.flatlaylib.api;

import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.res.FeaturedTabApiRes;
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

public class FeaturedTabApi extends AbsService {

    private String requestValue;
    private String requestType;

    public FeaturedTabApi(ServiceCallback serviceCallback) {
        super();
        this.mServiceCallback = serviceCallback;
    }

    public void getFeaturedTabData(String user_id, String pagenum) {
        this.METHOD_NAME = WebConstants.HOST_FILE + "getfeaturedtabdata";
        requestType = WebConstants.HTTP_METHOD_POST;
        Map<String, String> comment = new HashMap<String, String>();
        comment.put("user_id", user_id);
        comment.put("pagenum", pagenum);
        Gson gson = new Gson();
        requestValue = gson.toJson(comment);
    }

    @Override
    public String getActionName() {
        return METHOD_NAME;
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
    public List<NameValuePair> getNameValueRequest() {
        return null;
    }

    @Override
    public String getJsonRequest() {
        try {
            return requestValue;
        } catch (Exception e) {
            e.printStackTrace();
            JSONArray array = new JSONArray();
            return array.toString();
        }
    }

    @Override
    protected void processResponse(String response) {
        Syso.info("In RegisterUserApi processResponse16>>" + response);
       try {
/*
            JSONObject json = null;
            JSONArray arr_products;
            try {
                json = new JSONObject(response);
                JSONArray array = json.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    JSONArray arr_feed = object.getJSONArray("inspiration_feed");
                    for (int j = 0; j < arr_feed.length(); j++) {
                        JSONObject object1 = arr_feed.getJSONObject(j);
                        JSONArray arr_products1 = object1.getJSONArray("poducts");
                        if (arr_products1.length() == 0) {
                            JSONObject itemB = new JSONObject();
                            itemB.put("shortproductdesc", "null");
                            arr_products1.put(itemB);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                Syso.info(ex.getMessage());
            }

            if (json != null)
                response = json.toString();
*/

            //System.out.println(response);

        //response = response.replace(",\"poducts\":[]",",\"poducts\":[{\"id\":\"5d3afb88b54c7de7a1f6c372ec03b4e8\",\"prosperantproductId\":\"32b6b11f1cb44e5c505cb4367956a743\",\"upc\":\"\",\"primarycategory\":\"health & personal care > hair care > styling products > creams gels & lotions\",\"producturl\":\"http:\\/\\/www.anrdoezrs.net\\/click-3726866-10660381?url=http%3A%2F%2Fwww.kmart.com%2Fl-oreal-sulfate-free-smoothing-system-super-sleek-intense%2Fp-038W004170256001P&sid=cart\",\"affiliateurl\":\"http:\\/\\/prosperent.com\\/store\\/product\\/415738-27126-0\\/?k=L%27Oreal+Hair+Expertise+EverSleek+Intense+Serum%2C+Super+Sleek%2C+1.9+oz+%2855+ml%29&m=124316&b=L%27OREAL+U.S.A.%2C+INC.&p=5d3afb88b54c7de7a1f6c372ec03b4e8|user:&sid=12163|fromuser:insp_12214|fromcol:5465|Ifromcol5465|Ifromuserinsp_12214\",\"affiliateurlforsharing\":\"http:\\/\\/prosperent.com\\/store\\/product\\/415738-27126-0\\/?k=L%27Oreal+Hair+Expertise+EverSleek+Intense+Serum%2C+Super+Sleek%2C+1.9+oz+%2855+ml%29&m=124316&b=L%27OREAL+U.S.A.%2C+INC.&p=5d3afb88b54c7de7a1f6c372ec03b4e8|user:&sid=12163|fromuser:insp_12214|fromcol:5465|Ifromcol5465|Ifromuserinsp_12214X\",\"productimageurl\":\"https:\\/\\/images.prosperentcdn.com\\/images\\/250x250\\/c.shld.net\\/rpx\\/i\\/s\\/i\\/spin\\/image\\/spin_prod_ec_802525201\",\"shortproductdesc\":null,\"longproductdesc\":null,\"percentOff\":null,\"saleprice\":\"\",\"retailprice\":\"8.99\",\"brand\":\"L'OREAL U.S.A., INC.\",\"productname\":\"L'Oreal Hair Expertise EverSleek Intense Serum, Super Sleek, 1.9 oz (55 ml)\",\"isbn\":\"\",\"currency\":\"USD\",\"merchantid\":\"124316\",\"merchantname\":\"Kmart\",\"from_collection_id\":\"5465\",\"fromuserid\":\"insp_12214\",\"like_info\":{\"like_count\":\"0\",\"like_id\":\"\"}}]");
        //response = response.replace("null","\"null\"");
           response = response.replace("\"longproductdesc\":[]","\"longproductdesc\":\"null\"");
           response = response.replace("\"shortproductdesc\":[]","\"shortproductdesc\":\"null\"");

        FeaturedTabApiRes featuredTabApiRes = JsonUtils.fromJson(response, FeaturedTabApiRes.class);

            if (featuredTabApiRes.getCode().equals(WebConstants.SUCCESS_CODE)) {
                isValidResponse = true;
            }
            serviceResponse = featuredTabApiRes;
        } catch (JsonParseException e) {
            Syso.error(e);
            isValidResponse = false;
        } catch (NullPointerException e) {
            Syso.error(e);
            isValidResponse = false;
        }

    }
}
