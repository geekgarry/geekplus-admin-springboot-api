package com.geekplus.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.filtergraphs.OverlayWatermark;
import ws.schild.jave.filters.DrawtextFilter;
import ws.schild.jave.filters.helpers.Color;
import ws.schild.jave.filters.helpers.OverlayLocation;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoInfo;
import ws.schild.jave.info.VideoSize;
import ws.schild.jave.progress.EncoderProgressListener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @ClassName VideoFormatUtil
 * @Description 视频格式转换工具
 * @Author Garry
 * @Date 2018年5月23日 上午10:52:28
 */
public class VideoFormatUtil {

	public static Logger Log = LoggerFactory.getLogger(VideoFormatUtil.class);
	public static EncoderProgressListener encoderProgressListener;

	/**
	 * 视频文件转音频文件
	 * @param videoPath
	 * @param audioPath
	 * @return
	 */
	public static boolean videoToAudio(String videoPath, String audioPath) {
		Long times=System.currentTimeMillis();
		File fileMp4=new File(videoPath);
		File fileMp3=new File(audioPath);

		//Audio Attributes
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libmp3lame");
		audio.setBitRate(128000);
		audio.setChannels(2);
		audio.setSamplingRate(44100);

		//Encoding attributes
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setOutputFormat("mp3");
		attrs.setAudioAttributes(audio);
		Encoder encoder = new Encoder();
		MultimediaObject mediaObject=new MultimediaObject(fileMp4);
		try {
			encoder.encode(mediaObject, fileMp3, attrs);
			Log.info("File MP4 convertito in MP3");
			System.out.println("耗时："+(System.currentTimeMillis() - times));
			return true;
		} catch (Exception e) {
			Log.error("File non convertito");
			Log.error(e.getMessage());
			return false;
		}
	}

	/**
	 * 获取视频的基本信息，视频长宽高，视频的大小等
	 * @param fileSource
	 * @return
	 */
	public static Map getVideoInfo(String fileSource) {
		// String filePath =
		// Utils.class.getClassLoader().getResource(fileSource).getPath();
		File source = new File(fileSource);
		//Encoder encoder = new Encoder();
		FileInputStream fis = null;
		FileChannel fc = null;
		Map videoInfo = null;
		try {
			MultimediaObject MultimediaObject=new MultimediaObject(source);
			MultimediaInfo m = MultimediaObject.getInfo();
			fis = new FileInputStream(source);
			fc = fis.getChannel();
			videoInfo.put("width",m.getVideo().getSize().getWidth());
			videoInfo.put("height",m.getVideo().getSize().getHeight());
			videoInfo.put("size",fc.size());
			videoInfo.put("duration",m.getDuration());
			videoInfo.put("format",m.getFormat());
//			videoInfo.putAll(m.getVideo().getSize().getWidth(), m.getVideo().getSize().getHeight(), fc.size(),
//					m.getDuration(), m.getFormat());
			System.out.println(videoInfo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fc) {
				try {
					fc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return videoInfo;
	}
	/**
	 * 给图片增加文字水印
	 *
	 * @param imgPath
	 *            -要添加水印的图片路径
	 * @param outImgPath
	 *            -输出路径
	 * @param text-文字
	 * @param font
	 *            -字体
	 * @param color
	 *            -颜色
	 * @param x
	 *            -文字位于当前图片的横坐标
	 * @param y
	 *            -文字位于当前图片的竖坐标
	 */
	public void txtMark(String imgPath, String outImgPath, String text, Font font, Color color, int x, int y) {
		try {
			// 读取原图片信息
			File imgFile = null;
			Image img = null;
			if (imgPath != null) {
				imgFile = new File(imgPath);
			}
			if (imgFile != null && imgFile.exists() && imgFile.isFile() && imgFile.canRead()) {
				img = ImageIO.read(imgFile);
			}
			int imgWidth = img.getWidth(null);
			int imgHeight = img.getHeight(null);
			// 加水印
			BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
			mark(bufImg, img, text, font, color, x, y);
			// 输出图片
			FileOutputStream outImgStream = new FileOutputStream(outImgPath);
			ImageIO.write(bufImg, "jpg", outImgStream);
			outImgStream.flush();
			outImgStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 给图片增加图片水印
	 *
	 * @param inputImg
	 *            -源图片，要添加水印的图片
	 * @param markImg
	 *            - 水印图片
	 * @param outputImg
	 *            -输出图片(可以是源图片)
	 * @param width
	 *            - 水印图片宽度
	 * @param height
	 *            -水印图片高度
	 * @param x
	 *            -横坐标，相对于源图片
	 * @param y
	 *            -纵坐标，同上
	 */
	public void imageMark(String inputImg, String markImg, String outputImg, int width, int height, int x, int y) {
		// 读取原图片信息
		File inputImgFile = null;
		File markImgFile = null;
		Image img = null;
		Image mark = null;
		try {
			if (inputImg != null && markImg != null) {
				inputImgFile = new File(inputImg);
				markImgFile = new File(markImg);
			}
			if (inputImgFile != null && inputImgFile.exists() && inputImgFile.isFile() && inputImgFile.canRead()) {
				img = ImageIO.read(inputImgFile);
			}
			if (markImgFile != null && markImgFile.exists() && markImgFile.isFile() && markImgFile.canRead()) {
				mark = ImageIO.read(markImgFile);
			}
			int imgWidth = img.getWidth(null);
			int imgHeight = img.getHeight(null);
			BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
			mark(bufImg, img, mark, width, height, x, y);
			FileOutputStream outImgStream = new FileOutputStream(outputImg);
			ImageIO.write(bufImg, "jpg", outImgStream);
			outImgStream.flush();
			outImgStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 加图片水印
	 */
	public void mark(BufferedImage bufImg, Image img, Image markImg, int width, int height, int x, int y) {
		Graphics2D g = bufImg.createGraphics();
		g.drawImage(img, 0, 0, bufImg.getWidth(), bufImg.getHeight(), null);
		g.drawImage(markImg, x, y, width, height, null);
		g.dispose();
	}

	/**
	 * 加文字水印
	 */
	public void mark(BufferedImage bufImg, Image img, String text, Font font, Color color, int x, int y) {
		Graphics2D g = bufImg.createGraphics();
		g.drawImage(img, 0, 0, bufImg.getWidth(), bufImg.getHeight(), null);
		g.setColor(java.awt.Color.decode(color.getAlpha()));
		g.setFont(font);
		g.drawString(text, x, y);
		g.dispose();
	}

	/**
	 * 截取视频封面,it.sauronsoftware.jave
	 *
	 * @param veido_path
	 * @param ffmpeg_path
	 * @return
	 */
//	public static boolean processImg(String veido_path, String ffmpeg_path) {
//		File file = new File(veido_path);
//		if (!file.exists()) {
//			System.err.println("路径[" + veido_path + "]对应的视频文件不存在!");
//			return false;
//		}
//		VideoItem videoInfo = getVideoInfo(veido_path);
//		List<String> commands = new ArrayList<String>();
//		commands.add(ffmpeg_path);
//		commands.add("-i");
//		commands.add(veido_path);
//		commands.add("-y");
//		commands.add("-f");
//		commands.add("image2");
//		commands.add("-ss");
//		commands.add("1");// 这个参数是设置截取视频多少秒时的画面
//		commands.add("-t");
//		commands.add("0.001");
//		commands.add("-s");
//		commands.add(videoInfo.getWidth() + "x" + videoInfo.getHeight());// 宽X高
//		commands.add(veido_path.substring(0, veido_path.lastIndexOf(".")).replaceFirst("vedio", "file") + ".jpg");
//		try {
//			ProcessBuilder builder = new ProcessBuilder();
//			builder.command(commands);
//			builder.start();
//			System.out.println("截取成功");
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}

	/**
	 * 截取视频中某一帧作为图片
	 * @param videoPath
	 * @param imagePath
	 * @return
	 */
	public static boolean getVideoProcessImage(String videoPath,String imagePath){
		long times = System.currentTimeMillis();
		File videoSource = new File(videoPath);
		File imageTarget = new File(imagePath);
		MultimediaObject object = new MultimediaObject(videoSource);
		try {
			MultimediaInfo multimediaInfo = object.getInfo();
			VideoInfo videoInfo=multimediaInfo.getVideo();
			VideoAttributes video = new VideoAttributes();
			video.setCodec("png");
			video.setSize(videoInfo.getSize());
			EncodingAttributes attrs = new EncodingAttributes();
			//VideoAttributes attrs = ecodeAttrs.getVideoAttributes().get();
			attrs.setOutputFormat("image2");
			attrs.setOffset(40f);//设置偏移位置，即开始转码位置（3秒）
			attrs.setDuration(0.01f);//设置转码持续时间（1秒）
			attrs.setVideoAttributes(video);
			Encoder encoder = new Encoder();
			encoder.encode(object,imageTarget,attrs);
			System.out.println("耗时："+(System.currentTimeMillis() - times));
			return true;
		} catch (EncoderException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 为视频添加视频水印 it.sauronsoftware.jave
	 * @param veidoPath
	 * @param ffmpegPath
	 * @param fmtVideoPath
	 * @return
	 */
//	public static boolean videoWaterMark(String veidoPath, String ffmpegPath, String fmtVideoPath){
//		VideoItem videoInfo = getVideoInfo(veidoPath);
//		if(null == videoInfo){
//			Log.error("file not exists!!");
//			return false;
//		}
//		// drawtext=fontfile=simhei.ttf: text='雷':x=100:y=10:fontsize=24:fontcolor=yellow:shadowy=2
//		List<String> convert = new ArrayList<String>();
//		convert.add(ffmpegPath); // 添加转换工具路径
//		convert.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
//		convert.add(veidoPath); // 添加要转换格式的视频文件的路径
//		convert.add("-vf");
//		convert.add("drawtext=fontfile=src/main/resources/videoTools/MATURASC.TTF:text='guyun':x=20:y=20:fontsize=30:fontcolor=yellow:shadowy=2");
//		convert.add(fmtVideoPath);
//
//		try {
//			Process videoProcess = new ProcessBuilder(convert).redirectErrorStream(true).start();
//	        new PrintStream(videoProcess.getInputStream()).start();
//	        videoProcess.waitFor();
//			Log.info("添加水印成功");
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}

	/**
	 * 视频添加文字水印
	 * @param sourceFile
	 * @param distFile
	 * @param textWaterMark
	 * @param pListener
	 * @throws EncoderException
	 */
	public static void codecToMp4WithText(String sourceFile, String distFile, String textWaterMark, EncoderProgressListener pListener) {
		File sourceVideo = new File(sourceFile);
		File target = new File(distFile);
		if (target.exists()) {
			target.delete();
		}
		DrawtextFilter vf = new DrawtextFilter(textWaterMark, "(w-text_w)", "(h-text_h)", "宋体", 30.0, new ws.schild.jave.filters.helpers.Color("ffffff","56"));
		vf.setShadow(new ws.schild.jave.filters.helpers.Color("000000","44"), 2, 2);
		VideoAttributes videoAttributes = new VideoAttributes();
		videoAttributes.addFilter(vf);
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setVideoAttributes(videoAttributes);
		Encoder encoder = new Encoder();
		try {
			encoder.encode(new MultimediaObject(sourceVideo), target, attrs, (ws.schild.jave.progress.EncoderProgressListener) pListener);
		} catch (EncoderException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 视频添加图片水印
	 * @param sourceFile
	 * @param distFile
	 * @param waterMark
	 * @param pListener
	 * @throws EncoderException
	 */
	public static void codecToMp4WithWaterMark(String sourceFile, String distFile, String waterMark, EncoderProgressListener pListener) throws EncoderException {
		File sourceVideo = new File(sourceFile);
		File watermark = new File(waterMark);
		VideoAttributes vidAttr = new VideoAttributes();
		vidAttr.addFilter(new OverlayWatermark(watermark, OverlayLocation.BOTTOM_RIGHT, -10, -10));
		EncodingAttributes encAttr = new EncodingAttributes().setVideoAttributes(vidAttr);
		File target = new File(distFile);
		new Encoder().encode(new MultimediaObject(sourceVideo), target, encAttr, (ws.schild.jave.progress.EncoderProgressListener) pListener);
	}
    /**
     * 旋转90度并格式化视频
     * @param veidoPath
     * @param ffmpegPath
     * @param tranVideoPath
     * @return
     */
	public static boolean rotateVideo(String veidoPath, String ffmpegPath, String tranVideoPath) {
		Map videoInfo = getVideoInfo(veidoPath);
		if(null == videoInfo){
			Log.error("file not exists!!");
			return false;
		}
		if((Double)videoInfo.get("height")>=(Double)videoInfo.get("width")){
			Log.info("不需要转换");
			return false;
		}
		// ffmpeg -i INPUT.AVI -vcodec libx264 -preset slower -crf 18 -threads 4 -vf transpose=2 -acodec copy OUTPUT.MKV
		List<String> convert = new ArrayList<String>();
		convert.add(ffmpegPath); // 添加转换工具路径
		convert.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
		convert.add(veidoPath); // 添加要转换格式的视频文件的路径
		convert.add("-vcodec");
		convert.add("libx264");
		convert.add("-preset");
		convert.add("slower");
		convert.add("-crf");
		convert.add("18");
		convert.add("-threads");
		convert.add("2");
		//convert.add("-metadata:s:v rotate=\"90\"");
		convert.add("-vf");
		convert.add("transpose=1");
		convert.add("-acodec");
		convert.add("copy");
		convert.add(tranVideoPath);
		try {
			Process videoProcess = new ProcessBuilder(convert).redirectErrorStream(true).start();
	        new PrintStream(videoProcess.getInputStream()).start();
	        videoProcess.waitFor();
			Log.info("转换成功");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * m4r音频格式转换为mp3，audioPath可更换为要转换的音频格式
	 * @param audioPath
	 * @param mp3Path
	 */
	public static void m4rToMp3(String audioPath,String mp3Path){
		File source = new File(audioPath);
		File target = new File(mp3Path);
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libmp3lame");
		audio.setBitRate(new Integer(128000));
		audio.setChannels(new Integer(2));
		audio.setSamplingRate(new Integer(44100));
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setOutputFormat("mp3");
		attrs.setAudioAttributes(audio);
		Encoder encoder = new Encoder();
		try {
			encoder.encode(new MultimediaObject(source), target, attrs);
		} catch (EncoderException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从和视频中提取音频wav
	 * @param aviPath
	 * @param targetWavPath
	 */
	public static void videoExtractAudio(String aviPath,String targetWavPath){
		File source = new File(aviPath);
		File target = new File(targetWavPath);
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("pcm_s16le");
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setOutputFormat("wav");
		attrs.setAudioAttributes(audio);
		Encoder encoder = new Encoder();
		try {
			encoder.encode(new MultimediaObject(source), target, attrs);
		} catch (EncoderException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 视频转换为手机可播放的格式
	 * @param sourceVideo sourceVideo.avi
	 * @param targetVideo targetVideo.3gp
	 */
	public static void videoToMobileVideo(String sourceVideo, String targetVideo){
		File source = new File("source.avi");
		File target = new File("target.3gp");
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libfaac");
		audio.setBitRate(new Integer(128000));
		audio.setSamplingRate(new Integer(44100));
		audio.setChannels(new Integer(2));
		VideoAttributes video = new VideoAttributes();
		video.setCodec("mpeg4");
		video.setBitRate(new Integer(160000));
		video.setFrameRate(new Integer(15));
		video.setSize(new VideoSize(176, 144));
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setOutputFormat("3gp");
		attrs.setAudioAttributes(audio);
		attrs.setVideoAttributes(video);
		Encoder encoder = new Encoder();
		try {
			encoder.encode(new MultimediaObject(source), target, attrs);
		} catch (EncoderException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// getVideoInfo("src/main/resources/demo.mp4");
		//rotateVideo("src/main/resources/demo.mp4", "src/main/resources/ffmpeg.exe", "src/main/resources/demo_t.mp4");
		//processImg("src/main/resources/demo.mp4", "src/main/resources/ffmpeg.exe");
		//videoWaterMark("src/main/resources/demo.mp4", "src/main/resources/videoTools/ffmpeg.exe", "src/main/resources/demo_wm.mp4");
		//System.out.println("数据 = [" + getVideoProcessImage("E:/GEEKCJJFile/mylove/avideo/dPQRVRFuZHJ8.mp4","E:/GEEKCJJFile/sfaafasddxg.jpg") + "]");
		//System.out.println("数据 = [" + getVideoInfo("E:/GEEKCJJFile/mylove/avideo/dPQRVRFuZHJ8.mp4") + "]");
		//System.out.println("数据 = [" + videoToAudio("E:/GEEKCJJFile/mylove/avideo/dPQRVRFuZHJ8.mp4","E:/GEEKCJJFile/sfaafasddxgd.mp3") + "]");
		//System.out.println("数据 = [" + videoWaterMark("E:/GEEKCJJFile/mylove/avideo/dPQRVRFuZHJ8.mp4","","E:/GEEKCJJFile/sfaafasddxgd.mp4") + "]");
		//codecToMp4WithText("E:/GEEKCJJFile/mylove/avideo/dPQRVRFuZHJ8.mp4","E:/GEEKCJJFile/sfaafasddxgd.mp4","GarryChan",encoderProgressListener);
		m4rToMp3("E:/i4Tools7/Files/MadeRingtone/iOS7 原版电话铃声.m4r","E:/i4Tools7/Files/MadeRingtone/iOS7 原版电话铃声2.mp3");
	}
}

class PrintStream extends Thread {
	InputStream __is = null;

	public PrintStream(InputStream is) {
		__is = is;
	}

	public void run() {
		try {
			while (this != null) {
				int _ch = __is.read();
				if (_ch != -1)
					System.out.print((char) _ch);
				else
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
