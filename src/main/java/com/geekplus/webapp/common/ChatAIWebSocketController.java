package com.geekplus.webapp.common;

import com.alibaba.fastjson2.JSON;
import com.geekplus.common.domain.ChatPrompt;
import com.geekplus.common.domain.Result;
import com.geekplus.webapp.common.service.GeminiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 8/12/24 9:21 PM
 * description: 做什么的？TODO
 */
@RestController
public class ChatAIWebSocketController {

    @Resource
    private GeminiChatService geminiService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

//    @Autowired
//    private WebClient webClient; // 用于调用 AI API

    @MessageMapping("/chatAI.sendMessage") // 接收来自客户端的消息
    public void sendMessage(@Payload ChatPrompt chatPrompt) throws Exception {
        System.out.println("接收的payload：" + chatPrompt);
        System.out.println("接收的payload：" + JSON.toJSONString(chatPrompt));
        // 调用 AI API，获取流式响应
        Flux<ServerSentEvent<String>> apiResponse = callAiApi(chatPrompt);
        // 处理 AI API 响应，并逐条发送给前端
        apiResponse.delayElements(Duration.ofMillis(50)) // 模拟打字延迟
                .subscribe(chunk -> {
                    Result responseMessage = Result.success(chunk);
                    messagingTemplate.convertAndSend("/chatAITopic/chatAI", responseMessage);
                });
    }

    // 调用 AI API
    private Flux<ServerSentEvent<String>> callAiApi(ChatPrompt chatPrompt) {
        // 将 ApiRequest 对象转换为 JSON 字符串
//        try {
//            Map<String, Object> mapResponse = geminiService.getGeminiContent(chatPrompt);
//            // 发送 POST 请求，并指定返回数据类型为 Flux<String>
//            return Flux.<Map<String, Object>>create(sink -> {
//                sink.next(mapResponse); // 先发送初始回复
//                String textMsg = mapResponse.get("msg_data").toString();
//                Map<String, Object> responseFlux = new HashMap<>();
//                responseFlux.put("msg_date_time", mapResponse.get("msg_date_time"));
//                responseFlux.put("msg_type", mapResponse.get("msg_type"));
//                // 模拟逐字生成回复
//                for (int i = 0; i < textMsg.length(); i++) {
//                    try {
//                        Thread.sleep(100); // 模拟延迟
//                        //textMsg.substring(0, i + 1);
//                        sink.next(mapResponse);
//                    } catch (InterruptedException e) {
//                        sink.error(new RuntimeException(e));
//                    }
//                }
//                sink.complete();
//            }).map(data -> ServerSentEvent.<Map<String, Object>>builder()
//                    .data(data)
//                    .build())
//                    .delayElements(Duration.ofMillis(100)); // 每隔一段时间发送一个字符
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // 这里模拟 AI API 返回的流式数据
//        Object object = geminiService.streamGemini(chatPrompt);
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
        String reply = "收到消息: " +chatPrompt.getChatMsg()+ baseReply + ". 正在思考...";
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
        // 实际应用中，你需要调用真实的 AI API，并将响应数据解析为 Map 对象
//        return Flux.interval(Duration.ofMillis(50)) // 模拟延迟
//                .take(5) // 模拟返回 5 次数据
//                .map(i -> {
//                    Map<String, Object> responseChunk = new HashMap<>();
//                    responseChunk.put("content", "Response chunk " + (i + 1));
//                    responseChunk.put("timestamp", System.currentTimeMillis());
//                    return responseChunk;
//                });
//        return webClient.post()
//                .uri("https://api.example.com/ai-api") // 替换为实际的 AI API 地址
//                .bodyValue(chatPrompt)
//                .retrieve()
//                .bodyToFlux(String.class);
    }
}
