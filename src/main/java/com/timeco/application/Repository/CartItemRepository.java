package com.timeco.application.Repository;

import com.timeco.application.model.cart.Cart;
import com.timeco.application.model.cart.CartItem;
import com.timeco.application.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {


}
