package com.timeco.application.web.admincontrollers;


import com.timeco.application.model.product.ProductOffer;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminProductOfferController {

    @GetMapping("/productOfferList")
    public String showProductOfferList(Model model){
        List<ProductOffer>productOffers=productOfferService.getAllProductOffer();
        model.addAttribute("productOffers",productOffers);
        return"productOfferList";
    }
}
