package com.timeco.application.Repository;

import com.timeco.application.model.user.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User,Long> {
    @Query
    User findByEmail(String email);

   List<User> findByEmailContainingOrFirstNameContaining(String email,String firstName);


    User findByReferralCode(String referral);

//    Long countByBlockedUsers(boolean isBlocked);
}
