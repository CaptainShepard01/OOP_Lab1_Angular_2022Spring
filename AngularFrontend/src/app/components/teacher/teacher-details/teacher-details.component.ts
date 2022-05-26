import { Component, OnInit } from '@angular/core';
import {Teacher} from "../../../interfaces/Teacher";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {TeacherService} from "../../../services/teacher/teacher.service";
import {ActivatedRoute, Router} from "@angular/router";
import {faTimes} from '@fortawesome/free-solid-svg-icons'
import {FieldValidatorService} from "../../../services/utils/field-validator.service";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-teacher-details',
  templateUrl: './teacher-details.component.html',
  styleUrls: ['./teacher-details.component.css']
})
export class TeacherDetailsComponent implements OnInit {
  teacher!: Teacher;
  form!: FormGroup;

  name!: string;

  roles: string[] = [];

  faTimes = faTimes;

  constructor(private teacherService: TeacherService,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private fieldValidator: FieldValidatorService,
              private keycloakService: KeycloakService) {
  }

  get hasRole(): boolean {
    let requiredRoles = ["ROLE_ADMIN"]
    return requiredRoles.some((role) => this.roles.includes(role));
    // return true;
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(()=>{
      this.showTeacher();
    });

    this.form = this.formBuilder.group({
      name: ['', [Validators.required]]
    });
    this.fieldValidator.form = this.form;

    this.showTeacher();
    this.roles = this.keycloakService.getUserRoles();
  }

  onDelete(teacherId: number | undefined){
    if (teacherId != null) {
      this.teacherService.deleteTeacher(teacherId).subscribe({
          next: response => {
            console.log(`Response from deleting: ${response}`);
          },
          error: err => {
            console.log(`There was an error: ${err.message}`);
          },
          complete: () => {
            console.log('Done delete the teacher');
            this.router.navigate(['teachers']);
          }
        }
      );
    }
  }

  showTeacher() {
    // @ts-ignore
    const teacherId: number = +this.route.snapshot.paramMap.get('id');
    this.teacherService.getTeacher(teacherId).subscribe( data => {
      this.teacher = data;
      // @ts-ignore
      console.log("Teacher: " + (data.id))
    });
  }

  onUpdate() {
    if (this.form.valid) {
      const newTeacher = {
        name: this.name
      }

      this.teacherService.updateTeacher(this.teacher.id, newTeacher).subscribe({
          next: response => {
            console.log(`Response from updating: ${response}`);
            this.teacher = response;
          },
          error: err => {
            console.log(`There was an error: ${err.message}`);
          },
          complete: () => {
            console.log('Done update the teacher');
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
