package com.flatlay.activity;

import android.app.Activity;
import android.os.Bundle;

import com.flatlay.R;

/**
 * Created by RachelDi on 2/5/18.
 */

public class InspirationDetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_inspiration_detail);

    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }
}
