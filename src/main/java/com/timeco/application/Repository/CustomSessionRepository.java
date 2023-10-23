package com.timeco.application.Repository;

import com.timeco.application.model.user.CustomSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomSessionRepository extends JpaRepository<CustomSession,String> {
}
