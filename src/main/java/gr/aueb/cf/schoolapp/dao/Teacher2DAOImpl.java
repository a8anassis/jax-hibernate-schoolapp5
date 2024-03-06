package gr.aueb.cf.schoolapp.dao;


import gr.aueb.cf.schoolapp.model.Teacher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class Teacher2DAOImpl extends AbstractDAO<Teacher> implements ITeacher2DAO {
    
	public Teacher2DAOImpl() {
		this.setPersistentClass(Teacher.class);
	}

}
