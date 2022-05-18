import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {StudentCourse} from "../../../interfaces/StudentCourse";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Course} from "../../../interfaces/Course";
import {FieldValidatorService} from "../../../services/utils/field-validator.service";
import {CourseService} from "../../../services/course/course.service";
import {StudentService} from "../../../services/student/student.service";
import {Student} from "../../../interfaces/Student";

@Component({
  selector: 'app-add-student-course',
  templateUrl: './add-student-course.component.html',
  styleUrls: ['./add-student-course.component.css']
})
export class AddStudentCourseComponent implements OnInit {
  students: Student[] = [];
  courses: Course[] = [];
  form!: FormGroup;

  student!: Student
  course!: Course;
  grade!: number;
  review!: string;

  @Output() onAddStudentCourse: EventEmitter<StudentCourse> = new EventEmitter();

  constructor(private formBuilder: FormBuilder,
              private fieldValidator: FieldValidatorService,
              private studentService: StudentService,
              private courseService: CourseService) {

  }

  ngOnInit(): void {

    this.studentService.getStudents().subscribe((students) => (this.students = students));
    this.courseService.getCourses().subscribe((courses) => (this.courses = courses));

    this.form = this.formBuilder.group({
      student: ['', [Validators.required]],
      course: ['', [Validators.required]],
      grade: ['', [Validators.required, Validators.pattern("[0-9]+"), Validators.min(1), Validators.max(100)]],
      review: ['', [Validators.required]]
    });

    this.fieldValidator.form = this.form;
  }

  onSubmit() {
    if (this.form.valid) {
      const newStudentCourse = {
        student: this.student,
        course: this.course,
        grade: this.grade,
        review: this.review
      }

      this.onAddStudentCourse.emit(newStudentCourse);

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
