package cloud.apps.model;

import lombok.Data;

@Data
public class Comment {

	private Integer id;
	private Integer bookId;
	private String author;
	private String comment;
	private int rate;
	
	public Comment(Integer bookId, String author, String comment, int rate) {
		this.author = author;
		this.bookId = bookId;
		this.comment = comment;
		this.rate = rate;
	}
	
	public boolean isSameBookId(Integer id) {
		return this.bookId.equals(id);
	}
}
