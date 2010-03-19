package com.nixsolutions.calltracker.dao;

import com.nixsolutions.calltracker.model.Call;

import java.util.Date;
import java.util.List;

/**
 * @author denis_k
 *         Date: 11.03.2010
 *         Time: 14:38:58
 */
public interface CallDao extends GenericDAO<Call, Long> {
	List<Call> getByNumber(String numberPart);

	List<Call> getByDescription(String descriptionPart);

	List<Call> getByDateRange(Date start, Date finish);
}
