package pa165.project.bookservice.mappers;

import org.mapstruct.Mapper;
import pa165.project.bookservice.dto.BookDto;
import pa165.project.bookservice.model.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDto toDto(Book book);
    Book toModel(BookDto bookDto);
}
