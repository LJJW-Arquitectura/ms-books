package com.microservice.repository;

import com.microservice.entity.Author;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long>
{
    List<Author> findByName(String name);
}
