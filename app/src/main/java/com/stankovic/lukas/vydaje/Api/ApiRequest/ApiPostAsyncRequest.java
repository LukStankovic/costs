package com.stankovic.lukas.vydaje.Api.ApiRequest;

import android.os.AsyncTask;
import android.util.Log;

import com.stankovic.lukas.vydaje.Api.ApiReader.ApiReader;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class ApiPostAsyncRequest extends AsyncTask<String, Void, String> {

    private String baseUrl = "https://vydaje.stankoviclukas.cz/";

    private final int ACTION_URL = 0;
    private final int PARAMS_STRING = 1;



    @Override
    protected String doInBackground(String... strings) {
        return postData(strings[ACTION_URL], strings[PARAMS_STRING]);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    private String postData(String actionUrl, String paramsString) {
        int responseCode = 404;
        String status = "error";
        String response = "";
        try{
            String url = baseUrl + actionUrl;
            URL urlObj = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) urlObj.openConnection();

            SSLContext sc;
            sc = SSLContext.getInstance("TLS");
            sc.init(null, null, new java.security.SecureRandom());
            conn.setSSLSocketFactory(sc.getSocketFactory());

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);

            conn.connect();

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(paramsString);
            wr.flush();
            wr.close();
            responseCode = conn.getResponseCode();
            response = ApiReader.readStream(conn.getInputStream());

            //status = ApiReader.parseStatus(response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return response;
    }


    protected void onPostExecute(Boolean result) {

    }
}