
import java.sql.*;
import ado.DataBaseADO;

public class Test4 {
	public static void main(String[] args) {
		DataBaseADO ado = DataBaseADO.getAdo();
		// ��������mysql����
		for(int i=2;i<20;i++) {
			String sql = "insert into posts(id,creatorname,title,replycount,viewcount,plateid) values(?,?,?,?,?,?)";
			if(ado.preinsert_auto(sql,i,"sjm","title"+i,i,i,1)) {
				System.out.println("����ɹ�");
			}else {
				System.out.println("����ʧ��");
			}
		}
	}
}
