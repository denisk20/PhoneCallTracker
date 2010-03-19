package com.nixsolutions.calltracer.ui.executors;

import com.nixsolutions.calltracer.ui.data.CallVisitorsHolder;
import com.nixsolutions.calltracer.ui.data.DateRange;
import com.nixsolutions.calltracer.ui.handlers.CallDaoHandler;
import com.nixsolutions.calltracer.ui.visitors.QueryVisitor;
import com.nixsolutions.calltracker.model.Call;

import java.util.ArrayList;
import java.util.List;

/**
 * @author denis_k
 *         Date: 17.03.2010
 *         Time: 11:05:59
 */
public class CallQueryExecutor {
	private CallDaoHandler daoHandler;
	private CallVisitorsHolder callVisitorsHolder;

	public CallQueryExecutor(CallDaoHandler daoHandler, CallVisitorsHolder callVisitorsHolder) {
		this.daoHandler = daoHandler;
		this.callVisitorsHolder = callVisitorsHolder;
	}

	public List<Call> setPhoneAndQueryCalls(String phoneNumber) {
		if ((phoneNumber != null) && !(phoneNumber.equals(""))) {
			callVisitorsHolder.setPhone(phoneNumber);
		} else {
			callVisitorsHolder.setPhone("");
		}
		return queryCalls();
	}

	public List<Call> setDescriptionAndQueryCalls(String description) {
		if (description != null && ! description.equals("")) {
			callVisitorsHolder.setDescription(description);
		} else {
			callVisitorsHolder.setDescription("");
		}
		return queryCalls();
	}

	public List<Call> setDateRangeAndQueryCalls(DateRange range) {
		if (validRange(range)) {
			callVisitorsHolder.setDateRangeQuery(range);
		}
		return queryCalls();
	}

	public void setDateRange(DateRange range) {
		if (validRange(range)) {
			callVisitorsHolder.getDateRangeQueryVisitor().setRange(range);
		}
	}

	public void setPhone(String phone) {
		callVisitorsHolder.getPhoneNumberVisitor().setNumber(phone);
	}

	public void setDescription(String description) {
		if (notEmptyString(description)) {
			callVisitorsHolder.getDescriptionVisitor().setDescription(description);
		}
	}

	private boolean notEmptyString(String phone) {
		return phone != null && !phone.equals("");

	}

	private boolean validRange(DateRange range) {
		if (range != null) {
			if (range.getStartDate() != null && range.getEndDate() != null) {
				return true;
			}
		}
		return false;
	}

	public List<Call> queryCalls() {
		//set criteria for all visitors
		daoHandler.startTransaction();
		daoHandler.newQuery();

		callVisitorsHolder.acceptHandler(daoHandler);
		List<Call> callList = daoHandler.list();

		daoHandler.commitTransaction();
		return callList;
	}
}
