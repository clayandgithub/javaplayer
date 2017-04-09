/**
 * StringUtil.java
 * Copyright 2014 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */
package com.clayoverwind.javaplayer.util;

import java.io.*;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class StringUtil
{
    private static final String RANDOM_STRING_BASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public static String genRandomString(final int length) {
        final Random random = new Random();
        final StringBuilder retSb = new StringBuilder();
        final int baseLength = RANDOM_STRING_BASE.length();
        for(int i = 0; i < length; ++i) {
        	char c = RANDOM_STRING_BASE.charAt(random.nextInt(baseLength));
            retSb.append(c);
        }
        return retSb.toString();
    }

	public static boolean isEmpty(String s) {
		return s == null || s.isEmpty();
	}
	
	public static String gzipInputStreamToUTF8String(InputStream is) {
		// unzip to get htmlString
        GZIPInputStream gZipIs;
        StringBuilder sb = new StringBuilder();
		try {
			gZipIs = new GZIPInputStream(is);
			InputStreamReader isr = new InputStreamReader(gZipIs, "utf-8"); // 设置读取流的编码格式，自定义编码
	        BufferedReader br = new BufferedReader(isr);
	        
	        String tempbf;
	        while ((tempbf = br.readLine()) != null) {
	               sb.append(tempbf);
	               sb.append("\r\n");
	        }
	        isr.close();
	        gZipIs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return sb.toString();
	}
	
	public static String timeInSToString(long s) {
		long h = s / 3600;
		long m = s / 60 - h * 60;
		s = s - h * 3600 - m * 60;
		StringBuilder sb = new StringBuilder();
		if (h > 9) {
			sb.append(String.valueOf(h)).append(":");
		} else if (h > 0){
			sb.append("0").append(String.valueOf(h)).append(":");
		}
		if (m > 9) {
			sb.append(String.valueOf(m)).append(":");
		} else {
			sb.append("0").append(String.valueOf(m)).append(":");
		}
		if (s > 9) {
			sb.append(String.valueOf(s));
		} else {
			sb.append("0").append(String.valueOf(s));
		}
		return sb.toString();
	}
	
	public static String removeSpecialChar(String str) {
		  String regEx="[ `~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
          Pattern   p   =   Pattern.compile(regEx);     
          Matcher   m   =   p.matcher(str);
          return m.replaceAll("").trim();
	}
	
    public static void testCharset(String datastr){
        try {
                String temp = new String(datastr.getBytes(), "GBK");
                System.out.println("****** getBytes() -> GBK ******/n"+temp);
                temp = new String(datastr.getBytes("GBK"), "UTF-8");
                System.out.println("****** GBK -> UTF-8 *******/n"+temp);
                temp = new String(datastr.getBytes("GBK"), "ISO-8859-1");
                System.out.println("****** GBK -> ISO-8859-1 *******/n"+temp);
                temp = new String(datastr.getBytes("ISO-8859-1"), "UTF-8");
                System.out.println("****** ISO-8859-1 -> UTF-8 *******/n"+temp);
                temp = new String(datastr.getBytes("ISO-8859-1"), "GBK");
                System.out.println("****** ISO-8859-1 -> GBK *******/n"+temp);
                temp = new String(datastr.getBytes("UTF-8"), "GBK");
                System.out.println("****** UTF-8 -> GBK *******/n"+temp);
                temp = new String(datastr.getBytes("UTF-8"), "ISO-8859-1");
                System.out.println("****** UTF-8 -> ISO-8859-1 *******/n"+temp);
        } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
        }
}
}
