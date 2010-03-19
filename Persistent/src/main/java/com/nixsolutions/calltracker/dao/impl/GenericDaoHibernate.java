package com.nixsolutions.calltracker.dao.impl;

import com.nixsolutions.calltracker.dao.GenericDAO;
import com.nixsolutions.calltracker.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.LockMode;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Criterion;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;


public abstract class GenericDaoHibernate<T, ID extends Serializable> implements GenericDAO<T, ID> {
    private Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    protected GenericDaoHibernate() {
        persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

	public Session getSession() {
        return HibernateUtil.getSessionFactory().getCurrentSession();
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    @SuppressWarnings("unchecked")
    public T findById(ID id, boolean lock) {
        T result;
        if (lock) {
            result = (T) getSession().get(getPersistentClass(), id, LockMode.UPGRADE);
        } else {
            result = (T) getSession().get(getPersistentClass(), id);
        }
        return result;
    }

    public List<T> findAll() {
        return findByCriteria();
    }

    @SuppressWarnings("unchecked")
    public List<T> findByExample(T exampleInstance, String... excludeProperty) {
        Criteria criteria = getSession().createCriteria(getPersistentClass());
        Example example = Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        criteria.add(example);
        return criteria.list();
    }

    public T makePersistent(T entity) {
        getSession().saveOrUpdate(entity);
        return entity;
    }

    public void makeTransient(T entity) {
        getSession().delete(entity);
    }

    public void flush() {
        getSession().flush();
    }

    public void clear() {
        getSession().clear();
    }

    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(Criterion... criterions) {
        Criteria criteria = getSession().createCriteria(getPersistentClass());
        for (Criterion c : criterions) {
            criteria.add(c);
        }
        return criteria.list();
    }
}
