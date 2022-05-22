import {Component, Input, OnInit} from '@angular/core';
import {Teacher} from "../../../interfaces/Teacher";

@Component({
  selector: 'app-teacher-item',
  templateUrl: './teacher-item.component.html',
  styleUrls: ['./teacher-item.component.css']
})
export class TeacherItemComponent implements OnInit {
  @Input() teacher!: Teacher

  constructor() { }

  ngOnInit(): void {
  }

}
