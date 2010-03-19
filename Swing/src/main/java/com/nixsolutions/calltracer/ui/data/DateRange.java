package com.nixsolutions.calltracer.ui.data;

import java.util.Date;

/**
 * @author denis_k
 *         Date: 15.03.2010
 *         Time: 12:54:30
 */
public class DateRange {
	private Date startDate;
	private Date endDate;

	public DateRange(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}
}
