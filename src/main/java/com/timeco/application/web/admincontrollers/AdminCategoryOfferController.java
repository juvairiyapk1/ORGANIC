package com.timeco.application.web.admincontrollers;

import com.timeco.application.Dto.CategoryOfferDto;
import com.timeco.application.Dto.ProductOfferDto;
import com.timeco.application.Repository.CategoryOfferRepository;
import com.timeco.application.Service.categoryservice.CategoryOfferService;
import com.timeco.application.Service.categoryservice.CategoryService;
import com.timeco.application.model.category.Category;
import com.timeco.application.model.category.CategoryOffer;
import com.timeco.application.model.product.Product;
import com.timeco.application.model.product.ProductOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminCategoryOfferController {

    @Autowired
    private CategoryOfferRepository categoryOfferRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryOfferService categoryOfferService;

    @GetMapping("/categoryOfferManagement")
    public String showOfferList(Model model){
        List<CategoryOffer> categoryOffers=categoryOfferRepository.findAll();
        model.addAttribute("categoryOffers",categoryOffers);

        List<Category>categoryList=categoryService.getAllCategory();
        model.addAttribute("categories",categoryList);
        return "categoryOffer";
    }

    @PostMapping("/addCategoryOffer")
    public String addCategoryOffer(@ModelAttribute("categoryOffer") CategoryOfferDto categoryOfferDto, Long userId){
        categoryOfferService.addCategoryOffer(categoryOfferDto,userId);

        return "redirect:/admin/categoryOfferManagement";
    }


    @PostMapping("/editCategoryOffer/{categoryOfferId}")
    public String updateCategoryOffer(@PathVariable Long categoryOfferId, @ModelAttribute("categoryOffer")CategoryOfferDto categoryOfferDto)
    {
        categoryOfferService.updateProductOffer(categoryOfferDto,categoryOfferId);
        return "redirect:/admin/categoryOfferManagement";
    }


    @GetMapping("/activeCategoryOffer/{categoryOfferId}")
    public String activeCategoryOffer(@PathVariable Long categoryOfferId){

        categoryOfferService.activeOffer(categoryOfferId);
        System.out.println("this is active condition======================");
        return "redirect:/admin/categoryOfferManagement";
    }

    @GetMapping("/nonActiveCategoryOffer/{categoryOfferId}")
    public String nonActiveCategoryOffer(@PathVariable Long categoryOfferId){

        categoryOfferService.nonActiveOffer(categoryOfferId);
        System.out.println("this is nonActive condition==========================");
        return "redirect:/admin/categoryOfferManagement";
    }


    @PostMapping("/addCategoryOffer/{categoryOfferId}")
    public String addCatgoryOfferToCategory(@PathVariable Long categoryOfferId,@RequestParam(name="selectedCategories")Long[] categoryId,Model model)
    {
        try {
            categoryOfferService.addCategoryOfferToCategory(categoryOfferId, categoryId);
            model.addAttribute("successMessage", "Product offer added successfully");

            System.out.println("hi============================");
        }
        catch (IllegalStateException | EntityNotFoundException e){
            model.addAttribute("errorMessage",e.getMessage());
        }
        return "redirect:/admin/categoryOfferManagement";
    }

}
