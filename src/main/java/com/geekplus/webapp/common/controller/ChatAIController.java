package com.geekplus.webapp.common.controller;

import com.geekplus.common.core.async.AsyncProcessor;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.Result;
import com.geekplus.common.domain.ChatPrompt;
import com.geekplus.common.util.file.FileUtils;
import com.geekplus.webapp.common.service.ChatGPTService;
import com.geekplus.webapp.common.service.GeminiChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

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
//        return ()->{
//            log.info("副线程开始");
//            Thread.sleep(7000);
//            log.info("副线程返回");
//            return Result.success("返回成功数据！");
//        };
//        Callable<String> result =
          return new Callable<Result>() {
            @Override
            public Result call() throws Exception {
                log.info("副线程开始");
                /*这里沉睡1分钟,表示处理耗时业务执行*/
                Thread.sleep(10000);
                log.info("副线程返回");
                return Result.success("这是一个异步测试的例子");
            }
        };
    }

    /**
      * @Author geekplus
      * @Description //重要Gemini的发送消息和接收，此模式中仅需要接收到重要的消息内容chatData, @RequestBody 支持是以application/json，multipart/form-data不支持，直接以对象为名
      * @Param
      * @Throws
      * @Return {@link }
      */
    @PostMapping("/getGeminiContent")
    public Callable<Result> getGeminiContent(@RequestBody ChatPrompt chatPrompt) throws SocketException, UnknownHostException {
        log.info("主线程开始");
        return () -> {
            log.info("副线程开始");
            //Thread.sleep(5000);
            Map text = geminiService.getGeminiContent(chatPrompt);
            log.info("副线程返回");
            return Result.success(text);
        };
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
    public Callable<Result> getGeminiChat(@RequestBody ChatPrompt chatPrompt) throws SocketException, UnknownHostException {
        log.info("主线程开始");
        return () -> {
            log.info("副线程开始");
            //Thread.sleep(5000);
            Map text = geminiService.getGeminiContent(chatPrompt);
            log.info("副线程返回");
            return Result.success(text);
        };
    }

    /**
     * @Author geekplus
     * @Description //重要Gemini的发送消息和接收,采用对话模式，聊天模式,
     * //此模式中不仅需要消息内容chatData，而需要一个包含之前所有聊天记录的preChatData
     * @Param
     * @Throws
     * @Return {@link }
     * @return
     */
    @GetMapping(value = "/getStreamGeminiChat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public DeferredResult<Map> getStreamGeminiChat(@RequestBody ChatPrompt chatPrompt) throws SocketException, UnknownHostException {
        log.info("主线程开始");
        DeferredResult<Map> result = new DeferredResult<>();

        new Thread(() -> {
            // 调用外部 API
            Map text = null;
            try {
                text = geminiService.getGeminiContent(chatPrompt);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 设置 DeferredResult 的结果
            result.setResult(text);
        }).start();

        return result;
    }

    // 模拟调用ChatGPT API并逐字返回回复
    @PostMapping("/getStreamGeminiTest")
    public Callable<Result> getStreamGeminiTest(@RequestBody ChatPrompt chatPrompt) throws SocketException, UnknownHostException {
        log.info("主线程开始");
        return () -> {
            log.info("副线程开始");
            //Thread.sleep(5000);
            Object text = geminiService.streamGemini(chatPrompt);
            log.info("副线程返回");
            return Result.success(text);
        };
    }

    @GetMapping(value = "/streamChat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamEvents(@RequestParam String message) throws IOException {
        // 模拟调用 ChatGPT API 获取回复
//        Object textResponse = geminiService.streamGemini(chatPrompt);
        String baseReply = "打破等待，拥抱实时：Spring WebFlux 与 SSE 实现流式数据传输" +
                "在传统的 Web 应用中，我们已经习惯了请求-响应这种一问一答式的交互模式。然而，随着实时数据需求的不断增长，传统的 HTTP 请求-响应模型显得力不从心。想象一下，用户需要实时获取股票价格、比赛比分或者聊天消息，传统的轮询方式不仅效率低下，还会对服务器造成巨大压力。" +
                "为了解决这个问题，我们需要一种全新的数据传输模式，那就是服务器推送事件（Server-Sent Events，SSE）。SSE 允许服务器在事件发生时主动将数据推送给客户端，而无需客户端不断发起请求。" +
                "SSE：轻量级实时数据传输方案" +
                "SSE 是一种基于 HTTP 的服务器推送技术，它使用简单的文本格式传输数据，并利用浏览器原生的 EventSource 对象进行处理。相比于 WebSocket，SSE 更为轻量级，更易于实现，并且对于大多数浏览器都提供了良好的支持。" +
                "SSE 的优势：" +
                "简单易用: 无需复杂的协议和框架，使用 HTTP 协议即可实现。" +
                "轻量级: 数据格式简单，传输效率高。" +
                "浏览器兼容性好: 大多数现代浏览器都支持 SSE。" +
                "SSE 的适用场景：" +
                "单向数据推送: 例如，实时更新股票价格、新闻资讯、系统通知等。" +
                "轻量级实时数据交互: 例如，简单的聊天室、在线游戏等。" +
                "Spring WebFlux: 响应式编程，助力实时数据处理" +
                "Spring WebFlux 是 Spring Framework 5.0 推出的响应式 Web 框架，它基于 Reactor 库，支持异步非阻塞的编程模型，可以高效地处理大量并发连接和数据流。" +
                "Spring WebFlux 的优势：" +
                "非阻塞 I/O: 提高服务器资源利用率，能够处理更多并发请求。" +
                "响应式编程模型: 使用 Flux 和 Mono 类型处理数据流，代码简洁易懂。" +
                "与 Spring";
        String reply = "收到消息: " + message + baseReply + ". 正在思考...";
        return Flux.<String>create(sink -> {
            sink.next(reply); // 先发送初始回复
            // 模拟逐字生成回复
            for (int i = 0; i < reply.length(); i++) {
                try {
                    Thread.sleep(100); // 模拟延迟
                    sink.next(reply.substring(0, i + 1));
                } catch (InterruptedException e) {
                    sink.error(new RuntimeException(e));
                    return;
                }
            }
            sink.complete();
        }).map(data -> ServerSentEvent.<String>builder()
                        .data(data)
                        .build())
                .delayElements(Duration.ofMillis(100)); // 每隔一段时间发送一个字符
//        return Flux.interval(Duration.ofMillis(100))
//                .map(sequence -> ServerSentEvent.<String>builder()
//                        .data("数据流 - " + sequence)
//                        .build());
    }

    @GetMapping(value = "/streamTest", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamEvents() {
        return Flux.interval(Duration.ofMillis(100))
                .map(sequence -> ServerSentEvent.<String>builder()
                        .data("数据流 - " + sequence)
                        .build());
    }

    /**
     * @Author geekplus
     * @Description //发送文件, @RequestBody 支持是以application/json，multipart/form-data不支持，直接以对象为名
     * @Param
     * @Throws
     * @Return {@link }
     */
    @PostMapping("/getGeminiWithFile")
    public Callable<Result> getGeminiWithFile(@RequestPart(value = "file", required = false) MultipartFile file, ChatPrompt chatPrompt) throws SocketException, UnknownHostException {
        log.info("主线程开始");
        try {
            String fileName = "";
            // 上传并获取文件名称
            if(!FileUtils.isFileEmpty(file)) {
                fileName = file.getOriginalFilename();
                //String url = serverConfig.getUrl() + fileName;
                byte[] fileByte = file.getBytes();
                chatPrompt.setMediaData(fileByte);
                chatPrompt.setMediaFileName(fileName);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return () -> {
            log.info("副线程开始");
            //Thread.sleep(5000);
            Map text = geminiService.getGeminiContent(chatPrompt);
            log.info("副线程返回");
            return Result.success(text);
        };
//        Map text = geminiService.getGeminiContent(chatPrompt);
//        return Result.success(text);
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

    //获取历史聊天记录
    @RequestMapping(value = "/getHistoryMessage",method = {RequestMethod.POST, RequestMethod.GET})
    public Result getHistoryMsgList(@RequestParam("username") String username){
        List<String> msgList= geminiService.getOneHistoryMsgList(username);
        //JSONObject jsonObject=JSONObject.parseObject();
        return Result.success(msgList);
    }

    //根据redisKey获取历史聊天记录
    @RequestMapping(value = "/getOneHistoryMessage",method = {RequestMethod.POST, RequestMethod.GET})
    public Result getOneHistoryMsgList(@RequestParam String historyMsgKey){
        List<String> msgList= geminiService.getOneHistoryMsgListByKey(historyMsgKey);
        return Result.success(msgList);
    }

    //获取所有历史聊天记录
    @RequestMapping(value = "/getAllHistoryMessage",method = {RequestMethod.POST, RequestMethod.GET})
    public Result getAllHistoryMsgList(@RequestParam("username") String username){
        List<Map<String, Object>> allMsgList= geminiService.getAllHistoryMsgList(username);
        return Result.success(allMsgList);
    }

    //删除历史聊天记录
    @RequestMapping(value = "/deleteHistoryMessage",method = {RequestMethod.GET})
    public Result deleteHistoryMsgList(@RequestParam String historyMsgKey){
        Boolean isRemove= geminiService.deleteHistoryMsgList(historyMsgKey);
        return isRemove ? Result.success("删除成功") : Result.error("删除失败或不存在");
    }
}
