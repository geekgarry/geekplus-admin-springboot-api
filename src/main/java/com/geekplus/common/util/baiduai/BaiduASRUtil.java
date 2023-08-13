/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 3/1/23 18:43
 * description: 做什么的？
 */
package com.geekplus.common.util.baiduai;

import com.alibaba.fastjson.JSONObject;
import com.geekplus.common.myexception.BaseException;
import com.geekplus.common.util.Base64Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class BaiduASRUtil {
    private final boolean METHOD_RAW = false; // 默认以json方式上传音频文件

    //  填写网页上申请的appkey 如 $apiKey="g8eBUMSokVB1BHGmgxxxxxx"
    private final String APP_KEY = "qOfUIYDkqMC4iAYQjSt51KAk";

    // 填写网页上申请的APP SECRET 如 $SECRET_KEY="94dc99566550d87f8fa8ece112xxxxx"
    private final String SECRET_KEY = "GzKEvAFFTpfffW2SDCGjOAmRBrz8uyUD";

    // 需要识别的文件 示例16k.pcm
    private final String FILENAME = "16k.pcm";

    // 文件格式, 支持pcm/wav/amr 格式，极速版额外支持m4a 格式
    private final String FORMAT = FILENAME.substring(FILENAME.length() - 3);


    private String CUID = "1234567JAVA";

    // 采样率固定值
    private final int RATE = 16000;

    private String url;

    private int dev_pid;

    //private int LM_ID;//测试自训练平台需要打开此注释

    private String scope;

    //  普通版 参数
    {
        url = "http://vop.baidu.com/server_api"; // 可以改为https
        //  1537 表示识别普通话，使用输入法模型。 其它语种参见文档
        dev_pid = 1537;
        scope = "audio_voice_assistant_get";
    }

    // 自训练平台 参数
    /*{
        //自训练平台模型上线后，您会看见 第二步：“”获取专属模型参数pid:8001，modelid:1234”，按照这个信息获取 dev_pid=8001，lm_id=1234
        DEV_PID = 8001;
        LM_ID = 1234；
    }*/

    /* 极速版 参数
    {
        URL =   "http://vop.baidu.com/pro_api"; // 可以改为https
        DEV_PID = 80001;
        SCOPE = "brain_enhanced_asr";
    }
    */

    /* 忽略scope检查，非常旧的应用可能没有
    {
        SCOPE = null;
    }
    */

    public static void main(String[] args) throws IOException, BaseException {
        BaiduASRUtil demo = new BaiduASRUtil();
        // 填写下面信息
        String result = demo.run();
        System.out.println("识别结束：结果是：");
        System.out.println(result);

        // 如果显示乱码，请打开result.txt查看
        File file = new File("result.txt");
        FileWriter fo = new FileWriter(file);
        fo.write(result);
        fo.close();
        System.out.println("Result also wrote into " + file.getAbsolutePath());
    }


    public String run() throws IOException, BaseException {
        TokenHolder holder = new TokenHolder(APP_KEY, SECRET_KEY, scope);
        holder.refresh();
        String token = holder.getToken();
        String result = null;
        if (METHOD_RAW) {
            result = runRawPostMethod(FILENAME,token);
        } else {
            result = runJsonPostMethod(null,token);
        }
        return result;
    }

    private String runRawPostMethod(String fileName,String token) throws IOException, BaseException {
        if(token==null||token=="") {
            TokenHolder holder = new TokenHolder(APP_KEY, SECRET_KEY, scope);
            holder.refresh();
            token = holder.getToken();
        }
        String url2 = url + "?cuid=" + ConnUtil.urlEncode(CUID) + "&dev_pid=" + dev_pid + "&token=" + token;
        //测试自训练平台需要打开以下信息
        //String url2 = URL + "?cuid=" + ConnUtil.urlEncode(CUID) + "&dev_pid=" + DEV_PID + "&lm_id="+ LM_ID + "&token=" + token;
        String contentTypeStr = "audio/" + FORMAT + "; rate=" + RATE;
        //System.out.println(url2);
        byte[] content = getFileContent(fileName);
        HttpURLConnection conn = (HttpURLConnection) new URL(url2).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Type", contentTypeStr);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.getOutputStream().write(content);
        conn.getOutputStream().close();
        System.out.println("url is " + url2);
        System.out.println("header is  " + "Content-Type :" + contentTypeStr);
        String result = ConnUtil.getResponseString(conn);
        return result;
    }

    public String runJsonPostMethod(byte[] content,String token) throws BaseException, IOException {

        if(token==null||token=="") {
            TokenHolder holder = new TokenHolder(APP_KEY, SECRET_KEY, scope);
            holder.refresh();
            token = holder.getToken();
        }
        //byte[] content = getFileContent(fileName);
        String speech = base64Encode(content);

        JSONObject params = new JSONObject();
        params.put("dev_pid", dev_pid);
        //params.put("lm_id",LM_ID);//测试自训练平台需要打开注释
        params.put("format", FORMAT);
        params.put("rate", RATE);
        params.put("token", token);
        params.put("cuid", CUID);
        params.put("channel", "1");
        params.put("len", content.length);
        params.put("speech", speech);

        // System.out.println(params.toString());
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setDoOutput(true);
        conn.getOutputStream().write(params.toString().getBytes());
        conn.getOutputStream().close();
        String result = ConnUtil.getResponseString(conn);


        params.put("speech", "base64Encode(getFileContent(FILENAME))");
        System.out.println("url is : " + url);
        System.out.println("params is :" + params.toString());


        return result;
    }

    private byte[] getFileContent(String filename) throws BaseException, IOException {
        File file = new File(filename);
        if (!file.canRead()) {
            System.err.println("文件不存在或者不可读: " + file.getAbsolutePath());
            throw new BaseException("file cannot read: " + file.getAbsolutePath());
        }
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            return ConnUtil.getInputStreamContent(is);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private String base64Encode(byte[] content) {
        /**
         Base64.Encoder encoder = Base64.getEncoder(); // JDK 1.8  推荐方法
         String str = encoder.encodeToString(content);
         **/

        char[] chars = Base64Util.encode(content); // 1.7 及以下，不推荐，请自行跟换相关库
        String str = new String(chars);

        return str;
    }
}
