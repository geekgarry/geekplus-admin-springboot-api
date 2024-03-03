package com.geekplus.codegenerate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class TestDemo {

	public static void main(String[] args) {
		Date date = new Date();
		SimpleDateFormat bjSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		bjSdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		SimpleDateFormat hongkongSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		hongkongSdf.setTimeZone(TimeZone.getTimeZone("Asia/Hong_Kong"));
		System.out.println("毫秒数:" + date.getTime() + ", 北京时间:" + bjSdf.format(date));
		System.out.println("毫秒数:" + date.getTime() + ", 香港时间:" + hongkongSdf.format(date));
		System.out.println(date);
	}

	public void randomNum(){
		int[]arr=new int[20];
		Random suiji=new Random();


		for(int i=0;i<arr.length;i++)
		{        int num=suiji.nextInt(100)+1;
			arr[i]=num;
			for(int j=1;j<=i;j++)
			{
				if(arr[j]==arr[i])
					break;

			}
			System.out.println(arr[i]);
		}
	}
}
