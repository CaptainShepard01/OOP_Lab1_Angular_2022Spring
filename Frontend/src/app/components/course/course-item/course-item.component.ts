import {Component, Input, OnInit} from '@angular/core';
import { Course } from '../../../interfaces/Course';

@Component({
  selector: 'app-course-item',
  templateUrl: './course-item.component.html',
  styleUrls: ['./course-item.component.css']
})
export class CourseItemComponent implements OnInit {
  @Input() course!: Course;

  constructor() { }

  ngOnInit(): void {
  }
}
