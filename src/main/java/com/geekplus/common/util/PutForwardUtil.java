package com.geekplus.common.util;

import java.util.Date;

/**
 * 提现工具
 * @author cpp
 *
 */
public class PutForwardUtil {

	public static final Double SERVICE_CHARGE_RATE = 0.001d;
	public static final Double SERVICE_CHARGE_MIN = 0.01d;
	public static final Double ORIGINAL_MONEY_MIN = 100d;
	private static Date date = new Date();
    private static StringBuilder buf = new StringBuilder();
    private static int seq = 1;
    private static final int ROTATION = 99999;

	public static Double getServiceCharge(Double originalMoney) {
		Double serviceCharge = 0.1d;
		if(originalMoney > ORIGINAL_MONEY_MIN) {
			serviceCharge = BigDecimalUtil.mul(originalMoney, SERVICE_CHARGE_RATE);
		}
		return serviceCharge;
	}
	public static Double getRealMoney(Double originalMoney,Double serviceCharge) {
		return BigDecimalUtil.sub(originalMoney,serviceCharge);
	}
	public static Boolean isValidateOriginalMoney(Double originalMoney) {
		Boolean isSuccess = true;
		if(null == originalMoney || originalMoney <= 0.1d) {
			isSuccess = false;
		}
		return isSuccess;
	}
    public static synchronized String getRecordNumber() {
        if (seq > ROTATION)
            seq = 1;
        buf.delete(0, buf.length());
        date.setTime(System.currentTimeMillis());
        String str = String.format("%1$tY%1$tm%1$td%1$tk%1$tM%1$tS%2$05d", date, seq++);
        return str;
    }
    public static void main(String[] args) {
		System.out.println(getRecordNumber());
		System.out.println(getRecordNumber());
		System.out.println(getRecordNumber());
	}
}
