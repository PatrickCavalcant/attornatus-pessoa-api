package com.attornatus.api.repositories;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.attornatus.api.entities.Pessoa;

@Transactional(readOnly = true)
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
