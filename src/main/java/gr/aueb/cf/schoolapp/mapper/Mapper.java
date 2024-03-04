package gr.aueb.cf.schoolapp.mapper;

import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.TeacherUpdateDTO;
import gr.aueb.cf.schoolapp.model.Teacher;

public class Mapper {

    private Mapper() {}

    public static Teacher mapToTeacher(TeacherInsertDTO dto) {
        return new Teacher(null, dto.getFirstname(), dto.getLastname());
    }

    public static Teacher mapToTeacher(TeacherUpdateDTO dto) {
        return new Teacher(dto.getId(), dto.getFirstname(), dto.getLastname());
    }

    public static TeacherReadOnlyDTO mapToReadOnlyDTO(Teacher teacher) {
        return new TeacherReadOnlyDTO(teacher.getId(), teacher.getFirstname(), teacher.getLastname());
    }
}
