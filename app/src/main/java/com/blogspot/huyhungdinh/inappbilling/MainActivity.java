package com.blogspot.huyhungdinh.inappbilling;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler, View.OnClickListener {

    private Button btnPurchase, btnExample;
    private TextView tvResult;

    private boolean readyToPurchase = false;
    private BillingProcessor bp;
    private final String PURCHASE_ID = "android.test.purchased";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bp = BillingProcessor.newBillingProcessor(this, "YOUR LICENSE KEY FROM GOOGLE PLAY CONSOLE HERE", this); // doesn't bind
        bp.initialize(); // binds

        initView();
    }

    private void initView() {
        btnPurchase = (Button) findViewById(R.id.btnPurchase);
        btnExample = (Button) findViewById(R.id.btnExample);
        tvResult = (TextView) findViewById(R.id.tvResult);

        btnPurchase.setEnabled(false);

        btnPurchase.setOnClickListener(this);
        btnExample.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPurchase:
                if (readyToPurchase) {
                    bp.purchase(this, PURCHASE_ID);
                } else {
                    Toast.makeText(this, "Unable to initiate purchase", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnExample:
                startActivity(new Intent(MainActivity.this, UpgradeActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePurchase();
    }

    private void updatePurchase() {
        if (bp.isPurchased(PURCHASE_ID)) {
            tvResult.setText("THANK YOU!");
        }
    }

    @Override
    public void onBillingInitialized() {
        readyToPurchase = true;
        btnPurchase.setEnabled(true);
        updatePurchase();
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        Toast.makeText(this, "Thanks for your Purchased!", Toast.LENGTH_SHORT).show();
        updatePurchase();
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Toast.makeText(this, "Unable to process billing", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPurchaseHistoryRestored() {
        updatePurchase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();

        super.onDestroy();
    }
}
