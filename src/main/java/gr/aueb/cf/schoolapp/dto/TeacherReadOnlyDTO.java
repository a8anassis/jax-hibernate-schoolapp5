package gr.aueb.cf.schoolapp.dto;

public class TeacherReadOnlyDTO extends Base {
    String firstname;
    String lastname;

    public TeacherReadOnlyDTO() {}

    public TeacherReadOnlyDTO(Long id, String firstname, String lastname) {
        super.setId(id);
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
