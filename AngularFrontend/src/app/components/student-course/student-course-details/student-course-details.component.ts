import {Component, OnInit} from '@angular/core';
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
import {KeycloakService} from "keycloak-angular";
import {HttpClient} from "@angular/common/http";

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

  roles: string[] = [];

  constructor(private studentCourseService: StudentCourseService,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private studentService: StudentService,
              private courseService: CourseService,
              private router: Router,
              private fieldValidator: FieldValidatorService,
              private keycloakService: KeycloakService,
              private http: HttpClient) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(() => {
      this.showStudentCourse();
    });

    this.studentService.getStudents().subscribe((students) => (this.students = students));
    this.courseService.getCourses().subscribe((courses) => (this.courses = courses));

    this.form = this.formBuilder.group({
      student: ['', []],
      course: ['', []],
      grade: ['', [Validators.required, Validators.pattern("[0-9]+"), Validators.min(1), Validators.max(100)]],
      review: ['', [Validators.required]]
    });

    this.fieldValidator.form = this.form;

    this.showStudentCourse();

    this.roles = this.keycloakService.getUserRoles();
  }

  get hasTeacherRole(): boolean {
    let requiredRoles = ["ROLE_TEACHER"]
    return requiredRoles.some((role) => this.roles.includes(role));
    // return true;
  }

  get hasAdminRole(): boolean {
    let requiredRoles = ["ROLE_ADMIN"]
    return requiredRoles.some((role) => this.roles.includes(role));
    // return true;
  }

  onDelete(studentCourseId: number | undefined) {
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
    this.studentCourseService.getStudentCourse(studentCourseId).subscribe(data => {
      this.studentCourse = data;
      try {
        // @ts-ignore
        let student_link: string = this.studentCourse?._links.student.href;
        this.http.get<Student>(student_link).subscribe((student) => (this.studentCourse.student = student));

        // @ts-ignore
        let course_link: string = this.studentCourse?._links.course.href;
        this.http.get<Course>(course_link).subscribe((course) => (this.studentCourse.course = course));

      } catch (Error) {
        console.log(Error);
      }

      // @ts-ignore
      console.log("Student-course relation: " + (data.id))
    });
  }

  onUpdate() {
    if (this.form.valid) {
      let newStudentCourse = {
        student: this.student,
        course: this.course,
        grade: this.grade,
        review: this.review
      }

      if (this.hasTeacherRole && !this.hasAdminRole) {
        newStudentCourse.student = this.studentCourse.student;
        newStudentCourse.course = this.studentCourse.course;
      }

      try {
        // @ts-ignore
        newStudentCourse.student = this.student?._links.student.href;
        // @ts-ignore
        newStudentCourse.course = this.course?._links.course.href;
      } catch (Error) {
        newStudentCourse.student = this.student;
        newStudentCourse.course = this.course;
        console.log("Student-course relation: " + JSON.stringify(newStudentCourse.student) + JSON.stringify(newStudentCourse.course));
      }


      this.studentCourseService.updateStudentCourse(this.studentCourse.id, newStudentCourse).subscribe({
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
