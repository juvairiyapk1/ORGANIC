package com.timeco.application.web.admincontrollers;


import com.timeco.application.Dto.ProductOfferDto;
import com.timeco.application.Repository.CategoryOfferRepository;
import com.timeco.application.Repository.ProductOfferRepository;
import com.timeco.application.Service.categoryservice.CategoryOfferService;
import com.timeco.application.Service.productservice.ProductOfferService;
import com.timeco.application.Service.productservice.ProductService;
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
public class AdminOfferManagementController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductOfferService productOfferService;

    @Autowired
    private ProductOfferRepository productOfferRepository;

    @Autowired
    private CategoryOfferRepository categoryOfferRepository;

    @Autowired
    private CategoryOfferService categoryOfferService;

    @GetMapping("/productOfferManagement")
    public String showOfferList(Model model){
        List<ProductOffer>productOffers=productOfferRepository.findAll();
        model.addAttribute("productOffers",productOffers);

        List<Product>productList=productService.getAllProducts();
        model.addAttribute("products",productList);
        return "ProductOffer";
    }

    @PostMapping("/addProductOffer")
    public String addProductOffer(@ModelAttribute("productOffer") ProductOfferDto productOfferDto,Long userId){
        productOfferService.addProductOffer(productOfferDto,userId);

        return "redirect:/admin/productOfferManagement";
    }

    @PostMapping("/editProductOffer/{productOfferId}")
    public String updateProductOffer(@PathVariable Long productOfferId,@ModelAttribute("productOffer")ProductOfferDto productOfferDto)
    {
        productOfferService.updateProductOffer(productOfferDto,productOfferId);
        return "redirect:/admin/productOfferManagement";
    }

    @GetMapping("/activeOffer/{productOfferId}")
    public String activeProductOffer(@PathVariable Long productOfferId){

        productOfferService.activeOffer(productOfferId);
        System.out.println("this is active condition======================");
        return "redirect:/admin/productOfferManagement";
    }

    @GetMapping("/nonActiveOffer/{productOfferId}")
    public String nonActiveOffer(@PathVariable Long productOfferId){

        productOfferService.nonActiveOffer(productOfferId);
        System.out.println("this is nonActive condition==========================");
        return "redirect:/admin/productOfferManagement";
    }


    @PostMapping("/addProductOffer/{productOfferId}")
    public String addProductOfferToProduct(@PathVariable Long productOfferId,@RequestParam(name="selectedProducts")Long[] productId,Model model)
    {
        try {
            productOfferService.addProductOfferToProduct(productOfferId, productId);
            model.addAttribute("successMessage", "Product offer added successfully");
        }
        catch (IllegalStateException | EntityNotFoundException e){
            model.addAttribute("errorMessage",e.getMessage());
        }
        return "redirect:/admin/productOfferManagement";
    }

}
