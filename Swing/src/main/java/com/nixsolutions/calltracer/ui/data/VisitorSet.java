package com.nixsolutions.calltracer.ui.data;

import com.nixsolutions.calltracer.ui.visitors.Acceptable;
import com.nixsolutions.calltracer.ui.visitors.QueryVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author denis_k
 *         Date: 18.03.2010
 *         Time: 11:41:22
 */
public class VisitorSet {
	private List<QueryVisitor> visitors = new ArrayList<QueryVisitor>();

	protected void addVisitor(QueryVisitor visitor) {
		visitors.add(visitor);
	}

	protected void removeVisitor(QueryVisitor visitor) {
		visitors.remove(visitor);
	}

	public void acceptHandler(Acceptable handler) {
		for (QueryVisitor visitor : visitors) {
			handler.accept(visitor);
		}
	}
}
