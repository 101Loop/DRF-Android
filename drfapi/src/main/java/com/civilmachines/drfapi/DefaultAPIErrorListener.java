package com.civilmachines.drfapi;

import org.json.JSONObject;


/**
 * A class that implements all abstract method of DjangoErrorListener with blank
 * function body. It requires user to override and implement the same in activity
 * where Django Request is being created. This way, user has access to all activity/
 * context based variables.
 *
 * Programmer is expected to use this in a following manner:
 *
 * <pre>{@code
 *     new DefaultAPIErrorListener(){
 *         public void onNetworkError(String response){
 *             // Do something
 *             // Use variable present in activity/class such as alert dialog etc.
 *         }
 *     };
 * }</pre>
 *
 * @author <a href=https://himanshus.com target=_blank>Himanshu Shankar</a>
 * @author <a href="https://divyatiwari.me" target="_blank">Divya Tiwari</a>
 */
public class DefaultAPIErrorListener extends DjangoErrorListener {

    /**
     * {@inheritDoc}
     * @param response message String that is provided in error by Android
     */
    @Override
    public void onNetworkError(String response) {

    }

    /**
     * {@inheritDoc}
     * @param response message String that is provided in error by Android
     */
    @Override
    public void onAuthFailureError(String response) {

    }

    /**
     * {@inheritDoc}
     * @param response message String that is provided in error by Android
     */
    @Override
    public void onTimeoutError(String response) {

    }

    /**
     * {@inheritDoc}
     * @param response message String that is provided in error by Android
     */
    @Override
    public void onNoConnectionError(String response) {

    }

    /**
     * {@inheritDoc}
     * @param response message String that is sent by server
     */
    @Override
    public void onParseError(String response) {

    }

    /**
     * {@inheritDoc}
     * @param message extracted from "detail" key in JSON Body
     */
    @Override
    public void onMethodNotAllowedError(String message) {

    }

    /**
     * {@inheritDoc}
     * @param message extracted from "detail" key in JSON Body or
     */
    @Override
    public void onNotFoundError(String message) {

    }

    /**
     * {@inheritDoc}
     * @param message extracted from "detail" key in JSON Body or
     */
    @Override
    public void onBadRequestError(String message) {

    }

    /**
     * {@inheritDoc}
     * @param response raw JSONObject sent from server. This needs
     *                 to be parsed here as it may contain error in
     *                 following format:
     *                 <pre>
     *                     {@code {"field": ["message"], ...}}
     *                 </pre>
     */
    @Override
    public void onBadRequestError(JSONObject response) {

    }

    /**
     * {@inheritDoc}
     * @param message extracted from "detail" key in JSON Body or
     */
    @Override
    public void onForbiddenError(String message) {

    }

    /**
     * {@inheritDoc}
     * @param message extracted from "detail" or "data" key in JSON Body
     */
    @Override
    public void onUnprocessableEntityError(String message) {

    }

    /**
     * {@inheritDoc}
     * @param response raw JSONObject sent from server
     */
    @Override
    public void onUnprocessableEntityError(JSONObject response) {

    }

    /**
     * {@inheritDoc}
     * @param message extracted from "detail" or "data" key in JSON Body
     */
    @Override
    public void onUnsupportedMediaTypeError(String message) {

    }

    /**
     * {@inheritDoc}
     * @param response raw String body of response
     */
    @Override
    public void onNonJsonError(String response) {

    }

    /**
     * {@inheritDoc}
     * @param response raw JSON body of response
     */
    @Override
    public void onDefaultJsonError(JSONObject response) {

    }

    /**
     * {@inheritDoc}
     * @param response raw String body of response which is in HTML format
     */
    @Override
    public void onDefaultHTMLError(String response) {

    }

    /**
     * {@inheritDoc}
     * @param response raw String body of response
     */
    @Override
    public void onServerError(String response) {

    }

    /**
     * {@inheritDoc}
     * @param response raw String body of response
     */
    @Override
    public void onDefaultError(String response) {

    }
}
