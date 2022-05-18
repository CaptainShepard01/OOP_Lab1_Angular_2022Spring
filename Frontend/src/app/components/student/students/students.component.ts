import { Component, OnInit } from '@angular/core';
import {Student} from "../../../interfaces/Student";
import {StudentService} from "../../../services/student/student.service";

@Component({
  selector: 'app-students',
  templateUrl: './students.component.html',
  styleUrls: ['./students.component.css']
})
export class StudentsComponent implements OnInit {
  students: Student[] = [];

  constructor(private studentService: StudentService) {

  }

  ngOnInit(): void {
    this.studentService.getStudents().subscribe((students) => (this.students = students));
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
