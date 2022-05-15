import {Component, OnInit} from '@angular/core';
import {Course} from "../../../interfaces/Course";
import {CourseService} from "../../../services/course.service";

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.css']
})
export class CoursesComponent implements OnInit {
  courses: Course[] = [];

  constructor(private courseService: CourseService) {

  }

  ngOnInit(): void {
    this.courseService.getCourses().subscribe((courses) => (this.courses = courses));
  }

  deleteCourse(course: Course){
    this.courseService.deleteCourse(course).subscribe(() => (this.courses=this.courses.filter((item) => item.id !== course.id)));
  }

  addCourse(course: Course) {
    this.courseService.addCourse(course).subscribe((course) =>(this.courses.push(course)));
  }
}
