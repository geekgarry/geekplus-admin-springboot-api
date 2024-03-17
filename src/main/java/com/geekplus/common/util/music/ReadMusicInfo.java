package com.geekplus.common.util.music;

import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.flac.FlacFileReader;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.mp3.MP3FileReader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 3/18/24 01:56
 * description: 做什么的？
 */
@Slf4j
public class ReadMusicInfo {
    public static HashMap getMusicInfo(String filePath){
        HashMap<String ,Object> musicMap=new HashMap();
        try {
            File file =new File(filePath);
            AudioFile audioFile = AudioFileIO.read(file);
            FlacFileReader flacFileReader = new FlacFileReader();
            MP3FileReader mp3FileReader = new MP3FileReader();
            Tag tag=audioFile.getTag();
            RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
            byte buf[] = new byte[128];
            byte buf1[] = new byte[4];
            //byte[] buf =new byte[128];
            raf.seek(raf.length() - 128);
            raf.read(buf);
            raf.seek(45);// 指针往前走45个字节，从第46个字节开始
            raf.read(buf1);
//            for (byte b : buf) {
//                log.info(byteToBinary(b));
//            }
            if (buf.length != 128) {
                //System.err.println("MP3标签信息数据长度不合法!");
                log.error("MP3标签信息数据长度不合法!");
            }
            //byte[] tagBytes = new String(buf, 0, 3).getBytes("utf-8");
            if (!"TAG".equalsIgnoreCase(new String(buf, 0, 3))) {
                //System.err.println("MP3标签信息数据格式不正确!");
                log.error("MP3标签信息数据格式不正确!");
                log.error(new String(new String(buf, 0, 3,"gbk").getBytes("gbk")),"gbk");
            }
            byte[] songNameBytes=new String(buf, 3, 30,"gbk").getBytes("gbk");
            String songName = new String(songNameBytes,"gbk");
            //System.out.println("歌名：" + SongName);
            log.info("歌名：" + songName);
            musicMap.put("musicName",tag.getFirst(FieldKey.TITLE));
            String singer = new String(buf, 33, 30,"gbk");
            log.info("作者：" + singer);
            musicMap.put("author",tag.getFirst(FieldKey.ARTIST));
            //System.out.println("作者：" + Singer);
            String album = new String(buf, 63, 30,"gbk");
            //System.out.println("专辑：" + Album);
            //System.out.println("文件长度：" + raf.length());
            log.info("专辑：" + album);
            log.info("文件长度：" + raf.length());
            musicMap.put("album",tag.getFirst(FieldKey.ALBUM));
            raf.close();
            // 将字节转化为二进制字符串
            String s1 = String.format("%8s", Integer.toBinaryString(buf1[0] & 0xFF)).replace(' ', '0');
            String s2 = String.format("%8s", Integer.toBinaryString(buf1[1] & 0xFF)).replace(' ', '0');
            String s3 = String.format("%8s", Integer.toBinaryString(buf1[2] & 0xFF)).replace(' ', '0');
            String s4 = String.format("%8s", Integer.toBinaryString(buf1[3] & 0xFF)).replace(' ', '0');
            String c = s1 + s2 + s3 + s4;
            char sc[] = c.toCharArray();
            //System.out.println("音频版本：" + AudioVersion(sc[11], sc[12]));
            //System.out.println("采样频率：" + SampleFrequency(sc[11], sc[12], sc[20], sc[21]));
            //System.out.println("声道模式：" + ChannelMode(sc[24], sc[25]));
            log.info("音频版本：" + AudioVersion(sc[11], sc[12]));
            log.info("采样频率：" + SampleFrequency(sc[11], sc[12], sc[20], sc[21]));
            log.info("声道模式：" + ChannelMode(sc[24], sc[25]));
            musicMap.put("version",AudioVersion(sc[11], sc[12]));
            musicMap.put("frequency",SampleFrequency(sc[11], sc[12], sc[20], sc[21]));
            musicMap.put("channelMode",ChannelMode(sc[24], sc[25]));
        } catch (IOException e) {
            System.err.println("发生异常:" + e);
            e.printStackTrace();
        } catch (CannotReadException e) {
            e.printStackTrace();
        } catch (ReadOnlyFileException e) {
            e.printStackTrace();
        } catch (TagException e) {
            e.printStackTrace();
        } catch (InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        return musicMap;
    }

    public static void main(String[] args) {
        //System.out.println(getMusicInfo("/Users/geekplus/Downloads/故梦-橙翼.m4a"));
        MP3FileReader reader = new MP3FileReader();
        try {
            File file = new File("/Users/geekplus/Downloads/Nightingale.mp3");
            //AudioFile audioFile = reader.read(file);
            MP3File mp3File = (MP3File) AudioFileIO.read(file);
            AudioFile f = AudioFileIO.read(file);
            Tag tag = f.getTag();
            System.out.println(tag.getFirst(FieldKey.TITLE));
            MP3AudioHeader audioHeader = new MP3AudioHeader(file);
            AbstractID3v2Tag v2tag = mp3File.getID3v2Tag();
            System.out.println("标题:" + v2tag.getFirst(FieldKey.TITLE));//audioFile.getTag().getFirst(FieldKey.TITLE));
            System.out.println("作者:" + v2tag.getFirst(FieldKey.ARTIST));//audioFile.getTag().getFirst(FieldKey.ARTIST));
            System.out.println("专辑:" + v2tag.getFirst(FieldKey.ALBUM));//audioFile.getTag().getFirst(FieldKey.ALBUM));
            System.out.println("比特率:" + audioHeader.getBitRate());
            System.out.println("时长:" + audioHeader.getTrackLengthAsString() + " (" + audioHeader.getTrackLength() + "s)");
            // System.out.println("大小:" + (file.length() / 1024F / 1024F) + "MB");
            //System.out.println("大小:" + (audioFile.getFile().length() / 1024F / 1024F) + "MB");
            System.out.println("大小:" + (audioHeader.getTrackLength() / 1024F / 1024F) + "MB");
            System.out.println(" ----- ----- ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String byteToBinary(byte b) {
        StringBuilder binaryBuilder = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            binaryBuilder.append((b >> i) & 1); // 移位和与操作提取每个位
        }
        return binaryBuilder.toString();
    }

    //判断音频版本
    public static String AudioVersion(char a, char b) {
        String result = "";
        if ('0' == a && '0' == b)
            result = "MPEG 2.5";
        else if ('0' == a && '1' == b)
            result = "保留";
        else if ('1' == a && '0' == b)
            result = "MPEG 2";
        else if ('1' == a && '1' == b)
            result = "MPEG 1";
        return result;
    }
    //判断采样频率
    public static int SampleFrequency(char a1, char b1, char a, char b) {
        int f = 0;
        if (AudioVersion(a1, b1) == "MPEG 1" && '0' == a && '0' == b)
            f = 44100;
        if (AudioVersion(a1, b1) == "MPEG 2" && '0' == a && '0' == b)
            f = 22050;
        if (AudioVersion(a1, b1) == "MPEG 2.5" && '0' == a && '0' == b)
            f = 11025;
        if (AudioVersion(a1, b1) == "MPEG 1" && '0' == a && '1' == b)
            f = 48000;
        if (AudioVersion(a1, b1) == "MPEG 2" && '0' == a && '1' == b)
            f = 24000;
        if (AudioVersion(a1, b1) == "MPEG 2.5" && '0' == a && '1' == b)
            f = 12000;
        if (AudioVersion(a1, b1) == "MPEG 1" && '1' == a && '0' == b)
            f = 32000;
        if (AudioVersion(a1, b1) == "MPEG 2" && '1' == a && '0' == b)
            f = 16000;
        if (AudioVersion(a1, b1) == "MPEG 2.5" && '1' == a && '0' == b)
            f = 8000;
        return f;
    }
    //判断声道模式
    public static String ChannelMode(char a, char b) {
        String result = "";
        if ('0' == a && '0' == b)
            result = "立体声";
        else if ('0' == a && '1' == b)
            result = "联合立体声";
        else if ('1' == a && '0' == b)
            result = "双声道";
        else if ('1' == a && '1' == b)
            result = "单声道";
        return result;
    }
}
