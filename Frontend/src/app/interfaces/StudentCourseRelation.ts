import {Student} from "./Student";
import {Course} from "./Course";

export interface StudentCourseRelation {
  id?: number;
  student:Student;
  course:Course;
  grade?:number;
  review?:string;
}
