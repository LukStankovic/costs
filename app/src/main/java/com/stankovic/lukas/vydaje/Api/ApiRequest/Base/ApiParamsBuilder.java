package com.stankovic.lukas.vydaje.Api.ApiRequest.Base;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class ApiParamsBuilder {

    // API KLÍČ - todo do res/values/secrets.xml
    private static String clientId = "4ba498e3617fbc53bfcabe8574b66f851f97a99a1b34253b12ca4e0685c0f3e4";

    public static String buildParams(HashMap<String, String> params) {
        params.put("client", clientId);

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0) {
                    sbParams.append("&");
                }
                sbParams.append(key).append("=").append(URLEncoder.encode(params.get(key), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }

        return sbParams.toString();
    }

    public static String buildParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("client", clientId);

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0) {
                    sbParams.append("&");
                }
                sbParams.append(key).append("=").append(URLEncoder.encode(params.get(key), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }

        return sbParams.toString();
    }
}
