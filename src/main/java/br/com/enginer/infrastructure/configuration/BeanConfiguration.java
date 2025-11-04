package br.com.enginer.infrastructure.configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
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

import br.com.enginer.domain.system.usercase.ActionInboundUserCase;
import br.com.enginer.domain.system.usercase.SubscriberInboundUserCase;
import br.com.enginer.domain.system.usercase.UIInboundUserCase;
import br.com.enginer.domain.system.usercase.port.inbound.api.ActionInboundPort;
import br.com.enginer.domain.system.usercase.port.inbound.api.UIInboundPort;
import br.com.enginer.domain.system.usercase.port.inbound.subscriber.SubscriberInboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.publisher.PublisherOutboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.repository.RepositoryOutboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.storage.FileStorageOutboundPort;
import br.com.enginer.domain.system.usercase.schema.field.type.Id;
import br.com.enginer.domain.system.usercase.schema.instance.Domain;
import br.com.enginer.infrastructure.configuration.deserializer.SafeLocalDateDeserializer;
import br.com.enginer.infrastructure.configuration.deserializer.SafeLocalDateTimeDeserializer;
import br.com.enginer.infrastructure.configuration.deserializer.SafeLocalTimeDeserializer;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class BeanConfiguration {
	
	@Value("${aws.s3.region:us-east-1}")
	private String awsRegion;

	/**
	 * @return
	 */
	@Bean
	@Primary
	ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();

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

		// MixIn personalizado (se necessário para Id)
		mapper.addMixIn(Id.class, IdAbstractMixIn.class);

		return mapper;
	}
	
	@Bean
	S3Client s3Client() {
		return S3Client.builder()
			.region(Region.of(awsRegion))
			.credentialsProvider(DefaultCredentialsProvider.create())
			.build();
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
		return new UIInboundUserCase<Domain<?>>(logger, repositoryOutboundPort, publisherOutboundPort);
	}

	/**
	 * @param loggerOutboundPort
	 * @param repositoryOutboundPort
	 * @param publisherOutboundPort
	 * @param fileStorageOutboundPort
	 * @return
	 */
	@Bean
	ActionInboundPort<Domain<?>> actionInboundPort(LoggerOutboundPort loggerOutboundPort, RepositoryOutboundPort<Domain<?>> repositoryOutboundPort, PublisherOutboundPort<Domain<?>> publisherOutboundPort, FileStorageOutboundPort fileStorageOutboundPort) {
		return new ActionInboundUserCase<Domain<?>>(loggerOutboundPort, repositoryOutboundPort, publisherOutboundPort, fileStorageOutboundPort);
	}
	
	/**
	 * @param logger
	 * @param repositoryOutboundPort
	 * @param publisherOutboundPort
	 * @return
	 */
	@Bean
	SubscriberInboundPort<Domain<?>> subscriberInboundPort(LoggerOutboundPort logger, RepositoryOutboundPort<Domain<?>> repositoryOutboundPort, PublisherOutboundPort<Domain<?>> publisherOutboundPort) {
		return new SubscriberInboundUserCase<Domain<?>>(logger, repositoryOutboundPort, publisherOutboundPort);
	}
}
