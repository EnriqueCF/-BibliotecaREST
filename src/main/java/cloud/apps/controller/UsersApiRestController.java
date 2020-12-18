package cloud.apps.controller;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cloud.apps.dtos.response.CommentResponseDto;
import cloud.apps.exceptions.CommentFoundsException;
import cloud.apps.exceptions.UserNotFoundException;
import cloud.apps.model.Comment;
import cloud.apps.model.User;
import cloud.apps.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/users")
public class UsersApiRestController {

	private UserService userService;

	public UsersApiRestController(UserService userService) {
		this.userService = userService;
	}

	@Operation(summary = "Get all users")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Found all users", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class))) }) })
	@GetMapping("/")
	public Collection<User> getUsers() {
		return this.userService.findAll();
	}

	@Operation(summary = "Create a user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "User created", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid user supplied") })
	@PostMapping
	public ResponseEntity<User> createUser(@Parameter(description = "The user to be created") @RequestBody User user) {
		User newUser = this.userService.save(user);
		if (newUser != null) {
			URI location = fromCurrentRequest().path("/{nick}").buildAndExpand(newUser.getNick()).toUri();
			return ResponseEntity.created(location).body(newUser);
		}
		return ResponseEntity.badRequest().build();
	}

	@Operation(summary = "Get a book by its nick")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied"),
			@ApiResponse(responseCode = "404", description = "User not found") })
	@GetMapping("/{nick}")
	public ResponseEntity<User> getUser(
			@Parameter(description = "nick of book to be searched") @PathVariable String nick) {
		Optional<User> user = this.userService.findUserByNickName(nick);
		return ResponseEntity.of(user);
	}

	@Operation(summary = "Get a user by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied"),
			@ApiResponse(responseCode = "404", description = "User not found") })
	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(
			@Parameter(description = "id of user to be searched") @PathVariable Integer id) {
		Optional<User> user = this.userService.findById(id);
		return ResponseEntity.of(user);
	}

	@Operation(summary = "Delete a user")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Deleted comment from the book", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid format id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Book or comment not found", content = @Content) })
	@DeleteMapping("/{id}")
	public ResponseEntity<User> deleteComment(
			@Parameter(description = "id of user to be searched") @PathVariable Integer id) {
		Optional<User> user = this.userService.delete(id);
		if (user.isPresent()) {
			return ResponseEntity.of(user);
		} else {
			throw new CommentFoundsException();
		}
	}

	@Operation(summary = "Update email of user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied"),
			@ApiResponse(responseCode = "404", description = "Book not found") })
	@PatchMapping("/{id}")
	public ResponseEntity<User> updateEmail(@PathVariable Integer id, @RequestBody @Valid String email) {
		Optional<User> user = this.userService.updateEmail(id, email);
		if (user.isPresent()) {
			return ResponseEntity.of(user);
		} else {
			throw new CommentFoundsException();
		}
	}

	@Operation(summary = "Get all comments")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Found all comments", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentResponseDto.class))) }) })
	@GetMapping("/{id}/comments/")
	public ResponseEntity<List<CommentResponseDto>> getUsers(@PathVariable Integer id) {

		Optional<User> user = this.userService.findById(id);
		if (user.isPresent()) {
			User main = user.get();
			List<Comment> comments = main.getComments();
			List<CommentResponseDto> dtos = comments.stream()
					.map(c -> new CommentResponseDto(c.getId(), c.getUser().getNick(), c.getText(), c.getScore()))
					.collect(Collectors.toList());
			return ResponseEntity.ok().body(dtos);
		} else {
			throw new UserNotFoundException();
		}
	}

	@PostConstruct
	public void postConstructor() {

	}
}
