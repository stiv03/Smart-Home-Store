package com.ninjas.gig.repository;

import com.ninjas.gig.model.Category;
import com.ninjas.gig.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
