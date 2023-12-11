package com.timeco.application.Repository;

import com.timeco.application.model.category.CategoryOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryOfferRepository extends JpaRepository<CategoryOffer,Long> {

//    List<CategoryOffer> findAllIsEnabledTrue();

}
