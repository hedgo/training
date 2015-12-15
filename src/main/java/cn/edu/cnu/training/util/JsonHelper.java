package cn.edu.cnu.training.util;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

public class JsonHelper {
	private static Logger logger = LogManager.getLogger(JsonHelper.class);
	public static <T> T jsonToBean(String json,Class<T> c){
		try{
			JSONObject beanJson = JSONObject.fromObject(json);
			T t = (T)JSONObject.toBean(beanJson, c);
			return t;
		}catch(Exception e){
			//转换失败，格式不对
			logger.error(e);
			return null;
		}
	}
}
