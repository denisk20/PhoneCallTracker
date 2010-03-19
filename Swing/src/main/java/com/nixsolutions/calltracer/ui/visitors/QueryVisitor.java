package com.nixsolutions.calltracer.ui.visitors;

import com.nixsolutions.calltracer.ui.handlers.CallDaoHandler;

/**
 * @author denis_k
 *         Date: 15.03.2010
 *         Time: 12:45:41
 */
public interface QueryVisitor {
	void visit(CallDaoHandler callDaoHandler);
}
