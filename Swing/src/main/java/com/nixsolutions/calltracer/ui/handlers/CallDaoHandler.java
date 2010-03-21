package com.nixsolutions.calltracer.ui.handlers;

import com.nixsolutions.calltracer.ui.data.DeletableCall;
import com.nixsolutions.calltracer.ui.visitors.Acceptable;
import com.nixsolutions.calltracer.ui.visitors.QueryVisitor;
import com.nixsolutions.calltracker.dao.CallDao;
import com.nixsolutions.calltracker.dao.impl.CallDaoHibernate;
import com.nixsolutions.calltracker.model.Call;
import org.hibernate.Criteria;

import java.util.Date;
import java.util.List;

/**
 * @author denis_k
 *         Date: 15.03.2010
 *         Time: 12:47:10
 */
public class CallDaoHandler implements Acceptable {
	private CallDaoHibernate dao;

	public CallDaoHandler(CallDaoHibernate dao) {
		this.dao = dao;
	}

	public CallDaoHandler() {
		dao = new CallDaoHibernate();
	}

	public void setNumberCriteria(String numberPart) {
		dao.addNumberRestriction(numberPart);
	}

	public void addDescriptionCriteria(String description) {
		dao.addDescriptionRestriction(description);
	}

	public void addDateRangeCriteria(Date start, Date end) {
		dao.addDateRangeRestriction(start, end);
	}

	public List<Call> list() {
		return dao.listQuery();
	}

	public void newQuery() {
		dao.newQuery();
	}

	public void startTransaction() {
		dao.getSession().beginTransaction();
	}

	public void commitTransaction() {
		dao.getSession().getTransaction().commit();
	}

	public void accept(QueryVisitor visitor) {
		visitor.visit(this);
	}

    public void deleteCall(Call c) {
        dao.makeTransient(c);
    }

    public void rollbackTransaction() {
        dao.getSession().getTransaction().rollback();
    }
}
