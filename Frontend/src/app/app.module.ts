import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import {AppComponent} from './app.component';
import {CoursesComponent} from './components/course/courses/courses.component';
import {StudentsComponent} from './components/student/students/students.component';
import {TeachersComponent} from './components/teacher/teachers/teachers.component';
import {StudentCoursesComponent} from './components/student-course/student-courses/student-courses.component';
import {CourseItemComponent} from './components/course/course-item/course-item.component';
import {AddCourseComponent} from './components/course/add-course/add-course.component';
import {AppHeaderComponent} from './components/utils/app-header/app-header.component';
import {ButtonComponent} from './components/utils/button/button.component';
import { FieldErrorDisplayComponent } from './components/utils/field-error-display/field-error-display.component';


@NgModule({
  declarations: [
    AppComponent,
    CoursesComponent,
    StudentsComponent,
    TeachersComponent,
    StudentCoursesComponent,
    CourseItemComponent,
    AddCourseComponent,
    AppHeaderComponent,
    ButtonComponent,
    FieldErrorDisplayComponent
  ],
    imports: [
        BrowserModule,
        HttpClientModule,
        FontAwesomeModule,
        BrowserAnimationsModule,
        FormsModule,
        ReactiveFormsModule
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
