package cloud.apps.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cloud.apps.dtos.request.BookRequestDto;
import cloud.apps.dtos.request.CommentRequestDto;
import cloud.apps.dtos.response.BookDetailsResponseDto;
import cloud.apps.dtos.response.BookResponseDto;
import cloud.apps.dtos.response.CommentResponseDto;
import cloud.apps.exceptions.CommentFoundsException;
import cloud.apps.exceptions.UserNotFoundException;
import cloud.apps.model.Comment;
import cloud.apps.model.User;
import cloud.apps.service.BookService;
import cloud.apps.service.CommentService;
import cloud.apps.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/books")
public class BooksApiRestController {

	private BookService bookService;
	private CommentService commentService;
	private UserService userService;

	public BooksApiRestController(BookService bookService, CommentService commentService, UserService userService) {
		this.bookService = bookService;
		this.commentService = commentService;
		this.userService = userService;
	}

	@Operation(summary = "Get all books")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Found all books", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BookResponseDto.class))) }) })
	@GetMapping("/")
	public Collection<BookResponseDto> getBooks() {
		return this.bookService.findAll();
	}

	@Operation(summary = "Get a book by its id")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Found the book", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = BookDetailsResponseDto.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid format id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Book not found", content = @Content) })
	@GetMapping("/{bookId}")
	public BookDetailsResponseDto getBook(
			@Parameter(description = "id of book to be searched") @PathVariable Integer bookId) {
		return this.bookService.findById(bookId);
	}

	@Operation(summary = "Create a new book")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Book to be created", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookRequestDto.class)))
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Created the book", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = BookDetailsResponseDto.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid book attributes supplied", content = @Content) })
	@PostMapping("/")
	public BookDetailsResponseDto createBook(@Valid @RequestBody BookRequestDto bookRequestDto) {
		return this.bookService.save(bookRequestDto);
	}

	@Operation(summary = "Add a comment to a book")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "comment to be added", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentRequestDto.class)))
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Added comment to the book", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponseDto.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid comment attributes supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Book not found", content = @Content) })
	@PostMapping("/{bookId}/comments/")
	public ResponseEntity<CommentResponseDto> createComment(
			@Parameter(description = "identifier of the book to which the comment will be added") @PathVariable Integer bookId,
			@Valid @RequestBody CommentRequestDto commentRequestDto) {

		Optional<User> user = this.userService.findUserByNickName(commentRequestDto.getUser());
		if (user.isPresent()) {

			Optional<Comment> comment = this.commentService.addComment(bookId, user.get(), commentRequestDto);
			if (comment.isPresent()) {
				Comment commentE = comment.get();
				return ResponseEntity.ok().body(new CommentResponseDto(commentE.getId(), commentE.getUser().getNick(),
						commentE.getText(), commentE.getScore()));
			} else {
				throw new CommentFoundsException();
			}
		} else {
			throw new UserNotFoundException();
		}
	}

	@Operation(summary = "Delete a comment from a book")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Deleted comment from the book", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponseDto.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid format id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Book or comment not found", content = @Content) })
	@DeleteMapping("/{bookId}/comments/{commentId}")
	public ResponseEntity<CommentResponseDto> deleteComment(
			@Parameter(description = "identifier of the book to which the comment beIntegers") @PathVariable Integer bookId,
			@Parameter(description = "id of comment to be deleted") @PathVariable Integer commentId) {

		Optional<Comment> comment = this.commentService.deleteCommentOfBook(bookId, commentId);

		if (comment.isPresent()) {
			Comment commentE = comment.get();
			return ResponseEntity.ok().body(new CommentResponseDto(commentE.getId(), commentE.getUser().getNick(),
					commentE.getText(), commentE.getScore()));

		} else {
			throw new CommentFoundsException();
		}
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

	@PostConstruct
	public void postConstructor() {
		User user = new User();
		user.setEmail("e.casasf@alumnos.urjc.es");
		user.setNick("ECASASF");
		this.userService.save(user);

		User p = new User();
		p.setEmail("paula@hotmail.com");
		p.setNick("PAULOBA");
		this.userService.save(p);

		BookRequestDto dto = BookRequestDto.builder().author("Enrique Casas Fernandes").publicationYear(2020)
				.publisher("Espasa")
				.summary("Erase un master interminable lleno de practicas, que aprendias pero te costaba la vida.")
				.title("El master interminable").build();
		this.bookService.save(dto);
		dto = BookRequestDto.builder().author("de Jerry Peek (Autor), Shelley Powers (Autor), Tim O'Re")
				.publicationYear(2020).publisher("Kindle")
				.summary("With the growing popularity of Linux and the advent of Darwin, "
						+ "Unix has metamorphosed into something new and exciting. No Integerer perceived as a difficult operating system, "
						+ "more and more users are discovering the advantages of Unix for the first time")
				.title("Unix Power Tools (Classique Us) ").build();
		this.bookService.save(dto);

		CommentRequestDto commentRequestDto = new CommentRequestDto("ECASASF", "Libro buenisimo", 0);

		this.commentService.addComment(1, user, commentRequestDto);
	}

}
