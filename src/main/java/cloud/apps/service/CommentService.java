package cloud.apps.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cloud.apps.dtos.request.CommentRequestDto;
import cloud.apps.model.Book;
import cloud.apps.model.Comment;
import cloud.apps.model.User;
import cloud.apps.repository.BookRepository;
import cloud.apps.repository.CommentRepository;

@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private BookRepository bookRepository;

	public Optional<Comment> deleteCommentOfBook(Integer bookId, Integer commentId) {
		Optional<Book> book = bookRepository.findById(bookId);

		Optional<Comment> comment = commentRepository.findById(commentId);

		if (book.isPresent() && comment.isPresent()) {
			if (book.get().getId().equals(comment.get().getBook().getId())) {
				this.commentRepository.delete(comment.get());
			}
		}
		return comment;
	}

	public Comment add(Comment comment) {
		return this.commentRepository.save(comment);
	}

	public Comment addCommentOnBook(Comment comment, Integer bookId) {
		Optional<Book> book = bookRepository.findById(bookId);
		if (book.isPresent()) {
			comment.setBook(book.get());
		}
		this.commentRepository.save(comment);
		return comment;
	}

	public Optional<Comment> addComment(Integer bookId, User user, CommentRequestDto commentRequestDto) {
		Optional<Book> book = bookRepository.findById(bookId);
		
		if (book.isPresent()) {
			Comment comment = new Comment();
			comment.setScore(commentRequestDto.getScore());
			comment.setText(commentRequestDto.getComment());
			comment.setUser(user);
			comment.setBook(book.get());
			this.commentRepository.save(comment);
			return Optional.of(comment);
		}
		return Optional.empty();
	}
}
