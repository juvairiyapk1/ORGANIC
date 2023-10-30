package com.timeco.application.web.usercontrollers;

import com.timeco.application.Repository.ProductRepository;
import com.timeco.application.Service.productservice.ProductService;
import com.timeco.application.model.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;


    @GetMapping("/login")
    public String login()
    {
        return "login";
    }

    @GetMapping("/")
    public String home(Model model){
        List<Product> productList=productRepository.findAll();

        model.addAttribute("listProducts",productList);

        return"index";
    }


}
