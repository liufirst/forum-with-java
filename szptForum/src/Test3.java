
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
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		while(true) {
			ResultSet rs = ado.prequery_auto("select replycount from posts where id=?", 1);
			try {
				rs.next();
				int count = rs.getInt(1);
				synchronized (this) {
					count = count+1;	
				}
				ado.preupdate("update posts set replycount=? where id=?",count,1 );
				rs = ado.prequery_auto("select replycount from posts where id=?", 1);
				rs.next();
				System.out.println("�߳�:"+Thread.currentThread().getName()+"���һ�η�������ǰ���¥��Ϊ:"+rs.getInt(1));				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}