import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http'
import {map, Observable} from "rxjs";
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
  private apiUrl = 'http://localhost:8080/api/courses'

  constructor(private http: HttpClient) {
  }

  getCourses(): Observable<Course[]> {
    return this.http.get<GetResponseCourses>(this.apiUrl).pipe(
      map(response => response._embedded.courses)
    );
  }

  getCourse(id: number): Observable<Course> {
    return this.http.get<Course>(`${this.apiUrl}/${id}`);
  }

  deleteCourse(id: number):Observable<unknown>{
    return this.http.delete<unknown>(`${this.apiUrl}/${id}`);
  }

  addCourse(course: Course):Observable<Course>{
    return this.http.post<Course>(this.apiUrl, course, httpOptions);
  }

  updateCourse(course: Course):Observable<Course>{
    return this.http.put<Course>(this.apiUrl, course, httpOptions);
  }
}

interface GetResponseCourses{
  _embedded:{
    courses: Course[];
  }
}
