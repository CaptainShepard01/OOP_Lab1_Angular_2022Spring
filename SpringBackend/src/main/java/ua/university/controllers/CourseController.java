package ua.university.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.university.exceptions.BadRequestException;
import ua.university.modelEntities.Course;
import ua.university.modelEntities.Response;
import ua.university.services.CourseService;

import java.util.List;

@AllArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private CourseService courseService;

    @GetMapping
    public List<Course> getAllCourses(){
        return this.courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public Course getCourse(@PathVariable String id){
        return this.courseService.getCourse(id);
    }

    @PostMapping
    public Course addCourse(@RequestBody Course course){
        return this.courseService.save(course);
    }

    @PutMapping("/{id}")
    public Course updateCourse(@RequestBody Course course, @PathVariable String id){
        return this.courseService.updateCourse(id, course);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable String id){
        this.courseService.deleteCourse(id);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response> handleException(BadRequestException e){
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
