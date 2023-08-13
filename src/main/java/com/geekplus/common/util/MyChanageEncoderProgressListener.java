package com.geekplus.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.schild.jave.info.MultimediaInfo;

/**
 * @program: spring-code-generate
 * @description: 音视频编码监听
 * @author: GarryChan
 * @create: 2020-12-12 11:13
 **/
public class MyChanageEncoderProgressListener implements EncoderProgressListener{
    private static Logger logger = LoggerFactory.getLogger(MyChanageEncoderProgressListener.class);
    @Override
    public void sourceInfo(MultimediaInfo info) {
        long ls = info.getDuration() / 1000;
        int hour = (int) (ls / 3600);
        int minute = (int) (ls % 3600) / 60;
        int second = (int) (ls - hour * 3600 - minute * 60);
        String length = hour + "时" + minute + "分" + second + "秒";
        logger.info("MyChanageEncoderProgressListener#sourceInfo--->{}",info.toString());
        logger.info("MyChanageEncoderProgressListener#length--->{}",length);
    }

    @Override
    public void progress(int permil) {
        logger.info("MyChanageEncoderProgressListener#progress--->{}",permil);
    }

    @Override
    public void message(String message) {
        logger.info("MyChanageEncoderProgressListener#message--->{}",message);
    }
}
