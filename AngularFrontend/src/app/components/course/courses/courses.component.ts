import {Component, OnInit} from '@angular/core';
import {Course} from "../../../interfaces/Course";
import {CourseService} from "../../../services/course/course.service";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.css']
})
export class CoursesComponent implements OnInit {
  courses: Course[] = [];

  roles: string[] = [];

  constructor(private courseService: CourseService,
              private keycloakService: KeycloakService) {

  }

  get hasRole(): boolean {
    let requiredRoles = ["ROLE_ADMIN"]
    return requiredRoles.some((role) => this.roles.includes(role));
    // return true;
  }


  ngOnInit(): void {
    this.courseService.getCourses().subscribe((courses) => (this.courses = courses));

    this.roles = this.keycloakService.getUserRoles();
  }

  deleteCourse(courseId: number | undefined){
    if (courseId != null) {
      this.courseService.deleteCourse(courseId).subscribe(() => (this.courses = this.courses.filter((item) => item.id !== courseId)));
    }
  }

  addCourse(course: Course) {
    this.courseService.addCourse(course).subscribe((course) =>(this.courses.push(course)));
  }
}
