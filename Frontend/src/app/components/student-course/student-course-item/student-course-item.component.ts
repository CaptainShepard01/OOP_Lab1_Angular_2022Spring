import {Component, Input, OnInit} from '@angular/core';
import {StudentCourse} from "../../../interfaces/StudentCourse";

@Component({
  selector: 'app-student-course-item',
  templateUrl: './student-course-item.component.html',
  styleUrls: ['./student-course-item.component.css']
})
export class StudentCourseItemComponent implements OnInit {
  @Input() studentCourse!: StudentCourse;

  constructor() { }

  ngOnInit(): void {
  }

}
