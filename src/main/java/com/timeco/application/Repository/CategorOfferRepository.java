package com.timeco.application.Repository;

import com.timeco.application.model.category.CategoryOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorOfferRepository extends JpaRepository<CategoryOffer,Long> {

}

