package cloud.apps.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import cloud.apps.model.Book;
import cloud.apps.model.Comment;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

public interface BooksApi {

	ResponseEntity<Comment> deleteCommentOfBook(
			@Parameter(in = ParameterIn.PATH, description = "Complete info about one book", required = true, schema = @Schema()) @PathVariable("bookId") Integer bookId,
			@Parameter(in = ParameterIn.PATH, description = "Complete info about one book", required = true, schema = @Schema()) @PathVariable("commentId") Long commentId);

	ResponseEntity<Book> getBookById(
			@Parameter(in = ParameterIn.PATH, description = "Unique ID of one book", required = true, schema = @Schema()) @PathVariable("bookId") Integer bookId);

	ResponseEntity<List<Book>> getBooks();

	ResponseEntity<Book> postBook(
			@Parameter(in = ParameterIn.DEFAULT, description = "Complete info about one book", required = true, schema = @Schema()) @Valid @RequestBody Book body);

	ResponseEntity<Comment> postCommentInBook(
			@Parameter(in = ParameterIn.PATH, description = "Unique ID of one book", required = true, schema = @Schema()) @PathVariable("bookId") Long bookId,
			@Parameter(in = ParameterIn.DEFAULT, description = "Complete info about one book", required = true, schema = @Schema()) @Valid @RequestBody Comment body);

}
