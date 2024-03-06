package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.service.util.JPAHelper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @param <T>
 */
public abstract class AbstractDAO<T> implements IGenericDAO<T> {

	private Class<T> persistentClass;

	public AbstractDAO() {
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	public void setPersistentClass(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	@Override
	public T insert(T t) {
		EntityManager em = getEntityManager();
		em.persist(t);
		return t;
	}

	@Override
	public T update(T t) {
		return getEntityManager().merge(t);
	}

	@Override
	public void delete(Object id) {
		EntityManager em = getEntityManager();
		T toDelete = getById(id);
		em.remove(toDelete);
	}

	@Override
	public T getById(Object id) {
		EntityManager em = getEntityManager();
		return em.find(persistentClass, id);
	}

	@Override
	public List<T> getAll() {
		return getByCriteria(getPersistentClass(),Collections.<String, Object>emptyMap());
	}
	
	@Override
	public List<? extends T> getByCriteria(Map<String, Object> criteria) {
		return getByCriteria(getPersistentClass(), criteria);
	}
	
	@Override
	public <K extends T> List<K> getByCriteria(Class<K> clazz, Map<String, Object> criteria) {
		EntityManager em = getEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();

		CriteriaQuery<K> selectQuery = builder.createQuery(clazz);
		Root<K> entityRoot = selectQuery.from(clazz);

		List<Predicate> predicates = getPredicatesList(builder, entityRoot, criteria);
		selectQuery.select(entityRoot).where(predicates.toArray(new Predicate[predicates.size()]));

		TypedQuery<K> query = em.createQuery(selectQuery);
		//addParametersToQuery(query, criteria);

		return query.getResultList();
	}



	protected Path<?> resolvePath(Root<? extends T> root, String expression) {
		String[] fields = expression.split("\\.");

		Path<?> path = root.get(fields[0]);
		for (int i = 1; i < fields.length; i++) {
			path = path.get(fields[i]);
		}
		return path;
	}

	protected List<Predicate> getPredicatesList(CriteriaBuilder builder, Root<? extends T> entityRoot,
			Map<String, Object> parameters) {
		List<Predicate> predicateList = new ArrayList<>();

		for (Entry<String, Object> entry : parameters.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			//ParameterExpression<?> val = builder.parameter(value.getClass(), buildParameterAlias(key));
			ParameterExpression<String> val = builder.parameter(String.class, buildParameterAlias(key));
			//Predicate equal = builder.like(resolvePath(entityRoot, key), val);
			Predicate like = builder.like(resolvePath(entityRoot, key).as(String.class), value + "%");
			//Predicate equal = builder.like(resolvePath(entityRoot, key).as(String.class), val);
			//Predicate equal = builder.like(resolvePath(entityRoot, key), val);
			predicateList.add(like);
		}
		return predicateList;
	}

	protected void addParametersToQuery(TypedQuery<?> query, Map<String, Object> parameters) {
		for (Entry<String, Object> entry : parameters.entrySet()) {
			Object value = entry.getValue();
			query.setParameter(buildParameterAlias(entry.getKey()), value);
		}
	}

	protected String buildParameterAlias(String malformedAlias) {
		return malformedAlias.replaceAll("\\.", "");
	}

	public EntityManager getEntityManager() {
		return JPAHelper.getEntityManager();
	}
}
