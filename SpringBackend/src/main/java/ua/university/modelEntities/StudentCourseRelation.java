package ua.university.modelEntities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student_course_relations")
@Entity
public class StudentCourseRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @OneToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "grade", nullable = false)
    @JsonProperty
    private int grade;

    @Column(name = "review", nullable = false)
    @JsonProperty
    private String review;

    public Integer getCourseId() {
        return course.getId();
    }

    public Integer getStudentId() {
        return student.getId();
    }
}
