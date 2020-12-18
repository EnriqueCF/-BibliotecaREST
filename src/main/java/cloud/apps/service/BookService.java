package cloud.apps.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import cloud.apps.dtos.request.BookRequestDto;
import cloud.apps.dtos.response.BookDetailsResponseDto;
import cloud.apps.dtos.response.BookResponseDto;
import cloud.apps.dtos.response.CommentResponseDto;
import cloud.apps.exceptions.BookNotFoundException;
import cloud.apps.model.Book;
import cloud.apps.model.Comment;
import cloud.apps.repository.BookRepository;

@Service
public class BookService {

    private Mapper mapper;
    private BookRepository bookRepository;


    public BookService(Mapper mapper, BookRepository bookRepository) {
        this.mapper = mapper;
        this.bookRepository = bookRepository;

    }

    public Collection<BookResponseDto> findAll() {
    	List<Book> books = this.bookRepository.findAll();
        return this.bookRepository.findAll().stream()
                .map(book -> this.mapper.map(book, BookResponseDto.class))
                .collect(Collectors.toList());
    }

    public BookDetailsResponseDto save(BookRequestDto bookRequestDto) {
        Book book = this.mapper.map(bookRequestDto, Book.class);
        book = this.bookRepository.save(book);
        
        BookDetailsResponseDto dto =  new BookDetailsResponseDto();
        dto.setAuthor(book.getAuthor());
        dto.setId(book.getId());
        dto.setPublicationYear(dto.getPublicationYear());
        dto.setPublisher(book.getPublisher());
        dto.setSummary(book.getSummary());
        dto.setTitle(book.getTitle());
        dto.setComments(new ArrayList<>());
        for(Comment comment: book.getComments() ) {
        	dto.getComments().add(new CommentResponseDto(comment.getId(), comment.getUser().getNick(),
        			comment.getText(), comment.getScore()));
        }
        return dto;
    }

    public BookDetailsResponseDto findById(Integer bookId) {
        Book book = this.bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
//        book.setComments(this.commentRepository.findByBookId(bookId));
        BookDetailsResponseDto dto =  new BookDetailsResponseDto();
        dto.setAuthor(book.getAuthor());
        dto.setId(book.getId());
        dto.setPublicationYear(dto.getPublicationYear());
        dto.setPublisher(book.getPublisher());
        dto.setSummary(book.getSummary());
        dto.setTitle(book.getTitle());
        dto.setComments(new ArrayList<>());
        for(Comment comment: book.getComments() ) {
        	dto.getComments().add(new CommentResponseDto(comment.getId(), comment.getUser().getNick(),
        			comment.getText(), comment.getScore()));
        }
        return dto;
    }
}
