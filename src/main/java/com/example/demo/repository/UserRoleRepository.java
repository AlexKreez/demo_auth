package com.example.demo.repository;

import com.example.demo.domain.entityUser.UserRole;
import com.example.demo.domain.entityUser.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
//    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId")
//    List<UserRole> findByUserId(@Param("userId") Long userId);
List<UserRole> findByUserId(Long userId);

}
