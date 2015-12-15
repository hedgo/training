package cn.edu.cnu.training.interceptor;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.message.Message;
import org.springframework.stereotype.Component;

@Component
public class EncodingLoggingInInterceptor extends LoggingInInterceptor{

	public EncodingLoggingInInterceptor() {
		super();
	}
	
	@Override
	public void handleMessage(Message message) throws Fault {
		System.out.println("handleMessage" + System.currentTimeMillis());
		super.handleMessage(message);
	}
	
	@Override
	public void handleFault(Message message) {
		System.out.println("handleFault");
		super.handleFault(message);
	}
}
