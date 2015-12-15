package cn.edu.cnu.training.interceptor;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.ws.security.WSPasswordCallback;  

public class AuthInterceptor implements CallbackHandler {

	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  COME  IN @@@@@@@@@@@@@@");
		for (int i = 0; i < callbacks.length; i++) {
			WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
			String identifier = pc.getIdentifier();
			int usage = pc.getUsage();
			if (usage == WSPasswordCallback.USERNAME_TOKEN) {
				pc.setPassword("testPassword");
			} else if (usage == WSPasswordCallback.SIGNATURE) {
				pc.setPassword("testPassword");
			}
		}

	}

}
