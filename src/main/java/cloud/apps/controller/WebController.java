package cloud.apps.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import cloud.apps.constant.Endpoint;
import cloud.apps.model.Book;
import cloud.apps.model.Comment;
import cloud.apps.service.BookService;
import cloud.apps.service.CommentService;
import cloud.apps.service.UserSessionService;

@Controller
public class WebController {

	@Autowired
	private UserSessionService userSession;

	@Autowired
	private BookService bookService;

	@Autowired
	private CommentService commentService;

	@GetMapping(Endpoint.INDEX)
	public String index(Model model, HttpSession session) {
		model.addAttribute("books", bookService.getBooks());

		return "index";
	}

	@GetMapping(Endpoint.BOOKS_BY_ID)
	public String findBookById(Model model, @PathVariable Integer id) {
		model.addAttribute("book", bookService.getBookById(id));

		return "book";
	}

	@GetMapping("/books/{id}/comments/new")
	public String newComment(Model model, @PathVariable Integer id) {
		model.addAttribute("id", id);
		model.addAttribute("user", userSession.getUser());

		return "new_comment";
	}

	@PostMapping("/books/{id}/comments")
	public String newCommentFrom(Model model, @PathVariable Integer id, Comment comment) {
		if (comment != null && !comment.getAuthor().isEmpty()) {
			this.userSession.setUser(comment.getAuthor());
			commentService.addCommentOnBook(comment, id);
			String endpoint = "/books/" + id;
			return "redirect:" + endpoint;
		}
		return "redirect:/";
	}

	@GetMapping("/books/new")
	public String newBookForm(Model model) {

		model.addAttribute("user", userSession.getUser());

		return "new_book";
	}
	
	@PostMapping("/books/")
	public String newBookForm(Book book) {
		bookService.postBook(book);

		return "redirect:/";
	}
	
	@GetMapping("/books/{bookId}/comments/{commentId}")
	public String newBookForm(@PathVariable Integer bookId, @PathVariable Integer commentId) {
		commentService.deleteCommentOfBook(bookId, commentId);

		return "redirect:/books/"+bookId;
	}
}
