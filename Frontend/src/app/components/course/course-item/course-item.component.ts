import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import { Course } from '../../../interfaces/Course';
import {faTimes} from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'app-course-item',
  templateUrl: './course-item.component.html',
  styleUrls: ['./course-item.component.css']
})
export class CourseItemComponent implements OnInit {
  // @ts-ignore
  @Input() course: Course;
  @Output() onDeleteTask: EventEmitter<Course> = new EventEmitter<Course>()
  faTimes = faTimes;

  constructor() { }

  ngOnInit(): void {
  }

  onDelete(course: Course){
    this.onDeleteTask.emit(course);
  }
}
