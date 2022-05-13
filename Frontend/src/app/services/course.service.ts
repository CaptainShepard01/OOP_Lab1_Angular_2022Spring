import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http'
import {Observable} from "rxjs";
import {Course} from "../interfaces/Course";

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  // @ts-ignore
  private status: string;
  // @ts-ignore
  private errorMessage: string;
  private apiUrl = 'http://localhost:8080/courses'

  constructor(private http: HttpClient) {
  }

  getCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(this.apiUrl);
  }

  deleteCourse(course: Course){
    return this.http.delete<Course>(`${this.apiUrl}?id=${course.id}`);
  }

}
