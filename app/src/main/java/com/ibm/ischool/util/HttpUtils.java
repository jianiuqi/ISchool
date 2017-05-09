package com.ibm.ischool.util;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;

import com.ibm.ischool.base.Constant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class HttpUtils {

	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public static void httpAsyncGet(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void httpAsyncPost(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    	client.setConnectTimeout(60000);
    	client.setTimeout(60000);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void httpAsyncPost(Context context, String url, JSONObject params, AsyncHttpResponseHandler responseHandler){
    	ByteArrayEntity entity = null;
    	try {
            entity = new ByteArrayEntity(params.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    	client.post(context, url, entity, "application/json", responseHandler);
    }
    
    private static String getAbsoluteUrl(String relativeUrl) {
        return Constant.BASE_SERVER_URL + relativeUrl;
    }
}
