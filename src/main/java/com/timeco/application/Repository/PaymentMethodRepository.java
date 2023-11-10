package com.timeco.application.Repository;

import com.timeco.application.model.cart.CartItem;
import com.timeco.application.model.order.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod,Long> {

}
