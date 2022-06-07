import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {Student} from "../../interfaces/Student";
import {environment} from "../../../environments/environment";

const httpOptions = {
  headers: new HttpHeaders({
    'Content-type': 'application/json',
  })
}

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private apiUrl = `${environment.apiUrl}students`

  constructor(private http: HttpClient) {
  }

  getStudents(): Observable<Student[]> {
    return this.http.get<Student[]>(this.apiUrl);
  }

  getStudentByName(name: string): Observable<Student> {
    // return this.http.get<Student>(`${this.apiUrl}?name=${name}`);
    return this.http.get<Student>(`${this.apiUrl}/search/findByNameContaining?name=${name}`);
  }

  getStudent(id: number): Observable<Student> {
    return this.http.get<Student>(`${this.apiUrl}/${id}`);
  }

  deleteStudent(id: number): Observable<unknown> {
    return this.http.delete<unknown>(`${this.apiUrl}/${id}`);
  }

  addStudent(student: Student): Observable<Student> {
    return this.http.post<Student>(this.apiUrl, student, httpOptions);
  }

  updateStudent(id: number | undefined, student: Student): Observable<Student> {
    console.log(id, student);
    return this.http.put<Student>(`${this.apiUrl}/${id}`, student, httpOptions);
  }
}
