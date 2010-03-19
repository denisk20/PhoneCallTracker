package com.nixsolutions.calltracker.util;

import com.nixsolutions.calltracker.dao.CallDao;
import com.nixsolutions.calltracker.dao.impl.CallDaoHibernate;
import com.nixsolutions.calltracker.model.Call;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Calendar;
import java.util.Date;

/**
 * @author denis_k
 *         Date: 11.03.2010
 *         Time: 17:56:17
 */
public class DataGenerator {
	public final static String NUMBER1 = "80938393954";
	public final static String NUMBER2 = "20934723947";
	public final static String NUMBER3 = "03945793049";

	public final static String DESCRIPTION1 = "Bad call";
	public final static String DESCRIPTION2 = "Nice call";
	public final static String DESCRIPTION3 = "Great call";

	public final static Date date1;
	public final static Date date2 ;
	public final static Date date3;

	static {
		Calendar c = Calendar.getInstance();

		c.set(2010, 0, 23, 14, 25, 00);
		c.set(Calendar.MILLISECOND, 0);
		date1 = c.getTime();
		c.set(2012, 1, 22, 15, 12, 00);
		date2 = c.getTime();
		c.set(2000, 4, 26, 00, 18, 00);
		date3 = c.getTime();
	}
	
	public static void main(String[] args) {
		CallDaoHibernate dao = new CallDaoHibernate();
		Session session = dao.getSession();
		Transaction tx = session.beginTransaction();

		Call call1 = new Call();
		call1.setPhoneNumber(NUMBER1);
		call1.setDescription(DESCRIPTION1);
		call1.setCallDate(date1);

		Call call2 = new Call();
		call2.setPhoneNumber(NUMBER2);
		call2.setDescription(DESCRIPTION2);
		call2.setCallDate(date2);

		Call call3 = new Call();
		call3.setPhoneNumber(NUMBER3);
		call3.setDescription(DESCRIPTION3);
		call3.setCallDate(date3);

		dao.makePersistent(call1);
		dao.makePersistent(call2);
		dao.makePersistent(call3);
		
		tx.commit();
		if (session.isOpen()) {
			session.close();
		}
	}
}
