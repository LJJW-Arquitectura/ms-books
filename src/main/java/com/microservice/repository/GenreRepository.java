package com.microservice.repository;

import com.microservice.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface GenreRepository extends JpaRepository<Genre, Long>
{
    List<Genre> findByName(String name);
}
