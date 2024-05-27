package com.github.binarywang.demo.wx.cp.utils;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class OkHttpUtils {

    private static Logger log = LoggerFactory.getLogger(OkHttpUtils.class);

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.get("text/PermanentAuthorizationCodeResult-markdown; charset=utf-8");
    public static final MediaType MEDIA_TYPE_URLENCODED = MediaType.get("application/PermanentAuthorizationCodeResult-www-form-urlencoded; charset=utf-8");
    public static final MediaType MEDIA_TYPE_JSON = MediaType.get("application/json; charset=utf-8");

    public static OkHttpClient client;

    /**
     * @return
     */
    public static OkHttpClient client() {
        if (client == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            // 设置连接超时时间
            builder.connectTimeout(60, TimeUnit.SECONDS);
            // 设置读取超时时间
            builder.readTimeout(60, TimeUnit.SECONDS);
            client = builder.build();
        }
        return client;
    }

    /**
     * 设置请求头
     *
     * @param headersParams
     * @return
     */
    public static Headers setHeaders(Map<String, String> headersParams) {
        Headers.Builder builder = new Headers.Builder();
        if (headersParams != null) {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                builder.add(key, headersParams.get(key));
                log.info("headers key={},value={}", key, headersParams.get(key));
            }
        }
        return builder.build();
    }

    /**
     * 发送POST请求
     *
     * @param url
     * @param json
     * @return
     */
    public static String post(String url, String json) {
        return post(url, json, MEDIA_TYPE_JSON, null);
    }

    /**
     * 发送POST请求
     *
     * @param url
     * @param json
     * @return
     */
    public static String post(String url, String json, MediaType mediaType, Headers headers) {
        return post(url, RequestBody.create(json, mediaType), headers);
    }

    /**
     * 发送POST请求
     *
     * @param url
     * @param params
     * @return
     */
    public static String post(String url, Map<String, String> params, Headers headers) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            formBuilder.add(key, params.get(key));
        }
        return post(url, formBuilder.build(), headers);
    }

    /**
     * 发送POST请求
     *
     * @param url
     * @param bean
     * @return
     */
    public static <T> String post(String url, T bean, Headers headers) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                formBuilder.add(String.valueOf(key), String.valueOf(beanMap.get(key)));
            }
        }
        return post(url, formBuilder.build(), headers);
    }

    /**
     * 发送POST请求
     *
     * @param url
     * @param requestBody
     * @return
     */
    public static String post(String url, RequestBody requestBody, Headers headers) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        log.debug("post url={}", url);
        if (headers != null && headers.size() > 0) {
            builder.headers(headers);
            log.debug("post headers={}", headers);
        }
        log.debug("post requestBody ={}", requestBody);
        builder.post(requestBody);
        try {
            Response response = client().newCall(builder.build()).execute();
            log.info("url={},protocol:{},code:{},message:{}", url, response.protocol(), response.code(), response.message());
            String string = response.body().string();
            log.debug("response: {}", string);
            if (response.isSuccessful()) {
                return string;
            }
            return null;
        } catch (Exception e) {
            log.debug("url={},onFailure: {}", url, e.getMessage());
        }
        return null;
    }

    /**
     * 异步 发送POST请求 异步
     *
     * @param url
     * @param requestBody
     * @return
     */
    public static void postAsyn(String url, RequestBody requestBody, Headers headers, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        log.debug("postAsyn url={}", url);
        if (headers != null && headers.size() > 0) {
            builder.headers(headers);
            log.debug("post headers={}", headers);
        }
        builder.post(requestBody);
        Request request = builder.build();
        client().newCall(request).enqueue(callback);
    }

    /**
     * 发送 get 请求
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        return get(url, null);
    }

    /**
     * 发送 get 请求
     *
     * @param url
     * @return
     */
    public static String get(String url, Headers headers) {
        return get(url, headers, new HashMap<>());
    }

    /**
     * 发送 get 请求
     *
     * @param url
     * @return
     */
    public static String get(String url, Headers headers, Map<String, Object> parameters) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        log.debug("get parameters={}", parameters);
        if (parameters != null && parameters.size() > 0) {
            for (Map.Entry<String, Object> param : parameters.entrySet()) {
                httpBuilder.addQueryParameter(param.getKey(), String.valueOf(param.getValue()) );
            }
        }
        Request.Builder builder = new Request.Builder();
        builder.url(httpBuilder.build());
        log.debug("get url={}", url);
        if (headers != null && headers.size() > 0) {
            builder.headers(headers);
            log.debug("get headers={}", headers);
        }
        builder.get();
        try {
            Response response = client().newCall(builder.build()).execute();
            log.info("url={},protocol:{},code:{},message:{}", url, response.protocol(), response.code(), response.message());
            String string = response.body().string();
            log.debug("response: {}", string);
            if (response.isSuccessful()) {
                return string;
            }
            return null;
        } catch (Exception e) {
            log.debug("url={},onFailure: {}", url, e.getMessage());
        }
        return null;
    }

    /**
     * 异步 发送get请求  异步
     *
     * @param url
     * @return
     */
    public static void getAsyn(String url, Headers headers, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        log.debug("getAsyn url={}", url);
        if (headers != null && headers.size() > 0) {
            builder.headers(headers);
            log.debug("get headers={}", headers);
        }
        builder.get();
        Request request = builder.build();
        client().newCall(request).enqueue(callback);
    }

    /**
     * 发送get请求  异步
     *
     * @param url
     * @return
     */
    public static void getAsyn(String url, Callback callback) {
        getAsyn(url, null, callback);
    }


    public static Request buildRequest(String url, String param) {

        MediaType mediaType = MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, param);
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .addHeader("content-type", "application/json")
            .build();
        return request;
    }
}
