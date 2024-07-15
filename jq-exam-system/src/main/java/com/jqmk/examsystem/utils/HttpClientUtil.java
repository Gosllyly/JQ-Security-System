package com.jqmk.examsystem.utils;

import com.jqmk.examsystem.errors.ErrorCodeEnum;
import com.jqmk.examsystem.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName HttpClientUtil
 * @Author tian
 * @Date 2024/6/5 13:55
 * @Description http 请求处理工具类，不论是 get 还是 post 请求，返回值均为 String
 */
@Slf4j
public class HttpClientUtil {
    private static final String EMPTY_STR = "";
    private static final String UTF_8 = "UTF-8";
    public static HttpClient client = new HttpClient();
    private static PoolingHttpClientConnectionManager connectionManager;

    private static void init() {
        if (connectionManager == null) {
            // 忽略SSL验证
            SSLContext sslContext = null;
            try {
                sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
                    @Override
                    public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        return true;
                    }
                }).build();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslContext))
                    .build();

            connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // 整个连接池最大连接数
            connectionManager.setMaxTotal(100);
            // 每路由最大连接数，默认值是2
            connectionManager.setDefaultMaxPerRoute(5);
        }
    }

    /**
     * 初始化ip
     */

    /**
     * 通过连接池获取HttpClient
     *
     * @return
     */
    public static CloseableHttpClient getHttpClient() {
        init();
        return HttpClients.custom().setConnectionManager(connectionManager).build();
    }

    /**
     * url 中可以传参
     *
     * @param url
     * @return
     */
    public static String httpGetRequestWithHeaders(String url) {
        HttpGet httpGet = new HttpGet(url);
        return getResult(httpGet);
    }

    /**
     * get 请求，自定义参数
     *
     * @param url
     * @param getParams
     * @return
     * @throws URISyntaxException
     */
    public static String httpGetRequestWithParams(String url, Map<String, Object> getParams) throws URISyntaxException {
        URIBuilder ub = new URIBuilder(url);
        ArrayList<NameValuePair> pairs = covertParams2NVPS(getParams);
        ub.setParameters(pairs);
        HttpGet httpGet = new HttpGet(ub.build());
        return getResult(httpGet);
    }

    /**
     * get 请求，自定义请求头
     *
     * @param url
     * @param headers
     * @return
     */
    public static String httpGetRequestWithHeaders(String url, Map<String, Object> headers) {
        HttpGet httpGet = new HttpGet(url);
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        return getResult(httpGet);
    }

    /**
     * get 请求，自定义参数、自定义 header
     *
     * @param url
     * @param headers
     * @param params
     * @return
     * @throws URISyntaxException
     */
    public static String httpGetRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws URISyntaxException {

        URIBuilder uriBuilder = new URIBuilder(url, Charset.forName("GBK"));
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        uriBuilder.setParameters(pairs);
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        return getResult(httpGet);
    }

    public static String doGet(String url, String charset) throws Exception {
        GetMethod method = new GetMethod(url);
        method.setDoAuthentication(true);
        int statusCode = client.executeMethod(method);
        byte[] responseBody =
                method.getResponseBodyAsString().getBytes(method.getResponseCharSet());
        //在返回响应消息使用编码(utf-8 或 gb2312)
        String response = new String(responseBody, StandardCharsets.UTF_8);
        //释放连接
        method.releaseConnection();
        return response;
    }

    /**
     * 无参 post 请求
     *
     * @param url
     * @return
     */
    public static String httpPostRequest(String url) {
        HttpPost httpPost = new HttpPost(url);
        return getResult(httpPost);
    }

    /**
     * post 请求， body 为 json
     *
     * @param url
     * @param jsonBody
     * @return
     */
    public static String httpPostRequest(String url, String jsonBody, Map<String, Object> headers) {
        HttpPost httpPost = new HttpPost(url);
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, Object> param : headers.entrySet()) {
                httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
            }
        }
        StringEntity entity = new StringEntity(jsonBody, "utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        return getResult(httpPost);
    }

    /**
     * post 请求，body 部分进行了 url 编码
     *
     * @param url
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String httpPostRequest(String url, Map<String, Object> params) {
        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
        } catch (UnsupportedEncodingException e) {
            throw new BizException(ErrorCodeEnum.BUILD_REQUEST_ERROR);
        }
        return getResult(httpPost);
    }

    public static String httpPostTest(String url, Map<String, Object> headers) {
        HttpPost httpPost = new HttpPost(url);
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, Object> param : headers.entrySet()) {
                httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
            }
        }
        return getResult(httpPost);
    }

    /**
     * post 请求，body 部分进行了 url 编码、自定义 header
     *
     * @param url
     * @param headers
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url, Charset.forName("GBK"));
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        uriBuilder.setParameters(pairs);
        HttpPost httpPost = new HttpPost(uriBuilder.build());
        for (Map.Entry<String, Object> header : headers.entrySet()) {
            httpPost.addHeader(header.getKey(), String.valueOf(header.getValue()));
        }
        return getResult(httpPost);
    }


    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<>();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }
        return pairs;
    }

    /**
     * 处理Http请求，返回值统一使用 String 格式，调用方根据需要转为 Json -> Object
     *
     * @param request
     * @return
     */
    private static String getResult(HttpRequestBase request) {
        CloseableHttpClient httpClient = getHttpClient();
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // long len = entity.getContentLength();// -1 表示长度未知
                    String result = EntityUtils.toString(entity);
                    response.close();
                    // httpClient.close();
                    return result;
                }
            } else {
                log.info("请求失败，url = {}, response_code = {}, reason = {}", request.getURI(), statusCode, statusLine.getReasonPhrase());
                throw new BizException(ErrorCodeEnum.HTTP_CLIENT_ERROR);
            }

        } catch (IOException e) {
            log.info("请求失败，url = {}", request.getURI(), e);
        }
        return EMPTY_STR;
    }
    final static int TIMEOUT = 1000;

    final static int TIMEOUT_MSEC = 5 * 1000;
    public static String doPost(String url, Map<String, String> paramMap) throws IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";

        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);

            // 创建参数列表
            if (paramMap != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (Map.Entry<String, String> param : paramMap.entrySet()) {
                    paramList.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }

            httpPost.setConfig(builderRequestConfig());

            // 执行http请求
            response = httpClient.execute(httpPost);

            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                throw e;
            }
        }

        return resultString;
    }

    private static RequestConfig builderRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(TIMEOUT_MSEC)
                .setConnectionRequestTimeout(TIMEOUT_MSEC)
                .setSocketTimeout(TIMEOUT_MSEC).build();
    }
}


