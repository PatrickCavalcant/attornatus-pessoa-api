package com.attornatus.api.repositories;

import com.attornatus.api.entities.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    List<Endereco> findByEmpresaId(Long id);

    List<Endereco> findByPessoaId(Long id);

}
