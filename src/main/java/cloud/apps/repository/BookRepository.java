package cloud.apps.repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import cloud.apps.model.Book;

@Repository
public class BookRepository {

	private ConcurrentMap<Integer, Book> books = new ConcurrentHashMap<>();
	private AtomicInteger nextId = new AtomicInteger();

	public Collection<Book> findAll() {
		return this.books.values();
	}

	public Book findById(Integer id) {
		return this.books.get(id);
	}

	public Book save(Book book) {
		Integer id = nextId.getAndIncrement();
		book.setId(id);
		this.books.put(id, book);
		return book;
	}

	@PostConstruct
	private void postConstruct() {
		save(new Book("El club del crimen de los jueves", "Club de crimen", "Richard Osman", "Espasa", 2020));
		save(new Book("El camino del arquero", "Vida de un arquero", "Paulo Coelho", "Planeta", 2020));
		save(new Book("Como polvo en el viento", "Polvo en el viento y en el espejo", "Loenardo Padura",
				"Tusquets Editores S.A.", 2020));
		save(new Book("Aquitania",
				"Un poderoso thriller histórico que atraviesa un siglo repleto de venganzas, incestos y batallas",
				"Eva García Sáenz de Urturi", "Editorial Planeta", 2020));

	}
}
