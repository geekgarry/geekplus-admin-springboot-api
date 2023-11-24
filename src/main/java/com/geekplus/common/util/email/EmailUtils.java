/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 10/12/23 23:09
 * description: 做什么的？
 */
package com.geekplus.common.util.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailUtils {
    public static void  sendEmail(String toSomeOne,String body,String type,String title){
        String user = "xxx@xx.com";
        String password = "123456";

        Properties p = new Properties();
        Session session = Session.getInstance(p,new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
//		session.setDebug(true);
        //设置邮箱传输协议
        p.put("mail.transport.protocol", "smtp");
        //设置邮箱的主机地址
        p.put("mail.smtp.host", "smtp.qq.com");
        //设置邮箱的端口
        p.put("mail.smtp.port", "587");
        //邮箱是否认证
        p.put("mail.smtp.auth", "true");
        //是否开启ssl
//		p.put("mail.smtp.ssl.enable", "false");

        //简历邮箱消息对象
        MimeMessage message = new MimeMessage(session);
        try {
            //设置发送者邮箱地址信息
            Address address = new InternetAddress(user,"geekplus","utf-8");
            message.setFrom(address);
            //设置接受者邮箱地址信息
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toSomeOne));
            //设置接受者邮箱地址信息
//			message.setRecipient(RecipientType.CC, new InternetAddress("xxxx@qq.com"));
            //设置接受者邮箱地址信息
//			message.setRecipient(RecipientType.CC, new InternetAddress("xxxx@qq.com"));
            //设置邮件主题信息及编码
            message.setSubject(title,"utf-8");
            //设置邮件的内容编码
            //"text/plain;charset=utf-8"
            message.setContent(body,type);
            //设置邮件发送日期
            message.setSentDate(new Date());


            //保存邮件的所有信息
            message.saveChanges();

//			OutputStream os = new FileOutputStream("f:/1.eml");
//			 message.writeTo(os);
//			 os.close();
            //开始发送
            Transport.send(message);
            System.out.println("发送邮件");
        } catch ( Exception e) {
            e.printStackTrace();
            System.out.println("发送邮件异常失败");
        }
//		return message;
    }
}
