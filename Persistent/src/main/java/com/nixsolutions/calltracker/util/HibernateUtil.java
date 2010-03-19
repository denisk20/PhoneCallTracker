package com.nixsolutions.calltracker.util;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {
    private static final SessionFactory SESSION_FACTORY;
    private static Logger logger = Logger.getLogger(HibernateUtil.class);

    static {
        try {
            SESSION_FACTORY = new AnnotationConfiguration()
                    .configure().buildSessionFactory();
        } catch (HibernateException e) {
            logger.error(e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Session getSession() {
        return SESSION_FACTORY.getCurrentSession();
    }

	public static SessionFactory getSessionFactory() {
		return SESSION_FACTORY;
	}
}
