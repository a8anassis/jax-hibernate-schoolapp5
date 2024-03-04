package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.TeacherUpdateDTO;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;

import java.util.List;

public interface ITeacherService {
    Teacher insertTeacher(TeacherInsertDTO teacherDTO) throws Exception;
    Teacher updateTeacher(TeacherUpdateDTO teacherDTO) throws EntityNotFoundException;
    void deleteTeacher(Long id) throws EntityNotFoundException;
    List<Teacher> getTeachersByLastname(String lastname) throws EntityNotFoundException;
    Teacher getTeacherById(Long id) throws EntityNotFoundException;
}
