package com.nixsolutions.calltracker.dao.impl;

import com.nixsolutions.calltracker.model.Call;
import com.nixsolutions.calltracker.util.DataGenerator;
import com.nixsolutions.calltracker.util.HibernateUtil;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author denis_k
 *         Date: 11.03.2010
 *         Time: 19:36:17
 */
public class CallDaoHibernateTest {
	private static CallDaoHibernate dao;
	@Before
	public void setUp() {
		dao = new CallDaoHibernate();
		dao.getSession().beginTransaction();
	}

	@After
	public void tearDown() {
		dao.getSession().getTransaction().rollback();
	}

	@Test
	public void testDataLoad() {
		List<Call> calls = dao.findAll();
		assertEquals("Wrong number of calls in DB", 3, calls.size());
	}

	@Test
	public void testGetByNumber() {
		List<Call> results = dao.getByNumber(DataGenerator.NUMBER1);
		assertNotNull("No results", results);
		assertEquals("Wrong number of results", 1, results.size());
		Call call1 = results.get(0);
		assertEquals("Got wrong result", DataGenerator.NUMBER1, call1.getPhoneNumber());
	}
	
	@Test
	public void testGetByDescription() {
		List<Call> results = dao.getByDescription(DataGenerator.DESCRIPTION1);
		assertNotNull("No results", results);
		assertEquals("Wrong number of results", 1, results.size());
		Call call1 = results.get(0);
		assertEquals("Got wrong result", DataGenerator.DESCRIPTION1, call1.getDescription());
	}

	@Test
	public void testGetByDateRange() {
		Date startDate;
		Date endDate;
		Calendar c = Calendar.getInstance();
		c.setTime(DataGenerator.date1);

		c.add(Calendar.DATE, -1);
		startDate = c.getTime();

		c.add(Calendar.DATE, 2);
		endDate = c.getTime();

		List<Call> results = dao.getByDateRange(startDate, endDate);
		assertNotNull("No results", results);
		assertEquals("Wrong number of results", 1, results.size());
		Call call1 = results.get(0);
		assertEquals("Got wrong result", DataGenerator.date1, call1.getCallDate());

		//====================
		c.set(Calendar.YEAR, 1960);
		Date longAgoDate = c.getTime();

		c.set(Calendar.YEAR, 2020);
		Date farFromHereDate = c.getTime();

		List<Call> allResults = dao.getByDateRange(longAgoDate, farFromHereDate);
		assertNotNull("No results", allResults);
		assertEquals("Wrong number of results", 3, allResults.size());
	}

	@Test(expected = PropertyValueException.class)
	public void testNullDate() {
		List<Call> calls = dao.findAll();
		assertNotNull(calls);
		assertEquals("Wrong number of calls in DB", 3, calls.size());
		Call call = calls.get(0);

		call.setCallDate(null);
		dao.makePersistent(call);

		dao.flush();
	}

	@Test(expected = PropertyValueException.class)
	public void testNullPhone() {
		List<Call> calls = dao.findAll();
		assertNotNull(calls);
		assertEquals("Wrong number of calls in DB", 3, calls.size());
		Call call = calls.get(0);

		call.setPhoneNumber(null);
		dao.makePersistent(call);

		dao.flush();
	}


    @Test(expected = ConstraintViolationException.class)
    @Ignore("This test is not valid anymore")
	public void testNonUniquePhone() {
		List<Call> calls = dao.findAll();
		assertNotNull(calls);
		assertEquals("Wrong number of calls in DB", 3, calls.size());
		Call call = calls.get(0);

		Call newCall = new Call();
		newCall.setPhoneNumber(call.getPhoneNumber());
		newCall.setCallDate(new Date());

		dao.makePersistent(newCall);

		dao.flush();
	}

	@Test
	public void testSmallNumber() {
		Call call = new Call();
		call.setCallDate(new Date());
		final String smallNumber = "123";
		call.setPhoneNumber(smallNumber);

		Validator validator;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		Set<ConstraintViolation<Call>> constraintViolations =
				   validator.validate(call);

		assertEquals(1, constraintViolations.size());
	}

	@AfterClass
	public static void closeConnection() {
		if (dao.getSession().isOpen()) {
			dao.getSession().close();
		}
	}
}
