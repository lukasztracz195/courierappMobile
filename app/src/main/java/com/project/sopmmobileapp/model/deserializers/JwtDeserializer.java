package com.project.sopmmobileapp.model.deserializers;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class JwtDeserializer {

    private static String[] split;

    public static Map<String, String> decoded(String JWTEncoded)  {
        Log.d("JWT_TOKEN", JWTEncoded);
        split = JWTEncoded.split("\\.");
        Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
        Log.d("JWT_DECODED", "Body: " + getJson(split[1]));
        Log.d("JWT_DECODED", "Signiture: " + getJson(split[2]));
        return createMap(getJson(split[1]));
    }

    private static String getJson(String strEncoded) {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        try {
            return new String(decodedBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String, String> createMap(String json) {
        Map<String, String> jsonMap = new HashMap<>();
        String[] keyValues = json.split(",");
        for (String keyValue : keyValues) {
            String[] keyAndValue = keyValue.split(":");

            jsonMap.put(keyAndValue[0].replaceAll("/[\\[\\]']+/g",""),
                    keyAndValue[1].replaceAll("/[\\[\\]']+/g",""));
        }
        return jsonMap;
    }

}
