package ua.university.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.university.exceptions.BadRequestException;
import ua.university.modelEntities.StudentCourseRelation;
import ua.university.repositories.StudentCourseRelationRepository;
import ua.university.utils.ServerUtils;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class StudentCourseRelationService {
    private final StudentCourseRelationRepository studentCourseRelationRepository;
    private static final String BAD_TEACHER = "Bad studentCourseRelation id parameter!";

    public List<StudentCourseRelation> getAllStudentCourseRelations() {
        return studentCourseRelationRepository.findAll();
    }

    public StudentCourseRelation save(StudentCourseRelation studentCourseRelation) {
        return studentCourseRelationRepository.save(studentCourseRelation);
    }

    public StudentCourseRelation getStudentCourseRelation(String id) {
        int intId = ServerUtils.parseParameterId(id);
        Optional<StudentCourseRelation> value = studentCourseRelationRepository.findById(intId);
        if (value.isPresent()) {
            return value.get();
        } else {
            throw new BadRequestException(BAD_TEACHER);
        }
    }

    @Transactional
    public List<StudentCourseRelation> getRelationsForTeacher(String teacherName){
        return studentCourseRelationRepository.findByTeacherNameContaining(teacherName);
    }

    @Transactional
    public List<StudentCourseRelation> getRelationsForStudent(String studentName){
        return studentCourseRelationRepository.findByStudentNameContaining(studentName);
    }

    @Transactional
    public StudentCourseRelation updateStudentCourseRelation(String id, StudentCourseRelation studentCourseRelation) {
        int intId = ServerUtils.parseParameterId(id);
        if (studentCourseRelationRepository.findById(intId).isPresent()) {
            return studentCourseRelationRepository.findById(intId)
                    .map(studentCourseRelationNew -> {
                        studentCourseRelationNew.setCourse(studentCourseRelation.getCourse());
                        studentCourseRelationNew.setStudent(studentCourseRelation.getStudent());
                        studentCourseRelationNew.setGrade(studentCourseRelation.getGrade());
                        studentCourseRelationNew.setReview(studentCourseRelation.getReview());
                        return studentCourseRelationRepository.save(studentCourseRelationNew);
                    })
                    .orElseGet(() -> {
                        studentCourseRelation.setId(intId);
                        return studentCourseRelationRepository.save(studentCourseRelation);
                    });
        } else {
            throw new BadRequestException(BAD_TEACHER);
        }
    }

    @Transactional
    public void deleteStudentCourseRelation(String id) {
        int intId = ServerUtils.parseParameterId(id);
        if (studentCourseRelationRepository.findById(intId).isPresent()) {
            studentCourseRelationRepository.deleteById(intId);
        } else {
            throw new BadRequestException(BAD_TEACHER);
        }
    }
}
