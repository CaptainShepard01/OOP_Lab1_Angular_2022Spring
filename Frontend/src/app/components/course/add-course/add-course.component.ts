import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Teacher} from "../../../interfaces/Teacher";
import {Course} from "../../../interfaces/Course";
import {TeacherService} from "../../../services/teacher.service";

@Component({
  selector: 'app-add-course',
  templateUrl: './add-course.component.html',
  styleUrls: ['./add-course.component.css']
})
export class AddCourseComponent implements OnInit {
  teachers: Teacher[] = [];

  // @ts-ignore
  name: string;
  // @ts-ignore
  maxGrade: number;
  // @ts-ignore
  teacherId: number;

  @Output() onAddCourse: EventEmitter<Course> = new EventEmitter();

  constructor(private teacherService: TeacherService) {

  }

  ngOnInit(): void {
    this.teacherService.getTeachers().subscribe((teachers) => (this.teachers = teachers));
  }

  onSubmit() {
    const newCourse = {
      name: this.name,
      maxGrade: this.maxGrade,
      teacher: {
        id: this.teacherId,
        name: ''
      }
    }

    this.onAddCourse.emit(newCourse);

    this.name = '';
    this.maxGrade = 0;
    this.teacherId = 0;
  }
}
