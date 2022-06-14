package entities;

import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CourseModule {
    private Long id;
    private int eta;
    private String title;
    private String materials;
}
