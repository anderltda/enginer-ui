package br.com.enginer.domain.ui.port.inbound;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.InboundPort;
import br.com.enginer.domain.ui.dto.PageResult;
import br.com.enginer.domain.ui.dto.logger.ActionLogger;
import br.com.enginer.domain.ui.usercase.exception.CheckedException;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;

/**
 * Porta de entrada genérica para execução de ações de domínio (UserCases).
 * 
 * Esta interface é genérica o suficiente para suportar qualquer domínio que estenda {@link Domain},
 * incluindo ações padrão como busca, paginação, e execução de métodos de negócio dinâmicos.
 */
public interface ActionInboundPort<T extends Domain<?>> extends InboundPort {

    /**
     * Busca uma entidade pelo seu identificador.
     *
     * @param domain domínio com identificador preenchido
     * @return domínio encontrado (ou nulo se não encontrado)
     * @throws CheckedException em caso de erro de regra de negócio
     */
	Domain<?> searchWithById(Domain<?> domain) throws CheckedException;

    /**
     * Busca entidades com base em filtros dinâmicos.
     *
     * @param domain domínio base
     * @param filter mapa contendo os filtros aplicáveis
     * @return lista de domínios encontrados
     * @throws CheckedException em caso de erro
     */
    List<Domain<?>> searchByConditions(Domain<?> domain, Map<String, Object> filter) throws CheckedException;

    /**
     * Realiza uma busca paginada com base em filtros.
     *
     * @param domain domínio base
     * @param filter filtros e parâmetros de paginação
     * @return resultado paginado contendo lista e metadados
     * @throws CheckedException em caso de erro
     */
    PageResult<?> searchPaginated(Domain<?> domain, Map<String, Object> filter) throws CheckedException;

    /**
     * Executa uma busca paginada com base em um método específico do UserCase.
     *
     * @param domain domínio base
     * @param filter filtros aplicáveis
     * @param method nome do método a ser executado no caso de uso
     * @return resultado paginado
     * @throws CheckedException em caso de erro
     */
    PageResult<?> searchPaginatedByMethod(Domain<?> domain, Map<String, Object> filter, String method) throws CheckedException;

    /**
     * Executa uma ação genérica do domínio que pode retornar uma instância.
     *
     * @param domain domínio contendo dados de entrada
     * @return domínio atualizado ou resultado da ação
     * @throws CheckedException em caso de erro de execução
     */
    Domain<?> methodName(Domain<?> domain) throws CheckedException;

    /**
     * Executa uma ação genérica do domínio sobre uma lista de entidades.
     *
     * @param domain domínio principal
     * @param domains lista de domínios a serem processados
     * @param actionLogger log de auditoria da ação
     * @return lista de domínios processados
     * @throws CheckedException em caso de erro
     */
    List<Domain<?>> methodName(Domain<?> domain, List<Domain<?>> domains, ActionLogger actionLogger) throws CheckedException;
}