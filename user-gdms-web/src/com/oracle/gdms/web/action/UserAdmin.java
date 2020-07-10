package com.oracle.gdms.web.action;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alibaba.fastjson.JSONObject;
import com.oracle.gdms.entity.ResponseEntity;
import com.oracle.gdms.util.GDMSUtil;

@WebServlet("/admin/user")
public class UserAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//response.setCharacterEncoding("UTF-8");
		FileItemFactory fileItemFactory = new DiskFileItemFactory();
		ServletFileUpload sfu = new ServletFileUpload(fileItemFactory );
		
		sfu.setFileSizeMax(102400); //ָ���ļ����ߴ�
		
		//ָ��һ������·��
		String  path = this.getServletContext().getRealPath("/images");  //ȡ�õ�ǰӦ�ó�����images�ľ���·��
		path +="/upload";
		File f =new File(path);
		if ( !f.exists()) {
			f.mkdir();   //�������ļ��в����ڣ��ʹ�����
		}
		try {
			//��ʼ��������
			List<DiskFileItem> list = sfu.parseRequest(request);
			for (DiskFileItem item :list) {
				if(!item.isFormField()) {       //������item�����Ǳ�����ǾͰ������ļ�����
					String fileName = GDMSUtil.generic(24);
					int i =item.getName().lastIndexOf("."); //���ҵ��ļ��������һ��С�����λ��
					fileName+= item.getName().substring(i); //�����λ�ÿ�ʼ���Ѻ�������ȡ������Ϊ��׺��
					
					File file = new File(path + "/" +fileName);//����һ��һ�����ļ�
					
					item.write(file); //��Դ�ļ�����д�뵽���ļ�
					
					ResponseEntity entity = new ResponseEntity();
					entity.setCode(0);
					entity.setMessage(fileName);
					entity.setData("<img width='300' height='200' src='images/upload/"+fileName+ "'>");
					JSONObject j = new JSONObject();
					j.put("entity", entity);
					response.setContentType("application/json;charset=UTF-8");
					response.getWriter().print(j.toJSONString());   //���ϴ��Ľ����һ��JSON���󷵻�
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().print("�ϴ�ʧ�ܣ�");
		}
	}
	

}
