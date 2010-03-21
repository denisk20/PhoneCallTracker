package com.nixsolutions.calltracker.dao.impl;

import com.nixsolutions.calltracker.dao.CallDao;
import com.nixsolutions.calltracker.model.Call;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

/**
 * @author denis_k
 *         Date: 11.03.2010
 *         Time: 14:52:42
 */
public class CallDaoHibernate extends GenericDaoHibernate<Call, Long> implements CallDao {

	private Criteria criteria;
	@Override
	@SuppressWarnings("unchecked")
	public List<Call> getByNumber(String numberPart) {
		Criteria criteria = getSession().createCriteria(Call.class);
		addNumberRestriction(criteria, numberPart);
		List results = criteria.list();
		return results;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Call> getByDescription(String description) {
		Criteria criteria = getSession().createCriteria(Call.class);
		addDescriptionRestriction(criteria, description);
		List results = criteria.list();
		return results;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Call> getByDateRange(Date startDate, Date endDate) {
		Criteria criteria = getSession().createCriteria(Call.class);
		addDateRangeRestriction(criteria, startDate, endDate);
		List results = criteria.list();
		return results;
	}

	public void newQuery() {
		criteria = getSession().createCriteria(Call.class);
	}

	public void addNumberRestriction(String numberPart) {
		addNumberRestriction(criteria, numberPart);
	}

	public void addDescriptionRestriction(String description) {
		addDescriptionRestriction(criteria, description);
	}

	public void addDateRangeRestriction(Date start, Date end) {
		addDateRangeRestriction(criteria, start, end);
	}

    @SuppressWarnings("unchecked")
	public List<Call> listQuery() {
		return criteria.list();
	}
	private Criteria addNumberRestriction(Criteria c, String numberPart) {
		c.add(Restrictions.like("phoneNumber", '%' + numberPart + '%'));
		return c;
	}

	private Criteria addDescriptionRestriction(Criteria c, String description) {
		c.add(Restrictions.ilike("description", '%' + description + '%'));
		return c;
	}

	private Criteria addDateRangeRestriction(Criteria c, Date start, Date end) {
		c.add(Restrictions.between("callDate", start, end));
		return c;
	}
}
