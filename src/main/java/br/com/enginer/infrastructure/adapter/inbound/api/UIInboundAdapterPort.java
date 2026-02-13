package br.com.enginer.infrastructure.adapter.inbound.api;

import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.enginer.domain.system.usecase.core.annotation.instance.UIDomain;
import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;
import br.com.enginer.domain.system.usecase.core.schema.Form;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.port.outbound.logger.LoggerOutboundPort;

/**
 * Endpoint único para renderização de templates via registry.
 *
 * Exemplos:
 *  GET /v1/enginer-ui/module/tab
 *  GET /v1/enginer-ui/module/form/123
 */
@RestController
@RequestMapping("/v1/enginer-ui/module")
public class UIInboundAdapterPort {

    private final UIInboundTemplateRegistry registry;
    private final LoggerOutboundPort logger;

    public UIInboundAdapterPort(UIInboundTemplateRegistry registry, LoggerOutboundPort logger) {
        this.registry = registry;
        this.logger = logger;
    }

    @GetMapping({ "/{template}", "/{template}/{id}" })
    public ResponseEntity<Form> render(@PathVariable String template, @UIDomain Domain<?> domain) {

        TypeTemplate type = TypeTemplate.from(template);
        
        if (type == null) {
            return ResponseEntity.badRequest().build();
        }

        Function<Domain<?>, Form> handler = registry.get(type);
        if (handler == null) {
            return ResponseEntity.notFound().build();
        }

        String label = "template-" + type.name().toLowerCase();
        return execute(label, () -> handler.apply(domain));
    }

    private ResponseEntity<Form> execute(String label, Supplier<Form> supplier) {
    	
        long startNs = System.nanoTime();

        try {
            Form form = supplier.get();
            return ResponseEntity.ok(form);

        } catch (Exception ex) {
            logger.error(UIInboundAdapterPort.class, "Erro ao criar [" + label + "]", ex);
            return ResponseEntity.internalServerError().build();

        } finally {
            long durationMs = (System.nanoTime() - startNs) / 1_000_000L;
            logger.info(UIInboundAdapterPort.class, "[" + label + "] processado em " + durationMs + "ms ");
        }
    }
}