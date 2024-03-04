package gr.aueb.cf.schoolapp.rest;

import gr.aueb.cf.schoolapp.dto.TeacherDTO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.TeacherUpdateDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.service.ITeacherService;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.validator.ValidatorUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//@RequestScoped
@Path("/teachers")
public class TeacherRestController {

    @Inject
    private ITeacherService teacherService;

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeachersByLastname(@QueryParam("lastname") String lastname) {
        List<Teacher> teachers;
        try {
            teachers = teacherService.getTeachersByLastname(lastname);
            List<TeacherReadOnlyDTO> teachersDTO = new ArrayList<>();
            for (Teacher teacher : teachers) {
//                teachersDTO.add(new TeacherReadOnlyDTO(teacher.getId(),
//                        teacher.getFirstname(), teacher.getLastname()));
                teachersDTO.add(Mapper.mapToReadOnlyDTO(teacher));
            }
            return Response.status(Response.Status.OK).entity(teachersDTO).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("NOT FOUND").build();
        }
    }

    @Path("/{teacherId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeacher(@PathParam("teacherId") Long teacherId) {
        Teacher teacher;
        try {
            teacher = teacherService.getTeacherById(teacherId);
//            TeacherReadOnlyDTO teacherDto = new TeacherReadOnlyDTO(teacher.getId(),
//                    teacher.getFirstname(), teacher.getLastname());
            TeacherReadOnlyDTO teacherDto = Mapper.mapToReadOnlyDTO(teacher);
            return Response.status(Response.Status.OK).entity(teacherDto).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("NOT FOUND").build();
        }
    }

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTeacher(TeacherInsertDTO dto, @Context UriInfo uriInfo) {
        List<String> errors = ValidatorUtil.validateDTO(dto);
        if (!errors.isEmpty()) return Response
                .status(Response.Status.BAD_REQUEST).entity(errors).build();

        try {
            Teacher teacher = teacherService.insertTeacher(dto);
//            TeacherReadOnlyDTO teacherDTO = map(teacher);
            TeacherReadOnlyDTO teacherDTO = Mapper.mapToReadOnlyDTO(teacher);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            return Response.created(uriBuilder.path(Long.toString(teacherDTO.getId())).build())
                    .entity(teacherDTO).build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Teacher Error in insert")
                    .build();
        }
    }

    @Path("/{teacherId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTeacher(@PathParam("teacherId") Long teacherId) {
        try {
            Teacher teacher = teacherService.getTeacherById(teacherId);
            teacherService.deleteTeacher(teacherId);
//            TeacherReadOnlyDTO teacherDTO = map(teacher);
            TeacherReadOnlyDTO teacherDTO = Mapper.mapToReadOnlyDTO(teacher);
            return Response.status(Response.Status.OK).entity(teacherDTO).build();
        } catch (EntityNotFoundException e1) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Teacher Not Found")
                    .build();
        }
    }

    @Path("/{teacherId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTeacher(@PathParam("teacherId") Long teacherId, TeacherUpdateDTO dto) {
        List<String> errors = ValidatorUtil.validateDTO(dto);
        if (!errors.isEmpty()) return Response
                .status(Response.Status.BAD_REQUEST).entity(errors).build();

        try {
            if (!Objects.equals(dto.getId(), teacherId)) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
            }
            dto.setId(teacherId);
            Teacher teacher = teacherService.updateTeacher(dto);
//            TeacherReadOnlyDTO teacherDTO = map(teacher);
            TeacherReadOnlyDTO teacherDTO = Mapper.mapToReadOnlyDTO(teacher);
            return Response.status(Response.Status.OK).entity(teacherDTO).build();
        } catch (EntityNotFoundException e1) {
            return Response.status(Response.Status.NOT_FOUND).entity("Teacher Not Found").build();
        }
    }

//    private TeacherReadOnlyDTO map(Teacher teacher) {
//        TeacherReadOnlyDTO teacherDTO = new TeacherReadOnlyDTO();
//        teacherDTO.setId(teacher.getId());
//        teacherDTO.setFirstname(teacher.getFirstname());
//        teacherDTO.setLastname(teacher.getLastname());
//        return teacherDTO;
//    }
}
