package com.timeco.application.web.admincontrollers;

import com.timeco.application.Dto.ProductDto;
import com.timeco.application.Repository.*;
import com.timeco.application.Service.categoryservice.CategoryService;
//import com.timeco.application.Service.categoryservice.SubCategoryService;
import com.timeco.application.Service.productservice.ProductOfferService;
import com.timeco.application.Service.productservice.ProductService;
import com.timeco.application.Service.userservice.UserService;
import com.timeco.application.model.cart.CartItem;
import com.timeco.application.model.category.Category;
//import com.timeco.application.model.category.Subcategory;
import com.timeco.application.model.product.Product;
import com.timeco.application.model.product.ProductImage;
import com.timeco.application.model.product.ProductOffer;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.Id;
import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminProductController {

    @Autowired
    private  CartItemRepository cartItemRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;
//    @Autowired
//    private SubCategoryRepository subCategoryRepository;
//    @Autowired
//    private SubCategoryService subCategoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductOfferRepository productOfferRepository;

    @Autowired
    private ProductOfferService productOfferService;

    public static final String UPLOAD_DIR = "/home/lenovo/Music/PROJECT/application/ORGANIC/src/main/resources/static/img";

    @GetMapping("/listProducts")
    public String productList(Model model){
        List<Product> product = productService.getAllProducts();
        model.addAttribute("product", product);
        return "productList";
    }



   //  Product -------------


@GetMapping("/addProducts")
public String addproductsForm(Model model) {
    ProductDto productDto = new ProductDto();
    model.addAttribute("product", productDto);

    // Retrieve all categories that are listed (you can adjust this query as needed)
    List<Category>unListedCategories=categoryRepository.findByIsListed(false);
    model.addAttribute("categories", unListedCategories);

    // Retrieve all subcategories that are listed (you can adjust this query as needed)
//    List<Subcategory> unListedSubCategories = subCategoryRepository.findByIsListed(false);
//    model.addAttribute("subcategories", unListedSubCategories);

    List<ProductOffer>activeProductOffer=productOfferRepository.findByIsActive(false);
    model.addAttribute("productOffer",activeProductOffer);

    return "add-product";
}

    @PostMapping("/addProducts")
    public String addProductstoDatabase(@ModelAttribute("product") ProductDto productDto,
                                        @RequestParam("file") MultipartFile file,
                                        RedirectAttributes redirectAttributes) {
        Category category = categoryService.getCategoryById(productDto.getCategoryId());
//        Subcategory subcategory = subCategoryService.getSubCategoryById(productDto.getSubcategoryId());



        try {
            // Handle the file upload
            byte[] imageBytes = file.getBytes();

            // Save the image to the specified directory
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.write(filePath, imageBytes);

            // Rest of your code...

            // Create a new Product instance and set its properties
            Product product = new Product();
            product.setProductName(productDto.getProductName());
            product.setCurrent_state(productDto.getCurrent_state());
            product.setDescription(productDto.getDescription());
            product.setQuantity(productDto.getQuantity());
            product.setPrice(productDto.getPrice());
            product.setCategory(category);
//            product.setSubcategory(subcategory);
            product.setProductImages(fileName);  // Save the file name to the Product entity

            // Save the Product entity to the database
            productService.addProduct(product);

        } catch (IOException e) {
            // Handle the exception (e.g., log it, show an error message)
            redirectAttributes.addFlashAttribute("error", "Failed to add product. " + e.getMessage());
        }

        return "redirect:/admin/listProducts";
    }



    @GetMapping("/blockProduct/{id}")
    public String listProducr(@PathVariable Long id) {

        productService.lockProduct(id);

        return "redirect:/admin/listProducts";
    }

    @GetMapping("/unblockProduct/{id}")
    public String unlistProduct(@PathVariable Long id) {

        productService.unlockProduct(id);

        return "redirect:/admin/listProducts";
    }


    @GetMapping("/updateProduct/{id}")
    public String updateProduct(@PathVariable Long id,Model model){

        Product product=productRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("in valid product Id: "+id));
        List<Category>unListedCategories=categoryRepository.findByIsListed(false);
        model.addAttribute("category",unListedCategories);
        model.addAttribute("product",product);
        model.addAttribute("pro",id);


            return "editProduct";

    }
    @PostMapping("/updateProduct/{id}")
    public String updateProduct(@ModelAttribute ProductDto updatedProduct,@PathVariable Long id,@RequestParam("file") MultipartFile file) throws IOException {
        productService.updateProductById(id,updatedProduct,file);

        return "redirect:/admin/listProducts";
    }
    @GetMapping("/searchProduct")
    public String searchProducts(@RequestParam("searchTerm") String searchTerm, Model model) {
        List<Product> products = productService.searchProducts(searchTerm);
        model.addAttribute("product", products);
        return "productList";
    }
}
