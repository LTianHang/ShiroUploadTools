package Util;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;


public class HTTPUtils {
    /**
     * http方法发包
     *
     * @param sendURL
     * @param HTTPMethod
     * @param ProxyBooelan
     */
    public static HttpURLConnection HTTPSend(String sendURL, String HTTPMethod, Boolean ProxyBooelan, String rememberMeText) {
        HttpURLConnection conn = null;
        BufferedReader buffer = null;
        StringBuffer resultBuffer = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(sendURL);
            // 检查是否开启代理
            if (ProxyBooelan) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8080));
                conn = (HttpURLConnection) url.openConnection(proxy);
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod(HTTPMethod);
            conn.setUseCaches(false);
            if (rememberMeText != "") {
                conn.setRequestProperty("Cookie", "rememberMe=" + rememberMeText);
            } else {
                conn.setRequestProperty("Cookie", "rememberMe=test");

            }
            inputStream = conn.getInputStream();

            // 将inputStream数据转为StringBuffer类型
            resultBuffer = new StringBuffer();
            String line;
            buffer = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = buffer.readLine()) != null) {
                resultBuffer.append(line);
            }
//            System.out.println("result:" + resultBuffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * https方法发包
     *
     * @param sendURL
     * @param HTTPMethod
     * @param ProxyBooelan
     */
    public static HttpURLConnection HTTPSSend(String sendURL, String HTTPMethod, Boolean ProxyBooelan, String rememberMeText) {
        HttpsURLConnection conn = null;
        BufferedReader buffer = null;
        StringBuffer resultBuffer = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(sendURL);
            // 检查是否开启代理
            if (ProxyBooelan) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8080));
                conn = (HttpsURLConnection) url.openConnection(proxy);
            } else {
                conn = (HttpsURLConnection) url.openConnection();
            }
            // SSL证书
            SSLContext context = createIgnoreVerifySSL();
            conn.setSSLSocketFactory(context.getSocketFactory());

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod(HTTPMethod);
            conn.setUseCaches(false);
            if (rememberMeText != "") {
                conn.setRequestProperty("Cookie", "rememberMe=" + rememberMeText);
            } else {
                conn.setRequestProperty("Cookie", "rememberMe=test");

            }
            inputStream = conn.getInputStream();

            // 将inputStream数据转为StringBuffer类型
            resultBuffer = new StringBuffer();
            String line;
            buffer = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = buffer.readLine()) != null) {
                resultBuffer.append(line);
            }
//            System.out.println("result:" + resultBuffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    private static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("TLS");
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }
}
