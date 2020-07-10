package com.oracle.gdms.web.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oracle.gdms.entity.UserMode;
import com.oracle.gdms.service.UserService;
import com.oracle.gdms.service.impl.AreaServiceImpl;
import com.oracle.gdms.service.impl.UserServiceImpl;
import com.oracle.gdms.util.GDMSUtil;

@WebServlet("/admin/user/reg.php")
public class UserRegAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		//�����ύ�����Ĳ����������Ժ��װ��һ��UserModeʵ��
		String account = request.getParameter("name");
		String password = request.getParameter("password");
		String realname = request.getParameter("realname");
		String slogo = request.getParameter("slogo");
		String idnumber = request.getParameter("idnumber");
		String place = request.getParameter("prov");
		String city = request.getParameter("city");
		
		int pid = Integer.parseInt(place); //ʡ�ݵ�id
		String pname = new AreaServiceImpl().findNameById(pid).getName();//��ѯʡ������
		int cid = Integer.parseInt(city); //���е�id
		String cname = new AreaServiceImpl().findNameById(cid).getName();//��ѯ��������
		
		place =pname+"-"+cname;
		
		String code = request.getParameter("code");
		
		//
		HttpSession session =request.getSession();
		String yzm=(String)session.getAttribute("code");
		//�ù�����֤����Ч,����
		session.removeAttribute("code");
		
		if (yzm == null || !yzm.equalsIgnoreCase(code)) {
			request.setAttribute("err_msg", "��֤����Ч");
			System.out.println("-----------------");
			request.getRequestDispatcher("../../index.jsp").forward(request, response);
			return; 
		}
		
		
//		System.out.println("account="+account);
//		System.out.println("password="+password);
//		System.out.println("realname="+realname);
//		System.out.println("slogo="+slogo);
//		System.out.println("idnumber="+idnumber);
//		System.out.println("place="+place);
//		System.out.println("code="+code);
		
		UserMode user = new UserMode();
		user.setAccount(account);
		try {
			user.setLogpwd(GDMSUtil.getMD5(password.getBytes()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		user.setIdnumber(idnumber);
		user.setPlace(place);
		user.setPhoto(slogo);
		user.setRealname(realname);
		
		//ȡ���Ա�
		char c =idnumber.charAt(idnumber.length()-2);
		int i = Integer.parseInt(String.valueOf(c));
		String sex =i % 2 == 0 ? "Ů" :"��";
		
		user.setRowflag("oraU" + GDMSUtil.generic(20));
		user.setRoleid(3);
		user.setSex(sex);
		
		//ȡ������
		String s =idnumber.substring(6,14); //ȡ�Ӵ�,����6��������14
		//���ַ������͵�����ת��Timestamp����
		SimpleDateFormat sdf = new SimpleDateFormat();
		try {
			int yy=Integer.parseInt(s.substring(0,4));//ȡ�����
			int mm=Integer.parseInt(s.substring(4,6));//ȡ���·�
			int dd=Integer.parseInt(s.substring(6));//ȡ������
			Date d= new Date(yy-1900,mm-1,dd);
			Timestamp birthday =new Timestamp(d.getTime());
			user.setBirthday(birthday);
			
			Timestamp regtime = GDMSUtil.now();
			user.setRegtime(regtime);
			user.setLastlogin(regtime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		user.setStatus(false);
		
		//����ҵ���߼���Ķ���
		UserService userservice = new UserServiceImpl();
		int count = userservice.add(user);
		
		if (count >0) {
			request.setAttribute("msg", "ע��ɹ������¼");
		}else {
			request.setAttribute("err_msg", "Sorry,ע��ʧ�ܣ����Ժ�����");
		}
		request.getRequestDispatcher("../../index.jsp").forward(request, response);//ִ��ת��
	}
	

}
