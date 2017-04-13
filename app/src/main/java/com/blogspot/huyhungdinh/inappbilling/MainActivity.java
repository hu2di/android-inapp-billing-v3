package com.blogspot.huyhungdinh.inappbilling;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler, View.OnClickListener {

    private Button btnPurchase, btnSubscribe, btnConsume, btnExample;

    private boolean readyToPurchase = false;
    private BillingProcessor bp;

    private final String PURCHASE_ID = "android.test.purchased";
    private final String SUBSCRIBE_ID = "android.test.subscribed";
    private final String CONSUME_ID = "android.test.consumed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bp = new BillingProcessor(this, "YOUR LICENSE KEY FROM GOOGLE PLAY CONSOLE HERE", this);

        initView();
    }

    private void initView() {
        btnPurchase = (Button) findViewById(R.id.btnPurchase);
        btnSubscribe = (Button) findViewById(R.id.btnSubscribe);
        btnConsume = (Button) findViewById(R.id.btnConsume);
        btnExample = (Button) findViewById(R.id.btnExample);

        btnPurchase.setEnabled(false);
        btnSubscribe.setEnabled(false);
        btnConsume.setEnabled(false);

        btnPurchase.setOnClickListener(this);
        btnSubscribe.setOnClickListener(this);
        btnConsume.setOnClickListener(this);
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
            case R.id.btnSubscribe:
                if (readyToPurchase) {
                    bp.purchase(this, SUBSCRIBE_ID);
                } else {
                    Toast.makeText(this, "Unable to initiate subscribe", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnConsume:
                if (readyToPurchase) {
                    bp.purchase(this, CONSUME_ID);
                } else {
                    Toast.makeText(this, "Unable to initiate consume", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnExample:
                startActivity(new Intent(MainActivity.this, UpgradeActivity.class));
                break;
        }
    }

    private void checkStatus() {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                List<String> owned = bp.listOwnedProducts();
                return owned.contains(PURCHASE_ID);
                //return owned != null && owned.size() != 0;
            }

            @Override
            protected void onPostExecute(Boolean b) {
                super.onPostExecute(b);
                if (b) {
                     /*
                      * Called process purchased
		              */
                }
            }
        }.execute();
    }

    @Override
    public void onBillingInitialized() {
        /*
         * Called when BillingProcessor was initialized and it's ready to purchase
		 */
        readyToPurchase = true;

        btnPurchase.setEnabled(true);
        btnSubscribe.setEnabled(true);
        btnConsume.setEnabled(true);

        checkStatus();
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        /*
		 * Called when requested PRODUCT ID was successfully purchased
		 */
        switch (productId) {
            case PURCHASE_ID:
                Toast.makeText(this, "Thanks for your Purchased!", Toast.LENGTH_SHORT).show();
                break;
            case SUBSCRIBE_ID:
                Toast.makeText(this, "Thanks for your Subscribed!", Toast.LENGTH_SHORT).show();
                break;
            case CONSUME_ID:
                Toast.makeText(this, "Thanks for your Consumed!", Toast.LENGTH_SHORT).show();
                break;
        }

        checkStatus();
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
		/*
		 * Called when some error occurred. See Constants class for more details
		 *
		 * Note - this includes handling the case where the user canceled the buy dialog:
		 * errorCode = Constants.BILLING_RESPONSE_RESULT_USER_CANCELED
		 */
        Toast.makeText(this, "Unable to process billing", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPurchaseHistoryRestored() {
		/*
		 * Called when purchase history was restored and the list of all owned PRODUCT ID's
		 * was loaded from Google Play
		 */
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
