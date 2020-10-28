package domain;

import datamapper.SubjectInstructorMapMapper;



public class SubjectInstructorMap extends DomainObject{

    private String subjectID = null;
    private String instructorID = null;

    public SubjectInstructorMap(){
        super();
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getInstructorID() {
        return instructorID;
    }

    public void setInstructorID(String instructorID) {
        this.instructorID = instructorID;
    }

    public void load(){
        SubjectInstructorMap record = SubjectInstructorMapMapper.getSingletonInstance().findWithID(getId());
        if (subjectID == null) subjectID = record.getSubjectID();
        if (instructorID == null) instructorID = record.getInstructorID();
    }
}
