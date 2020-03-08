/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ceresdata.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 
 * @author wangjk
 */
public abstract class CompressUtil {

	/**
	 * GZIPѹ��
	 * 
	 * @param b
	 * @return byte[]
	 */
	public static byte[] gzipCompress(byte[] b) {
		ByteArrayOutputStream bos = null;
		GZIPOutputStream gos = null;
		try {
			bos = new ByteArrayOutputStream();
			gos = new GZIPOutputStream(bos);
			gos.write(b);
			gos.finish();
			return bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (gos != null) {
				try {
					gos.close();
				} catch (IOException e) {
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * GZIP��ѹ��
	 * 
	 * @param b
	 * @return byte[]
	 */
	public static byte[] gzipUncompress(byte[] b) {
		ByteArrayInputStream bis = null;
		GZIPInputStream gis = null;
		ByteArrayOutputStream baos = null;
		try {
			bis = new ByteArrayInputStream(b);
			gis = new GZIPInputStream(bis);
			byte[] buf = new byte[1024];
			int num = -1;
			baos = new ByteArrayOutputStream();
			while ((num = gis.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			baos.flush();
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (gis != null) {
				try {
					gis.close();
				} catch (IOException e) {
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * jdk 1.6 method
	 */

	public static byte[] copyOf(byte[] original, int newLength) {
		byte[] copy = new byte[newLength];
		System.arraycopy(original, 0, copy, 0, Math.min(original.length,
				newLength));
		return copy;
	}

	/**
	 * jdk 1.6 method
	 * 
	 **/
	public static byte[] copyOfRange(byte[] original, int from, int to) {
		int newLength = to - from;
		if (newLength < 0)
			throw new IllegalArgumentException(from + " > " + to);
		byte[] copy = new byte[newLength];
		System.arraycopy(original, from, copy, 0, Math.min(original.length
				- from, newLength));
		return copy;
	}

	public static void main(String[] args) {
		String t1 = "hello,worldhello,worldhello,worldhello,worldhello,worldhello,worldhello,world";
		String t2 = "���Բ��Բ��Բ��Բ��Բ��Բ��Բ��Բ��Բ��Բ���";
		System.out.println("t1: " + t1);
		byte[] t1c = gzipCompress(t1.getBytes());
		System.out.println("ѹ����: " + new String(t1c));
		System.out.println("ѹ�����ֽ���: " + t1c.length);
		byte[] t1u = gzipUncompress(t1c);
		System.out.println("��ѹ��: " + new String(t1u));
		System.out.println("��ѹ���ֽ���: " + t1u.length);

		System.out.println("t2: " + t2);
		byte[] t2c = gzipCompress(t2.getBytes());
		System.out.println("ѹ����: " + new String(t2c));
		System.out.println("ѹ�����ֽ���: " + t2c.length);
		byte[] t2u = gzipUncompress(t2c);
		System.out.println("��ѹ��: " + new String(t2u));
		System.out.println("��ѹ���ֽ���: " + t2u.length);
	}
}
