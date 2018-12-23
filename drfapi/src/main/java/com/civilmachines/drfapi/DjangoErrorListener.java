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

    abstract void onNetworkError(String response);
    abstract void onAuthFailureError(String response);
    abstract void onTimeoutError(String response);
    abstract void onNoConnectionError(String response);
    abstract void onParseError(String response);

    abstract void onMethodNotAllowedError(String message);
    abstract void onNotFoundError(String message);

    abstract void onBadRequestError(String message);
    abstract void onBadRequestError(JSONObject response);

    abstract void onForbiddenError(String message);

    abstract void onUnprocessableEntityError(String message);
    abstract void onUnprocessableEntityError(JSONObject response);

    abstract void onUnsupportedMediaTypeError(String message);

    abstract void onNonJsonError(String response);
    abstract void onDefaultJsonError(JSONObject response);
    abstract void onDefaultHTMLError(String response);

    abstract void onServerError(String response);
    abstract void onDefaultError(String response);

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
