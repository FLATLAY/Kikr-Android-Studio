package com.kikr.dialog;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.flatlay.R;
import com.kikr.activity.HomeActivity;
import com.kikr.adapter.PinterestBoardAdapter;
import com.kikr.post_upload.FragmentPostUploadTag;
import com.kikrlib.db.UserPreference;
import com.kikrlib.utils.Syso;
import com.pinterest.android.pdk.PDKBoard;

import java.util.ArrayList;
import java.util.List;

public class PinterestBoardDialog extends Dialog {
    private FragmentActivity mContext;
    private List<PDKBoard> bgImages = new ArrayList<>();
    private String imageUrl, text, link;
    private FragmentPostUploadTag fragmentPostUploadTag;


    //	onSavePin(imageUrl, response.getBoardList().get(0).getUid(), text, link);
    public PinterestBoardDialog(FragmentActivity context, List<PDKBoard> bgImages, String imageUrl, String text, String link, FragmentPostUploadTag fragmentPostUploadTag) {
        super(context, R.style.AdvanceDialogTheme);
        mContext = context;
        this.fragmentPostUploadTag = fragmentPostUploadTag;
        this.bgImages = bgImages;
        this.imageUrl = imageUrl;
        this.text = text;
        this.link = link;
        Syso.info("1234567890  2>>>>>> inside PinterestBoardDialog");
        init();
    }

    public PinterestBoardDialog(FragmentActivity context, int theme) {
        super(context, R.style.AdvanceDialogTheme);
        mContext = context;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_pinterest_board);
        setCancelable(true);
        ListView boardListView = (ListView) findViewById(R.id.boardListView);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        getWindow().setAttributes(lp);
//		setCanceledOnTouchOutside(true);
        PinterestBoardAdapter pinterestBoardAdapter = new PinterestBoardAdapter(mContext, bgImages);
        boardListView.setAdapter(pinterestBoardAdapter);
        boardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String boardId = bgImages.get(position).getUid();
                //((HomeActivity) mContext).onSavePin("https://s32.postimg.org/8flhbj29x/Companies_House_penalties.jpg", boardId, text, "https://s32.postimg.org/8flhbj29x/Companies_House_penalties.jpg");
                UserPreference.getInstance().setmIsPinterestSignedIn(true);
                UserPreference.getInstance().setmPinterestBoardId(boardId);
                fragmentPostUploadTag.checkedSocialSharingLogin();
                dismiss();
            }
        });
        Syso.info("1234567890  2>>>>>> inside init completed");
    }
}
