package com.geekplus.common.util.getpic;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetUrl {

	/**
	 * 使用正则表达式提取中括号中的内容
	 *
	 * @param htmlCode
	 * @return
	 */
	public static List<String> getImageSrc(String htmlCode) {
		List<String> imageSrcList = new ArrayList<String>();
		Pattern p = Pattern.compile(
				"<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic)\\b)[^>]*>",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(htmlCode);
		String quote = null;
		String src = null;
		while (m.find()) {
			quote = m.group(1);
			src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("\\s+")[0] : m.group(2);
			imageSrcList.add(src);

		}
		return imageSrcList;
	}

	/**
	 * 获取主机的主地址
	 *
	 * @param url
	 * @return
	 */
	public static String getFirstUrl(String url) {

		return url.substring(url.indexOf("http://"), url.indexOf("/", 7));
	}

}
