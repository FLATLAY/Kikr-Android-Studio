package com.kikr.dialog;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.activity.PinterestLoginActivity;
import com.kikr.adapter.CollectionListAdapter;
import com.kikr.adapter.PinterestBoardAdapter;
import com.kikrlib.bean.Product;
import com.kikrlib.utils.Syso;
import com.pinterest.android.pdk.PDKBoard;

import java.util.ArrayList;
import java.util.List;

public class PinterestBoardDialog extends Dialog {
	private FragmentActivity mContext;
	private List<PDKBoard> bgImages= new ArrayList<>();
	private String imageUrl, text, link;


//	onSavePin(imageUrl, response.getBoardList().get(0).getUid(), text, link);
	public PinterestBoardDialog(FragmentActivity context, List<PDKBoard> bgImages,String imageUrl,String text,String link) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		this.bgImages=bgImages;
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
		PinterestBoardAdapter pinterestBoardAdapter = new PinterestBoardAdapter(mContext,bgImages);
		boardListView.setAdapter(pinterestBoardAdapter);
		boardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String boardId = bgImages.get(position).getUid();
                ((PinterestLoginActivity) mContext).onSavePin(imageUrl, boardId, text, link);
                dismiss();
            }
        });
        Syso.info("1234567890  2>>>>>> inside init completed");
    }
	



}
