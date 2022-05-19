import {Component, Input, OnInit} from '@angular/core';
import { Course } from '../../../interfaces/Course';
import {HttpClient} from "@angular/common/http";
import {Teacher} from "../../../interfaces/Teacher";

@Component({
  selector: 'app-course-item',
  templateUrl: './course-item.component.html',
  styleUrls: ['./course-item.component.css']
})
export class CourseItemComponent implements OnInit {
  @Input() course!: Course;

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    try{
      // @ts-ignore
      let link: string = this.course?._links.teacher.href;
      this.http.get<Teacher>(link).subscribe((teacher) => (this.course.teacher = teacher));
    }
    catch (Error){
      console.log(Error)
    }
  }
}
