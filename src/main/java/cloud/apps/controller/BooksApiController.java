package cloud.apps.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import cloud.apps.model.Book;
import cloud.apps.model.Comment;
import cloud.apps.service.BookService;
import cloud.apps.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api")
public class BooksApiController {

	@Autowired
	private BookService bookService;

	@Autowired
	private CommentService commentService;

	@Operation(summary = "Delete one comment", description = "Delete one comment of book, and return its.", tags = {
			"Books" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Operación realizada correctamente."),

			@ApiResponse(responseCode = "400", description = "Invalid ID supplied") })
	@RequestMapping(value = "/books/{bookId}/comments/{commentId}", method = RequestMethod.DELETE)
	public ResponseEntity<Comment> deleteCommentOfBook(
			@Parameter(in = ParameterIn.PATH, description = "Complete info about one book", required = true, schema = @Schema()) @PathVariable("bookId") Integer bookId,
			@Parameter(in = ParameterIn.PATH, description = "Complete info about one book", required = true, schema = @Schema()) @PathVariable("commentId") Integer commentId) {

		Comment comment = commentService.deleteCommentOfBook(bookId, commentId);
		if (comment != null) {
			return ResponseEntity.ok(comment);
		}
		return ResponseEntity.notFound().build();
	}

	@Operation(summary = "Get book by ID", description = "Get book by ID", tags = { "Books" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Book.class))),

			@ApiResponse(responseCode = "400", description = "Invalid ID supplied"),

			@ApiResponse(responseCode = "404", description = "Book not found") })
	@RequestMapping(value = "/books/{bookId}", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<Book> getBookById(
			@Parameter(in = ParameterIn.PATH, description = "Unique ID of one book", required = true, schema = @Schema()) @PathVariable("bookId") Integer bookId) {

		Book book = bookService.getBookById(bookId);
		if (book != null) {
			return ResponseEntity.ok(book);
		}
		return ResponseEntity.notFound().build();
	}

	@JsonView(Book.Basic.class)
	@Operation(summary = "Get all books", description = "Get an array of ResumeBook", tags = { "Books" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Operación realizada correctamente.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Book.class)))),

			@ApiResponse(responseCode = "400", description = "Books not found") })
	@RequestMapping(value = "/books", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<List<Book>> getBooks() {
		return ResponseEntity.ok(bookService.getBooks());
	}

	@Operation(summary = "Post book", description = "PostBook", tags = { "Books" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Operación realizada correctamente.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Book.class)))),

			@ApiResponse(responseCode = "400", description = "Invalid ID supplied") })
	@RequestMapping(value = "/books", produces = { "application/json" }, consumes = {
			"*/*" }, method = RequestMethod.POST)
	public ResponseEntity<Book> postBook(
			@Parameter(in = ParameterIn.DEFAULT, description = "Complete info about one book", required = true, schema = @Schema()) @RequestBody Book body) {
		Book newbook = bookService.postBook(body);
		if (newbook != null) {
			return ResponseEntity.ok(newbook);
		}

		return ResponseEntity.badRequest().build();
	}

	@Operation(summary = "Post comment on a book", description = "Post comment on a book", tags = { "Books" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK.", content = @Content(schema = @Schema(implementation = Book.class))),

			@ApiResponse(responseCode = "400", description = "Invalid ID supplied") })
	@RequestMapping(value = "/books/{bookId}/comments", produces = { "application/json" }, consumes = {
			"*/*" }, method = RequestMethod.POST)
	public ResponseEntity<Comment> postCommentInBook(
			@Parameter(in = ParameterIn.PATH, description = "Unique ID of one book", required = true, schema = @Schema()) @PathVariable("bookId") Integer bookId,
			@Parameter(in = ParameterIn.DEFAULT, description = "Complete info about one book", required = true, schema = @Schema()) @Valid @RequestBody Comment body) {

		Comment comment = commentService.addCommentOnBook(body, bookId);
		if (comment != null) {
			return ResponseEntity.ok(comment);
		}
		return ResponseEntity.notFound().build();
	}

}
