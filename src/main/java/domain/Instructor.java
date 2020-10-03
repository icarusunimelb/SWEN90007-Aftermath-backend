package domain;

import datamapper.InstructorMapper;
import datamapper.SubjectMapper;

import java.util.ArrayList;
import java.util.List;

public class Instructor extends User{
	private List<Subject> subjects = null;

	public Instructor(){super();};

	public List<Subject> getSubjects() {
		if (subjects == null) {
			setSubjects(SubjectMapper.getSingletonInstance().findWithInstructorID(getId()));
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
		Instructor record = InstructorMapper.getSingletonInstance().findWithID(getId());
		if (super.getName() == null) setName(record.getName());
		if (super.getEmail() == null) setEmail(record.getEmail());
		if (super.getPassword() == null) setPassword(record.getPassword());
	}
}
