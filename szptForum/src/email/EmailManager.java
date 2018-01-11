package email;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailManager {
	private Properties props;
	Session session;
	MimeMessage message;
	String emailaccout = "16240011@mail.szpt.edu.cn";
	String emailpassword = "Sz09043330";
	String emailsmtphost = "smtp.exmail.qq.com";
	public EmailManager(){
		
		//���������ʼ��������Ĳ�������
		props = new Properties();
		
		//ʹ�õ�Э��
		props.setProperty("mail.transport.protocol", "smtp");
		
		//�����˵������SMTP��������ַ����
		props.setProperty("mail.smtp.host",emailsmtphost);
		
		//��Ҫ������֤
		props.setProperty("mail.smtp.auth", "true");
		
		//ssl��ȫ��֤
		String port = "465";
		props.setProperty("mail.smtp.port", port);
		props.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.socketFactory.port", port);
		
		//���ݲ������ô����Ự����
		session = Session.getDefaultInstance(props);
//		session.setDebug(true);
	}
	
	public boolean sendemail(String receivemail,String content){
		try{
			//����һ���ʼ�
			message = createMimeMessage(session,content,receivemail);
			
			//����Session��ȡ�ʼ��������
			Transport transport = session.getTransport();
			
			transport.connect(this.emailaccout,this.emailpassword);
			
			//�����ʼ�
			transport.sendMessage(message, message.getAllRecipients());
		
			//�ر���������
			transport.close();
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	/**
	 * �������²���������һƪ�ʼ�
	 * @param session �Ự����
	 * @param content ���͵�����
	 * @param receivemail �ռ��˵�����
	 * @return
	 * @throws MessagingException 
	 * @throws UnsupportedEncodingException 
	 */
	public MimeMessage createMimeMessage(Session session,String content,String receivemail) throws UnsupportedEncodingException, MessagingException{
		//����һ���ʼ�
		MimeMessage message = new MimeMessage(session);
		
		//from:������
		message.setFrom(new InternetAddress(emailaccout,"faker_qq������֤","UTF-8"));
		
		//to�� �ռ���
		message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receivemail,"�û�","UTF-8"));
		
		//4.Subject
		message.setSubject("Faker_QQ������֤","UTF-8");
		
		//5.Content���ʼ�����
		message.setContent(content,"text/html;charset=UTF-8");
		
		//6.���÷���ʱ��
		message.setSentDate(new Date());
		
		message.saveChanges();
		
		return message;
	}
}