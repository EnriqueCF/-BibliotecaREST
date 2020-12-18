package cloud.apps.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cloud.apps.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer>{

    Optional<Book> findById(long bookId);

}
