import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Student} from "../../../interfaces/Student";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {StudentService} from "../../../services/student/student.service";
import {FieldValidatorService} from "../../../services/utils/field-validator.service";

@Component({
  selector: 'app-add-student',
  templateUrl: './add-student.component.html',
  styleUrls: ['./add-student.component.css']
})
export class AddStudentComponent implements OnInit {
  students: Student[] = [];
  // @ts-ignore
  form: FormGroup;

  // @ts-ignore
  name: string;
  // @ts-ignore
  maxGrade: number;

  @Output() onAddStudent: EventEmitter<Student> = new EventEmitter();

  constructor(private studentsService: StudentService,
              private formBuilder: FormBuilder,
              private fieldValidator: FieldValidatorService) {

  }

  ngOnInit(): void {

    this.studentsService.getStudents().subscribe((students) => (this.students = students));
    this.form = this.formBuilder.group({
      name: ['', [Validators.required]]
    });
    this.fieldValidator.form = this.form;
  }

  onSubmit() {
    if (this.form.valid) {
      const newStudent = {
        name: this.name
      }

      this.onAddStudent.emit(newStudent);

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
