package com.timeco.application.Repository;

import com.timeco.application.model.order.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository  extends JpaRepository<WishList,Long> {
}
