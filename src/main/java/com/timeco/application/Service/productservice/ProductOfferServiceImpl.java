package com.timeco.application.Service.productservice;

import com.timeco.application.Dto.ProductOfferDto;
import com.timeco.application.Repository.ProductOfferRepository;
import com.timeco.application.Repository.ProductRepository;
import com.timeco.application.model.product.Product;
import com.timeco.application.model.product.ProductOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Access;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;

@Service
public class ProductOfferServiceImpl implements ProductOfferService{

    @Autowired
    private ProductOfferRepository productOfferRepository;

    @Autowired
    private ProductRepository productRepository;


    @Override
    public void addProductOffer(ProductOfferDto productOfferDto, Long userId) {
        ProductOffer productOffer=new ProductOffer();
        productOffer.setProductOfferName(productOfferDto.getProductOfferName());
        productOffer.setDiscountPercentage(productOfferDto.getDiscountPercentage());
        productOffer.setStartDate(productOfferDto.getStartDate());
        productOffer.setExpiryDate(productOfferDto.getExpiryDate());
        productOffer.setActive(true);
        productOfferRepository.save(productOffer);

    }

    @Override
    public void updateProductOffer(ProductOfferDto productOfferDto, Long productOfferId) {

        ProductOffer updateProductOffer=productOfferRepository.findById(productOfferId).orElse(null);
        assert updateProductOffer != null;
        updateProductOffer.setProductOfferName(productOfferDto.getProductOfferName());
        updateProductOffer.setStartDate(productOfferDto.getStartDate());
        updateProductOffer.setExpiryDate(productOfferDto.getExpiryDate());
        updateProductOffer.setDiscountPercentage(productOfferDto.getDiscountPercentage());
        updateProductOffer.setActive(productOfferDto.isActive());
        productOfferRepository.save(updateProductOffer);

    }

    @Override
    public void activeOffer(Long productOfferId) {
     ProductOffer active=productOfferRepository.findById(productOfferId).get();
     active.setActive(true);

     productOfferRepository.save(active);
    }

    @Override
    public void nonActiveOffer(Long productOfferId) {
        ProductOffer nonActive=productOfferRepository.findById(productOfferId).get();
        nonActive.setActive(false);

        productOfferRepository.save(nonActive);

    }

    @Override
    public void addProductOfferToProduct(Long productOfferId, Long[] productIds) {
        ProductOffer productOffer = productOfferRepository.findById(productOfferId).orElse(null);

        if (productOffer == null) {
            throw new EntityNotFoundException("Product offer not found");
        }

        for (Long productId : productIds) {
            Product product = productRepository.findById(productId).orElse(null);

            if (product != null) {
                if (product.getProductOffer() != null ) {
                    System.out.println("hello guys======================");
                    throw new IllegalStateException("Product already has a product offer");
                }

                Double discountedPrice = calculateDiscountedPrice(product.getPrice(), productOffer.getDiscountPercentage());
                // Check if there's a CategoryOffer and it's greater than the ProductOffer
                if (product.getCategory() != null && product.getCategory().getCategoryOffer() != null
                        && product.getCategory().getCategoryOffer().getDiscountCategoryOffer() > productOffer.getDiscountPercentage()) {
                    product.setDiscountedPrice(calculateDiscountedPrice(product.getPrice(), product.getCategory().getCategoryOffer().getDiscountCategoryOffer()));
                } else {
                    product.setDiscountedPrice(discountedPrice);
                }
                product.setProductOffer(productOffer);
                productRepository.save(product);
            } else {
                // Handle the case where a product is not found
                throw new EntityNotFoundException("Product not found for ID: " + productId);
            }
        }
    }

    private Double calculateDiscountedPrice(Double price, Double discountPercentage) {
        return price - (price * discountPercentage / 100);
    }


}
