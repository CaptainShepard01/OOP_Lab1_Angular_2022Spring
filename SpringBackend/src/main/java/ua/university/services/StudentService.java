package ua.university.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.university.exceptions.BadRequestException;
import ua.university.modelEntities.Student;
import ua.university.repositories.StudentRepository;
import ua.university.utils.ServerUtils;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class StudentService {
    private final StudentRepository studentRepository;
    private static final String BAD_STUDENT = "Bad student id parameter!";

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudent(String id) {
        int intId = ServerUtils.parseParameterId(id);
        Optional<Student> value = studentRepository.findById(intId);
        if (value.isPresent()) {
            return value.get();
        } else {
            throw new BadRequestException(BAD_STUDENT);
        }
    }

    @Transactional
    public Student updateStudent(String id, Student student) {
        int intId = ServerUtils.parseParameterId(id);
        if (studentRepository.findById(intId).isPresent()) {
            return studentRepository.findById(intId)
                    .map(studentNew -> {
                        studentNew.setName(student.getName());
                        return studentRepository.save(studentNew);
                    })
                    .orElseGet(() -> {
                        student.setId(intId);
                        return studentRepository.save(student);
                    });
        } else {
            throw new BadRequestException(BAD_STUDENT);
        }
    }

    @Transactional
    public void deleteStudent(String id) {
        int intId = ServerUtils.parseParameterId(id);
        if (studentRepository.findById(intId).isPresent()) {
            studentRepository.deleteById(intId);
        } else {
            throw new BadRequestException(BAD_STUDENT);
        }
    }
}
