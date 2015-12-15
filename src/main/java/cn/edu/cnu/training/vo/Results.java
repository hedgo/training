package cn.edu.cnu.training.vo;

import java.util.HashMap;
import java.util.Map;

public class Results {
	
	private String msg;
	
	private Map<String,Object> datas;
	
	public Results(){
		datas = new HashMap<String,Object>();
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getDatas() {
		return datas;
	}

	public void setDatas(Map<String, Object> datas) {
		this.datas = datas;
	}
	
	public void setData(String key,Object data){
		datas.put(key, data);
	}

}
