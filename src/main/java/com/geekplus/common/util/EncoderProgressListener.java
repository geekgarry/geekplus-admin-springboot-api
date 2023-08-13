package com.geekplus.common.util;

import ws.schild.jave.info.MultimediaInfo;

/**
 * 音视频转换过程编码监听
 */
public interface EncoderProgressListener {
    /**
     * This method is called before the encoding process starts, reporting
     * information about the source stream that will be decoded and re-encoded.
     * 这种方法是在编码过程开始之前被调用，报告关于将被解码和再编码的源数据位流的信息.
     * @param info Informations about the source multimedia stream.
     */
    public void sourceInfo(MultimediaInfo info);

    /**
     * This method is called to notify a progress in the encoding process.
     * 这种方法被称为通知在编码过程中的进度。
     * @param permil A permil value representing the encoding process progress.
     */
    public void progress(int permil);

    /**
     * This method is called every time the encoder need to send a message
     * (usually, a warning).
     * 这种方法被称为每次编码器需要发送一条消息（通常，一个警告）。
     * @param message The message sent by the encoder.
     */
    public void message(String message);
}
