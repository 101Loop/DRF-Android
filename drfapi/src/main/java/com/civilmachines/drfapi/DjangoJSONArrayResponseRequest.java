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

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Creates request on Django APIs where response is in JSONArray format while request is in
 * JSONObject format
 *
 * @author Himanshu Shankar (https://himanshus.com)
 * @author Divya Tiwari (https://divyatiwari.me)
 */
public class DjangoJSONArrayResponseRequest extends DjangoBaseRequest<JSONArray> {
    /**
     * Creates a new JSONObject request from Django REST Framework APIs
     *
     * Based on {@link com.android.volley.toolbox.JsonObjectRequest}
     * @param method the HTTP method to use
     * @param url URL to fetch the JSON from
     * @param jsonRequest A {@link JSONObject} to post with the request. Null is allowed and
     *   indicates no parameters will be posted along with request.
     * @param listener Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     * @param context A {@link Activity} to handle create {@link SharedPreferenceAdapter} object for token.
     *
     * @author Himanshu Shankar (https://himanshus.com)
     * @author Divya Tiwari (https://divyatiwari.me)
     */
    public DjangoJSONArrayResponseRequest(int method,
                                          String url,
                                          @Nullable JSONObject jsonRequest,
                                          Response.Listener<JSONArray> listener,
                                          @Nullable DjangoErrorListener errorListener,
                                          Context context) {
        super(
                method, url,
                (jsonRequest == null) ? null : jsonRequest.toString(),
                listener,
                errorListener,
                context);
    }

    /**
     * Constructor which defaults to <code>GET</code> if <code>jsonRequest</code> is
     * <code>null</code>, <code>POST</code> otherwise.
     *
     */
    public DjangoJSONArrayResponseRequest(String url,
                                          @Nullable JSONObject jsonRequest,
                                          Response.Listener<JSONArray> listener,
                                          @Nullable DjangoErrorListener errorListener,
                                          Context context) {
        this(
                jsonRequest == null ? Method.GET : Method.POST,
                url, jsonRequest, listener, errorListener, context);
    }

    /**
     * Returns response in JSONArray format
     *
     * @param response NetworkResponse
     * @return Response.success with JSONArray format of response or
     *         Response.error when response in non-JSONArray format
     *
     * @author Himanshu Shankar (https://himanshus.com)
     * @author Divya Tiwari (https://divyatiwari.me)
     */
    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, JsonRequest.PROTOCOL_CHARSET));

            return Response.success(new JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException |JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
