import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {Student} from "../../interfaces/Student";

const httpOptions = {
  headers: new HttpHeaders({
    'Content-type': 'application/json',
  })
}

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private apiUrl = 'http://localhost:8080/api/students'

  constructor(private http: HttpClient) {
  }

  getStudents(): Observable<Student[]> {
    return this.http.get<GetResponseStudents>(this.apiUrl).pipe(
      map(response => response._embedded.students)
    );
  }

  getStudentByName(name: string): Observable<Student>{
    return this.http.get<Student>(`${this.apiUrl}?name=${name}`);
  }

  getStudent(id: number): Observable<Student> {
    return this.http.get<Student>(`${this.apiUrl}/${id}`);
  }

  deleteStudent(id: number):Observable<unknown>{
    return this.http.delete<unknown>(`${this.apiUrl}/${id}`);
  }

  addStudent(student: Student):Observable<Student>{
    return this.http.post<Student>(this.apiUrl, student, httpOptions);
  }

  updateStudent(student: Student):Observable<Student>{
    return this.http.put<Student>(this.apiUrl, student, httpOptions);
  }
}

interface GetResponseStudents{
  _embedded:{
    students: Student[];
  }
}
