package com.compo.repository;

import com.compo.entity.Interpretacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterpretacionRepository extends JpaRepository<Interpretacion, Long> {

    @Query("SELECT i FROM Interpretacion i JOIN FETCH i.obra JOIN FETCH i.interprete JOIN FETCH i.director WHERE i.activo = true")
    List<Interpretacion> findByActivoTrue();

    @Query("SELECT i FROM Interpretacion i JOIN FETCH i.obra JOIN FETCH i.interprete JOIN FETCH i.director WHERE i.interprete.id = :id AND i.activo = true")
    List<Interpretacion> findByInterpreteIdAndActivoTrue(@Param("id") Long id);

    @Query("SELECT i FROM Interpretacion i JOIN FETCH i.obra JOIN FETCH i.interprete JOIN FETCH i.director WHERE i.director.id = :id AND i.activo = true")
    List<Interpretacion> findByDirectorIdAndActivoTrue(@Param("id") Long id);
}
