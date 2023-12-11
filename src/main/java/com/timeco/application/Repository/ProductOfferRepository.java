package com.timeco.application.Repository;

import com.timeco.application.model.product.ProductOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOfferRepository extends JpaRepository<ProductOffer,Long> {


    List<ProductOffer> findByIsActive(boolean IsActive);

//    List<ProductOffer> findByProductIdIn(List<Long> selectedProducts);

}
