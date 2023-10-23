package com.timeco.application.web.admincontrollers;

import com.timeco.application.Dto.ProductDto;
import com.timeco.application.Repository.CategoryRepository;
import com.timeco.application.Repository.ProductRepository;
import com.timeco.application.Repository.SubCategoryRepository;
import com.timeco.application.Service.categoryservice.CategoryService;
import com.timeco.application.Service.categoryservice.SubCategoryService;
import com.timeco.application.Service.productservice.ProductService;
import com.timeco.application.model.category.Category;
import com.timeco.application.model.category.Subcategory;
import com.timeco.application.model.product.Product;
import com.timeco.application.model.product.ProductImage;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.Id;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;
//    @Autowired
//    private ProductImageService productImageService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private SubCategoryService subCategoryService;



    @GetMapping("/listProducts")
    public String productList(Model model){
        List<Product> product = productService.getAllProducts();
        model.addAttribute("product", product);
        return "productList";
    }



   //  Product -------------

//    @GetMapping("/addProducts")
//    public String addproductsForm(Model model)
//    {
//        ProductDto productDto=new ProductDto();
//        model.addAttribute("product",productDto);
//        model.addAttribute("categories",categoryRepository.findByIsListed(false));
//        return "add-product";
//    }
@GetMapping("/addProducts")
public String addproductsForm(Model model) {
    ProductDto productDto = new ProductDto();
    model.addAttribute("product", productDto);

    // Retrieve all categories that are listed (you can adjust this query as needed)
    List<Category> categories = categoryRepository.findAll();
    model.addAttribute("categories", categories);

    // Retrieve all subcategories that are listed (you can adjust this query as needed)
    List<Subcategory> subcategories = subCategoryRepository.findAll();
    model.addAttribute("subcategories", subcategories);

    return "add-product";
}
    @PostMapping("/addProducts")
    public String addProductstoDatabase(@ModelAttribute("product") ProductDto productDto,  RedirectAttributes redirectAttributes) {

            Category category = categoryService.getCategoryById(productDto.getCategoryId());
            Subcategory subcategory = subCategoryService.getSubCategoryById(productDto.getSubcategoryId());

            if (category == null) {
                // Handle the case where the category doesn't exist
                redirectAttributes.addFlashAttribute("error", "Category not found.");
                return "redirect:/admin/listProducts";
            }

            // Create a new Product instance and set its properties
            Product product = new Product();
            product.setProductName(productDto.getProductName());
            product.setCurrent_state(productDto.getCurrent_state());
            product.setDescription(productDto.getDescription());
            product.setQuantity(productDto.getQuantity());
            product.setPrice(productDto.getPrice());
            product.setCategory(category);
            product.setSubcategory(subcategory);
            product.setProductImages(productDto.getProductImages());

            // Save the Product entity to the database
            productService.addProduct(product);


//        } catch (IOException e) {
//            // Handle the exception (e.g., log it, show an error message)
//            redirectAttributes.addFlashAttribute("error", "Failed to add product.");
//        }

        return "redirect:/admin/listProducts";
    }



    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Long id){

        productService.deleteProductById(id);

        return "redirect:/admin/listProducts";
    }
    @GetMapping("/updateProduct/{id}")
    public String updateProduct(@PathVariable Long id,Model model){

        Product product=productRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("in valid product Id: "+id));
        model.addAttribute("category",categoryRepository.findAll());
        model.addAttribute("product",product);
        model.addAttribute("pro",id);

            return "editProduct";

    }
    @PostMapping("/updateProduct/{id}")
    public String updateProduct(@ModelAttribute ProductDto updatedProduct,@PathVariable Long id) {
        productService.updateProductById(id,updatedProduct);

        return "redirect:/admin/listProducts";
    }
    @GetMapping("/searchProduct")
    public String searchProducts(@RequestParam("searchTerm") String searchTerm, Model model) {
        List<Product> products = productService.searchProducts(searchTerm);
        model.addAttribute("product", products);
        return "productList";
    }
}
