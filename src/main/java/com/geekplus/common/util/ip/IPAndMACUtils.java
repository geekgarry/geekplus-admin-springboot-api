/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2/19/23 03:07
 * description: 做什么的？
 */
package com.geekplus.common.util.ip;

import com.geekplus.common.util.AddressUtil;
import com.geekplus.common.util.ServletUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class IPAndMACUtils {

    public static final String MAC_ADDRESS_PREFIX01 = "MAC Address = ";
    public static final String MAC_ADDRESS_PREFIX02 = "MAC 地址 = ";
    public static final String LOOPBACK_ADDRESS = "127.0.0.1";
    public static final String IPv6Address = "0:0:0:0:0:0:0:1";

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

    /**
     * 获取登录用户IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
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

    public static void main(String[] args) throws Exception {
        InetAddress inetAddress = InetAddress.getLocalHost();
        //System.out.println("Mac="+IPAndMACUtils.getMACAddress("117.88.178.33"));
        //getAddressMac();
        //System.out.println("MAC="+getAddressMac());
        //System.out.println("IP="+getIpAddress(ServletUtil.getRequest()));
        //System.out.println("address="+AddressUtils.getRealAddressByIP("117.89.1.9"));
        System.out.println("address="+AddressUtil.getRealAddressByIP("117.89.1.9"));
    }
}

