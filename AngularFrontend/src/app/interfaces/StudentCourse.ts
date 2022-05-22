import {Student} from "./Student";
import {Course} from "./Course";

export interface StudentCourse {
  id?: number;
  student: Student;
  course: Course;
  grade?: number;
  review?: string;
}
