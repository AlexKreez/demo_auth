package com.example.demo.repository;

import com.example.demo.domain.entityUser.RequestEntity;
import com.example.demo.domain.entityUser.User;
import com.example.demo.domain.inventory.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
//    List<RequestEntity> findByStatus(RequestStatus status);
    List<RequestEntity> findByRequestedBy(User user);
}

