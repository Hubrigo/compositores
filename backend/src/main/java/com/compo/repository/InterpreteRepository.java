package com.compo.repository;

import com.compo.entity.Interprete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterpreteRepository extends JpaRepository<Interprete, Long> {

    List<Interprete> findByActivoTrue();
}
