package ua.university.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.university.exceptions.BadRequestException;
import ua.university.modelEntities.Response;
import ua.university.modelEntities.StudentCourseRelation;
import ua.university.services.StudentCourseRelationService;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/studentCourseRelations")

public class StudentCourseRelationController {
    private StudentCourseRelationService studentCourseRelationService;

    @GetMapping
    public List<StudentCourseRelation> getAllStudentCourseRelations() {
        return studentCourseRelationService.getAllStudentCourseRelations();
    }

    @GetMapping("/{id}")
    public StudentCourseRelation getStudentCourseRelation(@PathVariable String id) {
        return studentCourseRelationService.getStudentCourseRelation(id);
    }

    @GetMapping("/search/findByStudentNameContaining")
    public List<StudentCourseRelation> getRelationsForStudent(@RequestParam("name") String studentName){
        return studentCourseRelationService.getRelationsForStudent(studentName);
    }

    @GetMapping("/search/findByTeacherNameContaining")
    public List<StudentCourseRelation> getRelationsForTeacher(@RequestParam("name") String teacherName){
        return studentCourseRelationService.getRelationsForTeacher(teacherName);
    }

    @PostMapping
    public StudentCourseRelation addStudentCourseRelations(@RequestBody StudentCourseRelation studentCourseRelation) {
        return studentCourseRelationService.save(studentCourseRelation);
    }

    @PutMapping("/{id}")
    public StudentCourseRelation updateStudentCourseRelation(@RequestBody StudentCourseRelation studentCourseRelation, @PathVariable String id) {
        return studentCourseRelationService.updateStudentCourseRelation(id, studentCourseRelation);
    }

    @DeleteMapping("/{id}")
    public void deleteStudentCourseRelation(@PathVariable String id) {
        studentCourseRelationService.deleteStudentCourseRelation(id);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response> handleException(BadRequestException e) {
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
