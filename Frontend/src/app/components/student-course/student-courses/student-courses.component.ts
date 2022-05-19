import { Component, OnInit } from '@angular/core';
import {StudentCourseService} from "../../../services/student-course/student-course.service";
import {StudentCourse} from "../../../interfaces/StudentCourse";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-student-courses',
  templateUrl: './student-courses.component.html',
  styleUrls: ['./student-courses.component.css']
})
export class StudentCoursesComponent implements OnInit {
  studentCourses: StudentCourse[] = [];

  roles: string[] = [];

  constructor(private studentCourseService: StudentCourseService,
              private keycloakService: KeycloakService) {

  }

  get hasRole(): boolean {
    let requiredRoles = ["ROLE_ADMIN", "ROLE_STUDENT"]
    return requiredRoles.some((role) => this.roles.includes(role));
    // return true;
  }

  ngOnInit(): void {
    this.roles = this.keycloakService.getUserRoles();
    // @ts-ignore
    let name: string = this.keycloakService.getKeycloakInstance().profile.firstName;

    if(this.roles.includes("ROLE_ADMIN")){
      this.studentCourseService.getStudentCourses().subscribe((studentCourses) => (this.studentCourses = studentCourses));
    }
    else if(this.roles.includes("ROLE_STUDENT")){
      this.studentCourseService.getStudentCoursesForStudent(name).subscribe((studentCourses) => (this.studentCourses = studentCourses));
    }
    else if(this.roles.includes("ROLE_TEACHER")) {
      this.studentCourseService.getStudentCoursesForTeacher(name).subscribe((studentCourses) => (this.studentCourses = studentCourses));
    }
  }

  deleteStudentCourse(tudentCourseId: number | undefined){
    if (tudentCourseId != null) {
      this.studentCourseService.deleteStudentCourse(tudentCourseId).subscribe(() => (this.studentCourses = this.studentCourses.filter((item) => item.id !== tudentCourseId)));
    }
  }

  addStudentCourse(studentCourse: StudentCourse) {
    this.studentCourseService.addStudentCourse(studentCourse).subscribe((studentCourse) =>(this.studentCourses.push(studentCourse)));
  }
}
