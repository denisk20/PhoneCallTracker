package com.nixsolutions.calltracer.ui.executors;

import com.nixsolutions.calltracer.ui.data.CallVisitorsHolder;
import com.nixsolutions.calltracer.ui.data.DateRange;
import com.nixsolutions.calltracer.ui.data.DeletableCall;
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
		callVisitorsHolder.enablePhoneNumberQuery();
		if ((phoneNumber != null) && !(phoneNumber.equals(""))) {
			callVisitorsHolder.setPhone(phoneNumber);
		} else {
			callVisitorsHolder.setPhone("");
		}
		return queryCalls();
	}

	public List<Call> setDescriptionAndQueryCalls(String description) {
		callVisitorsHolder.enableDescriptionQuery();
		if (description != null && !description.equals("")) {
			callVisitorsHolder.setDescription(description);
		} else {
			callVisitorsHolder.setDescription("");
		}
		return queryCalls();
	}

	public List<Call> setDateRangeAndQueryCalls(DateRange range) {
		callVisitorsHolder.enableDateRangeQuery();
		if (validRange(range)) {
			//todo why don't we exit here?
			callVisitorsHolder.setDateRangeQuery(range);
		}
		return queryCalls();
	}

	//todo get rid of
	public void setDateRange(DateRange range) {
		if (validRange(range)) {
			callVisitorsHolder.getDateRangeQueryVisitor().setRange(range);
		}
	}

	//todo get rid of
	public void setPhone(String phone) {
		callVisitorsHolder.getPhoneNumberVisitor().setNumber(phone);
	}

	//todo get rid of
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
		List<Call> callList = null;
		try {
			daoHandler.startTransaction();
			daoHandler.newQuery();

			callVisitorsHolder.acceptHandler(daoHandler);
			callList = daoHandler.list();

			daoHandler.commitTransaction();
		} catch (Exception e) {
			daoHandler.rollbackTransaction();
			e.printStackTrace();
		}
		return callList;
	}

	public void deleteCalls(List<DeletableCall> calls) {
		try {
			daoHandler.startTransaction();
			for (DeletableCall c : calls) {
				if (c.isDelete()) {
					daoHandler.deleteCall(c.getCall());
				}
			}
			daoHandler.commitTransaction();
		} catch (Exception e) {
			daoHandler.rollbackTransaction();
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}

	public List<Call> subsractDateRangeAndRunQuery() {
		callVisitorsHolder.disableDateRangeQuery();
		List<Call> callList;
		callList = queryCalls();

		return callList;
	}

	public List<Call> substractPhoneAndRunQuery() {
		callVisitorsHolder.disablePhoneNumberQuery();
		List<Call> callList;
		callList = queryCalls();

		return callList;
	}

	public List<Call> substractDescriptionAndRunQuery() {
		callVisitorsHolder.disableDescriptionQuery();
		List<Call> callList;
		callList = queryCalls();

		return callList;
	}
}
