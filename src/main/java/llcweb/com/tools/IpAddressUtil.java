package llcweb.com.tools;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Random;

public class IpAddressUtil {
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if(ip.equals("127.0.0.1")){
				//根据网卡取本机配置的IP
				InetAddress inet=null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (Exception e) {
					e.printStackTrace();
				}
				ip= inet.getHostAddress();
			}
		}
		// 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if(ip != null && ip.length() > 15){
			if(ip.indexOf(",")>0){
				ip = ip.substring(0,ip.indexOf(","));
			}
		}
		return ip;
	}
	/**
	 * IP 地址转换成 long 数据
	 * @param ipAddress
	 * @return
	 */
	public static long ipAddressToLong(String ipAddress) {
		long ipInt = 0;
		if (ValidatorUtil.isIPv4Address(ipAddress)) {
			String[] ipArr = ipAddress.split("\\.");
			if (ipArr.length == 3) {
				ipAddress = ipAddress + ".0";
			}
			ipArr = ipAddress.split("\\.");
			long p1 = Long.parseLong(ipArr[0]) * 256 * 256 * 256;
			long p2 = Long.parseLong(ipArr[1]) * 256 * 256;
			long p3 = Long.parseLong(ipArr[2]) * 256;
			long p4 = Long.parseLong(ipArr[3]);
			ipInt = p1 + p2 + p3 + p4;
		}
		return ipInt;
	}

}
