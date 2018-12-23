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

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.ParseError;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class DjangoErrorListener implements Response.ErrorListener {

    public abstract void onNetworkError(String response);
    public abstract void onAuthFailureError(String response);
    public abstract void onTimeoutError(String response);
    public abstract void onNoConnectionError(String response);
    public abstract void onParseError(String response);

    public abstract void onMethodNotAllowedError(String message);
    public abstract void onNotFoundError(String message);

    public abstract void onBadRequestError(String message);
    public abstract void onBadRequestError(JSONObject response);

    public abstract void onForbiddenError(String message);

    public abstract void onUnprocessableEntityError(String message);
    public abstract void onUnprocessableEntityError(JSONObject response);

    public abstract void onUnsupportedMediaTypeError(String message);

    public abstract void onNonJsonError(String response);
    public abstract void onDefaultJsonError(JSONObject response);
    public abstract void onDefaultHTMLError(String response);

    public abstract void onServerError(String response);
    public abstract void onDefaultError(String response);

    /**
     * Parses the error based on StatusCode
     * @param error A {@link VolleyError} object.
     */
    public void onErrorResponse(VolleyError error) {
        // Extract response in form of string from data
        String response = new String(error.networkResponse.data);

        // Extract status code for future use
        int statusCode = error.networkResponse.statusCode;

        // Check of response is in HTML format
        boolean is_html_response;
        try {
            is_html_response = (error.networkResponse.headers.containsKey("Content-Type")
                    && error.networkResponse.headers.get("Content-Type").equals("text/html"));
        } catch (NullPointerException ex) {
            is_html_response = false;
        }

        // Check if error is an instance of ClientError i.e. error is on the client side
        if (error instanceof ClientError) {
            // Get error based on Status Code if error is in HTML format
            if (is_html_response) {
                switch (statusCode) {
                    case 400: {
                        onBadRequestError("Server configuration has some error.");
                    }
                    case 404: {
                        onNotFoundError("API Endpoint not found.");
                    }
                    default: {
                        onDefaultHTMLError(response);
                    }
                }
            } else {
                try {
                    // Try to convert response into a JSONObject
                    JSONObject error_response = new JSONObject(response);

                    // Switch case over status code for appropriate DjangoAPIError object
                    switch (statusCode) {
                        case 405: {
                            // Method not allowed error
                            onMethodNotAllowedError(error_response.optString("detail",
                                    "Invalid method used in request."));
                        }
                        case 404: {
                            // Object not found error
                            onNotFoundError(error_response.optString("detail",
                                    "Object with provided detail does not exists."));
                        }
                        case 400: {
                            // Bad request error
                            if (error_response.optString("detail") != null)
                                onBadRequestError(error_response.optString("detail"));
                            else
                                onBadRequestError(error_response);
                        }
                        case 403 | 401: {
                            // Must trigger a logout signal
                            onForbiddenError(error_response.optString("detail",
                                    "You're not allowed to make this request."));
                        }
                        case 422: {
                            // Similar use case as of bad request, used in drf_user
                            if (error_response.has("data"))
                                onUnprocessableEntityError(error_response.optString("data"));

                            else if (error_response.has("detail"))
                                onUnprocessableEntityError(error_response.optString("detail"));
                            else
                                onUnprocessableEntityError(error_response);

                        }
                        case 415: {
                            // Request sent in unsupported media type, such as text
                            onUnsupportedMediaTypeError(error_response.optString("detail",
                                    "Request sent in invalid format."));
                        }
                        default: {
                            // Default case scenario
                            onDefaultJsonError(error_response);
                        }
                    }
                } catch (JSONException ex) {
                    // Error is not a possible JSON Object, nor a HTML body.
                    onNonJsonError(response);
                }
            }
        }

        else if (error instanceof ServerError)
            onServerError(response);
        else if (error instanceof TimeoutError)
            onTimeoutError(response);
        else if (error instanceof ParseError)
            onParseError(response);
        else if (error instanceof NoConnectionError)
            onNoConnectionError(response);
        else if (error instanceof NetworkError)
            onNetworkError(response);
        else if( error instanceof AuthFailureError)
            onAuthFailureError(response);
        else
            onDefaultError(response);
    }
}
