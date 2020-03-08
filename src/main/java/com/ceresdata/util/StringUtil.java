package com.ceresdata.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
	/**
	 *  过滤非法字符
	 * @param content 写入xml的字符串
	 * @return 将非法字符过滤掉
	 */
	public static String replaceXmlChar(String content) {
		if (null == content) {
			return "NULL";
		}
		if (content.length() == 0) {
			return "";
		}
		Matcher m = null;
		m = Pattern.compile("[\\x00-\\x08|\\x0b-\\x0c|\\x0e-\\x1f]").matcher(
				content);
		String replaceAll = m.replaceAll("");
		return replaceAll;

	}
	/**
	 * 将文件设置转换成设计大小，如1k 返回 1024 ，1m：1024*1024，1g:返回1024*1024*1024
	 * @param size
	 * @return
	 */
	public static long getFileSize(String size){
		size = size.toUpperCase();
		if(size.endsWith("K")){
			return Long.parseLong(size.substring(0, size.length()-1)) * 1024;
		}else if(size.endsWith("M")){
			return Long.parseLong(size.substring(0, size.length()-1)) * 1024 * 1024;
		}else if(size.endsWith("G")){
			return Long.parseLong(size.substring(0, size.length()-1))  * 1024 * 1024 * 1024;
		}else{
			return Long.parseLong(size);
		}
	}
	
	/**
	 * 验证字符串是否为null 或者 为Empty
	 * 
	 * @param str 字符串变量
	 * @return true 和 false 
	 */
	public static boolean isNullOrEmpty(String str){
		if(str == null || "".equals(str)){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取当前时间，
	 * 
	 * @return yyyy-MM-dd HH:mm:ss 格式的时间
	 */
	public static String getCurrentTime() {
		long nCurrentTime = System.currentTimeMillis();
		Date utilDate = new Date(nCurrentTime);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String szDatetime1 = df.format(utilDate);
		return szDatetime1;
	}
	/**
	 * 比较两个文件的传输优先级，文件如果为同一天时间，则早上的文件优先于下午的文件
	 * @param file1_Modify 
	 * @param file2_Modify
	 * @return
	 */
	public static int compare(long file1_Modify,long file2_Modify) {
		Date file1_ModifyDate = new Date(file1_Modify);
		Date file2_ModifyDate = new Date(file2_Modify);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String szDatetime1 = df.format(file1_ModifyDate);
		String szDatetime2 = df.format(file2_ModifyDate);
		
		int pritry = szDatetime1.compareTo(szDatetime2);
		
//		if(pritry == 0){
//			if(file1_Modify > file2_Modify){
//				return -1;
//			}else{
//				return 1;
//			}
//		}
		return pritry * -1;
	}
	
	/**
	 * 获取唯一消息的ID
	 * 
	 * @return 返回唯一的消息ID
	 */
	public static synchronized  String getOneOnlyStringId() {
		String ip = getLocalIP();// ��ñ���IP
		// ��ʽ��Ip
		String StringIp = ip.replace(".", "");
		// ��ʽ������
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH24mmss");
		Date nowDate = new Date();
		String dateStr = sdf.format(nowDate);
		// ��ȡ�����
		Random ran = new Random();
		int ranStr = ran.nextInt(100000);
		String stringId = StringIp + "_" + dateStr + "_" + ranStr;
		return stringId;
	}
	
	/**
	 * 获取本机的IP地址，如果IP映射过则取不到影射的地址
	 * 
	 * @return 192.168.10.1
	 */
	public static String getLocalIP() {
		String ip = "";
		try {
			Enumeration<?> e1 = (Enumeration<?>) NetworkInterface
					.getNetworkInterfaces();
			while (e1.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) e1.nextElement();
				if (!ni.getName().equals("eth0")) {
					continue;
				} else {
					Enumeration<?> e2 = ni.getInetAddresses();
					while (e2.hasMoreElements()) {
						InetAddress ia = (InetAddress) e2.nextElement();
						if (ia instanceof Inet6Address)
							continue;
						ip = ia.getHostAddress();
					}
					break;
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		if (ip.equals("")) {
			ip = getIp();
		}
		return ip;
	}

	private static String getIp() {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
			return addr.getHostAddress().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws InterruptedException {
		System.out.println("2015-04-03".compareTo("2015-04-01"));
//		long s = System.currentTimeMillis();
//		Thread.sleep(5000);
//		long e = System.currentTimeMillis();
//		System.out.println(StringUtil.compare(e, s));
//		System.out.println(StringUtil.getFileSize("1G"));
	}

}
