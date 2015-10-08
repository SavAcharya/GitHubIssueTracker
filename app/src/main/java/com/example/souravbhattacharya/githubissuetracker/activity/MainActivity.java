package com.example.souravbhattacharya.githubissuetracker.activity;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.souravbhattacharya.githubissuetracker.R;
import com.example.souravbhattacharya.githubissuetracker.adapter.IssuesAdapter;
import com.example.souravbhattacharya.githubissuetracker.network.HTTPConnector;
import com.example.souravbhattacharya.githubissuetracker.network.HTTPConstants;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class MainActivity extends AppCompatActivity implements HTTPConnector.RequestListener{

    ListView mListView;
    IssuesAdapter mIssueAdapter;
TextView mTextView;
   String url = "https://api.github.com/repos/crashlytics/secureudid/issues";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView=(TextView)findViewById(R.id.txt);


    }


    @Override
    public void onHTTPRequestCompleted(String response, int mRequestCode) {

        if (response != null) {
            Log.i("Res",response);
        }
        if (mRequestCode == HTTPConstants.REQUEST_CODE_ISSUE_RES) {
            if (response != null) {
                Log.i("Res",response);
            }
        }
    }

    @Override
    public void onHTTPRequestError(Exception e, int mRequestCode) {

    }

    @Override
    public void onHTTPRequestStarted(int mRequestCode) {

    }







}
