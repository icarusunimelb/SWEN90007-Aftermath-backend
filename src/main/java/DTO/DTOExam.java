package DTO;

import domain.Exam;
import exceptions.RecordNotExistException;

public class DTOExam {
    public final String dataId;
    public final String examName;
    public final String status;

    public DTOExam(Exam exam) throws RecordNotExistException {
        dataId = exam.getId();
        examName = exam.getExamName();
        if( exam.getStatus().equals("PENDING")){
            status = "UNPUBLISHED";
        } else{
            status = exam.getStatus();
        }
    }

}
