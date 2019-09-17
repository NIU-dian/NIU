package com.yskj.push.framework.util;


import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class HttpRequestUtil {

    private HttpRequestUtil(){}


    private static final String DEFAULT_ENCODING = "UTF-8";

    private static Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);

    public static String saveFile(String url, String savePath) {
        // 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
        String result = "";
        FileOutputStream output = null;
        try {
            URL getUrl = new URL(url);
            // 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
            // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
            // 服务器
            connection.connect();
            // 取得输入流，并使用Reader读取
            InputStream input = connection.getInputStream();
            //设置本地保存的文件
            File saveFile = new File(savePath);
            output = new FileOutputStream(saveFile);
            byte[] b = new byte[1024];
            int i = 0;
            while ((i = input.read(b)) != -1) {
                output.write(b, 0, i);
            }
            output.flush();
            output.close();
            // 断开连接
            connection.disconnect();
        } catch (Exception e) {
            logger.error("httpRequest saveFile:{}", e);
        }finally {
            if( output != null){
                try {
                    output.close();
                } catch (IOException e) {
                    logger.error("关闭流异常:{}", e);
                }
            }
        }
        return result;
    }


    /**
     * 从网路上获取excel的内容
     * @param url
     * @param fileName
     * @param encoding
     * @return
     */
    public static List<List<String[]>> getExceFileContent(String url, String fileName, String encoding) {
        // 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
        List<List<String[]>> list = null;
        try {
            URL getUrl = new URL(url);
            // 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
            // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
            // 服务器
            connection.connect();
            // 取得输入流，并使用Reader读取
            InputStream input = connection.getInputStream();
            list = ReadExcel.readXlsByStream(input, fileName);
            // 断开连接
            connection.disconnect();
        } catch (Exception e) {
            logger.error("httpRequest saveFile:{}", e);
        }
        return list;
    }






    public static String getFileContent(String url, String encoding) {
        // 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
        String result = "";
        FileOutputStream output = null;
        try {
            URL getUrl = new URL(url);
            // 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
            // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
            // 服务器
            connection.connect();
            // 取得输入流，并使用Reader读取
            InputStream input = connection.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            result = outputStream.toString(encoding);
            // 断开连接
            connection.disconnect();
        } catch (Exception e) {
            logger.error("httpRequest saveFile:{}", e);
        }finally {
            if( output != null){
                try {
                    output.close();
                } catch (IOException e) {
                    logger.error("关闭流异常:{}", e);
                }
            }
        }
        return result;
    }

    /**
     * 根据url地址获取网络信息
     *
     * @param url 网络地址
     * @return
     */
    public static String get(String url) {
        return get(url, null);
    }

    /**
     * 根据网络地址和参数获取网络信息
     *
     * @param url    网络地址
     * @param params 参数信息
     * @return
     */
    public static String get(String url, Map<String, Object> params) {
        return get(url, params, null);
    }


    public static String get(String url, Map<String, Object> params, String encoding) {
        String requestEncoding = DEFAULT_ENCODING;
        if (StringUtils.isNotEmpty(encoding)) {
            requestEncoding = encoding;
        }
        // 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
        StringBuilder getURL = new StringBuilder(url);
        StringBuilder result = new StringBuilder();
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            if (null != params && params.size() > 0) {
                getURL.append("?");
                for (Entry<String, Object> param : params.entrySet()) {
                    if (param.getValue() != null) {
                        getURL.append(param.getKey()).append("=").append(param.getValue().toString()).append("&");
                    }
                }
                getURL.deleteCharAt(getURL.length() - 1);
            }
            logger.info("http request get url:" + getURL.toString());
            URL getUrl = new URL(getURL.toString());
            // 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
            // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
            connection = (HttpURLConnection) getUrl.openConnection();

            connection.setConnectTimeout(30 * 1000);
            connection.setReadTimeout(30 * 1000);

            // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
            // 服务器
            connection.connect();
            // 取得输入流，并使用Reader读取
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), requestEncoding));//设置编码,否则中文乱码
            String lines;
            while ((lines = reader.readLine()) != null) {
                result.append(lines);
            }
            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (Exception e) {
            logger.error("request get error", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                logger.error("request get error", e);
            }
        }
        logger.info("http request result:{}", result);
        return result.toString();
    }

    public static String post(String url, Map<String, Object> params) {
        return post(url, params,null, DEFAULT_ENCODING);
    }

    public static String post(String url, Map<String, Object> params, Map<String, String> heads) {
        return post(url, params, heads, DEFAULT_ENCODING);
    }

    public static String post(String url, Map<String, Object> params, Map<String, String> heads, String encoding) {
        String requestEncoding = DEFAULT_ENCODING;
        if (StringUtils.isNotEmpty(encoding)) {
            requestEncoding  = encoding;
        }
        String result = "";
        try {
            logger.info("http request post url:{}, params:{}", url, JSON.toJSONString(params));
            URL postUrl = new URL(url);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) postUrl
                    .openConnection();

            // 设置是否向connection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true
            connection.setDoOutput(true);
            // Read from the connection. Default is true.
            connection.setDoInput(true);
            // Set the post method. Default is GET
            connection.setRequestMethod("POST");
            // Post 请求不能使用缓存
            connection.setUseCaches(false);
            // This method takes effects to every instances of this class.
            // URLConnection.setFollowRedirects是static函数，作用于所有的URLConnection对象。
            // This methods only takes effacts to this instance.
            // URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
            connection.setInstanceFollowRedirects(true);
            // Set the content type to urlencoded,because we will write some URL-encoded content to the connection.
            // Settings above must be set before connect!
            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
            // 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
            // 进行编码
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            //设置超时时间为6s
            connection.setConnectTimeout(30 * 1000);
            //设置超时时间6s
            connection.setReadTimeout(30 * 1000);
            //设置请求头信息
            if( heads != null && !heads.isEmpty() ){
                for (Entry<String, Object> param : params.entrySet()) {
                    if (null == param.getValue()) {
                        connection.setRequestProperty(param.getKey(),"");
                    } else {
                        connection.setRequestProperty(param.getKey(),param.getValue().toString());
                    }
                }
            }

            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
            // 要注意的是connection.getOutputStream会隐含的进行connect。
            connection.connect();

            if (null != params && !params.isEmpty() ) {
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                StringBuilder content = new StringBuilder("");
                for (Entry<String, Object> param : params.entrySet()) {
                    if (null == param.getValue()) {
                        content.append(param.getKey()).append("=").append(URLEncoder.encode("", requestEncoding)).append("&");
                    } else {
                        content.append(param.getKey()).append("=").append(URLEncoder.encode(param.getValue().toString(), requestEncoding)).append("&");
                    }
                }
                content.deleteCharAt(content.length() - 1);
                // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
                out.writeBytes(content.toString());
                out.flush();
                out.close();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), requestEncoding));//设置编码,否则中文乱码
            String line = "";
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            logger.error("request post:{}", e);
        }
        logger.info("http request post result:{}", result);
        return result;
    }


    public static String postWithJson(String url, String json){
        return postWithJson(url, json, null);
    }


    /**
     * 使用POST传递json数据
     *
     * @param url
     * @param json
     * @throws Exception
     */
    public static String postWithJson(String url, String json, Map<String, Object> heads) {
        String response = null;
        logger.info("postJson url:{}, json:{}", url, json);
        try {
            //创建连接
            URL postUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json");
            //设置请求头信息
            if (null != heads && heads.size() > 0) {
                for (Entry<String, Object> head : heads.entrySet()) {
                    connection.setRequestProperty(head.getKey(), head.getValue().toString());
                }
            }

            connection.connect();

            //POST请求
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(json.getBytes("UTF-8"));
            out.flush();
            out.close();

            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            reader.close();
            // 断开连接
            connection.disconnect();
            response = sb.toString();
            logger.info("postJsonResult:{}", response);
        } catch (Exception e) {
            logger.info("postJsonResult error:{}", e);
        }
        return response;

    }

    /**
     * @param f   保存的文件
     * @param url 图片地址
     */
    public static void down(File f, String url) {
        byte[] buffer = new byte[8 * 1024];
        URL u;
        URLConnection connection = null;
        try {
            u = new URL(url);
            connection = u.openConnection();
        } catch (Exception e) {
            logger.error("ERR:" + url, e);
            return;
        }
        connection.setReadTimeout(100000);
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            connection.connect();
        } catch (IOException e1) {
            logger.error("IO error:{}", e1);
        }
        try {
            f.createNewFile();
            is = connection.getInputStream();
            fos = new FileOutputStream(f);
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            logger.error("写文件异常：{}", e);
            f.delete();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.error("IO error:{}", e);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("保存文件异常:{}", e);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private static TrustManager[] TRUST_MANAGER = {new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }


        public void checkServerTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {
        }
    }};

    /**
     * 使用post格式上传文件
     *
     * @param bytes
     * @param url
     * @param fileName
     * @return
     */
    public static String transferImg(byte[] bytes, String url, String fileName) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", "SOHUWapRebot");
        httpPost.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
        httpPost.setHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.7");
        httpPost.setHeader("Connection", "keep-alive");
        MultipartEntity multiEntity = new MultipartEntity();
        multiEntity.addPart("attachment", new ByteArrayBody(bytes, fileName));

        try {
            multiEntity.addPart("project", new StringBody(fileName));
        } catch (UnsupportedEncodingException var10) {
            logger.error("不支持的编码异常:{}", var10);
        }

        httpPost.setEntity(multiEntity);
        DefaultHttpClient httpclient = null;
        try {
             httpclient = new DefaultHttpClient();
            logger.info("上传文件到静态资源服务器: url:{}, fileName:{}",url, fileName );
            HttpResponse httpResponse = httpclient.execute(httpPost);
            HttpEntity e = httpResponse.getEntity();
            return EntityUtils.toString(e);
        } catch (IOException e) {
            logger.error("使用post格式上传文件 异常:{}", e);
        }finally {
            if( httpclient != null ){
                httpclient.close();
            }
        }

        return null;
    }
}
