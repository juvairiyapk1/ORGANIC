package com.timeco.application.Repository;

import com.timeco.application.model.cart.Cart;
import com.timeco.application.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {


    Cart findByUser(User user);

}
