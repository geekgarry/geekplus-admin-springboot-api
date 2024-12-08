package com.geekplus.common.util.uuid;

import com.geekplus.common.util.encrypt.EncryptUtil;
import com.geekplus.common.util.encrypt.SHAUtil;
import org.apache.commons.math3.util.MathUtils;

import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ID生成器工具类
 *
 * @author
 */
public class IdUtils {

    private static final String RND_STRING = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ";
    private static final String RND_NUMBER = "0123456789";

    private static final Random rnd = new Random();

    private static final SecureRandom RANDOM = new SecureRandom();

    private static String middle = "2024";

    private static AtomicInteger count = new AtomicInteger(0);

    // 每毫秒秒最多生成多少订单（最好是像99999这种准备进位的值）
    private static final int total = 99999;

    private static String now =null;

    public IdUtils() {
    }

    // 格式化的时间字符串
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    // 获取当前时间年月日时分秒毫秒字符串
    private static String getNowDateStr() {
        return sdf.format(new Date());
    }

    public static String timeMillisID() {
        Random r = new Random();
        // 如生成的随机位数不足6位则自动加零补充
        DecimalFormat g = new DecimalFormat("1000000");
        // 返回时间增量+随机数的序列
        return String.format("%s%s", System.currentTimeMillis(), g.format(r.nextInt(1000000)));
    }

    public synchronized static String createCount() {
        String dataStr = getNowDateStr();
        count.set(0);
        Integer i = 0;
        if (dataStr.equals(now)) {
            // 自增
            i = count.incrementAndGet();
        } else {
            i = count.get();
            now = dataStr;
        }
        if (count.intValue() >= total) {
            count.set(0);
        }
        String resultNum = String.format("%05d", i);
        // String reslut=s>=10?(s>=100?(s>=1000?s+"":"0"+s):"00"+s):"000"+s; // 计算 转型
        // (s>=10000?s+"":"0"+s)
        return resultNum;
    }

    /**
     * 传入相应的前缀生成自增的ID
     *
     * @param groupID 前缀
     * @param end     上一次结束的ID编号
     * @return 生成自增的ID
     */
    public static String getQualityNum(String groupID, int end) {
        // count.set(end);
        // count.incrementAndGet();
        Integer i = count.getAndIncrement();
        // 获取当前的年
        String getNowDateStr = getNowDateStr();
        // 将传入的前缀与项目部以及年拼接在以一起
        String format = groupID + getNowDateStr;
        String strNum = 100000 + "" + i;
        // 将1截取下来得到剩余的
        String substring = strNum.substring(1);
        // 进行拼接并返回
        return format + substring;
    }

    /**
     * 以毫秒做基础计数, 返回唯一有序增长ID, 有几率出现线程并发
     *
     * <pre>
     * System.currentTimeMillis()
     * </pre>
     *
     * <pre>
     *  线程数量:   100
     *  执行次数:   1000
     *  平均耗时:   206 ms
     *  数组长度:   100000
     *  Map Size:   99992
     * </pre>
     *
     * @return ID长度32位
     */
    public static String getIncreaseIdByCurrentTimeMillis() {
        // 2. 公式: 创建出Random类型的变量
        Random ran = new Random();
        // 3. 变量. 调用Random类中的功能,产生随机数
        // Random类中的,产生随机数的功能
        int i = ran.nextInt(100000000);
        return System.currentTimeMillis() + // 时间戳-14位
                middle + // 标志-8位
                MathUtils.copySign(Thread.currentThread().hashCode(), 3) + // 3位线程标志
                i; // 随机8位数
    }

    /**
     * 生成规则设备编号:设备类型+五位编号（从1开始，不够前补0）
     *
     * @param equipmentType 设备类型
     * @param equipmentNo   最新设备编号
     * @return
     */
    public synchronized String getNewEquipmentNo(String equipmentType, String equipmentNo) {
        String newEquipmentNo = "0001";
        String datastr = getNowDateStr();
        if (equipmentNo != null && !equipmentNo.isEmpty()) {
            int newEquipment = Integer.parseInt(equipmentNo) + 1;
            newEquipmentNo = String.format("%04d", newEquipment);
        }
        return datastr + newEquipmentNo;
    }

    public static String generateULID() {
        long timestamp = Instant.now().toEpochMilli();
        byte[] randomBytes = new byte[30];
        RANDOM.nextBytes(randomBytes);

        long ulid = (timestamp << 80) | byteArrayToLong(randomBytes);
        return Long.toHexString(ulid);
    }

    public static String getSHAULIDDate() {
        long timestamp = Instant.now().toEpochMilli();
        byte[] randomBytes = new byte[30];
        RANDOM.nextBytes(randomBytes);

        long ulid = (timestamp << 80) | byteArrayToLong(randomBytes);
        return SHAUtil.getSHA(Long.toHexString(ulid)+getNowDateStr());
    }

    public static String getFileULID() {
        long timestamp = Instant.now().toEpochMilli();
        byte[] randomBytes = new byte[30];
        RANDOM.nextBytes(randomBytes);

        long ulid = (timestamp << 80) | byteArrayToLong(randomBytes);
        return Long.toHexString(ulid) + getNowDateStr();
    }

    public static String getSHAFileULID() {
        long timestamp = Instant.now().toEpochMilli();
        byte[] randomBytes = new byte[30];
        RANDOM.nextBytes(randomBytes);

        long ulid = (timestamp << 80) | byteArrayToLong(randomBytes);
        return SHAUtil.getSHAStrBySaltAndNewMethod(Long.toHexString(ulid)+getNowDateStr());
    }

    public static String getMd5FileULID() {
        long timestamp = Instant.now().toEpochMilli();
        byte[] randomBytes = new byte[30];
        RANDOM.nextBytes(randomBytes);

        long ulid = (timestamp << 80) | byteArrayToLong(randomBytes);
        return EncryptUtil.md5Hex(Long.toHexString(ulid)+getNowDateStr());
    }

    public static String getBase64FileULID() {
        long timestamp = Instant.now().toEpochMilli();
        byte[] randomBytes = new byte[30];
        RANDOM.nextBytes(randomBytes);

        long ulid = (timestamp << 80) | byteArrayToLong(randomBytes);
        return EncryptUtil.base64Encode(Long.toHexString(ulid)+getNowDateStr());
    }

    private static long byteArrayToLong(byte[] bytes) {
        long result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result = (result << 8) | (bytes[i] & 0xFF);
        }
        return result;
    }

    public static void getLocalHost(){
        try{
            InetAddress ip = InetAddress.getLocalHost();
            String localName = ip.getHostName();
            String osName = System.getProperty("os.name");
            String userName = System.getProperty("user.name");
            String osVersion = System.getProperty("os.version");
            String osArch = System.getProperty("os.arch");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取随机字符串 较短数量
     * @param length
     * @return
     */
    public static String getRndStr(Integer length){
        String backMsg = "";
        for(int i=0;i<length;i++){
            backMsg += RND_STRING.charAt(rnd.nextInt(RND_STRING.length()));
        }
        return backMsg;
    }

    public static Boolean testRndStr(Integer size,Integer strSize,Boolean isDetail){
        long startTime = System.currentTimeMillis();
        if(size < 100 || size >100001){
            size = 5000;
        }
        if(strSize < 3 || strSize >100){
            strSize = 10;
        }
        List<String> arrayList = new ArrayList<>();
        Set<String> hashSet = new HashSet<>();
        for(int i=0;i<size;i++){
            String value = getRndStr(strSize);
            arrayList.add(value);
            hashSet.add(value);
        }
        if(isDetail){
            System.out.println("预生成随机数个数: " + size + ",尺寸: " + strSize);
            System.out.println("ArrayList size: " + arrayList.size());
            System.out.println("HashSet size: " + hashSet.size());
            System.out.println("总共消耗: "+(System.currentTimeMillis()-startTime)+" 毫秒");
        }
        //System.out.println("比率: " + hashSet.size() + "/" + arrayList.size());
        return (arrayList.size() == hashSet.size());
    }

    public static String getRndNum(Integer length){
        String backMsg = "";
        for(int i=0;i<length;i++){
            backMsg += RND_STRING.charAt(rnd.nextInt(RND_NUMBER.length()));
        }
        return backMsg;
    }

    /**
     * 获取用户分享码 全局唯一
     * timestamp + rnd 6 number
     * @return
     */
    public static String getShareKeyCode(){
        return EncryptUtil.MD5(""+new Date().getTime()+getRndNum(6));
    }

    public static void main(String[] args) {
        System.out.println(generateULID());
        System.out.println(getIncreaseIdByCurrentTimeMillis());
        System.out.println(MathUtils.copySign(Thread.currentThread().hashCode(), 3));
//        System.out.println(timeMillisID());
//        System.out.println(getQualityNum("rider", 0));
//        System.out.println(new Random().nextInt(1000000));
    }
}
