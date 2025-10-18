package br.com.enginer.infrastructure.adapter.outbound.comissao.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.com.enginer.infrastructure.adapter.outbound.comissao.entity.Comissao;

@Repository
public interface ComissaoRepository {

	Optional<Comissao> findByCodigo(String codigo);

}
