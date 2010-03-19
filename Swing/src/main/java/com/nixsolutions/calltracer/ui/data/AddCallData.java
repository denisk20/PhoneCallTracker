package com.nixsolutions.calltracer.ui.data;

import java.util.Date;

public class AddCallData {
	private String phoneNumber;
	private Date date;
	private String description;

	public AddCallData() {
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}
}