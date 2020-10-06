CREATE TABLE oes.students(
                             studentID VARCHAR(200),
                             firstName VARCHAR(200),
                             lastName VARCHAR(200),
                             email VARCHAR(200),
                             password VARCHAR(200),
                             PRIMARY KEY (studentID)
);

CREATE TABLE oes.instructors(
                                instructorID VARCHAR(200),
                                firstName VARCHAR(200),
                                lastName VARCHAR(200),
                                email VARCHAR(200),
                                password VARCHAR(200),
                                PRIMARY KEY (instructorID)
);

CREATE TABLE oes.subjects(
                             subjectID VARCHAR(200),
                             subjectCode   VARCHAR(20),
                             subjectName  VARCHAR(200),
                             PRIMARY KEY (subjectID)
);

CREATE TABLE oes.subjectInstructorMap(
                                         mapID INT GENERATED ALWAYS AS IDENTITY ,
                                         subjectID VARCHAR(200),
                                         instructorID VARCHAR(200),
                                         PRIMARY KEY (mapID),
                                         CONSTRAINT foreign_key_subjectID
                                             FOREIGN KEY (subjectID)
                                                 REFERENCES oes.subjects(subjectID),
                                         CONSTRAINT foreign_key_instructorID
                                             FOREIGN KEY (instructorID)
                                                 REFERENCES oes.instructors(instructorID)
);

CREATE TABLE oes.subjectStudentMap(
                                      mapID INT GENERATED ALWAYS AS IDENTITY ,
                                      subjectID VARCHAR(200),
                                      studentID VARCHAR(200),
                                      PRIMARY KEY (mapID),
                                      CONSTRAINT foreign_key_subjectID
                                          FOREIGN KEY (subjectID)
                                              REFERENCES oes.subjects(subjectID),
                                      CONSTRAINT foreign_key_studentID
                                          FOREIGN KEY (studentID)
                                              REFERENCES oes.students(studentID)
);

CREATE TABLE oes.exams(
                          examID VARCHAR(200),
                          subjectID VARCHAR(200),
                          examName VARCHAR(140),
                          status VARCHAR(200),
                          PRIMARY KEY (examID),
                          CONSTRAINT foreign_key_subjectID
                              FOREIGN KEY(subjectID)
                                  REFERENCES oes.subjects(subjectID)
);

CREATE TABLE oes.shortAnswerQuestions(
                                         questionID VARCHAR(200),
                                         title VARCHAR(200),
                                         examID  VARCHAR(200),
                                         totalMark INT,
                                         questionBody VARCHAR(10240),
                                         CONSTRAINT foreign_key_examID
                                             FOREIGN KEY(examID)
                                                 REFERENCES oes.exams(examID),
                                         PRIMARY KEY (questionID)
);

CREATE TABLE oes.multipleChoiceQuestion (
                                            questionID VARCHAR(200),
                                            examID  VARCHAR(200),
                                            title VARCHAR(200),
                                            totalMark INT,
                                            questionBody VARCHAR(10240),
                                            CONSTRAINT foreign_key_examID
                                                FOREIGN KEY(examID)
                                                    REFERENCES oes.exams(examID),
                                            PRIMARY KEY (questionID)
);

CREATE TABLE oes.choices (
                             choiceID   VARCHAR(200),
                             questionID VARCHAR(200),
                             index INT,
                             choice VARCHAR(5120),
                             CONSTRAINT foreign_key_questionID
                                 FOREIGN KEY (questionID)
                                     REFERENCES oes.multipleChoiceQuestion (questionID),
                             PRIMARY KEY (choiceID)
);

CREATE TABLE oes.examAnswer(
                               examAnswerID VARCHAR(200),
                               examID VARCHAR(200),
                               studentID VARCHAR(200),
                               finalMark INT,
                               PRIMARY KEY (examAnswerID),
                               CONSTRAINT foreign_key_studentID
                                   FOREIGN KEY (studentID)
                                       REFERENCES  oes.students(studentID),
                               CONSTRAINT foreign_key_examID
                                   FOREIGN KEY (examID)
                                       REFERENCES  oes.exams(examID)
);

CREATE TABLE oes.shortAnswerQuestionAnswers(
                                               shortAnswerQuestionAnswerID VARCHAR(200),
                                               questionID VARCHAR(200),
                                               examAnswerID VARCHAR(200),
                                               mark INT,
                                               answer VARCHAR(1024),
                                               PRIMARY KEY (shortAnswerQuestionAnswerID),
                                               CONSTRAINT foreign_key_questionID
                                                   FOREIGN KEY (questionID)
                                                       REFERENCES  oes.shortAnswerQuestions(questionID),
                                               CONSTRAINT foreign_key_examAnswerID
                                                   FOREIGN KEY (examAnswerID)
                                                       REFERENCES  oes.examAnswer(examAnswerID)
);

CREATE TABLE oes.multipleChoiceQuestionAnswers(
                                                  multipleChoiceQuestionAnswerID VARCHAR(200),
                                                  questionID VARCHAR(200),
                                                  examAnswerID VARCHAR(200),
                                                  mark INT,
                                                  answerIndex INT,
                                                  PRIMARY KEY (multipleChoiceQuestionAnswerID),
                                                  CONSTRAINT foreign_key_questionID
                                                      FOREIGN KEY (questionID)
                                                          REFERENCES  oes.multipleChoiceQuestion(questionID),
                                                  CONSTRAINT foreign_key_examAnswerID
                                                      FOREIGN KEY (examAnswerID)
                                                          REFERENCES  oes.examAnswer(examAnswerID)

);