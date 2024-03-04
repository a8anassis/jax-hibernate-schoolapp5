package gr.aueb.cf.schoolapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TeacherUpdateDTO extends Base {

    @NotNull
    @Size(min = 2, max = 255)
    private String firstname;

    @NotNull
    @Size(min = 2, max = 255)
    private String lastname;


    public TeacherUpdateDTO(Long id, String firstname, String lastname) {
        this.setId(id);
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
