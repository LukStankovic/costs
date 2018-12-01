package com.stankovic.lukas.vydaje.Api.ApiReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ApiReader {


    public static String parseStatus(String jsonString) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(jsonString);
            return jsonObject.get("result").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public static String parseOutput(String jsonString) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(jsonString);
            return jsonObject.get("output").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public static JSONObject parse(String jsonString, String element) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(jsonString);
            return (JSONObject) jsonObject.get(element);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static String parseElement(String jsonString, String element) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(jsonString);
            return jsonObject.get(element).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public static String readStream(InputStream inputStream) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(streamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String inputLine = "";

        while((inputLine = reader.readLine()) != null){
            stringBuilder.append(inputLine);
        }
        reader.close();
        streamReader.close();

        return stringBuilder.toString();
    }
}
