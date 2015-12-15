package cn.edu.cnu.training.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MakeMd5 {
	public static String toMd5(String plainText){   
        MessageDigest md;
        StringBuffer buf = new StringBuffer("");   
		try {
			md = MessageDigest.getInstance("MD5");
			  md.update(plainText.getBytes());   
		        byte b[] = md.digest();   
		        int i;   
		        for (int offset = 0; offset < b.length; offset++){   
		                i = b[offset];   
		                if (i < 0)   
		                i += 256;   
		                if (i < 16)   
		                buf.append("0");   
		                buf.append(Integer.toHexString(i));   
		        }   
		        
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        return buf.toString(); 
      
	}

}