package com.nixsolutions.calltracer.ui.enablers;

import java.util.ArrayList;

/**
 * @author denis_k
 *         Date: 16.03.2010
 *         Time: 10:18:04
 */
public class ConditionalEnabler extends JComponentEnabler {
	private boolean selected;
	private ElementEnabler firstEnabler;
	private ElementEnabler secondEnabler;

	public ConditionalEnabler(ElementEnabler firstEnabler, ElementEnabler secondEnabler) {
		this.firstEnabler = firstEnabler;
		this.secondEnabler = secondEnabler;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void enable() {
		super.enable();
		if (selected) {
			firstEnabler.enable();
		} else {
			secondEnabler.enable();
		}
	}

	public void disable() {
		super.disable();
		firstEnabler.disable();
		secondEnabler.disable();
	}
}
