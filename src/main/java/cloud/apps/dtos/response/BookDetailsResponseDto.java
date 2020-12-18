package cloud.apps.dtos.response;

import lombok.Data;

import java.util.Collection;

@Data
public class BookDetailsResponseDto {

    private Integer id;
    private String title;
    private String summary;
    private String author;
    private String publisher;
    private int publicationYear;
    private float score;
    private Collection<CommentResponseDto> comments;

}
