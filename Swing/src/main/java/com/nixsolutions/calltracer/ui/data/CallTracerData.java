package com.nixsolutions.calltracer.ui.data;

import java.util.Date;

public class CallTracerData {
	private String numberOfDays;
	private Date fromDate;
	private Date toDate;
	private String phoneNumber;
	private String description;

	public CallTracerData() {
	}

	public String getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(final String numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(final Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(final Date toDate) {
		this.toDate = toDate;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}
}