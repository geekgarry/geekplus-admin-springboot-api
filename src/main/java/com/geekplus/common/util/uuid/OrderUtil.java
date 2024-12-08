package com.geekplus.common.util.uuid;

import com.geekplus.common.util.datetime.DateTimeUtils;
import com.geekplus.common.util.datetime.DateUtil;
import com.geekplus.common.util.string.StringUtils;

import java.util.Date;
import java.util.Random;

/**
 * author     : geekplus
 * email      :
 * date       : 9/21/24 7:26 PM
 * description: //订单/商单工具
 */
public class OrderUtil {

    // 每毫秒秒最多生成多少订单（最好是像99999这种准备进位的值）
    private static final int total = 99999;

    // 全局自增数
    private static int autoInsert = 0;

    private static Date nowDate;

    private static StringBuilder buf = new StringBuilder();

    public static void main(String[] args) {
//    	String appString = "chan";
//	    System.out.println(StringUtil.getMainID(appString));
//	    System.out.println(StringUtil.getMainID(appString));
//    	for(int i=0;i<10;i++){
//    		System.out.println(getRndStr(6));
//    	}
//    	for(int i=0;i<15;i++){
//    		System.out.println(StringUtil.getMainID(null));
//    	}
        //System.out.println(getShareKeyCode());
        //System.out.println(HloveyRC4("ÏnBÈöÒ2þß	y7"));
//    	Integer isTrue = 0;
//    	for (int i = 0; i < 100; i++) {
//    		if(testRndStr(100000, 8,false)){
//    			isTrue ++;
//    		}
//		}
//    	System.out.println("测试总数: "+100+"/"+isTrue);
        //$2a$10$mYtRg282kepowy1XlRRJueO8YJ56S7IN8aw9BLiYTwBKvcs8g8.ru
        //String encrptPWd = springSecurityEncrpt("Uwsu3wdW");
        //String encrptPWd = springSecurityEncrpt("12345678");
        //System.out.println(encrptPWd);
        // $2a$10$kDMqweJAIZdOSXu/sp5Ew./JuBTDiq/6Tk1Pc9ff5xTWB8kNpkQ1m
        //System.out.println("$2a$10$C8kXIo6dp5ZIo9I4YRBVsOuvLbRWrSQ4vu91B9djRT8Dh6RSJRzEW".equals(encrptPWd));
    }

    public OrderUtil() {
        this.nowDate = new Date();
    }

    /**
     * @Author geekplus
     * @Description //生成订单编号
     * @Param
     * @Throws
     * @Return {@link }
     */
    public static synchronized long next() {
        if (autoInsert > total)
            autoInsert = 1;
        //buf.delete(0, buf.length());
        nowDate.setTime(System.currentTimeMillis());
        String str = String.format("%1$tY%1$tm%1$td%2$05d", nowDate, autoInsert++);
        return Long.parseLong(str);
    }

    /**
     * 获取订单编号 timestamp + 6位随机数字串
     * @return
     */
    public static String getOrderId(){
        String str = "";
        str += new Date().getTime() + IdUtils.getRndNum(6);
        return str;
    }

    /*
     * 生成一个订单号
     */
    public static String bornOrder(String param) {
		//String param="0005"; // 首先查询出那个counter值
        if (param == null) {
            param = "100000";
        }
        Random random = new Random();
        int s = random.nextInt(Integer.parseInt(param));
        ++s;
        s = (s == 100000 ? 1 : s); // 这里将规定最大数字设定为小于1000 如果对生成的数字没有特定要求可以注释掉 我这里没有要求所以进行了注释
        String result = String.format("%05d", s);// s>=10?(s>=100?s+"":"0"+s):"00"+s; // 计算 转型
        String dataStr = DateUtil.dateTimeNow();
        return dataStr + result;
    }

    /**
     *
     * @Title: HloveyRC4
     * @Description:
     * @param aInput 输入的字符
     * @return String 返回加密、解密后的字符 可以自动获取方式
     * @author geekplus
     * @date 2018年8月8日上午11:29:35
     */
    public static String HloveyRC4(String aInput)
    {
        String aKey = "Iloveyou.123";
        int[] iS = new int[256];
        byte[] iK = new byte[256];

        for (int i=0;i<256;i++)
            iS[i]=i;

        int j = 1;

        for (short i= 0;i<256;i++)
        {
            iK[i]=(byte)aKey.charAt((i % aKey.length()));
        }
        j=0;
        for (int i=0;i<255;i++)
        {
            j=(j+iS[i]+iK[i]) % 256;
            int temp = iS[i];
            iS[i]=iS[j];
            iS[j]=temp;
        }
        int i=0;
        j=0;
        char[] iInputChar = aInput.toCharArray();
        char[] iOutputChar = new char[iInputChar.length];
        for(short x = 0;x<iInputChar.length;x++)
        {
            i = (i+1) % 256;
            j = (j+iS[i]) % 256;
            int temp = iS[i];
            iS[i]=iS[j];
            iS[j]=temp;
            int t = (iS[i]+(iS[j] % 256)) % 256;
            int iY = iS[t];
            char iCY = (char)iY;
            iOutputChar[x] =(char)( iInputChar[x] ^ iCY) ;
        }
        return new String(iOutputChar);
    }
    /**
     * 拼接字符串
     * @Title: appendStr
     * @Description:
     * @param original
     * @param next
     * @param separator
     * @return String
     * @author geekplus
     * @date 2018年8月24日上午10:01:27
     */
    public static String appendStr(String original,String next,String separator){
        buf.setLength(0);
        buf.append(original);
        buf.append(separator);
        buf.append(next);
        return buf.toString();
    }
    /**
     * 生成订单标题内容
     * @Title: genOrderSubject
     * @Description:
     * @param name 第一件商品名称
     * @param count 总订单商品数量
     * @return String **..等共**件商品
     * @author geekplus
     * @date 2018年8月24日上午10:03:22
     */
    public static String genOrderSubject(String name,Integer count){
        buf.setLength(0);
        buf.append(name);
        buf.append("..等共");
        buf.append(count);
        buf.append("件商品");
        return buf.toString();
    }
}
