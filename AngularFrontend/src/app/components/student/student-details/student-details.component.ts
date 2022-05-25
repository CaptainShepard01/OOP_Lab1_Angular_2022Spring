import { Component, OnInit } from '@angular/core';
import {Student} from "../../../interfaces/Student";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {StudentService} from "../../../services/student/student.service";
import {ActivatedRoute, Router} from "@angular/router";
import {faTimes} from '@fortawesome/free-solid-svg-icons'
import {FieldValidatorService} from "../../../services/utils/field-validator.service";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-student-details',
  templateUrl: './student-details.component.html',
  styleUrls: ['./student-details.component.css']
})
export class StudentDetailsComponent implements OnInit {
  student!: Student;
  form!: FormGroup;

  name!: string;
  maxGrade!: number;

  roles: string[] = [];

  faTimes = faTimes;

  constructor(private studentService: StudentService,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private fieldValidator: FieldValidatorService,
              private keycloakService: KeycloakService) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(()=>{
      this.showStudent();
    });

    this.form = this.formBuilder.group({
      name: ['', [Validators.required]]
    });
    this.fieldValidator.form = this.form;

    this.showStudent();
    this.roles = this.keycloakService.getUserRoles();
  }

  get hasRole(): boolean {
    let requiredRoles = ["ROLE_ADMIN"]
    return requiredRoles.some((role) => this.roles.includes(role));
    // return true;
  }

  onDelete(studentId: number | undefined){
    if (studentId != null) {
      this.studentService.deleteStudent(studentId).subscribe({
          next: response => {
            console.log(`Response from deleting: ${response}`);
          },
          error: err => {
            console.log(`There was an error: ${err.message}`);
          },
          complete: () => {
            console.log('Done delete the student');
            this.router.navigate(['students']);
          }
        }
      );
    }
  }

  showStudent() {
    // @ts-ignore
    const studentId: number = +this.route.snapshot.paramMap.get('id');
    this.studentService.getStudent(studentId).subscribe( data => {
      this.student = data;
      // @ts-ignore
      console.log("Student: " + (data.id))
    });
  }

  onUpdate() {
    if (this.form.valid) {
      const newStudent = {
        id: this.student.id,
        name: this.name
      }

      this.studentService.updateStudent(newStudent).subscribe({
          next: response => {
            console.log(`Response from updating: ${response}`);
            this.student = response;
          },
          error: err => {
            console.log(`There was an error: ${err.message}`);
          },
          complete: () => {
            console.log('Done update the student');
          }
        }
      );

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
