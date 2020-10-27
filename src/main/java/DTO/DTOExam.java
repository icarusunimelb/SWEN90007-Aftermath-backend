package DTO;

import domain.Exam;

public class DTOExam {
    public final String dataId;
    public final String examName;
    public final String status;

    public DTOExam(Exam exam){
        dataId = exam.getId();
        examName = exam.getExamName();
        status = "EXAM_STATUS."+exam.getStatus();
    }

}
