package com.geekplus.common.util;

/**
 * 经纬度计算
 * @author cpp
 *
 */
public class HarvenSinUtil {

	public static Double EARTH_RADIUS = 6371.0;//km 地球半径 平均值，千米
	public static Double haverSin(Double theta) {
		Double v = Math.sin(theta/2);
		return BigDecimalUtil.mul(v, v);
	}
	/**
	 * 将角度换算成弧度
	 * @param degrees 角度
	 * @return
	 */
	public static Double convertDegreesToRadians(Double degrees) {
		return BigDecimalUtil.div(BigDecimalUtil.mul(degrees, Math.PI),180);
	}
	/**
	 * 将弧度转化成角度
	 * @param radian
	 * @return
	 */
	public static Double convertRadiansToDegrees(Double radian) {
		return BigDecimalUtil.div(BigDecimalUtil.mul(radian,180),Math.PI);
	}
	/**
	 * 计算两个经纬度之间距离
	 * @param lon1 经度1
	 * @param lat1 纬度1
	 * @param lon2 经度2
	 * @param lat2 纬度2
	 * @return
	 */
	public static Double distance(Double lon1,Double lat1, Double lon2,Double lat2)
    {
        //用haversine公式计算球面两点间的距离。
        //经纬度转换成弧度
        lat1 = convertDegreesToRadians(lat1);
        lon1 = convertDegreesToRadians(lon1);
        lat2 = convertDegreesToRadians(lat2);
        lon2 = convertDegreesToRadians(lon2);

        //差值
        Double vLon = Math.abs(lon1 - lon2);
        Double vLat = Math.abs(lat1 - lat2);

        //h is the great circle distance in radians, great circle就是一个球体上的切面，它的圆心即是球心的一个周长最大的圆。
        Double h = haverSin(vLat) + Math.cos(lat1) * Math.cos(lat2) * haverSin(vLon);

        Double distance = 2 * EARTH_RADIUS * Math.asin(Math.sqrt(h));

        return distance;
    }
	public static void main(String[] args) {
		Double distance = distance(116.327d, 40.003, 116.31, 39.993);
		System.out.println(distance);
	}
}
