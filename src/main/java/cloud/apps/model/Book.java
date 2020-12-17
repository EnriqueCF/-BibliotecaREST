package cloud.apps.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

	public interface Basic {
	}
	
	@JsonView(Basic.class)
	private Integer id;
	@JsonView(Basic.class)
	private String title;
	private String resume;
	private String author;
	private String editorial;
	private int year;
	
	List<Comment> comments;
	
	public Book(String title, String resume, String author, String editorial, Integer year) {
		this.title = title;
		this.resume = resume;
		this.author = author;
		this.editorial = editorial;
		this.year = year;
		this.comments = new ArrayList<>();
	}
}
