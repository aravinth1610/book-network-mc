package com.bookInventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookInventory.modal.BookInventory;

@Repository
public interface BookInventoryRepository extends JpaRepository<BookInventory, Long> {

}
