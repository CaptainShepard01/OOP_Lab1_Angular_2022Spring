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
  private formSubmitAttempt: boolean;

  // @ts-ignore
  name: string;
  // @ts-ignore
  maxGrade: number;
  // @ts-ignore
  teacherId: number;

  @Output() onAddCourse: EventEmitter<Course> = new EventEmitter();

  constructor(private teacherService: TeacherService, private formBuilder: FormBuilder, private fieldValidator: FieldValidatorService) {

  }

  ngOnInit(): void {

    this.teacherService.getTeachers().subscribe((teachers) => (this.teachers = teachers));
    this.form = this.formBuilder.group({
      name: [null, [Validators.required]],
      maxGrade: [null, [Validators.required, Validators.pattern("[0-9]+"), Validators.min(1), Validators.max(100)]],
      teacherId: [null, [Validators.required]]
    });
    this.fieldValidator.form = this.form;
  }

  onSubmit() {
    this.formSubmitAttempt = true;
    if (this.form.valid) {
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
      this.fieldValidator.reset();
    }
  }

  isFieldValid(field: string) {
    return this.fieldValidator.isFieldValid(field);
  }

  displayFieldCss(field: string) {
    return this.fieldValidator.displayFieldCss(field);
  }

  reset() {
    this.fieldValidator.reset();
  }
}
