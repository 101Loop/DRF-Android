/*
 * Copyright (C) 2018 Himanshu Shankar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.civilmachines.drfapi;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for creating Django REST Framework based request class
 * Adds a token header in the request, if user is logged in
 *
 * @param <T> specifies the format in which response will come
 *
 * @author Himanshu Shankar (https://himanshus.com)
 * @author Divya Tiwari (https://divyatiwari.me)
 */
public abstract class DjangoBaseRequest<T> extends JsonRequest<T> {

    // Declare and define extra variable that will be used while creating a request.
    private Context cont;

    // Change these static variable to define how token is stored in Android app and sent on server
    public static String key_token = "token";
    public static String keyAuthorizationHeader = "Authorization";
    public static String keyTokenPrefix = "Bearer ";

    /**
     * Base request for Django REST Framework based APIs.
     * Adds Authorization header, if JWT token is present in the system.
     *
     * @param method the HTTP method to use
     * @param url URL to fetch the JSON from
     * @param jsonRequest A {@link JSONObject} to post with the request. Null is allowed and
     *   indicates no parameters will be posted along with request.
     * @param listener Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     *
     * @author Himanshu Shankar (https://himanshus.com)
     * @author Divya Tiwari (https://divyatiwari.me)
     */
    public DjangoBaseRequest(int method,
                             String url,
                             @Nullable String jsonRequest,
                             Response.Listener<T> listener,
                             @Nullable DjangoErrorListener errorListener,
                             Context context) {
        super(method, url, jsonRequest, listener, errorListener);
        this.cont = context;
    }

    @Override
    abstract protected Response<T> parseNetworkResponse(NetworkResponse response);

    /**
     * Sets Content-Type to application/json
     * Checks for presence of token in SharedPreferenceAdapter and sets it.
     * @return Map<String, String> a Map of headers
     * @throws AuthFailureError from super
     * @author Himanshu Shankar (https://himanshus.com)
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        SharedPreferenceAdapter shaPre;

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        shaPre = new SharedPreferenceAdapter(cont);

        String token = shaPre.getString(key_token);

        if(token != null){
            headers.put(keyAuthorizationHeader, keyTokenPrefix + token);
        }
        return headers;
    }
}
