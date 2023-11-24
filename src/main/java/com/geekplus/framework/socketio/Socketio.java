package com.geekplus.framework.socketio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author: geekgarry
 * @date: 8/8 09:06
 * @description:
 */
public class Socketio {

    static SocketIOServer socketIOServer;

    /**
     * 添加客户端
     */
    public void startSocketio() throws InterruptedException {
        //配置
        Configuration conf = new Configuration();
        //指定要主机ip地址，这个和页面请求ip地址一致
        conf.setHostname("localhost");
        //指定端口号
        conf.setPort(9092);
        //设置最大的WebSocket帧内容长度限制
        conf.setMaxFramePayloadLength(1024 * 1024);
        //设置最大HTTP内容长度限制
        conf.setMaxHttpContentLength(1024 * 1024);

        conf.setAuthorizationListener((handshakeData) -> {
            Map<String, List<String>> urlParams = handshakeData.getUrlParams();
            System.out.println("sid: " + urlParams.get("sid"));
            urlParams.forEach((k, v) -> {
                System.out.println("key:" + k + " value: " + v.toString());
            });
            String cookie = handshakeData.getHttpHeaders().get("Cookie");
            System.out.println("cookie:" + cookie);
            return true;
        });

        /**
         * 设置跨域 不设置测试的时候前端会阻止跨域请求造成不能访问到socket服务器；
         * 这个参数高版本才有最初使用的是1.6几的版本 没有这个参数；升级到 1.7.11 才有；具体哪个版本中添加的不知道 尽量用高版本
         */
        conf.setOrigin("http://localhost:28050");
        socketIOServer = new SocketIOServer(conf);

        ConnectListener connect = new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                System.out.println("添加");
                System.out.println("sid:" + client.getSessionId());
            }

        };
        //添加客户端
        socketIOServer.addConnectListener(connect);
        socketIOServer.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                System.out.println("断开：");
                System.out.println("sid:" + client.getSessionId());
            }
        });
        socketIOServer.addEventListener("chatevent", ChatObject.class, new DataListener<ChatObject>() {
            @Override
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) throws Exception {
                System.out.println("消息：");
                //chatevent为 事件的名称，data为发送的内容
                socketIOServer.getBroadcastOperations().sendEvent("chatevent", data);
            }
        });

        socketIOServer.addEventListener("chatfiles", ByteBuffer.class, new DataListener<ByteBuffer>() {
                    @Override
                    public void onData(SocketIOClient socketIOClient, ByteBuffer byteBuffer, AckRequest ackRequest) throws Exception {
                        System.out.println("传图");

                        String filename = UUID.randomUUID() + ".jpg";

                        FileOutputStream output = new FileOutputStream(new File("D:\\wrapper\\" + filename));
                        FileChannel channel = output.getChannel();

                        while(byteBuffer.hasRemaining()){
                            channel.write(byteBuffer);
                        }

                        channel.close();
                        output.close();

                        socketIOServer.getBroadcastOperations().sendEvent("chatfiles", filename);
                    }
                });

        socketIOServer.start();

        //设置超时时间
        Thread.sleep(Integer.MAX_VALUE);

        socketIOServer.stop();
    }

    /**
     * 全体消息推送
     *
     * @param type    前台根据类型接收消息，所以接收的消息类型不同，收到的通知就不同
     *                推送的事件类型
     * @param content 推送的内容
     */
    public void pushArr(String type, String content) {
        //获取全部客户端
        Collection<SocketIOClient> allClients = socketIOServer.getAllClients();
        for (SocketIOClient socket : allClients) {
            socket.sendEvent(type, content);
        }
    }

    /**
     * 启动服务
     */
    public void startServer() {
        if (socketIOServer == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        startSocketio();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * 停止服务
     */
    public void stopSocketio() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

}
