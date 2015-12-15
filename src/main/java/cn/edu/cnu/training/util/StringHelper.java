package cn.edu.cnu.training.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class StringHelper {

	/**
	 * 验证一个字符串是否一个正整数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

}
