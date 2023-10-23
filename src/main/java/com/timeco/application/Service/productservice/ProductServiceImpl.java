package com.timeco.application.Service.productservice;

import com.timeco.application.Dto.ProductDto;
import com.timeco.application.Repository.CategoryRepository;
import com.timeco.application.Repository.ProductRepository;
import com.timeco.application.model.category.Category;
import com.timeco.application.model.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;



    @Override
    @Transactional
    public Product addProduct(Product product) {

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void updateProductById(Long id, ProductDto products) {

        // Retrieve the existing Product from the database
        Product product = productRepository.findById(id).orElse(null);

        // Retrieve the Category based on categoryId from the ProductDto
        Category category = categoryRepository.findById(products.getCategoryId()).orElse(null);


        if (product != null && category != null) {
            product.setProductName(products.getProductName());
            product.setDescription(products.getDescription());
            product.setCurrent_state(products.getCurrent_state());
            product.setPrice(products.getPrice());
            product.setQuantity(products.getQuantity());

            // Set the Category on the Product
            product.setCategory(category);
        }

        // Save the updated Product
        productRepository.save(product);
    }

    @Override
    public void deleteProductById(Long id) {

        this.productRepository.deleteById(id);

    }

    @Override
    public List<Product> getAllProducts(){

        return productRepository.findAll();
    }

    @Override
    public List<Product> searchProducts(String searchTerm) {

       return productRepository.findByProductNameContaining(searchTerm);
    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

//    @Override
//    public List<ProductImageDto> getAllProductsWithImages() {
//        List<Product> products = productRepository.findAll(); // Retrieve all products
//
//        // Create a list to store ProductWithImagesDto objects
//        List<ProductImageDto> productsWithImages = new ArrayList<>();
//
//        // Iterate through products and convert them to ProductWithImagesDto
//        for (Product product : products) {
//            ProductImageDto productWithImages = new ProductImageDto();
//            productWithImages.setProductId(product.getId());
//            productWithImages.setProductName(product.getProductName());
//            productWithImages.setDescription(product.getDescription());
//            productWithImages.setQuantity(product.getQuantity());
//            productWithImages.setPrice(product.getPrice());
//
//            // Retrieve image data for the current product
////            List<byte[]> imageDatas = new ArrayList<>();
////            for (ProductImage productImage : product.getProductImages()) {
////                imageDatas.add(productImage.getImageData());
////            }
////            productWithImages.setImageDatas(imageDatas);
//
//            // Add the productWithImages to the list
//            productsWithImages.add(productWithImages);
//        }
//
//        return productsWithImages;
//    }


}
