package datamapper;

import domain.ShortAnswerQuestionAnswer;
import exceptions.NoSuchMapperTypeException;

import java.lang.reflect.Type;

public class DataMapperFactory {
    public static DataMapper getMapper(String className) throws NoSuchMapperTypeException{
        switch (className) {
            case "Choice":
                return ChoiceMapper.getSingletonInstance();
            case "ExamAnswer":
                return ExamAnswerMapper.getSingletonInstance();
            case "Exam":
                return ExamMapper.getSingletonInstance();
            case "Instructor":
                return InstructorMapper.getSingletonInstance();
            case "MultipleChoiceQuestionAnswer":
                return MultipleChoiceQuestionAnswerMapper.getSingletonInstance();
            case "MultipleChoiceQuestion":
                return MultipleChoiceQuestionMapper.getSingletonInstance();
            case "ShortAnswerQuestionAnswer":
                return ShortAnswerQuestionAnswerMapper.getSingletonInstance();
            case "ShortAnswerQuestion":
                return ShortAnswerQuestionMapper.getSingletonInstance();
            case "Student":
                return StudentMapper.getSingletonInstance();
            case "Subject":
                return SubjectMapper.getSingletonInstance();
            default:
                throw new NoSuchMapperTypeException("No suitable mapper found for "+ className);
        }
    }
}
