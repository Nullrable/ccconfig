package io.cc.config.client.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author nhsoft.lsd
 */
public interface HttpUtils {

    Logger log = LoggerFactory.getLogger(HttpUtils.class);

    HttpInvoker Default = new OkHttpInvoker();

    static HttpInvoker getDefault() {
        if(((OkHttpInvoker)Default).isInitialized()) {
            return Default;
        }
        int timeout = Integer.parseInt(System.getProperty("utils.http.timeout", "1000"));
        int maxIdleConnections = Integer.parseInt(System.getProperty("utils.http.maxconn", "128"));
        int keepAliveDuration = Integer.parseInt(System.getProperty("utils.http.keepalive", "300"));
        ((OkHttpInvoker)Default).init(timeout, maxIdleConnections, keepAliveDuration);
        return Default;
    }

    static String get(String url) {
        return getDefault().get(url);
    }

    static String post(String requestString, String url) {
        return getDefault().post(requestString, url);
    }

    @SneakyThrows
    static <T> T httpGet(String url, Class<T> clazz) {
        log.debug(" =====>>>>>> httpGet: " + url);
        String respJson = get(url);
        log.debug(" =====>>>>>> response: " + respJson);
        return JSON.parseObject(respJson, clazz);
    }

    @SneakyThrows
    static <T> T httpGet(String url, TypeReference<T> typeReference) {
        log.debug(" =====>>>>>> httpGet: " + url);
        String respJson = get(url);
        log.debug(" =====>>>>>> response: " + respJson);
        return JSON.parseObject(respJson, typeReference);
    }

    @SneakyThrows
    static <T> T httpPost(String requestString, String url, Class<T> clazz) {
        log.debug(" =====>>>>>> httpPost: " + url);
        String respJson = post(requestString, url);
        log.debug(" =====>>>>>> response: " + respJson);
        return JSON.parseObject(respJson, clazz);
    }

    @SneakyThrows
    static <T> T httpPost(String requestString, String url, TypeReference<T> typeReference) {
        log.debug(" =====>>>>>> httpPost: " + url);
        String respJson = post(requestString, url);
        log.debug(" =====>>>>>> response: " + respJson);
        return JSON.parseObject(respJson, typeReference);
    }

    interface HttpInvoker {
        String post(String requestString, String url);
        String get(String url);
    }

    @Slf4j
    class OkHttpInvoker implements HttpInvoker {
        final static MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");


        @Getter
        boolean initialized = false;
        OkHttpClient client;

        public void init(int timeout, int maxIdleConnections, int keepAliveDuration) {
            client = new OkHttpClient.Builder()
                    .connectionPool(new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.SECONDS))
                    .readTimeout(timeout, TimeUnit.MILLISECONDS)
                    .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                    .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
            initialized = true;
        }

        public String post(String requestString, String url) {
            log.debug(" ===> post  url = {}, requestString = {}", requestString, url);
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(requestString, JSONTYPE))
                    .build();
            try {
                String respJson = client.newCall(request).execute().body().string();
                log.debug(" ===> respJson = " + respJson);
                return respJson;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String get(String url) {
            log.debug(" ===> get url = " + url);
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            try {
                String respJson = client.newCall(request).execute().body().string();
                log.debug(" ===> respJson = " + respJson);
                return respJson;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
