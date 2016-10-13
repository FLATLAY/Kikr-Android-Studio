package com.flatlay.utility;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.flatlay.dialog.PinterestBoardDialog;
import com.flatlay.post_upload.FragmentPostUploadTag;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PinterestUtility {
    FragmentActivity context;

    private static PinterestUtility pinterestUtility;
    FragmentPostUploadTag fragmentPostUploadTag;

    private PinterestUtility(FragmentActivity context, FragmentPostUploadTag fragmentPostUploadTag) {
        this.context = context;
        this.fragmentPostUploadTag = fragmentPostUploadTag;
    }


    public static PinterestUtility getInstance(FragmentActivity context, FragmentPostUploadTag fragmentPostUploadTag) {

        if (pinterestUtility == null)
            return new PinterestUtility(context, fragmentPostUploadTag);
        else
            return pinterestUtility;
    }

    public void init() {
        PDKClient.configureInstance(context, AppConstants.PINTEREST_APP_ID);
        PDKClient.getInstance().onConnect(context);
        loginPinterest(null, null, null);
    }


    public void loginPinterest(final String text, final String link, final String imageUrl) {
        List scopes = new ArrayList<String>();
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);
        Syso.info("1234567890 >>> in loginPinterest");
        PDKClient.getInstance().login(context, scopes, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                Log.d(getClass().getName(), response.getData().toString());
                //user logged in, use response.getUser() to get PDKUser object
                Syso.info("1234567890 >>> in callback");
                Syso.info("123456789 >>> " + response.getUser().getFirstName() + "," + response.getUser().getUsername());
                Syso.info("123456789 board UId >>> " + response.getBoard().getUid());
                getBoardList(text, link, imageUrl);
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), exception.getDetailMessage());
                try {
                    JSONObject jsonObject = new JSONObject(exception.getDetailMessage());
                    AlertUtils.showToast(context, jsonObject.getString("message"));
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertUtils.showToast(context, exception.getDetailMessage());
                }
            }
        });
    }

    private static final String BOARD_FIELDS = "id,name,description,creator,image,counts,created_at";

    private void getBoardList(final String text, final String link, final String imageUrl) {
        PDKClient.getInstance().getMyBoards(BOARD_FIELDS, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                Syso.info("1234567890  2>>>>>>" + response.getBoardList().get(0).getName());
                Syso.info("1234567890  2>>>>>>" + response.getBoardList().get(0).getUid());
//               	onSavePin(imageUrl, response.getBoardList().get(0).getUid(), text, link);
                if (response.getBoardList() != null && response.getBoardList().size() > 0) {
                    Syso.info("1234567890  2>>>>>> inside condition");
                    PinterestBoardDialog boardDialog = new PinterestBoardDialog(context, response.getBoardList(), imageUrl, text, link,fragmentPostUploadTag);
                    boardDialog.show();
                } else
                    AlertUtils.showToast(context, "No board found, please create board first");
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), exception.getDetailMessage());
                Syso.info("12345678 >>> output" + exception.getDetailMessage());
                try {
                    JSONObject jsonObject = new JSONObject(exception.getDetailMessage());
                    AlertUtils.showToast(context, jsonObject.getString("message"));
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertUtils.showToast(context, exception.getDetailMessage());
                }
            }
        });
    }


}
