import { Teacher } from "./Teacher";

export interface Course{
  id?:number;
  name:string;
  maxGrade:number;
  teacher:Teacher;
}
