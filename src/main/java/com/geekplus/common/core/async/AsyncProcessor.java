package com.geekplus.common.core.async;

import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 6/30/24 5:37 AM
 * description: 做什么的？
 */
@Component
public class AsyncProcessor {
    public CompletableFuture processAsync() {
        CompletableFuture future = new CompletableFuture();
        // 模拟异步处理耗时操作
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(27);
                String result = "Async processing completed.";
                future.complete(result);
            } catch (InterruptedException e) {
                future.completeExceptionally(e);
            }
        }).start();
        return future;
    }
}
