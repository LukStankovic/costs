package com.stankovic.lukas.vydaje.Api.ApiRequest;

import android.content.Context;
import android.content.res.Resources;

import com.stankovic.lukas.vydaje.MainActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class ApiParamsBuilder {

    // API KLÍČ - todo do res/values/secrets.xml
    private static String clientId = "";

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
}
