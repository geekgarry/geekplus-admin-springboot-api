//package com.geekplus.common.domain;
//
//
//import com.geekplus.common.constant.ConstValue;
//import com.geekplus.common.util.DateUtil;
//import com.geekplus.common.util.SHAUtil;
//import com.geekplus.common.util.string.StringUtil;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//
///**
// *
// * @ClassName AuthService
// * @Description 登录权限验证
// * @Author GarryChan
// * @Date 2017年9月28日 上午11:32:58
// */
//@Service
//public class AuthService {
//
//	// 存储用户登录token和Login信息
//	public static Map<String, Login> loginUserMap = new HashMap<>();
//	// 存储accountName和token
//	public static Map<String,String> tokenMap = new HashMap<>();
//	// 临时的授权key token ,value checkCode
//	public static Map<String,TmpAuth> tempAuth = new HashMap<>();
//	/**
//	 * 这边三种用户 都不能出现用户名相同 否则会出现Bug token 冲突,
//	 * TODO 注册的时候需要进行 几个账户表的用户不能相同 这边就OK
//	 * 获取登录令牌
//	 * @param accountId
//	 * @param accountName
//	 * @param password
//	 * @param userLevel
//	 * @return
//	 */
//	public static String getToken(String accountId,String accountName ,String password,Integer userLevel){
//		String token = tokenMap.get(accountName);
//		Login login ;
//		// 如果token 为空则表示第一次登录
//		if(null==token||"".equals(token)){
//			login = new Login();
//			login.setAccountName(accountName);
//			login.setPassword(password);
//			login.setLoginTime(new Date());
//			login.setAccountId(accountId);
//			login.setUserLevel(userLevel);
//		}else{
//			login = loginUserMap.get(token);
//		}
//		// 生成Token name + pwd + UUID
//		token = SHAUtil.getMD5(login.getAccountName() + login.getPassword() + UUID.randomUUID());
//		// 放入map中
//		tokenMap.put(login.getAccountName(), token);
//	    loginUserMap.put(token, login);
//		return token;
//	}
//
//	public static Login validateToken(String token){
//		Login login = loginUserMap.get(token);
//		if(null != login){
//			if(DateUtil.isOutOfDay(login.getLoginTime(), ConstValue.LOGIN_OVER_TIME)){
//			    return login;
//			}else{
//				// 进行退出操作
//				logOut(login.getAccountName());
//			}
//		}
//		return null;
//	}
//	public static Login validateToken(String token,Integer userLevel){
//		Login login = loginUserMap.get(token);
//		if(null != login){
//			if(DateUtil.isOutOfDay(login.getLoginTime(), ConstValue.LOGIN_OVER_TIME)){
//				if(login.getUserLevel() == userLevel)
//			        return login;
//			}else{
//				// 进行退出操作
//				logOut(login.getAccountName());
//			}
//		}
//		return null;
//	}
//	public static String validateAccountName(String accountName){
//		return tokenMap.get(accountName);
//	}
//	public static void logOut(String accountName){
//		String token = tokenMap.get(accountName);
//		tokenMap.remove(accountName);
//		loginUserMap.remove(token);
//	}
//
//	/**
//	 * 获得临时授权
//	 * @param checkCode 验证码
//	 * @return 临时授权 null则获取授权失败
//	 */
//	public static String getTempAuth(String checkCode){
//		String token = SHAUtil.getMD5(StringUtil.getRndString()+checkCode);
//		TmpAuth tmpAuth =  new TmpAuth(checkCode);
//		if(tempAuth.put(token,tmpAuth) !=null){
//			System.out.println(token);
//		}
//		return token;
//	}
//	public static void main(String[] args) {
//		String token = getTempAuth("asjjdkas");
//		TmpAuth  tmpAuth = tempAuth.get(token);
//		if(tmpAuth != null){
//			System.out.println(tmpAuth.getData()+":"+DateUtil.dateToStr(tmpAuth.getCreateTime(), null));
//		}
//
//	}
//	/**
//	 * 获取临时授权方式2
//	 * @param key
//	 * @param value
//	 * @return if get the tempAuth fail return null else success return the original key
//	 */
//	public static String getTempAuth(String key ,String value){
//		if(StringUtil.validNotNull(key)){
//			return null;
//		}
//		if(tempAuth.put(key, new TmpAuth(value)) !=null){
//			return key;
//		}
//		return null;
//	}
//	/**
//	 * 验证临时授权并移除 临时授权失效1小时
//	 * @param token
//	 * @param checkCode 验证码
//	 * @return true 成功  false 失败
//	 */
//	public static boolean validateTempAuth(String token,String checkCode){
//		TmpAuth value = tempAuth.get(token);
//		if(null !=value){
//			/**
//			 * 验证是否过时
//			 * 验证是否正确
//			 */
//			if(DateUtil.isOutOfDay(value.getCreateTime(), 1)){
//				// 忽略大小写
//				boolean flag = value.getData().equalsIgnoreCase(checkCode);
//				if(flag)
//					tempAuth.remove(token);
//				return flag;
//			}
//		}
//		return false;
//	}
//}
//
//class TmpAuth{
//	private String data;
//	private Date createTime;
//
//	public TmpAuth() {
//	}
//
//	public TmpAuth(String data) {
//		this.data = data;
//		this.createTime = new Date();
//	}
//
//	public String getData() {
//		return data;
//	}
//	public void setData(String data) {
//		this.data = data;
//	}
//	public Date getCreateTime() {
//		return createTime;
//	}
//	public void setCreateTime(Date createTime) {
//		this.createTime = createTime;
//	}
//
//}
