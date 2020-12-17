package cloud.apps.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cloud.apps.model.Book;
import cloud.apps.model.Comment;
import cloud.apps.repository.BookRepository;
import cloud.apps.repository.CommentRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository repository;

	@Autowired
	private CommentRepository commentRepository;

	public List<Book> getBooks() {
		return new ArrayList<>(repository.findAll());
	}

	public Book getBookById(Integer id) {
		Book book = repository.findById(id);
		if (book != null) {
			List<Comment> comments = new ArrayList<>(commentRepository.findAllByBookId(id));
			book.setComments(comments);
		}
		return book;
	}
	
	public Book postBook(Book newBook) {
		return this.repository.save(newBook);
	}
}
