package domain;

import datamapper.ExamMapper;
import datamapper.SubjectMapper;
import datasource.DBConnection;

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

	public String getSubjectCode() {
		if (subjectCode == null) load();
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getSubjectName() {
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

	public void load(){
		Subject record = SubjectMapper.getSingletonInstance().findWithID(getId());
		if (subjectCode == null) subjectCode = record.getSubjectCode();
		if (subjectName == null) subjectName = record.getSubjectName();
	}
}
