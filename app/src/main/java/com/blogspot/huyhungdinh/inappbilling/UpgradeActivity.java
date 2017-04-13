package com.blogspot.huyhungdinh.inappbilling;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by HUNGDH on 4/13/2017.
 */

public class UpgradeActivity extends Activity {

    private ImageView ivClose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        initView();
    }

    private void initView() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpgradeActivity.this.finish();
            }
        });
    }
}
