import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Teacher} from "../../../interfaces/Teacher";
import {Course} from "../../../interfaces/Course";
import {TeacherService} from "../../../services/teacher/teacher.service";
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {FieldValidatorService} from "../../../services/utils/field-validator.service";

@Component({
  selector: 'app-add-course',
  templateUrl: './add-course.component.html',
  styleUrls: ['./add-course.component.css']
})
export class AddCourseComponent implements OnInit {
  teachers: Teacher[] = [];
  // @ts-ignore
  form: FormGroup;

  // @ts-ignore
  name: string;
  // @ts-ignore
  maxGrade: number;
  // @ts-ignore
  teacher: Teacher;

  @Output() onAddCourse: EventEmitter<Course> = new EventEmitter();

  constructor(private teacherService: TeacherService,
              private formBuilder: FormBuilder,
              private fieldValidator: FieldValidatorService) {

  }

  ngOnInit(): void {

    this.teacherService.getTeachers().subscribe((teachers) => (this.teachers = teachers));
    this.form = this.formBuilder.group({
      name: ['', [Validators.required]],
      maxGrade: ['', [Validators.required, Validators.pattern("[0-9]+"), Validators.min(1), Validators.max(100)]],
      teacher: ['', [Validators.required]]
    });
    this.fieldValidator.form = this.form;
  }

  onSubmit() {
    if (this.form.valid) {
      let newCourse = {
        name: this.name,
        maxGrade: this.maxGrade,
        teacher: this.teacher
      }
      // try {
      //   // @ts-ignore
      //   newCourse.teacher = this.teacher?._links.teacher.href;
      // } catch (Error) {
      //   newCourse.teacher = this.teacher;
      //   console.log("Teacher: " + JSON.stringify(newCourse.teacher));
      // }

      this.onAddCourse.emit(newCourse);

      this.fieldValidator.reset();
    }
  }

  isFieldValid(field: string) {
    // @ts-ignore
    return this.fieldValidator.isFieldValid(field);
  }

  reset() {
    this.fieldValidator.reset();
  }
}
