import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {StudentCourse} from "../../interfaces/StudentCourse";
import {environment} from "../../../environments/environment";

const httpOptions = {
  headers: new HttpHeaders({
    'Content-type': 'application/json',
  })
}

@Injectable({
  providedIn: 'root'
})

export class StudentCourseService {
  private apiUrl = `${environment.apiUrl}studentCourseRelations`

  constructor(private http: HttpClient) {
  }

  getStudentCourses(): Observable<StudentCourse[]> {
    return this.http.get<StudentCourse[]>(this.apiUrl);
  }

  getStudentCoursesForTeacher(name: string): Observable<StudentCourse[]> {
    return this.http.get<StudentCourse[]>(`${this.apiUrl}/search/findByTeacherNameContaining?name=${name}`);
  }

  getStudentCoursesForStudent(name: string): Observable<StudentCourse[]> {
    return this.http.get<StudentCourse[]>(`${this.apiUrl}/search/findByStudentNameContaining?name=${name}`);
  }

  getStudentCourse(id: number): Observable<StudentCourse> {
    return this.http.get<StudentCourse>(`${this.apiUrl}/${id}`);
  }


  deleteStudentCourse(id: number): Observable<unknown> {
    return this.http.delete<unknown>(`${this.apiUrl}/${id}`);
  }

  addStudentCourse(studentCourse: StudentCourse): Observable<StudentCourse> {
    return this.http.post<StudentCourse>(this.apiUrl, studentCourse, httpOptions);
  }

  updateStudentCourse(id: number | undefined, studentCourse: StudentCourse): Observable<StudentCourse> {
    return this.http.put<StudentCourse>(`${this.apiUrl}/${id}`, studentCourse, httpOptions);
  }
}
