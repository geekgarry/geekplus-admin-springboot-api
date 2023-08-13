/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2/19/23 03:07
 * description: 做什么的？
 */
package com.geekplus.common.util.IPMAC;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.*;

public class IPAndMACUtils {

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

    private static String getAddressIp(){
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

    private static String getAddressMac() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            //ni.getInetAddresses().nextElement().getAddress();            
            byte[] mac = ni.getHardwareAddress();
            String sIP = address.getHostAddress();
            String sMAC = "";
            for (int i = 0; i < mac.length; i++) {
                sMAC = sMAC + String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
            }
            System.out.println("IP" + sIP);
            System.out.println("MAC" + sMAC);
            return sMAC+":"+sIP;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            //return "ab:cd:24:12:34:56";
        } catch (SocketException e){
            e.printStackTrace();
            //return "ab:cd:24:12:34:56";
        }
        return "ab:cd:24:12:34:56";
    }

    /**
     * 获取登录用户IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;

    }

    public static void main(String[] args) {
        //System.out.println("IP="+getAddressIp());
        getAddressIp();

    }
}

