package cn.edu.cnu.training.service;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.activation.DataHandler;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FileService {
	private static Logger logger = LogManager.getLogger(FileService.class);
	private static final String UPLOAD_FILE_NAME = "filename";
	private static final String UPLOAD_FILE_TEMP = "tmp";
	private static final String UPLOAD_FILE_PATH = System.getProperty("user.dir") + "\\upload\\";

	/**
	 * 文件下载，目前只支持非中文文件名
	 * 
	 * @param filename 要下载的文件全名，包括扩展名
	 * @return
	 */
	@GET
	@Path("/get/{filename}")
	@Produces("text/plain")
	public Response getFile(@PathParam("filename") String filename) {
		File file = new File(UPLOAD_FILE_PATH + filename);
		if(file.exists()){
			ResponseBuilder response = Response.ok(file);
			response.header("Content-Disposition", "attachment;filename='" + filename + "'");
			logger.info("file download:" + filename);
			return response.build();
		}else{
			return Response.ok("{\"results\":{\"msg\":\"file: " + filename +" not exits!\"}}").build();
		}
	}

	/**
	 * 上传任意格式的单个文件
	 * 
	 * @param multipartBody
	 * @return
	 */
	@POST
	@Path("/add")
	@Consumes({ "multipart/form-data" })
	@Transactional(readOnly = false)
	public Response addFile(@Multipart(value = "filename") MultipartBody multipartBody) {
		try {
			addAttachment(multipartBody.getRootAttachment(),UPLOAD_FILE_PATH);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok().entity("ok").build();
	}

	/**
	 * 多文件上传
	 * @param body MultipartBody类型
	 * @return
	 */
	@POST
	@Path("/adds")
	@Consumes({ "multipart/form-data" })
	@Transactional(readOnly = false)
	public Response addFiles(MultipartBody body) {
		for (Attachment attachment : body.getAllAttachments()) {
			try {
				addAttachment(attachment,UPLOAD_FILE_PATH);
			} catch (IOException e) {
			} catch (Exception e) {
			}
		}
		return Response.ok().entity("ok").build();

	}

	/**
	 * 添加文件到本地
	 * @param attachment
	 * @param uploadPath 要保存到的路径
	 * @return 已经保存的文件名
	 * @throws IOException
	 */
	private String addAttachment(Attachment attachment,String uploadPath) throws IOException {
			DataHandler dataHandler = attachment.getDataHandler();
			ContentDisposition cd = attachment.getContentDisposition();
			String name = cd.getParameter(UPLOAD_FILE_NAME);
			name = new String(name.getBytes("iso-8859-1"), "UTF-8");
			name = createFileName(name);
			if (name == null) {
				name = UPLOAD_FILE_TEMP + (new Date()).getTime() + "." + "png";
			}
			writeToFile(dataHandler.getInputStream(), uploadPath + name);
			return name;
	}
	
	
	/**
	 * 生成全局唯一文件名
	 * @param fileWholeName 要生成的文件全名
	 * @return
	 */
	private static String createFileName(String fileWholeName){
		String tail = fileWholeName.substring(fileWholeName.lastIndexOf("."),fileWholeName.length()-1);
		return System.currentTimeMillis() + (int)(Math.random() * 1000)+ tail;
	}

	
	/**
	 * 向本地文件中写数据
	 * @param ins
	 * @param path
	 */
	private void writeToFile(InputStream ins, String path) {
		try {
			OutputStream out = new FileOutputStream(new File(path));
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = ins.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String writeToString(InputStream ins) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int i = -1;
		while ((i = ins.read(b)) != -1) {
			out.write(b, 0, i);
		}
		ins.close();
		return new String(out.toByteArray(), "UTF-8");
	}
	
	

	/*@POST
	@Path("/uploadlist")
	@Consumes("multipart/form-data")
	public Response uploadFileList(List<Attachment> attachments, @Context HttpServletRequest request) {
		if (attachments.size() > 0)
			System.out.println("ok");

		for (Attachment attach : attachments) {
			DataHandler dh = attach.getDataHandler();
			System.out.println(attach.getContentType().toString());

			if (attach.getContentType().toString().equals("text/plain")) {
				try {
					System.out.println(dh.getName());
					System.out.println(writeToString(dh.getInputStream()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					writeToFile(dh.getInputStream(), "D:\\upload\\" + dh.getName());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return Response.ok().entity("ok").build();
	}*/
}
