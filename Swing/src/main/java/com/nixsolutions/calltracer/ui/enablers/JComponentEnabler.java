package com.nixsolutions.calltracer.ui.enablers;

import javax.swing.*;
import java.util.ArrayList;

/**
 * @author denis_k
 *         Date: 16.03.2010
 *         Time: 10:11:30
 */
public class JComponentEnabler implements ElementEnabler{
	private ArrayList<JComponent> elements = new ArrayList<JComponent>();

	public void addElement(JComponent c) {
		elements.add(c);
	}

	public void removeElement(JComponent c) {
		elements.remove(c);
	}

	public void enable() {
		for (JComponent c : elements) {
			c.setEnabled(true);
		}
	}

	public void disable() {
		for (JComponent c : elements) {
			c.setEnabled(false);
		}
	}
}
