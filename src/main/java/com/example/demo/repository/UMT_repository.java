package com.example.demo.repository;

import com.example.demo.model.T_UMT;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UMT_repository extends JpaRepository<T_UMT, String> {
    List<T_UMT> findT_UMTByIdUMT(String id);
    T_UMT getT_UMTByIdUMT(String id);
    boolean existsT_UMTByIdUMT(String id);
}
