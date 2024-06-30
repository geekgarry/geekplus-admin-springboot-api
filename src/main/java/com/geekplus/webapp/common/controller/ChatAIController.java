package com.geekplus.webapp.common.controller;

import com.geekplus.common.core.async.AsyncProcessor;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.Result;
import com.geekplus.common.domain.ChatPrompt;
import com.geekplus.common.util.ServletUtils;
import com.geekplus.webapp.common.service.ChatGPTService;
import com.geekplus.webapp.common.service.GeminiChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2/18/23 21:13
 * description: 做什么的？
 */
@Slf4j
@RestController
@RequestMapping("/AIBot")
public class ChatAIController extends BaseController {
    //ChatGPTAI操作服务类
    @Resource
    private ChatGPTService chatGPTService;
    //GeminiAI操作服务类
    @Resource
    private GeminiChatService geminiService;
    @Resource
    private AsyncProcessor asyncProcessor;

//    public ChatAIController(AsyncProcessor asyncProcessor) {
//        this.asyncProcessor = asyncProcessor;
//    }
//    @Resource
//    public RedisTemplate redisTemplate;
//    private final StringRedisTemplate stringRedisTemplate;
//
//    public ChatGPTController(StringRedisTemplate stringRedisTemplate) {
//        this.stringRedisTemplate = stringRedisTemplate;
//    }

    @PostMapping("/testAsyncProcess")
    public Callable<Result> testAsyncProcess() {
//        CompletableFuture<Result> completableFuture=asyncProcessor.processAsync();
//        HttpServletRequest request=ServletUtils.getRequest();
//        AsyncContext asyncContext = request.startAsync();
        log.info("主线程开始");
        return ()->{
            log.info("副线程开始");
            Thread.sleep(7000);
            log.info("副线程返回");
            return Result.success("返回成功数据！");
        };
//        Callable<String> result = new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                log.info("副线程开始");
//                /*这里沉睡1分钟,表示处理耗时业务执行*/
//                Thread.sleep(10000);
//                log.info("副线程返回");
//                return "这是一个异步测试的例子";
//            }
//        };
//        return result;
    }

    @PostMapping("/chatgpttest")
    public Result getChat(@RequestBody ChatPrompt chatPrompt) {
        String text = chatGPTService.getChatContent("",chatPrompt.getChatData(),chatPrompt.getUsername(),chatPrompt.getOpenAiKey());
        return Result.success(text);
    }

    @PostMapping("/getchatgpt")
    public Result getChatGPTV1(@RequestBody ChatPrompt chatPrompt) throws SocketException, UnknownHostException {
        Map text = chatGPTService.reply(chatPrompt.getChatData(),chatPrompt.getUsername(),chatPrompt.getOpenAiKey());
        return Result.success(text);
    }

    /**
      * @Author geekplus
      * @Description //重要Gemini的发送消息和接收，此模式中仅需要接收到重要的消息内容chatData
      * @Param
      * @Throws
      * @Return {@link }
      */
    @PostMapping("/getGeminiContent")
    public Callable<Result> getGeminiContent(MultipartFile file, @RequestBody ChatPrompt chatPrompt) throws SocketException, UnknownHostException {
        log.info("主线程开始");
        return ()->{
            log.info("副线程开始");
            //Thread.sleep(5000);
            Map text = geminiService.getGeminiContent(chatPrompt);
            log.info("副线程返回");
            return Result.success(text);
        };
//        Map text = geminiService.getGeminiContent(chatPrompt);
//        return Result.success(text);
    }

    /**
     * @Author geekplus
     * @Description //重要Gemini的发送消息和接收,采用对话模式，聊天模式,
     * //此模式中不仅需要消息内容chatData，而需要一个包含之前所有聊天记录的preChatData
     * @Param
     * @Throws
     * @Return {@link }
     */
    @PostMapping("/getGeminiChat")
    public Callable<Result> getGeminiChat(MultipartFile file, @RequestBody ChatPrompt chatPrompt) throws SocketException, UnknownHostException {
        log.info("主线程开始");
        return ()->{
            log.info("副线程开始");
            //Thread.sleep(5000);
            Map text = geminiService.getGeminiContent(chatPrompt);
            log.info("副线程返回");
            return Result.success(text);
        };
//        Map text = geminiService.getGeminiContent(chatPrompt);
//        return Result.success(text);
    }

    @PostMapping("/getImageGeminiContent")
    public Result getImageGeminiContent(@RequestBody ChatPrompt chatPrompt) throws SocketException, UnknownHostException {
        String text = geminiService.getGeminiImage(chatPrompt.getChatData(),chatPrompt.getMediaData(),chatPrompt.getUsername());
        return Result.success(text);
    }

    @PostMapping("/testGeminiContent")
    public Result testImageGeminiContent(@RequestBody ChatPrompt chatPrompt) throws SocketException, UnknownHostException {
        Map text = geminiService.testGemini(chatPrompt.getChatData());
        return Result.success(text);
    }

    //@RequestParam("text")也可以用post传输，但是前端要是用params携带 @RequestBody可以实现封装json也可以以text文本形式
    @PostMapping("/getTextToVoice")
    public Result getTextToVoice(@RequestBody String text){
        try{
            String result= chatGPTService.getTextToVoice(text);
            return Result.success(result);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/uploadVoiceBlob")
    public Result uploadVoiceBlob(@RequestPart("file") MultipartFile file){
        try
        {
            // 上传文件路径
            //String filePath = MaikeConfig.getUploadPath();
            // 上传并获取文件名称
            String fileName = file.getOriginalFilename();
            //String url = serverConfig.getUrl() + fileName;
            byte[] voiceByte= file.getBytes();
            Object text=chatGPTService.getVoiceToText(voiceByte);
            Result ajax = Result.success(text);
            //ajax.put("fileName", fileName);
            //ajax.put("url", url);
            return ajax;
        }
        catch (Exception e)
        {
            return Result.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/getHistoryMessage",method = {RequestMethod.POST, RequestMethod.GET})
    public Result getHistoryMsgList(@RequestParam("username") String username){
        List<String> msgList= geminiService.getHistoryMsgList(username);
        //JSONObject jsonObject=JSONObject.parseObject();
        return Result.success(msgList);
    }
}
