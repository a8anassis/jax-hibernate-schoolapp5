package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dao.ITeacherDAO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherUpdateDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.service.util.JPAHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Provider
@ApplicationScoped
public class TeacherServiceImpl implements ITeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);

    @Inject
    private ITeacherDAO teacherDAO;

    @Override
    public Teacher insertTeacher(TeacherInsertDTO teacherDTO) throws Exception {
        Teacher teacher;
        try {
            JPAHelper.beginTransaction();
//            teacher = mapInsert(teacherDTO);
            teacher = Mapper.mapToTeacher(teacherDTO);
            teacher = teacherDAO.insert(teacher);
            if (teacher.getId() == null) {
                throw new Exception("Insert Error");
            }
            JPAHelper.commitTransaction();
            logger.info("Teacher with id: " + teacher.getId() + " inserted");
        } catch (Exception e) {
            JPAHelper.rollbackTransaction();
            logger.warn("Insert teacher rollback - Error");
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
        return teacher;
    }

    @Override
    public Teacher updateTeacher(TeacherUpdateDTO teacherDTO) throws EntityNotFoundException {
        Teacher teacherToUpdate;
        Teacher existingTeacher = null;

        try {
            JPAHelper.beginTransaction();
            //teacherToUpdate = mapUpdate(teacherDTO);
//            if (teacherDAO.getById(teacherToUpdate.getId()) == null) {
//                throw new EntityNotFoundException(Teacher.class, teacherToUpdate.getId());
//            }
            Optional.ofNullable(teacherDAO.getById(teacherDTO.getId()))
                    .orElseThrow(() -> new EntityNotFoundException(Teacher.class, teacherDTO.getId()));
//            teacherDAO.update(teacherToUpdate);
//            teacherToUpdate = mapUpdate(teacherDTO);
            teacherToUpdate = Mapper.mapToTeacher(teacherDTO);
            existingTeacher = teacherDAO.update(teacherToUpdate);
            JPAHelper.commitTransaction();
            logger.info("Teacher with id: " + teacherToUpdate.getId() + " updated");
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            logger.warn("Update rollback - Entity not found");
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
        // return teacherToUpdate;
        return existingTeacher;
    }

    @Override
    public void deleteTeacher(Long id) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
//            if (teacherDAO.getById(id) == null) {
//                throw new EntityNotFoundException(Teacher.class, id);
//            }
            Optional.ofNullable(teacherDAO.getById(id))
                    .orElseThrow(() -> new EntityNotFoundException(Teacher.class, id));
            teacherDAO.delete(id);
            JPAHelper.commitTransaction();
            logger.info("Teacher with id: " + id + " deleted");
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            logger.warn("Delete teacher rollback");
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
    }

    @Override
    public List<Teacher> getTeachersByLastname(String lastname)
            throws EntityNotFoundException {
        List<Teacher> teachers;
        try {
            JPAHelper.beginTransaction();
//            teachers = teacherDAO.getByLastName(lastname);
//            if (teachers.size() == 0) {
//                throw new EntityNotFoundException(List.class, 0L);
//            }
            teachers = Optional.of(teacherDAO.getByLastName(lastname))
                    .orElseThrow(() -> new EntityNotFoundException(List.class, 0L));
            JPAHelper.commitTransaction();
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            logger.info("Teachers not found by lastname");
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
        return teachers;
    }

    @Override
    public Teacher getTeacherById(Long id) throws EntityNotFoundException {
        Teacher teacher;
        try {
            JPAHelper.beginTransaction();
            teacher = teacherDAO.getById(id);
//            if (teacher == null) {
//                throw new EntityNotFoundException(Teacher.class, id);
//            }
            teacher = Optional.ofNullable(teacherDAO.getById(id))
                    .orElseThrow(() -> new EntityNotFoundException(List.class, id));
            JPAHelper.commitTransaction();
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            logger.info("Teacher with id: " + id + " not found");
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
        return teacher;
    }

    private Teacher mapUpdate(TeacherUpdateDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setId(dto.getId());
        teacher.setFirstname(dto.getFirstname());
        teacher.setLastname(dto.getLastname());
        return teacher;
    }

    private Teacher mapInsert(TeacherInsertDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setId(null);
        teacher.setFirstname(dto.getFirstname());
        teacher.setLastname(dto.getLastname());
        return teacher;
    }
}
