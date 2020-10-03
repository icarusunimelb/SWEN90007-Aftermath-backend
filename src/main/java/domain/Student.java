package domain;

import datamapper.InstructorMapper;
import datamapper.StudentMapper;
import datamapper.SubjectMapper;

import java.util.ArrayList;
import java.util.List;

public class Student extends User{

    public Student(){super();}

    private List<Subject> subjects = null;

    public List<Subject> getSubjects() {
        if (subjects == null) {
            setSubjects(SubjectMapper.getSingletonInstance().findWithStudentID(getId()));
        }
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public Name getName() {
        if(super.getName() == null) load();
        return super.getName();
    }

    @Override
    public String getEmail() {
        if(super.getEmail() == null) load();
        return super.getEmail();
    }

    @Override
    public String getPassword() {
        if(super.getPassword() == null) load();
        return super.getPassword();
    }

    public void load(){
        Student record = StudentMapper.getSingletonInstance().findWithID(getId());
        if (super.getName() == null) setName(record.getName());
        if (super.getEmail() == null) setEmail(record.getEmail());
        if (super.getPassword() == null) setPassword(record.getPassword());
    }
}
