package com.nixsolutions.calltracer.ui.visitors;

import com.nixsolutions.calltracer.ui.handlers.CallDaoHandler;

/**
 * @author denis_k
 *         Date: 15.03.2010
 *         Time: 15:00:25
 */
public class PhoneNumberVisitor implements QueryVisitor {
	private String number;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void visit(CallDaoHandler callDaoHandler) {
		callDaoHandler.setNumberCriteria(number);
	}
}
