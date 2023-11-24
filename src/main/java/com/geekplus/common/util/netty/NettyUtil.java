package com.geekplus.common.util.netty;

import io.netty.channel.Channel;

import java.net.SocketAddress;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 10/13/23 23:45
 * description: 做什么的？
 */
public class NettyUtil {

    public static String parseChannelRemoteAddr(final Channel channel) {
        if (null == channel) {
            return "";
        }
        final SocketAddress remote = channel.remoteAddress();
        final String addr = remote != null ? remote.toString() : "";

        if (addr.length() > 0) {
            int index = addr.lastIndexOf("/");
            if (index >= 0) {
                return addr.substring(index + 1);
            }
            return addr;
        }
        return "";
    }
}
