package servlet.forum;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.imageio.*;
import java.io.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.*;

//����ͼ����֤���servlet
@WebServlet("/vali.bin")
public class TreateCodeGraphHash extends HttpServlet{
	String ver[] = new String[62];
	public void init() {
		for(int i=0;i<10;i++) {
			ver[i] = new Integer(i).toString();
		}
		for(int i=0;i<26;i++) {
			ver[i+10] = new Character((char)(97+i)).toString();
		}
		for(int i=0;i<26;i++) {
			ver[i+36] = new Character((char)(65+i)).toString();
		}
	}
	// ����������ֺ���ĸ
	public String getVaildateCode() {
		StringBuffer vail = new StringBuffer();
		for(int i=0;i<4;i++) {
			vail.append(ver[(int)(Math.random()*36)]);
		}
		return vail.toString();
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// ����ͼ�����
		response.setContentType("image/jpeg");
		
		// ��ȡ�����
		OutputStream os = response.getOutputStream();
		
		//���ڴ���׼��һ��image
		BufferedImage image = new BufferedImage(50, 20, BufferedImage.TYPE_INT_RGB);
		
		Graphics g = image.getGraphics();
		
		// ��ͼ
		g.setColor(new Color(200,200,200));
		
		g.fillRect(0, 0, 50, 20);
		
		// ���Ƹ�����
		drawexraline(g);
		
		//����ͼ����֤��
		String vail = getVaildateCode();
		
		// ��ͼ����֤�����session
		HttpSession session = request.getSession();
		session.setAttribute("graphhashcode", vail);
		
		for(int i=0;i<vail.length();i++) {
			g.setColor(new Color((int)(Math.random()*150),(int)(Math.random()*150),(int)(Math.random()*150)));
			g.drawString(new Character(vail.charAt(i)).toString(),8*i+10, 15);
		}
		
		g.dispose();
		
		//��image����ʽ���
		ImageIO.write(image, "JPEG", os);
	}
	
	// ���Ƹ�����
	public void drawexraline(Graphics g) {
		g.setColor(new Color(150,150,150));
		for(int i=0;i<20;i++) {
			int x1 = (int)(Math.random()*50);
			int y1 = (int)(Math.random()*20);
			int x2 = (int)(Math.random()*50);
			int y2 = (int)(Math.random()*20);
			g.drawLine(x1, y1, x2, y2);
		}
	}
	
}
