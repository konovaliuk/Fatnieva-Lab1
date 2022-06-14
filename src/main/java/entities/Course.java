package entities;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    private Long id;
    private String title;
    private CourseLevel level;
    private String description;
}
