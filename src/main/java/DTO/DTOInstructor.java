package DTO;

import domain.Instructor;
import java.util.stream.Collectors;

public class DTOInstructor {
    public final String dataId;
    public final String lecturerName;

    public DTOInstructor(Instructor instructor) {
        dataId = instructor.getId();
        lecturerName = instructor.getName().toString();
    }

}
