package com.nixsolutions.calltracer.ui.data;

import com.nixsolutions.calltracker.model.Call;

/**
 * @author denis_k
 *         Date: 17.03.2010
 *         Time: 13:45:16
 */
public class DeletableCall {
	private Call call;
	private boolean delete = false;

	public DeletableCall(Call call) {
		this.call = call;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public Call getCall() {
		return call;
	}
}
