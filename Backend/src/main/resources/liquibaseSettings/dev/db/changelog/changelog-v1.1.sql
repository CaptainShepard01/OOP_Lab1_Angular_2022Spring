--liquibase formatted sql

--changeset balanton:1

INSERT into teachers (name) VALUES('Teacher 1');
INSERT into teachers (name) VALUES('Teacher 2');
INSERT into teachers (name) VALUES('Teacher 3.');

INSERT into students (name) VALUES('Student 1');
INSERT into students (name) VALUES('Student 2');
INSERT into students (name) VALUES('Student 3');
INSERT into students (name) VALUES('Student 4');

INSERT into courses (name, max_grade, teacher_id) VALUES('Course 1', 10, 1);
INSERT into courses (name, max_grade, teacher_id) VALUES('Course 2', 20, 2);
INSERT into courses (name, max_grade, teacher_id) VALUES('Course 3', 30, 2);
INSERT into courses (name, max_grade, teacher_id) VALUES('Course 4', 40, 3);

INSERT into student_course_relations(student_id, course_id, grade, review) VALUES(1,1,7,'Wonderful!');
INSERT into student_course_relations(student_id, course_id, grade, review) VALUES(1,2,17,'Good!');
INSERT into student_course_relations(student_id, course_id, grade, review) VALUES(2,1,8,'Amazing!');
INSERT into student_course_relations(student_id, course_id, grade, review) VALUES(3,1,9,'Finally!');
INSERT into student_course_relations(student_id, course_id, grade, review) VALUES(4,2,17,'Nice work!');
INSERT into student_course_relations(student_id, course_id, grade, review) VALUES(4,3,26,'Cryptolog!');
