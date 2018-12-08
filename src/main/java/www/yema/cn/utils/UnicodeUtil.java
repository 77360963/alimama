package www.yema.cn.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnicodeUtil {
	
	public static String unicodetoString(String unicode){  
		if(unicode==null||"".equals(unicode)){
			return null;
		}
		StringBuilder sb = new StringBuilder();  
		int i = -1;  
		int pos = 0;  
		while((i=unicode.indexOf("\\u", pos)) != -1){  
			sb.append(unicode.substring(pos, i));  
			if(i+5 < unicode.length()){  
				pos = i+6;  
				sb.append((char)Integer.parseInt(unicode.substring(i+2, i+6), 16));  
			}  
		}  
		return sb.toString();  
	} 
	public static String stringtoUnicode(String string) {
		if(string==null||"".equals(string)){
			return null;
		}
		StringBuffer unicode = new StringBuffer();
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			unicode.append("\\u" + Integer.toHexString(c));
		}
		return unicode.toString();
	}
	
	
	/**
	 * 转码    %u554A 转成  啊
	 * @param src
	 * @return
	 */
	public static String unescape(String src) {
		Pattern pattern = Pattern.compile("%u[0-9a-fA-F]{4}");
		Matcher macher = pattern.matcher(src);
		StringBuffer buffer = new StringBuffer();
		int start = 0;
		while (macher.find()) {
			buffer.append(src, start, macher.start());
			start = macher.start();
			String code = src.substring(start + 2, start + 6);
			char ch = (char) Integer.parseInt(code, 16);
			buffer.append(ch);
			start = macher.end();
		}
		buffer.append(src, start, src.length());
		return buffer.toString();
	}

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = stringtoUnicode("中文");
		System.out.println("编码："+s);
		String s1 = unicodetoString(s);
		System.out.println("解码："+s1);
 
	}


}
