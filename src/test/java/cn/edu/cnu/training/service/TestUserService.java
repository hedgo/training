package cn.edu.cnu.training.service;

import org.junit.Test;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestUserService {
	
	
	@Test
	public void TestLogin(){
		ClassPathXmlApplicationContext applicationContextAware = new ClassPathXmlApplicationContext("applicationContext.xml"
				,"database.properties","hibernateContext.xml","restapp.xm");
		UserService userService = applicationContextAware.getBean(UserService.class);
		System.out.println(userService);
	}
}
