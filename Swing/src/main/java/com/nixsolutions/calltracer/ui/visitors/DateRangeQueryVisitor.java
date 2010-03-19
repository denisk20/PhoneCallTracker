package com.nixsolutions.calltracer.ui.visitors;

import com.nixsolutions.calltracer.ui.data.DateRange;
import com.nixsolutions.calltracer.ui.handlers.CallDaoHandler;

/**
 * @author denis_k
 *         Date: 15.03.2010
 *         Time: 12:52:23
 */
public class DateRangeQueryVisitor implements QueryVisitor{
	private DateRange range;

	public DateRange getRange() {
		return range;
	}

	public void setRange(DateRange range) {
		this.range = range;
	}

	public void visit(CallDaoHandler callDaoHandler) {
		if (range != null) {
			if (range.getStartDate() != null && range.getEndDate() != null) {
				callDaoHandler.addDateRangeCriteria(range.getStartDate(), range.getEndDate());
			}
		}
	}
}
