package com.timeco.application.Repository;

import com.timeco.application.model.order.PurchaseOrder;
import com.timeco.application.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Long> {

    List<PurchaseOrder> findByUser(User user);

    List<PurchaseOrder> findByOrderedDateAfterAndOrderedDateBefore(LocalDateTime startOfWeek, LocalDateTime endOfWeek);





//    List<PurchaseOrder> findAllByOrderStatus(String orderStatus);
//
//    Collection<Object> findAllByOrderedDate(LocalDate date);
}
