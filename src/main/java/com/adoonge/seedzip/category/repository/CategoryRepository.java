package com.adoonge.seedzip.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adoonge.seedzip.category.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
