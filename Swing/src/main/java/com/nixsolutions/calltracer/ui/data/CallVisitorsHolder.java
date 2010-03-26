package com.nixsolutions.calltracer.ui.data;

import com.nixsolutions.calltracer.ui.handlers.CallDaoHandler;
import com.nixsolutions.calltracer.ui.visitors.Acceptable;
import com.nixsolutions.calltracer.ui.visitors.DateRangeQueryVisitor;
import com.nixsolutions.calltracer.ui.visitors.DescriptionVisitor;
import com.nixsolutions.calltracer.ui.visitors.PhoneNumberVisitor;
import com.nixsolutions.calltracer.ui.visitors.QueryVisitor;

/**
 * @author denis_k
 *         Date: 17.03.2010
 *         Time: 11:41:16
 */
public class CallVisitorsHolder extends VisitorSet {
	private DateRangeQueryVisitor dateRangeQueryVisitor;
	private PhoneNumberVisitor phoneNumberVisitor;
	private DescriptionVisitor descriptionVisitor;

	public CallVisitorsHolder(DateRangeQueryVisitor dateRangeQueryVisitor, PhoneNumberVisitor phoneNumberVisitor, DescriptionVisitor descriptionVisitor) {
		this.dateRangeQueryVisitor = dateRangeQueryVisitor;
		this.phoneNumberVisitor = phoneNumberVisitor;
		this.descriptionVisitor = descriptionVisitor;

		addVisitor(dateRangeQueryVisitor);
		addVisitor(phoneNumberVisitor);
		addVisitor(descriptionVisitor);
	}

	public CallVisitorsHolder() {
		dateRangeQueryVisitor = new DateRangeQueryVisitor();
		phoneNumberVisitor = new PhoneNumberVisitor();
		descriptionVisitor = new DescriptionVisitor();

		addVisitor(dateRangeQueryVisitor);
		addVisitor(phoneNumberVisitor);
		addVisitor(descriptionVisitor);
	}

	public void setDateRangeQuery(DateRange range) {
		dateRangeQueryVisitor.setRange(range);
	}

	public void setPhone(String phone) {
		phoneNumberVisitor.setNumber(phone);
	}

	public void setDescription(String description) {
		descriptionVisitor.setDescription(description);
	}

	public DateRangeQueryVisitor getDateRangeQueryVisitor() {
		return dateRangeQueryVisitor;
	}

	public PhoneNumberVisitor getPhoneNumberVisitor() {
		return phoneNumberVisitor;
	}

	public DescriptionVisitor getDescriptionVisitor() {
		return descriptionVisitor;
	}

	public void disableDateRangeQuery() {
		dateRangeQueryVisitor.setActive(false);
	}

	public void enableDateRangeQuery() {
		dateRangeQueryVisitor.setActive(true);
	}

	public void disablePhoneNumberQuery() {
		phoneNumberVisitor.setActive(false);
	}

	public void enablePhoneNumberQuery() {
		phoneNumberVisitor.setActive(true);
	}

	public void disableDescriptionQuery() {
		descriptionVisitor.setActive(false);
	}

	public void enableDescriptionQuery() {
		descriptionVisitor.setActive(true);
	}
}
