-- CREATE TYPE EXAM_STATUS as ENUM('CLOSED', 'PUBLISHED', 'UNPUBLISHED', 'ONGOING', 'MARKED');

CREATE TABLE students(
    studentID VARCHAR(200),
    firstName VARCHAR(200),
    lastName VARCHAR(200),
    email VARCHAR(200),
    password VARCHAR(200),
    PRIMARY KEY (studentID)
);

CREATE TABLE instructors(
    instructorID VARCHAR(200),
    firstName VARCHAR(200),
    lastName VARCHAR(200),
    email VARCHAR(200),
    password VARCHAR(200),
    PRIMARY KEY (instructorID)
);

CREATE TABLE subjects(
   subjectID VARCHAR(200),
   subjectCode   VARCHAR(20),
   subjectName  VARCHAR(200),
   PRIMARY KEY (subjectID)
);

CREATE TABLE subjectInstructorMap(
    mapID INT GENERATED ALWAYS AS IDENTITY ,
    subjectID VARCHAR(200),
    instructorID VARCHAR(200),
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
     subjectID VARCHAR(200),
     studentID VARCHAR(200),
     PRIMARY KEY (mapID),
     CONSTRAINT foreign_key_subjectID
         FOREIGN KEY (subjectID)
             REFERENCES subjects(subjectID),
     CONSTRAINT foreign_key_studentID
         FOREIGN KEY (studentID)
             REFERENCES students(studentID)
);

CREATE TABLE exams(
    examID VARCHAR(200),
    subjectID VARCHAR(200),
    examName VARCHAR(140),
    status VARCHAR(200),
    PRIMARY KEY (examID),
    CONSTRAINT foreign_key_subjectID
        FOREIGN KEY(subjectID)
            REFERENCES subjects(subjectID)
);

CREATE TABLE shortAnswerQuestions(
    questionID VARCHAR(200),
    examID  VARCHAR(200),
    totalMark DECIMAL(5,2),
    questionBody VARCHAR(10240),
    CONSTRAINT foreign_key_examID
        FOREIGN KEY(examID)
            REFERENCES exams(examID),
    PRIMARY KEY (questionID)
);

CREATE TABLE multipleChoiceQuestion (
    questionID VARCHAR(200),
    examID  VARCHAR(200),
    totalMark DECIMAL(5,2),
    questionBody VARCHAR(10240),
    CONSTRAINT foreign_key_examID
        FOREIGN KEY(examID)
            REFERENCES exams(examID),
    PRIMARY KEY (questionID)
);

CREATE TABLE choices (
    choiceID   VARCHAR(200),
    questionID VARCHAR(200),
    index INT,
    choice VARCHAR(5120),
    CONSTRAINT foreign_key_questionID
        FOREIGN KEY (questionID)
            REFERENCES multipleChoiceQuestion (questionID),
    PRIMARY KEY (choiceID)
);

CREATE TABLE examAnswer(
    examAnswerID VARCHAR(200),
    examID VARCHAR(200),
    studentID VARCHAR(200),
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
    shortAnswerQuestionAnswerID VARCHAR(200),
    questionID VARCHAR(200),
    examAnswerID VARCHAR(200),
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
   multipleChoiceQuestionAnswerID VARCHAR(200),
   questionID VARCHAR(200),
   examAnswerID VARCHAR(200),
   mark DECIMAL(5,2),
   answerIndex INT,
   PRIMARY KEY (multipleChoiceQuestionAnswerID),
   CONSTRAINT foreign_key_questionID
       FOREIGN KEY (questionID)
           REFERENCES  multipleChoiceQuestion(questionID),
   CONSTRAINT foreign_key_examAnswerID
       FOREIGN KEY (examAnswerID)
           REFERENCES  examAnswer(examAnswerID)

);



