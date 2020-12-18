package cloud.apps.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User has comments yet")
public class CommentFoundsException extends RuntimeException {

	private static final long serialVersionUID = 7873006935065897090L;
}
