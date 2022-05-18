import {Component, OnInit} from '@angular/core';
import {Course} from "../../../interfaces/Course";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Teacher} from "../../../interfaces/Teacher";
import {CourseService} from "../../../services/course/course.service";
import {ActivatedRoute, Router} from "@angular/router";
import {TeacherService} from "../../../services/teacher/teacher.service";
import {faTimes} from '@fortawesome/free-solid-svg-icons'
import {FieldValidatorService} from "../../../services/utils/field-validator.service";

@Component({
  selector: 'app-course-details',
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.css']
})
export class CourseDetailsComponent implements OnInit {
  course!: Course;
  teachers!: Teacher[];
  form!: FormGroup;

  name!: string;
  maxGrade!: number;
  teacher!: Teacher;

  faTimes = faTimes;

  constructor(private courseService: CourseService,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private teacherService: TeacherService,
              private router: Router,
              private fieldValidator: FieldValidatorService) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(()=>{
      this.showCourse();
    });

    this.teacherService.getTeachers().subscribe((teachers) => (this.teachers = teachers));
    this.form = this.formBuilder.group({
      name: ['', [Validators.required]],
      maxGrade: ['', [Validators.required, Validators.pattern("[0-9]+"), Validators.min(1), Validators.max(100)]],
      teacher: ['', [Validators.required]]
    });
    this.fieldValidator.form = this.form;

    this.showCourse();
  }

  onDelete(courseId: number | undefined){
    if (courseId != null) {
      this.courseService.deleteCourse(courseId).subscribe({
          next: response => {
            console.log(`Response from deleting: ${response}`);
          },
          error: err => {
            console.log(`There was an error: ${err.message}`);
          },
          complete: () => {
            console.log('Done delete the course');
            this.router.navigate(['courses']);
          }
        }
      );
    }
  }

  showCourse() {
    // @ts-ignore
    const courseId: number = +this.route.snapshot.paramMap.get('id');
    this.courseService.getCourse(courseId).subscribe( data => {
      this.course = data;
      // @ts-ignore
      console.log("Course: " + (data.id))
    });
  }

  onUpdate() {
    if (this.form.valid) {
      const newCourse = {
        name: this.name,
        maxGrade: this.maxGrade,
        teacher: this.teacher
      }

      this.courseService.updateCourse(newCourse).subscribe({
          next: response => {
            console.log(`Response from updating: ${response}`);
            this.course = response;
          },
          error: err => {
            console.log(`There was an error: ${err.message}`);
          },
          complete: () => {
            console.log('Done update the course');
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
