/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 9/28/23 05:27
 * description: 做什么的？
 */
package com.geekplus.common.util.html;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {
    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符
    private static final String regEx_w = "<w[^>]*?>[\\s\\S]*?<\\/w[^>]*?>";//定义所有w标签
    /**
     * @param htmlStr
     * @return 删除Html标签
     * @author LongJin
     */
    public static String delHTMLTag(String htmlStr) {
        Pattern p_w = Pattern.compile(regEx_w, Pattern.CASE_INSENSITIVE);
        Matcher m_w = p_w.matcher(htmlStr);
        htmlStr = m_w.replaceAll(""); // 过滤script标签
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签
        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签
        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
        htmlStr = htmlStr.replaceAll(" ", ""); //过滤
        return htmlStr.trim(); // 返回文本字符串
    }

    public static Object buildHtmlWords(String text){
        StringBuffer buffer=new StringBuffer();
        if(text.contains("；")){
            String[] textArr=text.split("；");
            if(textArr.length==2){
                String[] textArr1=textArr[0].split("，");
                if(textArr1.length>16) {
                    if(textArr1.length>2){
                        String textTemp=null;
                        for (String oneText: textArr1) {
                            if(oneText.length()>=9){
                                buffer.append("<div class=\"wow rollIn bg-red\" data-wow-iteration=\"1\" data-wow-duration=\"0.5s\">"+oneText+"，</div>");
                            }
                            if(oneText.length()<9){
                                buffer.append("<div class=\"wow pulse bg-yellow\" data-wow-delay=\"0.1s\">"+oneText+"，</div>");
                            }
                        }
                    }else if(textArr1.length==2){
                        buffer.append("<div class=\"wow pulse bg-yellow\" data-wow-delay=\"0.1s\">"+textArr1[0]+"，</div>");
                        buffer.append("<div class=\"wow rollIn bg-red\" data-wow-iteration=\"1\" data-wow-duration=\"0.5s\">"+textArr1[1]+"，</div>");
                    }else if(textArr1.length==1){
                        buffer.append("<div class=\"wow pulse bg-yellow\" data-wow-delay=\"0.1s\">"+textArr1[0]+"，</div>");
                    }
                }else{
                    buffer.append("<div class=\"wow pulse bg-yellow\" data-wow-delay=\"0.1s\">"+textArr[0]+"；</div>");
                }
                String textTemp=textArr[1];
                if(textTemp.contains("——")){
                    String[] textArr2=textTemp.split("——");
                    buffer.append("<div class=\"wow bounceInRight bg-blue\">"+textArr2[0]+"</div>");
                    buffer.append("<div class=\"wow bounceInRight bg-blue\">———"+textArr2[1]+"</div>");
                }
            }
        }else{
            String[] textArr=text.split("，");
            List<String> textTemp=new ArrayList<>();
            String shortText=null;
            int j=-1;
            for (int i = 0; i < textArr.length; i++) {
                String oneText=textArr[i];
                Boolean hasJuHao=oneText.endsWith("。");
                String endString=hasJuHao?"":"，";
                if(oneText.length()>=9&&!oneText.contains("——")){
                    textTemp.add("<div class=\"wow rollIn bg-red\" data-wow-iteration=\"1\" data-wow-duration=\"0.5s\">"+oneText+endString+"</div>");
                    //buffer.append("<div class=\"wow rollIn bg-red\" data-wow-iteration=\"1\" data-wow-duration=\"0.5s\">"+oneText+endString+"</div>");
                }
                if(oneText.length()<9&&!oneText.contains("——")){
                    if(j==i-1){
                        shortText+='，'+oneText;
                        textTemp.add("<div class=\"wow pulse bg-yellow\" data-wow-delay=\"0.1s\">"+shortText+"，</div>");
                        textTemp.remove(j-1);
                    }else {
                        shortText = oneText;
                        textTemp.add("<div class=\"wow pulse bg-yellow\" data-wow-delay=\"0.1s\">"+shortText+"，</div>");
                    }
                    j=i;
                    //buffer.append("<div class=\"wow pulse bg-yellow\" data-wow-delay=\"0.1s\">"+oneText+"，</div>");
                }
                if(oneText.contains("——")){
                    String[] textArr3=oneText.split("——");
                    textTemp.add("<div class=\"wow bounceInRight bg-blue\">"+textArr3[0]+"</div>");
                    textTemp.add("<div class=\"wow bounceInRight bg-blue\">———"+textArr3[1]+"</div>");
                    //buffer.append("<div class=\"wow bounceInRight bg-blue\">"+textArr3[0]+"</div>");
                    //buffer.append("<div class=\"wow bounceInRight bg-blue\">———"+textArr3[1]+"</div>");
                }
            }
            for (String oneWord:textTemp) {
                buffer.append(oneWord);
            }
        }
        return buffer;
    }

    public static void main(String[] args) {
        //System.out.println(delHTMLTag("<p>蜜蜂从花中啜蜜，离开时营营的道谢。浮夸的蝴蝶却相信花是应该向他道谢的。—— 泰戈尔</p>"));
        System.out.println(buildHtmlWords("我太清楚存在于我们之间的困难，遂不敢有所等待，几次想忘于世，总在山穷水尽处又悄然相见，算来即是一种不舍。——简媜《四月裂帛》"));
    }
}
