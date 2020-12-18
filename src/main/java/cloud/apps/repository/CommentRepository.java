package cloud.apps.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cloud.apps.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>{

    Collection<Comment> findByBookId(Integer bookId);

    Optional<Comment> findByBookIdAndId(Integer bookId, Integer commentId);

}
