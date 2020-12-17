package cloud.apps.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import cloud.apps.model.Comment;

@Repository
public class CommentRepository {
	private ConcurrentMap<Integer, Comment> comments = new ConcurrentHashMap<>();
	private AtomicInteger nextId = new AtomicInteger();

	public Collection<Comment> findAll() {
		return this.comments.values();
	}

	public Comment findById(Integer id) {
		return this.comments.get(id);
	}

	public Comment save(Comment comments) {
		Integer id = nextId.getAndIncrement();
		comments.setId(id);
		return this.comments.put(id, comments);
	}

	@PostConstruct
	private void postConstruct() {
		save(new Comment(0, "Enrique", "Comentario absurdo 0", 0));
		save(new Comment(1, "Enrique", "Comentario absurdo 1", 1));
		save(new Comment(2, "Enrique", "Comentario absurdo 2", 2));
		save(new Comment(3, "Enrique", "Comentario absurdo 3", 3));
	}

	public Collection<Comment> findAllByBookId(Integer bookId) {
		Collection<Comment> collection = new ArrayList<>();
		for (Comment comment : this.comments.values()) {
			if (comment.isSameBookId(bookId)) {
				collection.add(comment);
			}
		}
		return collection;
	}

	public Comment deleteCommentByBookId(Integer id, Integer bookId) {
		if (this.comments.containsKey(id)) {
			Comment comment = this.comments.get(id);
			if (comment.getBookId().equals(bookId)) {
				return this.comments.remove(id);
			}
		}
		return null;
	}
}
