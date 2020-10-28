package domain;

import datamapper.InstructorMapper;
import datamapper.SubjectMapper;
import exceptions.RecordNotExistException;

import java.util.ArrayList;
import java.util.List;

public class Instructor extends User{
	private List<Subject> subjects = null;

	private String role = "instructor";

	public Instructor(){super();};

	@Override
	public String getRole() {
		return role;
	}

	@Override
	public void setRole(String role) {
		this.role = role;
	}

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
	public Name getName() throws RecordNotExistException{
		if(super.getName() == null) load();
		return super.getName();
	}

	@Override
	public String getEmail() throws RecordNotExistException{
		if(super.getEmail() == null) load();
		return super.getEmail();
	}

	@Override
	public String getPassword() throws RecordNotExistException{
		if(super.getPassword() == null) load();
		return super.getPassword();
	}

	public void load() throws RecordNotExistException{
		Instructor record = InstructorMapper.getSingletonInstance().findWithID(getId());
		if (record.getId() == null) {
			throw new RecordNotExistException(getClass()+" "+ getId() + " has no record in the database");
		}
		if (super.getName() == null) setName(record.getName());
		if (super.getEmail() == null) setEmail(record.getEmail());
		if (super.getPassword() == null) setPassword(record.getPassword());
	}
}
