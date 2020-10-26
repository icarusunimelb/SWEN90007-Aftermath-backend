package domain;

import datamapper.ExamMapper;
import datamapper.SubjectMapper;
import datasource.DBConnection;
import exceptions.RecordNotExistException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Subject extends DomainObject{
	private String subjectCode = null;
    private String subjectName = null;
    private List<Exam> exams = null;

	public Subject(){super();};

	public String getSubjectCode() throws RecordNotExistException{
		if (subjectCode == null) load();
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getSubjectName() throws RecordNotExistException{
		if (subjectName == null) load();
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public List<Exam> getExams() {
		if (exams == null) {
			setExams(ExamMapper.getSingletonInstance().findWithSubjectCode(getId()));
		}
		return exams;
	}

	public void setExams(List<Exam> exams) {
		this.exams = exams;
	}

	public void load() throws RecordNotExistException{
		Subject record = SubjectMapper.getSingletonInstance().findWithID(getId());
		if (record.getId() == null) {
			throw new RecordNotExistException(getClass()+" "+ getId() + " has no record in the database");
		}
		if (subjectCode == null) subjectCode = record.getSubjectCode();
		if (subjectName == null) subjectName = record.getSubjectName();
	}
}
