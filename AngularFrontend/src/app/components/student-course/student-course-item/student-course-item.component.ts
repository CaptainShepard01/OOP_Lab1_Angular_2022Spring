import {Component, Input, OnInit} from '@angular/core';
import {StudentCourse} from "../../../interfaces/StudentCourse";
import {HttpClient} from "@angular/common/http";
import {Teacher} from "../../../interfaces/Teacher";
import {Student} from "../../../interfaces/Student";
import {Course} from "../../../interfaces/Course";

@Component({
  selector: 'app-student-course-item',
  templateUrl: './student-course-item.component.html',
  styleUrls: ['./student-course-item.component.css']
})
export class StudentCourseItemComponent implements OnInit {
  @Input() studentCourse!: StudentCourse;

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    try{
      // @ts-ignore
      let student_link: string = this.studentCourse?._links.student.href;
      this.http.get<Student>(student_link).subscribe((student) => (this.studentCourse.student = student));

      // @ts-ignore
      let course_link: string = this.studentCourse?._links.course.href;
      this.http.get<Course>(course_link).subscribe((course) => (this.studentCourse.course = course));
    }
    catch (Error){
      console.log(Error)
    }
  }

}
