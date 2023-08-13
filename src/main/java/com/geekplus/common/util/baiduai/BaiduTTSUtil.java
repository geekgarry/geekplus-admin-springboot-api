/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 3/1/23 18:16
 * description: 做什么的？
 */
package com.geekplus.common.util.baiduai;

import com.geekplus.common.config.WebAppConfig;
import com.geekplus.common.constant.Constant;
import com.geekplus.common.myexception.BaseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class BaiduTTSUtil {
    //  填写网页上申请的appkey 如 $apiKey="g8eBUMSokVB1BHGmgxxxxxx"
    private final String appKey = "qOfUIYDkqMC4iAYQjSt51KAk";

    // 填写网页上申请的APP SECRET 如 $secretKey="94dc99566550d87f8fa8ece112xxxxx"
    private final String secretKey = "GzKEvAFFTpfffW2SDCGjOAmRBrz8uyUD";

    // text 的内容为"欢迎使用百度语音合成"的urlencode,utf-8 编码
    // 可以百度搜索"urlencode"
    private final String text1 = "欢迎使用百度语音";

    // 发音人选择, 基础音库：0为度小美，1为度小宇，3为度逍遥，4为度丫丫，
    // 精品音库：5为度小娇，103为度米朵，106为度博文，110为度小童，111为度小萌，默认为度小美
    private final int per = 0;
    // 语速，取值0-15，默认为5中语速
    private final int spd = 5;
    // 音调，取值0-15，默认为5中语调
    private final int pit = 5;
    // 音量，取值0-9，默认为5中音量
    private final int vol = 5;

    // 下载的文件格式, 3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav
    private final int aue = 6;

    public final String url = "http://tsn.baidu.com/text2audio"; // 可以使用https

    private String cuid = "MaikeThousand";

    //文字合成语音
    public String textToVoice(String text,String token) throws IOException, BaseException {
        String result=null;
        if(token==null||token=="") {
            TokenHolder holder = new TokenHolder(appKey, secretKey, TokenHolder.ASR_SCOPE);
            holder.refresh();
            token = holder.getToken();
        }
        // 此处2次urlencode， 确保特殊字符被正确编码
        String params = "tex=" + ConnUtil.urlEncode(ConnUtil.urlEncode(text));
        params += "&per=" + per;
        params += "&spd=" + spd;
        params += "&pit=" + pit;
        params += "&vol=" + vol;
        params += "&cuid=" + cuid;
        params += "&tok=" + token;
        params += "&aue=" + aue;
        params += "&lan=zh&ctp=1";
        System.out.println(url + "?" + params); // 反馈请带上此url，浏览器上可以测试
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(5000);
        PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
        printWriter.write(params);
        printWriter.close();
        String contentType = conn.getContentType();
        if (contentType.contains("audio/")) {
            byte[] bytes = ConnUtil.getResponseBytes(conn);
            String format = getFormat(aue);
            //服务器存储路径
            String filePath=WebAppConfig.getProfile()+"/TTS/";
            //文件名称 与服务器存储路径合起来就是服务器文件的绝对路径
            String fileName = "result." + format;
            //写入文件的操作，把上面两者组合起来的服务器文件绝对路径填入
            File file = new File(filePath+fileName); // 打开mp3文件即可播放
            //判断父文件夹是否存在（getParentFile()也就是获取除了fileName文件之前的文件路径filePath）
            if (!file.getParentFile().exists())
            {
                //不存在则创建filePath文件夹
                file.getParentFile().mkdirs();
            }
//            if (!file.exists())
//            {
//                file.createNewFile();
//            }
            // System.out.println( file.getAbsolutePath());
            //写入文件到服务器地址
            FileOutputStream os = new FileOutputStream(file);
            os.write(bytes);
            os.close();
            System.out.println("audio file write to " + file.getAbsolutePath());
            result= Constant.RESOURCE_PREFIX + "/TTS/" + fileName;;
        } else {
            System.err.println("ERROR: content-type= " + contentType);
            result = ConnUtil.getResponseString(conn);
            System.err.println(result);
        }
        return result;
    }

    // 下载的文件格式, 3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav
    private String getFormat(int aue) {
        String[] formats = {"mp3", "pcm", "pcm", "wav"};
        return formats[aue - 3];
    }
}
