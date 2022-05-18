import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Teacher} from "../../../interfaces/Teacher";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {TeacherService} from "../../../services/teacher/teacher.service";
import {FieldValidatorService} from "../../../services/utils/field-validator.service";

@Component({
  selector: 'app-add-teacher',
  templateUrl: './add-teacher.component.html',
  styleUrls: ['./add-teacher.component.css']
})
export class AddTeacherComponent implements OnInit {
  teachers: Teacher[] = [];
  form!: FormGroup;

  name!: string;

  @Output() onAddTeacher: EventEmitter<Teacher> = new EventEmitter();

  constructor(private teachersService: TeacherService,
              private formBuilder: FormBuilder,
              private fieldValidator: FieldValidatorService) {

  }

  ngOnInit(): void {

    this.teachersService.getTeachers().subscribe((teachers) => (this.teachers = teachers));
    this.form = this.formBuilder.group({
      name: ['', [Validators.required]]
    });
    this.fieldValidator.form = this.form;
  }

  onSubmit() {
    if (this.form.valid) {
      const newTeacher = {
        name: this.name
      }

      this.onAddTeacher.emit(newTeacher);

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
