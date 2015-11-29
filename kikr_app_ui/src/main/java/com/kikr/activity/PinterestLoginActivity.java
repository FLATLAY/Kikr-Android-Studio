package com.kikr.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.kikr.dialog.PinterestBoardDialog;
import com.kikr.utility.AppConstants;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ujjwal on 11/29/2015.
 */
public class PinterestLoginActivity extends FragmentActivity{
    FragmentActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        PDKClient.configureInstance(this, AppConstants.PINTEREST_APP_ID);
        PDKClient.getInstance().onConnect(this);

        Intent intent = getIntent();
        Syso.info("1234567890 >>> in onCreate");
        if(intent.hasExtra("link")&&intent.hasExtra("link_url")&&intent.hasExtra("image_url")){
            Syso.info("1234567890 >>> in onCreate in condition");
            loginPinterest(intent.getStringExtra("link"),intent.getStringExtra("link_url"),intent.getStringExtra("image_url"));
        }else{
            finish();
        }

    }


    public void loginPinterest(final String text,final String link,final String imageUrl){
        List scopes = new ArrayList<String>();
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);
        Syso.info("1234567890 >>> in loginPinterest");
        PDKClient.getInstance().login(this, scopes, new PDKCallback() {
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
                AlertUtils.showToast(context, exception.getDetailMessage());
            }
        });
    }
    private static final String BOARD_FIELDS = "id,name,description,creator,image,counts,created_at";
    private void getBoardList(final String text,final String link,final String imageUrl){
        PDKClient.getInstance().getMyBoards(BOARD_FIELDS, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                Syso.info("1234567890  2>>>>>>" + response.getBoardList().get(0).getName());
                Syso.info("1234567890  2>>>>>>" + response.getBoardList().get(0).getUid());
//               	onSavePin(imageUrl, response.getBoardList().get(0).getUid(), text, link);
                if(response.getBoardList()!=null&&response.getBoardList().size()>0) {
                    Syso.info("1234567890  2>>>>>> inside condition");
                    PinterestBoardDialog boardDialog = new PinterestBoardDialog(context, response.getBoardList(), imageUrl, text, link);
                    boardDialog.show();
                }else
                    AlertUtils.showToast(context, "No board found, please create board first");
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), exception.getDetailMessage());
                Syso.info("12345678 >>> output" + exception.getDetailMessage());
                AlertUtils.showToast(context, exception.getDetailMessage());
            }
        });
    }

    public void onSavePin(String imageUrl,String boardId,String text,String linkUrl) {

        if (!Utils.isEmpty(text) &&!Utils.isEmpty(boardId) && !Utils.isEmpty(imageUrl)) {
            PDKClient.getInstance().createPin(text, boardId, imageUrl, linkUrl, new PDKCallback() {
                @Override
                public void onSuccess(PDKResponse response) {
                    Log.d(getClass().getName(), response.getData().toString());
                    Syso.info("12345678 >>> output" + response.getData().toString());
                    AlertUtils.showToast(context, "Shared Successfully");
                    finish();
                }

                @Override
                public void onFailure(PDKException exception) {
                    Log.e(getClass().getName(), exception.getDetailMessage());
                    Syso.info("12345678 >>> output" + exception.getDetailMessage());
                    AlertUtils.showToast(context, exception.getDetailMessage());
                    finish();
                }
            });
        } else {
            Toast.makeText(this, "Required fields cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
}
