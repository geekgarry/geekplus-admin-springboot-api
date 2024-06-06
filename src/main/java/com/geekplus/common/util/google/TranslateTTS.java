package com.geekplus.common.util.google;

import com.alibaba.fastjson.JSONObject;
import com.geekplus.common.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 6/3/24 3:16 AM
 * description: 做什么的？
 */
@Slf4j
public class TranslateTTS {
//    client: 指定客户端类型，这里使用tw-ob。
//    ie: 输入编码，这里使用UTF-8。
//    tl: 语言代码，用于指定发音语言。
//    q: 要转换为语音的文本。
//    请求头: 需要添加User-Agent头来模拟浏览器请求。
//    返回值: 音频数据，MIME类型为audio/mpeg。
    String ttsUrl="http://translate.google.com/translate_tts?ie=UTF-8&q=%E6%BA%90%E8%AF%AD%E8%A8%80&tl=zh-CN&total=1&idx=0&textlen=3";
    //https://translate.google.com/translate_tts?ie=UTF-8&total=1&idx=0&textlen=32&client=tw-ob&q=Hello%20World&tl=en-gb
    //Google TranslateTTS 请求
    public static Object getGoogleTTS(String url, String ttsText, Map<String, String> headerMap) throws UnsupportedEncodingException {
        RestTemplate client = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        headerMap.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
        httpHeaders.setAll(headerMap);
        if(url==null||url==""){
            url="http://translate.google.com/translate_tts?ie=utf-8&total=1&idx=0&textlen=99999&client=tw-ob"
            + "&tl=zh-CN"
            + "&q=" + URLEncoder.encode(ttsText, "UTF-8");
        }
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity response = client.exchange(url, HttpMethod.GET, entity, Object.class);
        System.out.println("返回响应体：");
        System.out.println(response.getBody());
        return response.getBody();
    }

    public static void getGoogleTTSVoice(HttpServletResponse response, String url, String ttsText) {
        try {
            response.setCharacterEncoding("UTF-8");
            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            //response.setContentType("multipart/form-data");
            response.setContentType("audio/mpeg");
            response.setHeader("Content-Disposition", "inline; filename=ttsCNText");
            if(url==null||url==""||"".equals(url)){
                url="http://translate.google.com/translate_tts?ie=utf-8&total=1&idx=0&textlen=99999&client=tw-ob"
                        + "&tl=zh-CN"
                        + "&q=" + URLEncoder.encode(ttsText, "UTF-8");
            }
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置连接方式
            conn.setRequestMethod("GET");
            // 设置主机连接时间超时时间10000毫秒
            conn.setConnectTimeout(10000);
            // 设置读取远程返回数据的时间6000毫秒
            conn.setReadTimeout(8000);
            // 发送请求
            conn.connect();
            // 获取输入流
            InputStream is = new BufferedInputStream(conn.getInputStream());
            //BufferedInputStream bis=new BufferedInputStream(is);
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes=new byte[1024*1024*5];
            int len=0;
            while ((len=is.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
            }
            // 封装输入流
            //BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            // 接收读取数据
            //StringBuffer sb = new StringBuffer();
            //String line = null;
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//                sb.append("\r\n");
//            }
//            if (null != br) {
//                br.close();
//            }
//            if (null != is) {
//                is.close();
//            }
            // 关闭连接
            outputStream.flush();
            conn.disconnect();
        }catch (UnsupportedEncodingException | MalformedURLException e){
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    public static void postGoogleTTSVoice(HttpServletResponse response, String url, String ttsText) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            //response.setContentType("multipart/form-data");
            response.setContentType("audio/mpeg");
            response.setHeader("Content-Disposition", "inline; filename=ttsCNText");
            if(url==null||url==""||"".equals(url)){
                url="http://translate.google.com/translate_tts";
            }
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setDoOutput(true);
            // 允许传入body参数
            conn.setDoInput(true);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            //构造订阅消息
            Map subscribeMessage = new HashMap<String, Object>();
            subscribeMessage.put("ie", "utf-8");//
            subscribeMessage.put("total", 1);//
            subscribeMessage.put("textlen", 99999);
            subscribeMessage.put("client", "tw-ob");
            subscribeMessage.put("tl", "zh-CN");
            subscribeMessage.put("q", URLEncoder.encode(ttsText, "UTF-8"));
            JSONObject subscribeMessageJson = new JSONObject(subscribeMessage);
            /*
            String s = subscribeMessageJson.toJSONString();
            System.out.println("JSONString:" + s);*/
            String s1 = subscribeMessageJson.toString();
            System.out.println("String:" + s1);
            byte[] paramsBytes = s1.getBytes();
            // 获取URLConnection对象对应的输出流
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            // 发送请求参数
            wr.writeBytes(s1);
            //wr.write(paramsBytes);
            // flush输出流的缓冲
            wr.flush();
            // wr.close();
            // 设置连接方式
            conn.setRequestMethod("POST");
            // POST不支持缓存
            conn.setUseCaches(false);
            // 设置主机连接时间超时时间10000毫秒
            conn.setConnectTimeout(10000);
            // 设置读取远程返回数据的时间6000毫秒
            conn.setReadTimeout(8000);
            // 发送请求
            conn.connect();
            // 获取输入流
            InputStream is = new BufferedInputStream(conn.getInputStream());
            //BufferedInputStream bis=new BufferedInputStream(is);
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes=new byte[1024*1024*5];
            int len=0;
            while ((len=is.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
            }
            // 封装输入流
            //BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            // 接收读取数据
            //StringBuffer sb = new StringBuffer();
            //String line = null;
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//                sb.append("\r\n");
//            }
//            if (null != br) {
//                br.close();
//            }
//            if (null != is) {
//                is.close();
//            }
            // 关闭连接
            outputStream.flush();
            conn.disconnect();
        }catch (UnsupportedEncodingException | MalformedURLException e){
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
