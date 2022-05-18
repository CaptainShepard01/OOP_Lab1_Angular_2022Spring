import { Component, OnInit } from '@angular/core';
import {StudentCourseService} from "../../../services/student-course/student-course.service";
import {StudentCourse} from "../../../interfaces/StudentCourse";

@Component({
  selector: 'app-student-courses',
  templateUrl: './student-courses.component.html',
  styleUrls: ['./student-courses.component.css']
})
export class StudentCoursesComponent implements OnInit {
  studentCourses: StudentCourse[] = [];

  constructor(private studentCourseService: StudentCourseService) {

  }

  ngOnInit(): void {
    this.studentCourseService.getStudentCourses().subscribe((studentCourses) => (this.studentCourses = studentCourses));
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
