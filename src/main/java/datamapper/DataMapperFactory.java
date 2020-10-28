package datamapper;

import domain.ShortAnswerQuestionAnswer;
import domain.SubjectInstructorMap;
import exceptions.NoSuchMapperTypeException;

import java.lang.reflect.Type;

public class DataMapperFactory {
    public static DataMapper getMapper(String className) throws NoSuchMapperTypeException{
        switch (className) {
            case "domain.Choice":
                return ChoiceMapper.getSingletonInstance();
            case "domain.ExamAnswer":
                return ExamAnswerMapper.getSingletonInstance();
            case "domain.Exam":
                return ExamMapper.getSingletonInstance();
            case "domain.Instructor":
                return InstructorMapper.getSingletonInstance();
            case "domain.MultipleChoiceQuestionAnswer":
                return MultipleChoiceQuestionAnswerMapper.getSingletonInstance();
            case "domain.MultipleChoiceQuestion":
                return MultipleChoiceQuestionMapper.getSingletonInstance();
            case "domain.ShortAnswerQuestionAnswer":
                return ShortAnswerQuestionAnswerMapper.getSingletonInstance();
            case "domain.ShortAnswerQuestion":
                return ShortAnswerQuestionMapper.getSingletonInstance();
            case "domain.Student":
                return StudentMapper.getSingletonInstance();
            case "domain.Subject":
                return SubjectMapper.getSingletonInstance();
            case "domain.Admin":
                return AdminMapper.getSingletonInstance();
            case "domain.SubjectInstructorMap":
                return SubjectInstructorMapMapper.getSingletonInstance();
            case "domain.SubjectStudentMap":
                return SubjectStudentMapMapper.getSingletonInstance();
            default:
                throw new NoSuchMapperTypeException("No suitable mapper found for "+ className);
        }
    }
}
