package com.geekplus.common.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/5/23 8:58 上午
 * description: 验证码生成工具类 （根据需要调整图片宽高 验证码个数 同时需要注意字体大小位置的调整）
 */
public class CaptchaImageUtil {
    private BufferedImage buffImg;
    private String code;

    // 图片的宽度
    private static final int CAPTCHA_WIDTH = 110;
    // 图片的高度
    private static final int CAPTCHA_HEIGHT = 45;
    // 验证码的个数
    private static final int CAPTCHA_CODECOUNT = 4;

    private static final int XX = 25;
    private static final int CAPTCHA_FONT_HEIGHT = 33;
    private static final int CAPTCHA_CODE_Y = 34;
    private static final char[] CODE_SEQUENCE = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    // 过期时间为60秒
    private static final long EXPIRE_MINUTES = 60;

    private CaptchaImageUtil() {
        // 定义图像 Buffer
        buffImg = new BufferedImage(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, BufferedImage.TYPE_INT_RGB);
        // 创建一个绘制图像的对象
        Graphics2D g = buffImg.createGraphics();
        // 创建一个随机数生成器类
        Random random = new Random();
        // 将图像填充为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, CAPTCHA_WIDTH, CAPTCHA_HEIGHT);
        // 设置字体
        g.setFont(new Font("Fixedsys", Font.ITALIC, CAPTCHA_FONT_HEIGHT));
        // 设置字体边缘光滑
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 画边框
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, CAPTCHA_WIDTH - 1, CAPTCHA_HEIGHT - 1);
        // 随机产生40条干扰线，使图象中的认证码不易被其它程序探测到。
        g.setColor(Color.GRAY);
        for (int i = 0; i < 40; i++) {
            int x = random.nextInt(CAPTCHA_WIDTH);
            int y = random.nextInt(CAPTCHA_HEIGHT);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        // 保存随机产生的验证码，以便用户登录后进行验证
        StringBuilder randomCode = new StringBuilder();
        int red = 0, green = 0, blue = 0;
        // 随机产生验证码
        for (int i = 0; i < CAPTCHA_CODECOUNT; i++) {
            // 得到随机产生的验证码数字
            String code = String.valueOf(CODE_SEQUENCE[random.nextInt(CODE_SEQUENCE.length)]);
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            // 用随机产生的颜色将验证码绘制到图像中
            g.setColor(new Color(red, green, blue));
            g.drawString(code, (i) * XX, CAPTCHA_CODE_Y);
            // 将产生的随机数组合在一起
            randomCode.append(code);
        }
        code = randomCode.toString();
    }

    public static CaptchaImageUtil getInstance(){
        return new CaptchaImageUtil();
    }

    public BufferedImage getBuffImg() {
        return buffImg;
    }

    public String getCode() {
        return code;
    }

    public static void main(String[] args) {
//        // png类型
//        SpecCaptcha captcha = new SpecCaptcha(130, 48);
//        captcha.text();  // 获取验证码的字符
//        captcha.textChar();  // 获取验证码的字符数组
//
//        // gif类型
//        GifCaptcha captcha = new GifCaptcha(130, 48);
//
//        // 中文类型
//        ChineseCaptcha captcha = new ChineseCaptcha(130, 48);
//
//        // 中文gif类型
//        ChineseGifCaptcha captcha = new ChineseGifCaptcha(130, 48);
//
//        // 算术类型
//        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
//        captcha.setLen(3);  // 几位数运算，默认是两位
//        captcha.getArithmeticString();  // 获取运算的公式：3+2=?
//        captcha.text();  // 获取运算的结果：5
//
//        // 如果不想要base64的头部data:image/png;base64,
//        specCaptcha.toBase64("");  // 加一个空的参数即可
//
//        FileOutputStream outputStream = new FileOutputStream(new File("C:/captcha.png"))
//        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
//        captcha.out(outputStream);  // 输出验证码
    }
}
