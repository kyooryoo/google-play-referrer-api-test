package io.branch.googleplayreferrerapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.TextView;
import android.widget.Toast;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;

public class MainActivity extends AppCompatActivity {

    private TextView refrerTV;
    InstallReferrerClient referrerClient;
    String refrer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refrerTV = findViewById(R.id.idTVRefrer);
        referrerClient = InstallReferrerClient.newBuilder(this).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        ReferrerDetails response = null;
                        try {
                            response = referrerClient.getInstallReferrer();
                            String referrerUrl = response.getInstallReferrer();
                            long referrerClickTime = response.getReferrerClickTimestampSeconds();
                            long appInstallTime = response.getInstallBeginTimestampSeconds();
                            boolean instantExperienceLaunched = response.getGooglePlayInstantParam();
                            refrer = response.getInstallReferrer();
                            refrerTV.setText("Referrer is : \n" + referrerUrl + "\n" +
                                    "Referrer Click Time is : " + referrerClickTime + "\n" +
                                    "App Install Time : " + appInstallTime);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        Toast.makeText(MainActivity.this, "Feature not supported..", Toast.LENGTH_SHORT).show();
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        Toast.makeText(MainActivity.this, "Failed to establish connection", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                Toast.makeText(MainActivity.this, "Service disconnected..", Toast.LENGTH_SHORT).show();
            }
        });

    }
}