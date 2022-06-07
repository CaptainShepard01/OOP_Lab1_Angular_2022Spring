package ua.university.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.university.exceptions.BadRequestException;
import ua.university.modelEntities.Response;
import ua.university.modelEntities.Student;
import ua.university.services.StudentService;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/students")
public class StudentController {
    private StudentService studentService;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable String id) {
        return studentService.getStudent(id);
    }

    @PostMapping
    public Student addStudents(@RequestBody Student student) {
        return studentService.save(student);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@RequestBody Student student, @PathVariable String id) {
        return studentService.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response> handleException(BadRequestException e) {
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
