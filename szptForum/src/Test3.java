
import java.io.*;
import java.util.*;
import ado.DataBaseADO;
import java.sql.*;

public class Test3 {
	public static void main(String[] args) {
		myThread t = new myThread();
		myThread t2 = new myThread();
		myThread t3 = new myThread();
		t.start();
		t2.start();
		t3.start();
	}
}

// ʹ�ö��߳�ģ��ͬʱ�������۲�¥���Ƿ����仯
class myThread extends Thread{
	public DataBaseADO ado;
	public myThread() {
		ado = DataBaseADO.getAdo();
	}
	@Override
	public void run() {
		synchronized (ado) {
			for(int i=0;i<10;i++) {
				ResultSet rs = ado.prequery_auto("select replycount from posts where id=?", 1);
				try {
					rs.next();
					int count = rs.getInt(1);
					count = count+1;	
					ado.preupdate("update posts set replycount=? where id=?",count,1 );
					ResultSet rs2 = ado.prequery_auto("select replycount from posts where id=?", 1);
					rs2.next();
					System.out.println("�߳�:"+Thread.currentThread().getName()+"���һ�η�������ǰ���¥��Ϊ:"+rs2.getInt(1));				
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		
		}
	}
}