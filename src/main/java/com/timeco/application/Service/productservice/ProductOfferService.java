package com.timeco.application.Service.productservice;

import com.timeco.application.Dto.ProductOfferDto;
import com.timeco.application.model.product.ProductOffer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductOfferService {
    void addProductOffer(ProductOfferDto productOfferDto, Long userId);


    void updateProductOffer(ProductOfferDto productOfferDto, Long productOfferId);

    void activeOffer(Long productOfferId);

    void nonActiveOffer(Long productOfferId);

    void addProductOfferToProduct(Long productOfferId,Long[] productIds);



}
