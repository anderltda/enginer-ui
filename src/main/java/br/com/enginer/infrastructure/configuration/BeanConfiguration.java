package br.com.enginer.infrastructure.configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import br.com.enginer.domain.system.usecase.ActionInboundUseCase;
import br.com.enginer.domain.system.usecase.SubscriberInboundUseCase;
import br.com.enginer.domain.system.usecase.UIInboundUseCase;
import br.com.enginer.domain.system.usecase.port.inbound.api.ActionInboundPort;
import br.com.enginer.domain.system.usecase.port.inbound.api.UIInboundPort;
import br.com.enginer.domain.system.usecase.port.inbound.subscriber.SubscriberInboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.publisher.PublisherOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.RepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.TagRepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.UploadFileRepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.storage.FileStorageOutboundPort;
import br.com.enginer.domain.system.usecase.schema.instance.Domain;
import br.com.enginer.infrastructure.configuration.deserializer.SafeLocalDateDeserializer;
import br.com.enginer.infrastructure.configuration.deserializer.SafeLocalDateTimeDeserializer;
import br.com.enginer.infrastructure.configuration.deserializer.SafeLocalTimeDeserializer;

@Configuration
public class BeanConfiguration {
	
	/**
	 * @return
	 */
	@Bean
	@Primary
	ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		// Módulo de suporte a datas Java 8
		JavaTimeModule module = new JavaTimeModule();

		// Formatações desejadas
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		// Serializers e Deserializers
		module.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
		module.addDeserializer(LocalDate.class, new SafeLocalDateDeserializer()); 

		module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
		module.addDeserializer(LocalDateTime.class, new SafeLocalDateTimeDeserializer());
		
		module.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
		module.addDeserializer(LocalTime.class, new SafeLocalTimeDeserializer());

		mapper.registerModule(module);

		// Evita serializar datas como arrays
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		
		// Adiciona suporte para converter "" em null para objetos
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		
		// Ignorar propriedades desconhecidas para todas as deserializações
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		// Ignora campos nulos ou vazios
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

		// MixIn personalizado (se necessário para isIdNull)
	    mapper.addMixIn(Domain.class, DomainMixIn.class);

		return mapper;
	}
	
	/**
	 * @param builder
	 * @return
	 */
	@Bean
	WebClient webClient(ObjectMapper objectMapper) {
	    ExchangeStrategies strategies = ExchangeStrategies.builder().codecs(configurer -> configurer .defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper))).build();
	    return WebClient.builder()
	        .exchangeStrategies(strategies)
	        .build();
	}

	/**
	 * @param logger
	 * @param repositoryOutboundPort
	 * @return
	 */
	@Bean
	UIInboundPort<Domain<?>> uIInboundPort(LoggerOutboundPort logger, RepositoryOutboundPort<Domain<?>> repositoryOutboundPort, PublisherOutboundPort<Domain<?>> publisherOutboundPort) {
		return new UIInboundUseCase<Domain<?>>(logger, repositoryOutboundPort, publisherOutboundPort);
	}

	/**
	 * @param loggerOutboundPort
	 * @param uploadFileRepositoryOutboundPort
	 * @param tagRepositoryOutboundPort
	 * @param repositoryOutboundPort
	 * @param publisherOutboundPort
	 * @param fileStorageOutboundPort
	 * @return
	 */
	@Bean
	ActionInboundPort<Domain<?>> actionInboundPort(LoggerOutboundPort loggerOutboundPort, UploadFileRepositoryOutboundPort uploadFileRepositoryOutboundPort, TagRepositoryOutboundPort tagRepositoryOutboundPort, RepositoryOutboundPort<Domain<?>> repositoryOutboundPort, PublisherOutboundPort<Domain<?>> publisherOutboundPort, FileStorageOutboundPort fileStorageOutboundPort) {
		return new ActionInboundUseCase<Domain<?>>(loggerOutboundPort, uploadFileRepositoryOutboundPort, tagRepositoryOutboundPort, repositoryOutboundPort, publisherOutboundPort, fileStorageOutboundPort);
	}
	
	/**
	 * @param logger
	 * @param repositoryOutboundPort
	 * @param publisherOutboundPort
	 * @return
	 */
	@Bean
	SubscriberInboundPort<Domain<?>> subscriberInboundPort(LoggerOutboundPort logger, RepositoryOutboundPort<Domain<?>> repositoryOutboundPort, PublisherOutboundPort<Domain<?>> publisherOutboundPort) {
		return new SubscriberInboundUseCase<Domain<?>>(logger, repositoryOutboundPort, publisherOutboundPort);
	}
}
