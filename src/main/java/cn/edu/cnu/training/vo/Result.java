package cn.edu.cnu.training.vo;

import java.util.Map;

/**
 * 服务器返回固定格式数据。{"results":{"msg":"","datas":""}}
 * @author meng
 *
 */
public class Result {
	private Results results;
	
	public Result(){
		results = new Results();
	}

	
	/**
	 * json-lib转换是调用getter方法。因此需要序列化的属性提供get方法
	 * @return
	 */
	public Results getResults() {
		return results;
	}

	public Result setResults(Results results) {
		this.results = results;
		return this;
	}

	public Result setMsg(String msg) {
		this.results.setMsg(msg); 
		return this;
	}


	public Result setDatas(Map<String, Object> datas) {
		this.results.setDatas(datas);
		return this;
	}
	
	public Result setData(String key,Object data){
		this.results.setData(key, data);
		return this;
	}
	
	
}
