package ua.university.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.university.exceptions.BadRequestException;
import ua.university.modelEntities.Teacher;
import ua.university.repositories.TeacherRepository;
import ua.university.utils.ServerUtils;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private static final String BAD_TEACHER = "Bad teacher id parameter!";

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public Teacher getTeacher(String id) {
        int intId = ServerUtils.parseParameterId(id);
        Optional<Teacher> value = teacherRepository.findById(intId);
        if (value.isPresent()) {
            return value.get();
        } else {
            throw new BadRequestException(BAD_TEACHER);
        }
    }

    @Transactional
    public Teacher updateTeacher(String id, Teacher teacher) {
        int intId = ServerUtils.parseParameterId(id);
        if (teacherRepository.findById(intId).isPresent()) {
            return teacherRepository.findById(intId)
                    .map(teacherNew -> {
                        teacherNew.setName(teacher.getName());
                        return teacherRepository.save(teacherNew);
                    })
                    .orElseGet(() -> {
                        teacher.setId(intId);
                        return teacherRepository.save(teacher);
                    });
        } else {
            throw new BadRequestException(BAD_TEACHER);
        }
    }

    @Transactional
    public void deleteTeacher(String id) {
        int intId = ServerUtils.parseParameterId(id);
        if (teacherRepository.findById(intId).isPresent()) {
            teacherRepository.deleteById(intId);
        } else {
            throw new BadRequestException(BAD_TEACHER);
        }
    }
}
