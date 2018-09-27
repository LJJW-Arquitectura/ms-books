package com.microservice.repository;

import com.microservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface BookRepository extends JpaRepository<Book, Long>
{
  List<Book> findByTitle(String title);
}
