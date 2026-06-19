package com.compo.repository;

import com.compo.entity.Obra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObraRepository extends JpaRepository<Obra, Long> {

    List<Obra> findByActivoTrue();

    @Query("SELECT DISTINCT o FROM Obra o LEFT JOIN FETCH o.compositores WHERE o.activo = true")
    List<Obra> findByActivoTrueWithCompositores();

    @Query("SELECT DISTINCT o FROM Obra o LEFT JOIN FETCH o.compositores WHERE o.id = :id")
    Optional<Obra> findByIdWithCompositores(Long id);
}
