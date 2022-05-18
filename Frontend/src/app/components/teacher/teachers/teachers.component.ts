import { Component, OnInit } from '@angular/core';
import {Teacher} from "../../../interfaces/Teacher";
import {TeacherService} from "../../../services/teacher/teacher.service";

@Component({
  selector: 'app-teachers',
  templateUrl: './teachers.component.html',
  styleUrls: ['./teachers.component.css']
})
export class TeachersComponent implements OnInit {
  teachers: Teacher[] = [];

  constructor(private teacherService: TeacherService) {

  }

  ngOnInit(): void {
    this.teacherService.getTeachers().subscribe((teachers) => (this.teachers = teachers));
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
