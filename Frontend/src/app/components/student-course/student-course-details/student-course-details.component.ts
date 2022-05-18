import { Component, OnInit } from '@angular/core';
import {Course} from "../../../interfaces/Course";
import {Student} from "../../../interfaces/Student";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {FieldValidatorService} from "../../../services/utils/field-validator.service";
import {faTimes} from '@fortawesome/free-solid-svg-icons';
import {StudentCourseService} from "../../../services/student-course/student-course.service";
import {StudentService} from "../../../services/student/student.service";
import {CourseService} from "../../../services/course/course.service";
import {StudentCourse} from "../../../interfaces/StudentCourse";

@Component({
  selector: 'app-student-course-details',
  templateUrl: './student-course-details.component.html',
  styleUrls: ['./student-course-details.component.css']
})
export class StudentCourseDetailsComponent implements OnInit {
  studentCourse!: StudentCourse;
  students!: Student[];
  courses!: Course[];
  form!: FormGroup;

  student!: Student;
  course!: Course;
  grade!: number;
  review!: string;

  faTimes = faTimes;

  constructor(private studentCourseService: StudentCourseService,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private studentService: StudentService,
              private courseService: CourseService,
              private router: Router,
              private fieldValidator: FieldValidatorService) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(()=>{
      this.showStudentCourse();
    });

    this.studentService.getStudents().subscribe((students) => (this.students = students));
    this.courseService.getCourses().subscribe((courses) => (this.courses = courses));

    this.form = this.formBuilder.group({
      student: ['', []],
      course: ['', []],
      grade: ['', [Validators.pattern("[0-9]+"), Validators.min(1), Validators.max(100)]],
      review: ['', []]
    });

    this.fieldValidator.form = this.form;

    this.showStudentCourse();
  }

  onDelete(studentCourseId: number | undefined){
    if (studentCourseId != null) {
      this.studentCourseService.deleteStudentCourse(studentCourseId).subscribe({
          next: response => {
            console.log(`Response from deleting: ${response}`);
          },
          error: err => {
            console.log(`There was an error: ${err.message}`);
          },
          complete: () => {
            console.log('Done delete the student-course relation');
            this.router.navigate(['studentsCourses']);
          }
        }
      );
    }
  }

  showStudentCourse() {
    // @ts-ignore
    const studentCourseId: number = +this.route.snapshot.paramMap.get('id');
    this.studentCourseService.getStudentCourse(studentCourseId).subscribe( data => {
      this.studentCourse = data;
      // @ts-ignore
      console.log("Course: " + (data.id))
    });
  }

  onUpdate() {
    if (this.form.valid) {
      const newStudentCourse = {
        student: this.student,
        course: this.course,
        grade: this.grade,
        review: this.review
      }

      this.studentCourseService.updateStudentCourse(newStudentCourse).subscribe({
          next: response => {
            console.log(`Response from updating: ${response}`);
            this.studentCourse = response;
          },
          error: err => {
            console.log(`There was an error: ${err.message}`);
          },
          complete: () => {
            console.log('Done update the student-course relation');
          }
        }
      );

      this.fieldValidator.reset();
    }
  }

  isFieldValid(field: string) {
    // @ts-ignore
    return this.fieldValidator.isFieldValid(field);
  }

  reset() {
    this.fieldValidator.reset();
  }
}
