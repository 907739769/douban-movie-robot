package cn.jackding.doubanmovierobot.util;

import cn.jackding.doubanmovierobot.pojo.HttpClientResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * @Author Jack
 * @Date 2022/8/3 13:08
 * @Version 1.0.0
 */
@Slf4j
public class HttpClientUtils {

    // 编码格式。发送编码格式统一用UTF-8
    private static final String ENCODING = "UTF-8";

    // 设置连接超时时间，单位毫秒。
    private static final int CONNECT_TIMEOUT = 10000;

    // 请求获取数据的超时时间(即响应时间)，单位毫秒。
    private static final int SOCKET_TIMEOUT = 60000;

    /**
     * 发送get请求；带请求头和请求参数
     *
     * @param url     请求地址
     * @param headers 请求头集合
     * @param params  请求参数集合
     * @return
     * @throws Exception
     */
    public static HttpClientResult doGet(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        // 创建访问的地址
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            Set<Entry<String, String>> entrySet = params.entrySet();
            for (Entry<String, String> entry : entrySet) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }

        // 创建http对象
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpGet.setConfig(requestConfig);

        // 设置请求头
        packageHeader(headers, httpGet);

        HttpClientResult httpClientResult = null;
        long startTime = System.currentTimeMillis();

        // 创建httpClient对象
        try (CloseableHttpClient httpClient = HttpClients.createDefault();) {
            // 执行请求并获得响应结果
            return httpClientResult = getHttpClientResult(httpClient, httpGet);
        } finally {
            log.info("url:{},rsp:{},costTime:{}ms", httpGet.getURI(), httpClientResult, System.currentTimeMillis() - startTime);
        }
    }


    /**
     * 发送post请求；带请求头和请求参数
     *
     * @param url     请求地址
     * @param headers 请求头集合
     * @param params  请求参数集合
     * @return
     * @throws Exception
     */
    public static HttpClientResult doPost(String url, Map<String, String> headers, String params) throws Exception {

        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpPost.setConfig(requestConfig);
        packageHeader(headers, httpPost);

        // 封装请求参数
        if (params != null) {
            StringEntity stringEntity = new StringEntity(params, ENCODING);
            stringEntity.setContentType("application/json");
            // 设置到请求的http对象中
            httpPost.setEntity(stringEntity);
        }
        HttpClientResult httpClientResult = null;
        long startTime = System.currentTimeMillis();
        // 创建httpClient对象
        try (CloseableHttpClient httpClient = HttpClients.createDefault();) {
            // 执行请求并获得响应结果
            return httpClientResult = getHttpClientResult(httpClient, httpPost);
        } finally {
            log.info("url:{},req:{},rsp:{},costTime:{}ms", httpPost.getURI(), params, httpClientResult, System.currentTimeMillis() - startTime);
        }
    }

    /**
     * Description: 封装请求头
     *
     * @param params
     * @param httpMethod
     */
    public static void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
        // 封装请求头
        if (params != null) {
            Set<Entry<String, String>> entrySet = params.entrySet();
            for (Entry<String, String> entry : entrySet) {
                // 设置到请求头到HttpRequestBase对象中
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Description: 获得响应结果
     *
     * @param httpClient
     * @param httpMethod
     * @return
     * @throws Exception
     */
    public static HttpClientResult getHttpClientResult(CloseableHttpClient httpClient, HttpRequestBase httpMethod) throws Exception {
        try (CloseableHttpResponse httpResponse = httpClient.execute(httpMethod);) {
            // 获取返回结果
            if (httpResponse != null && httpResponse.getStatusLine() != null) {
                String content = null;
                if (httpResponse.getEntity() != null) {
                    content = EntityUtils.toString(httpResponse.getEntity(), ENCODING);
                }
                return new HttpClientResult(httpResponse.getStatusLine().getStatusCode(), content);
            }
        }
        return new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

}