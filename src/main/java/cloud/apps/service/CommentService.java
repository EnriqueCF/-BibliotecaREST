package cloud.apps.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cloud.apps.model.Comment;
import cloud.apps.repository.CommentRepository;

@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepository;

	public Comment deleteCommentOfBook(Integer bookId, Integer commentId) {
		return this.commentRepository.deleteCommentByBookId(commentId, bookId);
	}

	public Comment add(Comment comment) {
		return this.commentRepository.save(comment);
	}

	public Comment addCommentOnBook(Comment comment, Integer bookId) {
		comment.setBookId(bookId);
		this.commentRepository.save(comment);
		return comment;
	}
}
