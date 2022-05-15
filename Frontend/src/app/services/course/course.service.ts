import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http'
import {Observable} from "rxjs";
import {Course} from "../../interfaces/Course";

const httpOptions = {
  headers: new HttpHeaders({
    'Content-type': 'application/json',
  })
}

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  private apiUrl = 'http://localhost:8080/courses'

  constructor(private http: HttpClient) {
  }

  getCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(this.apiUrl);
  }

  deleteCourse(course: Course):Observable<Course>{
    return this.http.delete<Course>(`${this.apiUrl}?id=${course.id}`);
  }

  addCourse(course: Course):Observable<Course>{
    return this.http.post<Course>(this.apiUrl, course, httpOptions);
  }
}
