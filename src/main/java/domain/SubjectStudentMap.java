package domain;

import datamapper.SubjectStudentMapMapper;


public class SubjectStudentMap extends DomainObject{

    private String subjectID = null;
    private String studentID = null;

    public SubjectStudentMap(){
        super();
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public void load(){
        SubjectStudentMap record = SubjectStudentMapMapper.getSingletonInstance().findWithID(getId());
        if (subjectID == null) subjectID = record.getSubjectID();
        if (studentID == null) studentID = record.getStudentID();
    }
}
