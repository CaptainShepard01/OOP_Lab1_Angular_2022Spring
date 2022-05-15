import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Course} from "../../interfaces/Course";
import {Teacher} from "../../interfaces/Teacher";

@Injectable({
  providedIn: 'root'
})
export class TeacherService {
  private apiUrl = 'http://localhost:8080/teachers'

  constructor(private http: HttpClient) {
  }

  getTeachers(): Observable<Teacher[]> {
    return this.http.get<Teacher[]>(this.apiUrl);
  }
}
