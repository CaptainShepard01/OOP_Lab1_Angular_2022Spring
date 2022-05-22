import { Component, OnInit } from '@angular/core';
import {Teacher} from "../../../interfaces/Teacher";
import {TeacherService} from "../../../services/teacher/teacher.service";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-teachers',
  templateUrl: './teachers.component.html',
  styleUrls: ['./teachers.component.css']
})
export class TeachersComponent implements OnInit {
  teachers: Teacher[] = [];

  roles: string[] = [];

  constructor(private teacherService: TeacherService,
              private keycloakService: KeycloakService) {

  }

  get hasRole(): boolean {
    let requiredRoles = ["ROLE_ADMIN"]
    return requiredRoles.some((role) => this.roles.includes(role));
    // return true;
  }

  ngOnInit(): void {
    this.teacherService.getTeachers().subscribe((teachers) => (this.teachers = teachers));

    this.roles = this.keycloakService.getUserRoles();
  }

  deleteTeacher(teacherId: number | undefined){
    if (teacherId != null) {
      this.teacherService.deleteTeacher(teacherId).subscribe(() => (this.teachers = this.teachers.filter((item) => item.id !== teacherId)));
    }
  }

  addTeacher(teacher: Teacher) {
    this.teacherService.addTeacher(teacher).subscribe((teacher) =>(this.teachers.push(teacher)));
  }
}
