package com.example.souravbhattacharya.githubissuetracker.network;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.example.souravbhattacharya.githubissuetracker.R;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Sourav.Bhattacharya on 9/30/2015.
 */
public class HTTPConnector extends AsyncTask{
    private Map<String, String> mParams = null;
    private Type mType = null;
    private String mUrl = null;
    private String mFunctionName = null;
    private int mRequestCode = 0;
    private final Context mContext;
    private String mResponse;
    private Exception mError;
    private boolean mRunning=false;
    private ProgressDialog  progressDialog;

    public enum Type {
        POST, GET, mType, POST_WITH_FILE
    };

    RequestListener mRequestListener = null;

    public interface RequestListener {
        void onHTTPRequestCompleted(String response,int mRequestCode);

        void onHTTPRequestError(Exception e,int mRequestCode);

        void onHTTPRequestStarted(int mRequestCode);
    }

    public void setRequestListener( RequestListener listener) {
        if (listener != null)
            this.mRequestListener = listener;

    }


    public HTTPConnector(Context context, String baseUrl,
                            String functionName, Map<String,String> params,int requestCode,
                            Type type) {
        this.mParams = params;

        this.mType = type;
        this.mUrl = baseUrl;
        this.mFunctionName = functionName;
        this.mRequestCode=requestCode;
        this.mContext = context;
    }

    public HTTPConnector(Context context){

        this.mContext = context;
    }

    public static boolean isConnected( final Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    public boolean isRunning() {
        return mRunning;
    }
    public  String GETRequest(String url){
    String result = null;
    URL _URL;

    try {
        _URL = new URL(url);

        HttpsURLConnection  urlConnection = (HttpsURLConnection) _URL
                .openConnection();
        BufferedReader reader =new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        while ((result = reader.readLine()) != null){
            result += result;
            Log.i("Response",result);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
return result;
}

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Object doInBackground(Object[] params) {

        if (HTTPConnector.isConnected(mContext))

        {
            mRunning = true;
            try {
                if (mType == Type.GET) {
                    mResponse = GETRequest(mUrl);
                } else if (mType == Type.POST)
                    mResponse = PostRequest(mFunctionName, mParams, mUrl);

            } catch (Exception e) {
                e.printStackTrace();
                mError = e;
            }
        } else {
            mError = new Exception(
                    mContext.getString(R.string.async_task_no_internet));
        }
        mHandler.sendEmptyMessage(1);
        return null;
    }


    protected void onPostExecute(Void result) {
        progressDialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        if (mRequestListener != null)
            mRequestListener.onHTTPRequestStarted(mRequestCode);
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancel(true);
            }
        });

        // if(mRequestCode!=000||mRequestCode!=001)
         progressDialog.show();
        super.onPreExecute();
    }



    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            mRunning = false;
            if (mError != null && mRequestListener != null) {
                mRequestListener.onHTTPRequestError(mError,mRequestCode);
            } else if (mResponse != null && mRequestListener != null) {
                mRequestListener.onHTTPRequestCompleted(mResponse,mRequestCode);
            }
        }
    };

    private String PostRequest(String mFunctionName, Map<String, String> mParams, String mUrl) {
        return null;
    }












}
