import { Component, OnInit } from '@angular/core';
import {Student} from "../../../interfaces/Student";
import {StudentService} from "../../../services/student/student.service";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-students',
  templateUrl: './students.component.html',
  styleUrls: ['./students.component.css']
})
export class StudentsComponent implements OnInit {
  students: Student[] = [];

  roles: string[] = [];

  constructor(private studentService: StudentService,
              private keycloakService: KeycloakService) {

  }

  get hasRole(): boolean {
    let requiredRoles = ["ROLE_ADMIN"]
    return requiredRoles.some((role) => this.roles.includes(role));
    // return true;
  }

  ngOnInit(): void {
    this.studentService.getStudents().subscribe((students) => (this.students = students));

    this.roles = this.keycloakService.getUserRoles();
  }

  deleteStudent(studentId: number | undefined){
    if (studentId != null) {
      this.studentService.deleteStudent(studentId).subscribe(() => (this.students = this.students.filter((item) => item.id !== studentId)));
    }
  }

  addStudent(student: Student) {
    this.studentService.addStudent(student).subscribe((student) =>(this.students.push(student)));
  }
}
