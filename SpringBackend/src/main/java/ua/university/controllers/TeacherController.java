package ua.university.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.university.exceptions.BadRequestException;
import ua.university.modelEntities.Response;
import ua.university.modelEntities.Teacher;
import ua.university.services.TeacherService;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/teachers")
public class TeacherController {
    private TeacherService teacherService;

    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{id}")
    public Teacher getTeacher(@PathVariable String id) {
        return teacherService.getTeacher(id);
    }

    @PostMapping
    public Teacher addTeachers(@RequestBody Teacher teacher) {
        return teacherService.save(teacher);
    }

    @PutMapping("/{id}")
    public Teacher updateTeacher(@RequestBody Teacher teacher, @PathVariable String id) {
        return teacherService.updateTeacher(id, teacher);
    }

    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable String id) {
        teacherService.deleteTeacher(id);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response> handleException(BadRequestException e) {
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
