package com.amtrak.rider.scancard;

import android.app.Activity;
import android.content.Context;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.google.android.gms.wallet.PaymentCardRecognitionIntentRequest;
import com.google.android.gms.wallet.PaymentsClient;

import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.Wallet;


public class ScanCard extends CordovaPlugin {

    private CallbackContext PUBLIC_CALLBACKS = null;
    private static final int PAYMENT_CARD_RECOGNITION_REQUEST_CODE = 1971;

    private PaymentsClient paymentsClient;
    private PendingIntent cardRecognitionPendingIntent;

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        PUBLIC_CALLBACKS = callbackContext;
        if (action.equals("launchScanCard")) {
            init();
        }

        // Send no result, to execute the callbacks later
        PluginResult pluginResult = new  PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true); // Keep callback

        return true;
    }

    private void init() {
        paymentsClient = createPaymentsClient(cordova.getActivity());
        possiblyShowPaymentCardOcrButton();
    }

    public static PaymentsClient createPaymentsClient(Activity activity) {
        Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST).build();
        return Wallet.getPaymentsClient(activity, walletOptions);
    }

    public void possiblyShowPaymentCardOcrButton() {
        // The request can be used to configure the type of the payment card recognition. Currently the
        // only supported type is card OCR, so it is sufficient to call the getDefaultInstance() method.
      PaymentCardRecognitionIntentRequest request = PaymentCardRecognitionIntentRequest.getDefaultInstance();
        paymentsClient
            .getPaymentCardRecognitionIntent(request)
            .addOnSuccessListener(intentResponse -> {
                cardRecognitionPendingIntent = intentResponse.getPaymentCardRecognitionPendingIntent();
                System.out.println("Card Recognition Intent created");

                startPaymentCardOcr();
            })
            .addOnFailureListener(e -> {
                // The API is not available either because the feature is not enabled on the device
                // or because your app is not registered.
                System.out.println("Payment card ocr not available." + e);
                PUBLIC_CALLBACKS.error("ScanCard: Encountered Unknown Error");
            });
    }

    public void startPaymentCardOcr() {
        try {
        ActivityCompat.startIntentSenderForResult(
            cordova.getActivity(), cardRecognitionPendingIntent.getIntentSender(),
            PAYMENT_CARD_RECOGNITION_REQUEST_CODE,
            null, 0, 0, 0, null);
        } catch (IntentSender.SendIntentException e) {
        throw new RuntimeException("Failed to start payment card recognition.", e);
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
      if(requestCode == PAYMENT_CARD_RECOGNITION_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();// Get data sent by the Intent

            System.out.println(extras);

            JSONObject creditCardDetails = new JSONObject();

            PUBLIC_CALLBACKS.success(creditCardDetails);
            return;
        }else if(requestCode == PAYMENT_CARD_RECOGNITION_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED){
            //Check if it is cancelled or due to error
           PluginResult resultado = new PluginResult(PluginResult.Status.OK, "canceled action, process this in javascript");
           resultado.setKeepCallback(true);
           PUBLIC_CALLBACKS.sendPluginResult(resultado);
           return;
        }
        // Handle other results if exists.
        super.onActivityResult(requestCode, resultCode, data);
    }
}