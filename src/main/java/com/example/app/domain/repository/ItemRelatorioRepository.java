package com.example.app.domain.repository;

import com.example.app.domain.model.ItemRelatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRelatorioRepository extends JpaRepository<ItemRelatorio, Long> {

}
