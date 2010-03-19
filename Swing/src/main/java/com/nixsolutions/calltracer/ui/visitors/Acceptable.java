package com.nixsolutions.calltracer.ui.visitors;

/**
 * @author denis_k
 *         Date: 15.03.2010
 *         Time: 12:48:04
 */
public interface Acceptable {
	void accept(QueryVisitor visitor);
}
