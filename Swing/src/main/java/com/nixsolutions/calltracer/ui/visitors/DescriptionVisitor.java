package com.nixsolutions.calltracer.ui.visitors;

import com.nixsolutions.calltracer.ui.handlers.CallDaoHandler;

/**
 * @author denis_k
 *         Date: 15.03.2010
 *         Time: 15:01:45
 */
public class DescriptionVisitor implements QueryVisitor {
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void visit(CallDaoHandler callDaoHandler) {
		if (description != null) {
			callDaoHandler.addDescriptionCriteria(description);
		}
	}
}
