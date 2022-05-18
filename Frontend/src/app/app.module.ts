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
import {RouterModule, Routes} from "@angular/router";
import { MainPageComponent } from './components/main-page/main-page.component';
import { PageAccessDeniedComponent } from './components/errorPages/page-access-denied/page-access-denied.component';
import { PageNotFoundComponent } from './components/errorPages/page-not-found/page-not-found.component';
import { CourseDetailsComponent } from './components/course/course-details/course-details.component';
import { TeacherDetailsComponent } from './components/teacher/teacher-details/teacher-details.component';
import { StudentDetailsComponent } from './components/student/student-details/student-details.component';
import { StudentCourseDetailsComponent } from './components/student-course/student-course-details/student-course-details.component';
import { StudentItemComponent } from './components/student/student-item/student-item.component';
import { AddStudentComponent } from './components/student/add-student/add-student.component';
import { TeacherItemComponent } from './components/teacher/teacher-item/teacher-item.component';
import { AddTeacherComponent } from './components/teacher/add-teacher/add-teacher.component';
import { StudentCourseItemComponent } from './components/student-course/student-course-item/student-course-item.component';
import { AddStudentCourseComponent } from './components/student-course/add-student-course/add-student-course.component';

const routes: Routes = [
  {path: 'mainPage', component: MainPageComponent},
  {
    path: 'studentsCourse/:id',
    component: StudentCourseDetailsComponent
  },
  {
    path: 'studentsCourses',
    component: StudentCoursesComponent
  },
  {
    path: 'course/:id',
    component: CourseDetailsComponent
  },
  {
    path: 'courses',
    component: CoursesComponent
  },
  {
    path: 'student/:id',
    component: StudentDetailsComponent
  },
  {
    path: 'students',
    component: StudentsComponent
  },
  {
    path: 'teacher/:id',
    component: TeacherDetailsComponent
  },
  {
    path: 'teachers',
    component: TeachersComponent,
    // canActivate: [AuthGuard],
    // data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER'] },
  },
  {
    path: 'rocket',
    component: PageNotFoundComponent
  },
  {
    path: 'access-denied',
    component: PageAccessDeniedComponent,
    // canActivate: [AuthGuard],
  },
  {path: '', redirectTo: '/mainPage', pathMatch: 'full'},
  {path: '**', redirectTo: '/mainPage', pathMatch: 'full'}
];


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
    FieldErrorDisplayComponent,
    MainPageComponent,
    PageAccessDeniedComponent,
    PageNotFoundComponent,
    CourseDetailsComponent,
    TeacherDetailsComponent,
    StudentDetailsComponent,
    StudentCourseDetailsComponent,
    StudentItemComponent,
    AddStudentComponent,
    TeacherItemComponent,
    AddTeacherComponent,
    StudentCourseItemComponent,
    AddStudentCourseComponent
  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
    HttpClientModule,
    FontAwesomeModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule
  ],
  providers: [
    // AuthService,
    // {
    //   provide: APP_INITIALIZER,
    //   useFactory: initializeKeycloak,
    //   multi: true,
    //   deps: [KeycloakService]
    // },
    // {
    //   provide: HTTP_INTERCEPTORS,
    //   useClass: KeycloakBearerInterceptor,
    //   multi: true,
    // }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
