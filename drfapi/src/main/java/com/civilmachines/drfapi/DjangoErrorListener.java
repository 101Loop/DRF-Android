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

/**
 * An abstract class that implements {@link Response.ErrorListener} and handles
 * error commonly raised by Django REST Framework based REST APIs.
 *
 * This class has 18 abstract functions that are required to be implemented.
 *
 * onErrorResponse function parses an error from API and invokes appropriate
 * function.
 *
 * @author Himanshu Shankar (https://himanshus.com)
 * @author Divya Tiwar (https://divyatiwari.me)
 */
public abstract class DjangoErrorListener implements Response.ErrorListener {

    /**
     * Function should handle error: onNetworkError
     * This error is thrown by Android when some network error occurs.
     *
     * @param response message String that is provided in error by Android
     */
    public abstract void onNetworkError(String response);

    /**
     * Function should handle onAuthFailure Error
     * This is thrown by Android.
     * Although server based auth error (403/401) are also categorized
     * as onAuthFailureError by Android, those errors invoke
     * onForbiddenError.
     *
     * @param response message String that is provided in error by Android
     *                 or sent by server
     */
    public abstract void onAuthFailureError(String response);

    /**
     * Function should handle timeout Error
     * This is thrown by Android.
     *
     * @param response message String that is provided in error by Android
     */
    public abstract void onTimeoutError(String response);

    /**
     * Function should handle no connection error thrown by Android when due to
     * any circumstance, connection can't be established.
     *
     * @param response message String that is provided in error by Android
     */
    public abstract void onNoConnectionError(String response);

    /**
     * Function should handle parse error thrown by Android when server sends a
     * successful status code but the response is not in proper format.
     *
     * While making a DELETE request with Django, expect this error to be
     * thrown as the server sends a blank body.
     *
     * @param response message String that is sent by server
     */
    public abstract void onParseError(String response);

    /**
     * Function should handle 415 Error code i.e. Method Not Allowed
     *
     * @param message extracted from "detail" key in JSON Body
     */
    public abstract void onMethodNotAllowedError(String message);

    /**
     * Function should handle 404 Error code i.e. Not Found
     *
     * @param message extracted from "detail" key in JSON Body or
     *                a default string
     */
    public abstract void onNotFoundError(String message);

    /**
     * Function should handle 400 Error code i.e. Bad Request
     * This occurs at various occasions such as:
     * 1. Server is not configured properly (ALLOWED_HOST incorrect)
     * 2. Client based error
     *
     * @param message extracted from "detail" key in JSON Body or
     *                a default string
     */
    public abstract void onBadRequestError(String message);

    /**
     * Function should handle 400 Error code i.e. Bad Request
     * This function is called only when client has not sent a proper
     * JSON Request.
     *
     * @param response raw JSONObject sent from server. This needs
     *                 to be parsed here as it may contain error in
     *                 following format:
     *                 <pre>
     *                     {@code {"field": ["message"], ...}}
     *                 </pre>
     */
    public abstract void onBadRequestError(JSONObject response);

    /**
     * Function should handle 403 & 401 Error code i.e. Forbidden & Unauthorized
     * It is expected from programmer to logout user, if logged in, and clear
     * all private data in this function.
     *
     * @param message extracted from "detail" key in JSON Body or
     *                a default string
     */
    public abstract void onForbiddenError(String message);

    /**
     * Function should handle 422 Error code i.e. Unprocessable Entity
     *
     * @param message extracted from "detail" or "data" key in JSON Body
     */
    public abstract void onUnprocessableEntityError(String message);

    /**
     * Function should handle 422 Error code i.e. Unprocessable Entity
     *
     * @param response raw JSONObject sent from server
     */
    public abstract void onUnprocessableEntityError(JSONObject response);

    /**
     * Function should handle 415 Error code i.e. Unsupported Media Type
     *
     * @param message extracted from "detail" or "data" key in JSON Body
     */
    public abstract void onUnsupportedMediaTypeError(String message);

    /**
     * Function should handle a non JSON Error which is neither in JSON
     * Format nor in HTML Format
     *
     * @param response raw String body of response
     */
    public abstract void onNonJsonError(String response);

    /**
     * Function should handle JSON Error which is not categorized into
     * any of above thrown errors
     *
     * @param response raw JSON body of response
     */
    public abstract void onDefaultJsonError(JSONObject response);

    /**
     * Function should handle a error in HTML Format
     *
     * @param response raw String body of response which is in HTML format
     */
    public abstract void onDefaultHTMLError(String response);

    /**
     * Function should handle a server error i.e. error response having a
     * 5xx error code
     *
     * @param response raw String body of response
     */
    public abstract void onServerError(String response);

    /**
     * Function should handle an non categorized error
     *
     * @param response raw String body of response
     */
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
                        break;
                    }
                    case 404: {
                        onNotFoundError("API Endpoint not found.");
                        break;
                    }
                    default: {
                        onDefaultHTMLError(response);
                        break;
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
                            break;
                        }
                        case 404: {
                            // Object not found error
                            onNotFoundError(error_response.optString("detail",
                                    "Object with provided detail does not exists."));
                            break;
                        }
                        case 400: {
                            // Bad request error
                            if (error_response.optString("detail", null) != null)
                                onBadRequestError(error_response.optString("detail"));
                            else
                                onBadRequestError(error_response);
                            break;
                        }
                        case 403 | 401: {
                            onForbiddenError(error_response.optString("detail",
                                    "You're not allowed to make this request."));
                            break;
                        }
                        case 422: {
                            // Similar use case as of bad request, used in drf_user
                            if (error_response.has("data"))
                                onUnprocessableEntityError(error_response.optString("data"));

                            else if (error_response.has("detail"))
                                onUnprocessableEntityError(error_response.optString("detail"));
                            else
                                onUnprocessableEntityError(error_response);
                            break;

                        }
                        case 415: {
                            // Request sent in unsupported media type, such as text
                            onUnsupportedMediaTypeError(error_response.optString("detail",
                                    "Request sent in invalid format."));
                            break;
                        }
                        default: {
                            // Default case scenario
                            onDefaultJsonError(error_response);
                            break;
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
        else if (error instanceof AuthFailureError) {
            JSONObject error_response;
            try {
                error_response = new JSONObject(response);
                onForbiddenError(error_response.optString("detail", "Couldn't perform task because of permission error."));
            } catch (JSONException ex) {
                onAuthFailureError(response);
            }
        }
        else
            onDefaultError(response);
    }
}
