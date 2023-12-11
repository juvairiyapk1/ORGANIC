package com.timeco.application.Service.categoryservice;

import com.timeco.application.Dto.CategoryOfferDto;
import com.timeco.application.Repository.CategoryOfferRepository;
import com.timeco.application.Repository.CategoryRepository;
import com.timeco.application.Repository.ProductRepository;
import com.timeco.application.model.category.Category;
import com.timeco.application.model.category.CategoryOffer;
import com.timeco.application.model.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class CategoryOfferServiceImpl implements CategoryOfferService{

    @Autowired
    private CategoryOfferRepository categoryOfferRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void addCategoryOffer(CategoryOfferDto categoryOfferDto, Long userId) {
        CategoryOffer categoryOffer=new CategoryOffer();
        categoryOffer.setCategoryOfferName(categoryOfferDto.getCategoryOfferName());
        categoryOffer.setStartDate(categoryOfferDto.getStartDate());
        categoryOffer.setExpiryDate(categoryOfferDto.getExpiryDate());
        categoryOffer.setDiscountCategoryOffer(categoryOfferDto.getDiscountCategoryOffer());
        categoryOffer.setActive(true);
        categoryOfferRepository.save(categoryOffer);
    }

    @Override
    public void updateProductOffer(CategoryOfferDto categoryOfferDto, Long categoryOfferId) {

        CategoryOffer updateCategoryOffer=categoryOfferRepository.findById(categoryOfferId).orElse(null);
        assert updateCategoryOffer != null;
        updateCategoryOffer.setCategoryOfferName(categoryOfferDto.getCategoryOfferName());
        updateCategoryOffer.setStartDate(categoryOfferDto.getStartDate());
        updateCategoryOffer.setExpiryDate(categoryOfferDto.getExpiryDate());
        updateCategoryOffer.setDiscountCategoryOffer(categoryOfferDto.getDiscountCategoryOffer());
        updateCategoryOffer.setActive(categoryOfferDto.isActive());
        categoryOfferRepository.save(updateCategoryOffer);

    }

    @Override
    public void activeOffer(Long categoryOfferId) {
        CategoryOffer active=categoryOfferRepository.findById(categoryOfferId).get();
        active.setActive(true);
        categoryOfferRepository.save(active);

    }

    @Override
    public void nonActiveOffer(Long categoryOfferId) {
        CategoryOffer nonActive=categoryOfferRepository.findById(categoryOfferId).get();
        nonActive.setActive(false);
        categoryOfferRepository.save(nonActive);

    }

    @Override
    public void addCategoryOfferToCategory(Long categoryOfferId, Long[] categoryIds) {
        CategoryOffer categoryOffer = categoryOfferRepository.findById(categoryOfferId)
                .orElseThrow(() -> new EntityNotFoundException("Category offer not found"));

        for (Long categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if (category != null) {
                List<Product> products = productRepository.findByCategory(category);
                for (Product product : products) {
                    if (product != null) {
                        if (product.getCategoryOffer() != null) {
                            throw new IllegalStateException("Product already has a category offer");
                        }

                        Double discountedPrice = calculateDiscountedPrice(product.getPrice(), categoryOffer.getDiscountCategoryOffer());

                        product.setCategoryOffer(categoryOffer);

                        // Check if there's no existing product offer and apply the new discounted price
                        if (product.getProductOffer() == null && (product.getDiscountedPrice() == null || product.getDiscountedPrice() < discountedPrice)) {
                            product.setDiscountedPrice(discountedPrice);
                            System.out.println("jubi===============");
//                            productRepository.save(product);
                        }
                    }
                    productRepository.save(product);
                }


            } else {
                throw new EntityNotFoundException("Category not found for ID: " + categoryId);
            }
        }
    }




    private Double calculateDiscountedPrice(Double price, Double discountCategoryOffer) {
        return price-(price*discountCategoryOffer/100);
    }


}
