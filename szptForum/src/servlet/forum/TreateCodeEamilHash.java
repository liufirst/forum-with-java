package servlet.forum;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import email.*;

// ����������֤���servlet��
@WebServlet("/TreateCodeEamilHash.bin")
public class TreateCodeEamilHash extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//����������֤���jspҳ��
		
		// ��������˺�
		String email = (String)request.getAttribute("email");
		
		System.out.println("email:"+email);
		
		//�����ʼ�
		EmailManager em = new EmailManager();
		
		//������֤��
		int random = (int)(Math.random()*100000);
		String checknumber = String.valueOf(random);
		
		HttpSession session = request.getSession();
		
		//������֤�뱣�������û���session��
		session.setAttribute("checknumber", checknumber);
		
		String sendmessage = "��ӭ��ʹ��ע���˺Ź��ܣ�����Ҫ����֤�������������֤����ɲ���:\n"+checknumber+"\n����sjm��һ�β���~";
		
		// ������֤���ʼ�
		em.sendemail(email, sendmessage);
	}

}
