package com.compo.repository;

import com.compo.entity.Compositor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompositorRepository extends JpaRepository<Compositor, Long> {

    List<Compositor> findByActivoTrue();
}
