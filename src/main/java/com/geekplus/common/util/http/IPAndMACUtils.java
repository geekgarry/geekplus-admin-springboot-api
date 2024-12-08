package com.geekplus.common.util.http;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2/19/23 03:07
 * description: 做什么的？
 */
@Slf4j
public class IPAndMACUtils {

    public static final String MAC_ADDRESS_PREFIX01 = "MAC Address = ";
    public static final String MAC_ADDRESS_PREFIX02 = "MAC 地址 = ";
    public static final String LOOPBACK_ADDRESS = "127.0.0.1";
    public static final String IPv6Address = "0:0:0:0:0:0:0:1";

    private String sRemoteAddr;
    private int iRemotePort = 137;
    private byte[] buffer = new byte[1024];
    private DatagramSocket ds = null;

    public IPAndMACUtils(String strAddr) throws Exception {
        sRemoteAddr = strAddr;
        ds = new DatagramSocket();
    }
    protected final DatagramPacket send(final byte[] bytes) throws IOException {
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length, InetAddress
                .getByName(sRemoteAddr), iRemotePort);
        ds.send(dp);
        return dp;
    }
    protected final DatagramPacket receive() throws Exception {
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        ds.receive(dp);
        return dp;
    }
    // 询问包结构:
    // Transaction ID 两字节（16位） 0x00 0x00
    // Flags 两字节（16位） 0x00 0x10
    // Questions 两字节（16位） 0x00 0x01
    // AnswerRRs 两字节（16位） 0x00 0x00
    // AuthorityRRs 两字节（16位） 0x00 0x00
    // AdditionalRRs 两字节（16位） 0x00 0x00
    // Name:array [1..34] 0x20 0x43 0x4B 0x41(30个) 0x00 ;
    // Type:NBSTAT 两字节 0x00 0x21
    // Class:INET 两字节（16位）0x00 0x01
    protected byte[]  getQueryCmd() throws Exception {
        byte[] t_ns = new byte[50];
        t_ns[0] = 0x00;
        t_ns[1] = 0x00;
        t_ns[2] = 0x00;
        t_ns[3] = 0x10;
        t_ns[4] = 0x00;
        t_ns[5] = 0x01;
        t_ns[6] = 0x00;
        t_ns[7] = 0x00;
        t_ns[8] = 0x00;
        t_ns[9] = 0x00;
        t_ns[10] = 0x00;
        t_ns[11] = 0x00;
        t_ns[12] = 0x20;
        t_ns[13] = 0x43;
        t_ns[14] = 0x4B;
        for (int i = 15; i < 45; i++) {
            t_ns[i] = 0x41;
        }
        t_ns[45] = 0x00;
        t_ns[46] = 0x00;
        t_ns[47] = 0x21;
        t_ns[48] = 0x00;
        t_ns[49] = 0x01;
        return t_ns;
    }
    // 表1 “UDP－NetBIOS－NS”应答包的结构及主要字段一览表
    // 序号 字段名 长度
    // 1 Transaction ID 两字节（16位）
    // 2 Flags 两字节（16位）
    // 3 Questions 两字节（16位）
    // 4 AnswerRRs 两字节（16位）
    // 5 AuthorityRRs 两字节（16位）
    // 6 AdditionalRRs 两字节（16位）
    // 7 Name 34字节（272位）
    // 8 Type:NBSTAT 两字节（16位）
    // 9 Class:INET 两字节（16位）
    // 10 Time To Live 四字节（32位）
    // 11 Length 两字节（16位）
    // 12 Number of name 一个字节（8位）
    // NetBIOS Name Info 18×Number Of Name字节
    // Unit ID 6字节（48位
    protected final String getMacAddr(byte[] brevdata) throws Exception {
        // 获取计算机名
        int i = brevdata[56] * 18 + 56;
        String sAddr = "";
        StringBuffer sb = new StringBuffer(17);
        // 先从第56字节位置，读出Number Of Names（NetBIOS名字的个数，其中每个NetBIOS Names
        // Info部分占18个字节）
        // 然后可计算出“Unit ID”字段的位置＝56＋Number Of
        // Names×18，最后从该位置起连续读取6个字节，就是目的主机的MAC地址。
        for (int j = 1; j < 7; j++) {
            sAddr = Integer.toHexString(0xFF & brevdata[i + j]);
            if (sAddr.length() < 2) {
                sb.append(0);
            }
            sb.append(sAddr.toUpperCase());
            if (j < 6)
                sb.append('-');
        }
        return sb.toString();
    }
    public final void close() {
        try {
            ds.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public final String getRemoteMacAddr() throws Exception {
        byte[] bqcmd = getQueryCmd();
        send(bqcmd);
        DatagramPacket dp = receive();
        String smac = getMacAddr(dp.getData());
        close();
        return smac;
    }

    /**
     * 通过IP地址获取MAC地址
     *
     * @param ip String,127.0.0.1格式
     * @return mac String
     * @throws Exception
     */
    public static String getMACAddress(String ip) {
        String line = "";
        String macAddress = "00:00:00:11:11:11";
        try
        {
            //如果为127.0.0.1,则获取本地MAC地址。
            if (LOOPBACK_ADDRESS.equals(ip)) {
                InetAddress inetAddress = InetAddress.getLocalHost();
                byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
                //下面代码是把mac地址拼装成String
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    if (i != 0) {
                        sb.append("-");
                    }
                    String s = Integer.toHexString(mac[i] & 0xFF);
                    sb.append(s.length() == 1 ? 0 + s : s);
                }
                //把字符串所有小写字母改为大写成为正规的mac地址并返回
                macAddress = sb.toString().trim().toUpperCase();
                return macAddress;
            }
            //获取非本地IP的MAC地址
            Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
            InputStreamReader isr = new InputStreamReader(p.getInputStream(), "GBK");
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line != null) {
                    //英文环境下，命令行执行nbtstat -A [ip] 返回结果包含"MAC Address ="
                    if (line.contains(MAC_ADDRESS_PREFIX01)) {
                        macAddress = fromLineToGetMacAddress(line, MAC_ADDRESS_PREFIX01);
                    }
                    //中文环境下，命令行执行nbtstat -A [ip] 返回结果包含"MAC 地址 ="
                    if (line.contains(MAC_ADDRESS_PREFIX02)) {
                        macAddress = fromLineToGetMacAddress(line, MAC_ADDRESS_PREFIX02);
                    }
                }
            }
            br.close();
            return macAddress;
        }catch (UnknownHostException e){
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return macAddress;
    }

    public static String fromLineToGetMacAddress(String line, String MAC_ADDRESS_PREFIX) {
        String macAddress = "";
        int index = line.indexOf(MAC_ADDRESS_PREFIX);
        if (index != -1) {
            macAddress = line.substring(index + MAC_ADDRESS_PREFIX.length()).trim().toUpperCase();
        }
        return macAddress;
    }

    public static String getAddressIp(){
        String ip="";
        try
        {
            ip= InetAddress.getLocalHost().getHostAddress();
            System.out.println(ip);
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
            //return "127.0.0.1";
        }
        return ip;
    }

    public static String getAddressMac() {
        StringBuilder sb = new StringBuilder();
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            byte[] mac = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || netInterface.isPointToPoint() || !netInterface.isUp()) {
                    continue;
                } else {
                    mac = netInterface.getHardwareAddress();
                    if (mac != null) {
                        for (int i = 0; i < mac.length; i++) {
                            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "\n"));
                        }
                    }
                }
            }
        } catch (SocketException e){
            e.printStackTrace();
            //return "ab:cd:24:12:34:56";
        }
        log.info("mac地址：\n"+sb.toString());
        String[] strArr=sb.toString().split("\n");
        return strArr[strArr.length-1];
    }

    /**
     * 获取mac地址
     * @param
     * @return
     * @throws SocketException
     * @throws UnknownHostException
     */
    public static String getMAC() {
        String mac = null;
        try {
            Process pro = Runtime.getRuntime().exec("cmd.exe /c ipconfig/all");
            InputStream is = pro.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String message = br.readLine();
            int index = -1;
            while (message != null) {
                if ((index = message.indexOf("Physical Address")) > 0) {
                    mac = message.substring(index + 36).trim();
                    break;
                }
                message = br.readLine();
            }
            System.out.println(mac);
            br.close();
            pro.destroy();
        } catch (IOException e) {
            System.out.println("Can't get mac address!");
            return null;
        }
        return mac;
    }

    public static String getMacAddrByIp(String ip) {
        String macAddr = null;
        try {
            Process process = Runtime.getRuntime().exec("`nbtstat` -a " + ip);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            Pattern pattern = Pattern.compile("([A-F0-9]{2}-){5}[A-F0-9]{2}");
            Matcher matcher;
            for (String strLine = br.readLine(); strLine != null;
                 strLine = br.readLine()) {
                matcher = pattern.matcher(strLine);
                if (matcher.find()) {
                    macAddr = matcher.group();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(macAddr);
        return macAddr;
    }

    private static String getIPAddressMac() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            //ni.getInetAddresses().nextElement().getAddress();            
            byte[] mac = ni.getHardwareAddress();
            //String sIP = address.getHostAddress();
            String sMAC = "";
            for (int i = 0; i < mac.length; i++) {
                sMAC = sMAC + String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
            }
            return sMAC;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            //return "ab:cd:24:12:34:56";
        } catch (SocketException e){
            e.printStackTrace();
            //return "ab:cd:24:12:34:56";
        }
        return "ab:cd:24:12:34:56";
    }

    public static void main(String[] args) throws Exception {
        InetAddress inetAddress = InetAddress.getLocalHost();
        //System.out.println("Mac="+IPAndMACUtils.getMACAddress("117.88.178.33"));
        //getAddressMac();
        //System.out.println("MAC="+getAddressMac());
        //System.out.println("IP="+getIpAddress(ServletUtil.getRequest()));
        //System.out.println("address="+AddressUtils.getRealAddressByIP("117.89.1.9"));
        System.out.println(getIPAddressMac());
    }
}

