import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {Teacher} from "../../interfaces/Teacher";

const httpOptions = {
  headers: new HttpHeaders({
    'Content-type': 'application/json',
  })
}

@Injectable({
  providedIn: 'root'
})
export class TeacherService {
  private apiUrl = 'http://localhost:8080/api/teachers'

  constructor(private http: HttpClient) {
  }

  getTeachers(): Observable<Teacher[]> {
    return this.http.get<GetResponseTeachers>(this.apiUrl).pipe(
      map(response => response._embedded.teachers)
    );
  }

  getTeacher(id: number): Observable<Teacher> {
    return this.http.get<Teacher>(`${this.apiUrl}/${id}`);
  }

  deleteTeacher(id: number):Observable<unknown>{
    return this.http.delete<unknown>(`${this.apiUrl}/${id}`);
  }

  addTeacher(teacher: Teacher):Observable<Teacher>{
    return this.http.post<Teacher>(this.apiUrl, teacher, httpOptions);
  }

  updateTeacher(teacher: Teacher):Observable<Teacher>{
    return this.http.put<Teacher>(this.apiUrl, teacher, httpOptions);
  }
}

interface GetResponseTeachers{
  _embedded:{
    teachers: Teacher[];
  }
}
