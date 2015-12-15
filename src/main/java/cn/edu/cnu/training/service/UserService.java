package cn.edu.cnu.training.service;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.cnu.training.dao.BaseDAO;
import cn.edu.cnu.training.pojo.User;
import cn.edu.cnu.training.util.JsonHelper;
import cn.edu.cnu.training.util.MakeMd5;
import cn.edu.cnu.training.util.StringHelper;
import cn.edu.cnu.training.vo.Result;
import net.sf.json.JSONObject;

/**
 * 用户user服务
 * 其中：
 * 		@Path("/user"),选填。表示所有的url是/user的请求都交由此类处理，支持正则表达式。
 * 			注：用户提交的url被解析的过程分为几个部分：首先是匹配服务发布配置文件（restapp.xml）中address的值/userService,
 * 				然后匹配类路径即在类上配置的path，然后再匹配方法路径，即在方法上配置的路径。
 * 				例如restapp.xml中配置address是/userService，类中配置的路径是/file,方法中配置路径是/get/{userId}
 * 				，则用户访问的url应该是http://localhost:8080/training/userService/user/get/{userId}
 * 		@Produces({"application/json","application/xml"})表示可以返回的MIME类型
 * 		@Consumes注释代表的是一个资源可以接受的 MIME 类型
 * @author meng
 *
 */
@Component
@Produces("application/json; charset=UTF-8")
public class UserService {
	private static Logger logger = LogManager.getLogger(UserService.class);
	@Autowired
	private BaseDAO<User> userBaseDao;
	
	/**
	 * 查询单个特定子资源user。
	 * 其中@GET是子资源方法，也就是说配置了该项之后，java方法只接受http的get请求。其他的@POST,@PUT@DELETE都一样
	 * 		@Path("/get/{userId}")是子资源定位器，配置了此项之后，所有请求的url是：/userService/user/get/{userId}，都会执行此java方法
	 * 		写法等价于：@Path(value="/get/{userId}")
	 * @param userId 要查询的用户的id号
	 * @return
	 */
	@GET
	@Path("/get/{userId}")
	public Response getUser(@PathParam("userId") String userId,@Context HttpServletRequest request){
		
		ResponseBuilder builder = Response.ok();
		Result result = new Result();
		
		if(!StringHelper.isNumeric(userId)){
			return builder.entity(JSONObject.fromObject(result.setMsg("userId不能为包含字符").setData("userId", userId)).toString()).build();
		}
		User user = userBaseDao.lookup(User.class, Integer.valueOf(userId));
		user.setPassword("null");
		return builder.entity(JSONObject.fromObject(result.setMsg("ok").setData("user", user)).toString()).build();
	}
	
	@GET
	@Path("/getall")
	public Response getAllUsers(@Context HttpServletRequest request){
		
		ResponseBuilder builder = Response.ok();
		Result result = new Result();
		
		List<User> users = userBaseDao.list("from User");
		for(User tmp : users){
			tmp.setPassword("null");
		}
		
		return builder.entity(JSONObject.fromObject(result.setMsg("ok").setData("user", users)).toString()).build();
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	@POST
	@Path("/login")
	public Response login(String userJson,@Context HttpServletRequest request){
		
		ResponseBuilder builder = Response.ok();
		Result result = new Result();
		
		User user = JsonHelper.jsonToBean(userJson,User.class);
		User userValidate = validateUser(user);
		
		if(userValidate != null){
			request.getSession().setAttribute("user", userValidate);
			logger.info("user login!userId : " + userValidate.getId());
			return builder.entity(JSONObject.fromObject(result.setMsg("ok")).toString()).build();
		}else{
			return builder.entity(JSONObject.fromObject(result.setMsg("failed")).toString()).build();
		}
	}
	
	
	@POST
	@Path("/add")
	public Response addUser(String userJson){
		
		ResponseBuilder builder = Response.ok();
		Result result = new Result();
		
		User user = JsonHelper.jsonToBean(userJson,User.class);
		if(user == null){
			return builder.entity(JSONObject.fromObject(result.setMsg("failed")).toString()).build();
		}
		
		user.setPassword(MakeMd5.toMd5(user.getPassword()));
		userBaseDao.create(user);
		logger.info("add user,userId : " + user.getId());
		return builder.entity(JSONObject.fromObject(result.setMsg("ok")).toString()).build();
	}
	
	@DELETE
	@Path("/delete/{userId}")
	public Response deleteUser(@PathParam("userId") String userId){
		
		ResponseBuilder builder = Response.ok();
		Result result = new Result();
		
		if(!StringHelper.isNumeric(userId)){
			
			return builder.entity(JSONObject.fromObject(result.setMsg("userId不能为包含字符").setData("userId", userId)).toString()).build();
		}
		
		boolean flag = userBaseDao.delete(new User(Integer.valueOf(userId)));
		logger.info("delete user , status : " + flag);
		return flag?builder.entity(JSONObject.fromObject(result.setMsg("ok")).toString()).build():builder.entity(JSONObject.fromObject(result.setMsg("failed").setData("userId", userId)).toString()).build();
	}
	
	
	@PUT
	@Path("/update")
	public Response updateUser(String userJson){
		
		ResponseBuilder builder = Response.ok();
		Result result = new Result();
		
		User user = JsonHelper.jsonToBean(userJson,User.class);
		if(user == null){
			return builder.entity(JSONObject.fromObject(result.setMsg("failed")).toString()).build();
		}
		if(user.getId() < 1){
			return builder.entity(JSONObject.fromObject(result.setMsg("userId不能为空！")).toString()).build();
		}
		logger.info("update user , userId : " + user.getId());
		
		user.setPassword(MakeMd5.toMd5(user.getPassword()));
		boolean flag = userBaseDao.update(user);
		user.setPassword("null");
		
		return flag?builder.entity(JSONObject.fromObject(result.setMsg("ok").setData("user",user)).toString()).build():builder.entity(JSONObject.fromObject(result.setMsg("更新失败！"))).build();
	}
	
	
	private User validateUser(String username,String password){
		if(username == null || password == null){
			return null;	
		}
		String passwordMd5 = MakeMd5.toMd5(password);
		List<User> list = userBaseDao.list("from User as u where u.name='" + username +"' and u.password='" + passwordMd5 +"'");
		return list.size() == 0?null:list.get(0);
	}
	
	private User validateUser(User user){
		if(user == null){
			return null;
		}
		return validateUser(user.getName(),user.getPassword());
	}
}
