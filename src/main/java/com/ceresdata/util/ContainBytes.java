package com.ceresdata.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;


/**
 * 写入文件时，分个的写入文件
 * 
 * @author ytwps
 * 
 */
public class ContainBytes {
	/**
	 * 
	 * @param fous 写入文件的信息
	 */
	public ContainBytes(FileOutputStream fous) {
		this.fous = fous;
	}

	/**
	 * 默认构造函数
	 */
	public ContainBytes() {}

	private static final int write_length = 102400;
	private byte[] tempContent = null;
	private int tempLength = 0;
	private FileOutputStream fous = null;
	private static final int length = 9;
	/**
	 * 
	 * @param content 将要写入文件的字符串加入到集合中
	 *            
	 */
	public void addString(String content) {
		if (content == null) {
			return;
		}
		byte[] cons = null;
		try {
			cons = content.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (tempContent == null) {
			tempContent = cons;
			tempLength = cons.length;
		} else {
			tempContent = CompressUtil.copyOf(tempContent, tempLength + cons.length);
			System.arraycopy(cons, 0, tempContent, tempLength, cons.length);
			//
			tempLength = tempContent.length;
		}
		// add to list
		addToList();

	}
	

	/**
	 * 如果数组超过1024大小，把数组中前102400字节加入list，并将剩余的放回去
	 */
	private void addToList() {
		if (tempLength == 0) {
			return;
		}
		// 数组中是否已经大于1024，大于1024则把1024的写入list，留下剩余的。
		int count = tempLength % write_length;
		int index = tempLength / write_length;

		for (int i = 0; i < index; i++) {
			int from = i * write_length;
			int to = (i + 1) * write_length;
			byte[] temp = CompressUtil.copyOfRange(tempContent, from, to);
			// 压缩/加密
			temp = this.getBytes(temp);
			// 写入到文件中
			try {
				fous.write(buildLength(temp.length));
				fous.write(temp);
				fous.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 说明真好是1024的倍数
		if (count == index) {
			// 说明是1024的倍数，并且已经全部加入到list了
			if (index != 0) {
				tempContent = null;
				tempLength = 0;
			} else {
				// 还不到1024，等待下一次的合并
			}

		} else {
			int from = index * write_length;
			int to = tempLength;
			tempContent = CompressUtil.copyOfRange(tempContent, from, to);
			tempLength = tempContent.length;
		}

	}

	/**
	 * 将字符串进行
	 * 
	 * @param str  需要压缩加密的字符串
	 *           
	 * @return 压缩加密后的字节数组
	 */
	private byte[] getBytes(String str) {
		return getBytes(str.getBytes());
	}

	/**
	 * 
	 * @param encodes
	 *            要压缩和加密的字节数组
	 * @return 压缩和加密后的数组
	 */
	private byte[] getBytes(byte[] encodes) {
		// 压缩
		byte[] zips = CompressUtil.gzipCompress(encodes);
		// 加密
		byte[] encoded = Base64.getEncoder().encode(zips);

		return encoded;
	}

	public void writeFile() throws IOException {
		// 先把最后剩的字节加入list
		if (tempContent != null) {
			tempContent = getBytes(tempContent);
			fous.write(buildLength(tempContent.length));
			fous.write(tempContent);
		}
	}

	/**
	 * 如果数字不够9位长，则在数字的前面补0，以达到长度为9
	 * 
	 * @param str
	 *            数字字符串，如125、7895
	 * @return 将125 返回 000000125、7895 返回 000007895
	 */
	public byte[] buildLength(String str) {
		String temp = "";
		if (str.length() > length) {
			return str.getBytes();
		}
		// 补字符串前补0
		for (int i = 0; i < length - str.length(); i++) {
			temp += "0";
		}
		temp += str;
		return temp.getBytes();
	}
	
	/**
	 * 将字节数组转换成整
	 * @param bytes
	 * @return
	 */
	public static int byteToInt(byte[] bytes) {
		int temp = 0;
		String str = new String(bytes);
		temp = Integer.parseInt(str);
		return temp;
	}

	public static byte[] decode(byte[] bytes) {
		// 解密
		byte[] encoded = Base64.getDecoder().decode(bytes);
		// 解压缩
		byte[] zips = CompressUtil.gzipUncompress(encoded);

		return zips;
	}

	/**
	 * 将一个不够4位的数字，前面补0，应为102400 压缩后的大小一般都是在1870左右
	 * 
	 * @param number
	 * @return 拼成字符串的字节数组
	 */
	public byte[] buildLength(int number) {
		String temp = "";
		if (number <= 0) {
			temp = "0";
		} else {
			temp = number + "";
		}
		// 补字符串前补0
		String str = "";
		for (int i = 0; i < length - temp.length(); i++) {
			str += "0";
		}
		temp = str + temp;
		return temp.getBytes();
	}
}
