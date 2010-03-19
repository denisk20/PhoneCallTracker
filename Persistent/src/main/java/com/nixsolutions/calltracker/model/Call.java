package com.nixsolutions.calltracker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;


/**
 * @author denis_k
 *         Date: 11.03.2010
 *         Time: 14:07:49
 */
@NamedQueries({
		@NamedQuery(name = "getByNumber", query = "from Call c where c.phoneNumber like :number"),
		@NamedQuery(name = "getByDescription", query = "from Call c where c.description like :description")
})
@Entity
public class Call {
	private long id;

	@NotNull
	@Size(min = 6, max = 10, message = "Телефон - от 6 до 9 цифр")
	private String phoneNumber;
	private String description;
	private Date callDate;

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	//todo unit test this
	@Column(nullable = false, unique = true)
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Lob
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getCallDate() {
		return callDate;
	}

	public void setCallDate(Date callDate) {
		this.callDate = callDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Call)) return false;

		Call call = (Call) o;

		if (description != null ? !description.equals(call.description) : call.description != null) return false;
		if (!phoneNumber.equals(call.phoneNumber)) return false;
		if (!callDate.equals(call.callDate)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = phoneNumber.hashCode();
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (callDate != null ? callDate.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Call{" +
				"id=" + id +
				", phoneNumber='" + phoneNumber + '\'' +
				", description='" + description + '\'' +
				", callDate='" + callDate + '\'' +
				'}';
	}
}
