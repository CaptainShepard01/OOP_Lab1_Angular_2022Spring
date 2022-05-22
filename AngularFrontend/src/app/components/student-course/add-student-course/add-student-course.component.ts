import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {StudentCourse} from "../../../interfaces/StudentCourse";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Course} from "../../../interfaces/Course";
import {FieldValidatorService} from "../../../services/utils/field-validator.service";
import {CourseService} from "../../../services/course/course.service";
import {StudentService} from "../../../services/student/student.service";
import {Student} from "../../../interfaces/Student";
import {KeycloakService} from "keycloak-angular";

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

  roles: string[] = [];

  constructor(private formBuilder: FormBuilder,
              private fieldValidator: FieldValidatorService,
              private studentService: StudentService,
              private courseService: CourseService,
              private keycloakService: KeycloakService) {

  }

  ngOnInit(): void {
    this.roles = this.keycloakService.getUserRoles();

    if(this.hasAdminRole) {
      this.studentService.getStudents().subscribe((students) => (this.students = students));
    }
    else if(this.hasStudentRole){
      // @ts-ignore
      let name: string = this.keycloakService.getKeycloakInstance().profile.firstName;

      this.studentService.getStudentByName(name).subscribe((student) => (this.students.push(student)));
    }

    this.courseService.getCourses().subscribe((courses) => (this.courses = courses));

    this.form = this.formBuilder.group({
      student: ['', [Validators.required]],
      course: ['', [Validators.required]],
      grade: ['', [Validators.pattern("[0-9]+"), Validators.min(1), Validators.max(100)]],
      review: ['', []]
    });

    this.fieldValidator.form = this.form;

  }

  get hasStudentRole(): boolean {
    let requiredRoles = ["ROLE_STUDENT"]
    return requiredRoles.some((role) => this.roles.includes(role));
    // return true;
  }

  get hasAdminRole(): boolean {
    let requiredRoles = ["ROLE_ADMIN"]
    return requiredRoles.some((role) => this.roles.includes(role));
    // return true;
  }

  onSubmit() {
    if (this.form.valid) {
      let newStudentCourse = {
        student: this.student,
        course: this.course,
        grade: this.grade,
        review: this.review
      }

      if (this.hasStudentRole && !this.hasAdminRole){
        newStudentCourse.grade = 0;
        newStudentCourse.review = '';
      }

      console.log(newStudentCourse);

      try {
        // @ts-ignore
        newStudentCourse.student = this.student?._links.student.href;
        // @ts-ignore
        newStudentCourse.course = this.course?._links.course.href;
      } catch (Error) {
        newStudentCourse.student = this.student;
        newStudentCourse.course = this.course;
        console.log("Student-course relation: " + JSON.stringify(newStudentCourse.student)+ JSON.stringify(newStudentCourse.course));
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
