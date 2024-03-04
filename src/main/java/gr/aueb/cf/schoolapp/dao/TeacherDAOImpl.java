package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.service.util.JPAHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

@Provider
@ApplicationScoped
public class TeacherDAOImpl implements ITeacherDAO {

    @Override
    public Teacher insert(Teacher teacher) {
        EntityManager em = getEntityManager();
        em.persist(teacher);
        return teacher;
    }

    @Override
    public Teacher update(Teacher teacher) {
        getEntityManager().merge(teacher);
        return teacher;
    }

    @Override
    public void delete(Long id) {
        EntityManager em = getEntityManager();
        Teacher teacherToDelete = em.find(Teacher.class, id);
        em.remove(teacherToDelete);
    }

    @Override
    public List<Teacher> getByLastName(String lastname) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Teacher> selectQuery = builder.createQuery(Teacher.class);
        Root<Teacher> root = selectQuery.from(Teacher.class);

        ParameterExpression<String> paramLastname = builder.parameter(String.class);
        selectQuery.select(root).where(builder.like(root.get("lastname"), paramLastname));
        return getEntityManager()
                .createQuery(selectQuery)
                .setParameter(paramLastname, lastname + "%")
                .getResultList();
    }

    @Override
    public Teacher getById(Long id) {
        return getEntityManager().find(Teacher.class, id);
    }

    private EntityManager getEntityManager() {
        return JPAHelper.getEntityManager();
    }
}
