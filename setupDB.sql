CREATE TYPE EXAM_STATUS as ENUM('CLOSED', 'OPEN');

CREATE TABLE students(
    studentID VARCHAR(50),
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    email VARCHAR(50),
    password VARCHAR(50),
    PRIMARY KEY (studentID)
);

CREATE TABLE instructors(
    instructorID VARCHAR(50),
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    email VARCHAR(50),
    password VARCHAR(50),
    PRIMARY KEY (instructorID)
);

CREATE TABLE subjects(
   subjectID VARCHAR(50),
   subjectCode   VARCHAR(20),
   subjectName  VARCHAR(50),
   PRIMARY KEY (subjectID)
);

CREATE TABLE subjectInstructorMap(
    mapID INT GENERATED ALWAYS AS IDENTITY ,
    subjectID VARCHAR(50),
    instructorID VARCHAR(50),
    PRIMARY KEY (mapID),
    CONSTRAINT foreign_key_subjectID
        FOREIGN KEY (subjectID)
            REFERENCES subjects(subjectID),
    CONSTRAINT foreign_key_instructorID
        FOREIGN KEY (instructorID)
            REFERENCES instructors(instructorID)
);

CREATE TABLE subjectStudentMap(
     mapID INT GENERATED ALWAYS AS IDENTITY ,
     subjectID VARCHAR(50),
     studentID VARCHAR(50),
     PRIMARY KEY (mapID),
     CONSTRAINT foreign_key_subjectID
         FOREIGN KEY (subjectID)
             REFERENCES subjects(subjectID),
     CONSTRAINT foreign_key_studentID
         FOREIGN KEY (studentID)
             REFERENCES students(studentID)
);

CREATE TABLE exams(
    examID VARCHAR(50),
    subjectID VARCHAR(50),
    examName VARCHAR(140),
    status EXAM_STATUS,
    PRIMARY KEY (examID),
    CONSTRAINT foreign_key_subjectID
        FOREIGN KEY(subjectID)
            REFERENCES subjects(subjectID)
);

CREATE TABLE shortAnswerQuestions(
    questionID VARCHAR(50),
    examID  VARCHAR(50),
    totalMark DECIMAL(5,2),
    questionBody VARCHAR(10240),
    CONSTRAINT foreign_key_examID
        FOREIGN KEY(examID)
            REFERENCES exams(examID),
    PRIMARY KEY (questionID)
);

CREATE TABLE multipleChoiceQuestion (
    questionID VARCHAR(50),
    examID  VARCHAR(50),
    totalMark DECIMAL(5,2),
    questionBody VARCHAR(10240),
    CONSTRAINT foreign_key_examID
        FOREIGN KEY(examID)
            REFERENCES exams(examID),
    PRIMARY KEY (questionID)
);

CREATE TABLE choices (
    choiceID   VARCHAR(50),
    questionID VARCHAR(50),
    choice VARCHAR(5120),
    CONSTRAINT foreign_key_questionID
        FOREIGN KEY (questionID)
            REFERENCES multipleChoiceQuestion (questionID),
    PRIMARY KEY (choiceID)
);

CREATE TABLE examAnswer(
    examAnswerID VARCHAR(50),
    examID VARCHAR(50),
    studentID VARCHAR(50),
    finalMark DECIMAL(5,2),
    PRIMARY KEY (examAnswerID),
    CONSTRAINT foreign_key_studentID
        FOREIGN KEY (studentID)
            REFERENCES  students(studentID),
    CONSTRAINT foreign_key_examID
        FOREIGN KEY (examID)
            REFERENCES  exams(examID)
);

CREATE TABLE shortAnswerQuestionAnswers(
    shortAnswerQuestionAnswerID VARCHAR(50),
    questionID VARCHAR(50),
    examAnswerID VARCHAR(50),
    mark DECIMAL(5,2),
    answer VARCHAR(1024),
    PRIMARY KEY (shortAnswerQuestionAnswerID),
    CONSTRAINT foreign_key_questionID
        FOREIGN KEY (questionID)
            REFERENCES  shortAnswerQuestions(questionID),
    CONSTRAINT foreign_key_examAnswerID
        FOREIGN KEY (examAnswerID)
            REFERENCES  examAnswer(examAnswerID)
);

CREATE TABLE multipleChoiceQuestionAnswers(
   multipleChoiceQuestionAnswerID VARCHAR(50),
   questionID VARCHAR(50),
   examAnswerID VARCHAR(50),
   mark DECIMAL(5,2),
   answerChoiceID VARCHAR(50),
   PRIMARY KEY (multipleChoiceQuestionAnswerID),
   CONSTRAINT foreign_key_questionID
       FOREIGN KEY (questionID)
           REFERENCES  multipleChoiceQuestion(questionID),
   CONSTRAINT foreign_key_examAnswerID
       FOREIGN KEY (examAnswerID)
           REFERENCES  examAnswer(examAnswerID),
   CONSTRAINT foreign_key_choiceID
       FOREIGN KEY (answerChoiceID)
           REFERENCES  choices(choiceID)
);



