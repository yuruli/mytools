package com.fzs.commons.filelisttomysql.utils;
import org.apache.commons.lang.StringUtils;

public class FileDataUtils {

	public static String getType(String name) {
		int index = StringUtils.lastIndexOf(name, ".");
		if (index == -1) {
			return "uk";
		} else {
			String t = StringUtils.substring(name, index, name.length());
			if (t.length() < 4) {
				return t;
			} else {
				return StringUtils.right(t, 3);
			}
		}
	}
	
	public static String getFile(String name ,String path,String fix_path) {
		if (path.equals("/")) {
			return fix_path+path + name;
		} else {
			return fix_path+path + "/" + name;
		}
	}
	
	
	public static final char getTypeknow(String tyep) {
		return '0';
	}
}
