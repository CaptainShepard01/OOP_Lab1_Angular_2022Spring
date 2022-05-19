package ua.university.modelEntities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courses")
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "max_grade", nullable = false)
    @JsonProperty
    private int maxGrade;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Teacher teacher;

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Integer getTeacherId() {
        return teacher.getId();
    }
}