package com.example.yeabkalwubshit.calvin2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Level;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class HomePage extends AppCompatActivity {

    private static final String TAG = "HomePage_Scan";

    public void connectToHotspot() {
        for(WifiConfiguration config: wifiManager.getConfiguredNetworks()) {
            Log.d(TAG, "Got network...." + config.SSID);
            if (config.SSID.contains("UDAIR-Hotspot")) {
                Log.d(TAG, "Connecting.... " + config.SSID);
                wifiManager.enableNetwork(config.networkId, true);
                wifiManager.reconnect();


                final String pass = "";

//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            login("ywubshit", pass.toCharArray());
//                        }
//                    }).start();
                //login("ywubshit", pass.toCharArray());
                break;
            }
        }
    }
    public BroadcastReceiver wifiBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<ScanResult> scanResults = wifiManager.getScanResults();
            Log.d(TAG, scanResults.toString());
            for(ScanResult result: scanResults) {
                Log.d(TAG, result.SSID);
            }


            for(WifiConfiguration config: wifiManager.getConfiguredNetworks()) {
                Log.d(TAG, "Got network...." + config.SSID);
                if (config.SSID.contains("UDAIR-Hotspot")) {
                    Log.d(TAG, "Connecting.... " + config.SSID);
                    wifiManager.disconnect();
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {}
                    wifiManager.enableNetwork(config.networkId, true);
                    wifiManager.reconnect();

                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {}

                    context.unregisterReceiver(wifiBroadcastReceiver);



                    final String pass = "";

//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            login("ywubshit", pass.toCharArray());
//                        }
//                    }).start();
                    //login(context, "ywubshit", pass.toCharArray());
                    break;
                }
            }
        }
    };
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final int TIMEOUT = 20000;

    public void openInBrowser() {
        Intent browserIntent =
                new Intent(Intent.ACTION_VIEW, Uri.parse("https://udair2.udallas.edu"));
        startActivity(browserIntent);
    }

    public void login(String user, char[] pass) {
        login(null, user, pass);
    }

    public void login(Context context, String user, char[] pass) {
        /*
         * This prevents a handshake alert from terminating the program.
         * Should be safe as long as udallas.edu is the only domain being connected to.
         */
        //System.setProperty("jsse.enableSNIExtension", "false");
        HttpsURLConnection con = null;

        Log.d(TAG, "Logging in...");
        try {
            URL url = new URL("https://udair2.udallas.edu/cgi-bin/login");
            Log.d(TAG, "Going to create....");
            con = (HttpsURLConnection) url.openConnection();
            Log.d(TAG, "Created....");
            con.setConnectTimeout(TIMEOUT);
            con.setReadTimeout(TIMEOUT);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);

            user = URLEncoder.encode(user, "UTF-8");
            String urlParameters1 = "user=" + user + "&password=";
            String urlParameters2 = "&cmd=authenticate&Login=Log+In";
            con.setDoOutput(true);

            try (BufferedOutputStream wr =
                         new BufferedOutputStream(con.getOutputStream())) {
                wr.write(urlParameters1.getBytes());
                for(char c : pass){
                    wr.write(URLEncoder.encode(String.valueOf(c), "UTF-8").getBytes());
                }

                wr.write(urlParameters2.getBytes());
                wr.flush();
                Log.d(TAG, "Setup complete....");
                Log.d(TAG, "Response code" + con.getResponseCode());
            }
        } catch (IOException e) {
            Log.d(TAG, "Exception..." + e);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        if (context != null) {
            try {
                context.unregisterReceiver(wifiBroadcastReceiver);
            } catch (Exception e) {

            }
        }
        System.setProperty("jsse.enableSNIExtension", "true");
    }

    public static WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


//        Log.d(TAG, "Check log");
//        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        wifiManager.setWifiEnabled(true);
//
//        registerReceiver(
//                wifiBroadcastReceiver,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//        wifiManager.startScan();

        String pass = "";
        login("ywubshit", pass.toCharArray());



    }

}
