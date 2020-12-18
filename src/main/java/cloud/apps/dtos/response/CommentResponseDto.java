package cloud.apps.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponseDto {

    private Integer id;
    private String user;
    private String comment;
    private float score;

}
