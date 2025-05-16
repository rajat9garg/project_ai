package learn.ai.tinder.config

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ContainerProperties
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions
import java.util.*

/**
 * Configuration for Kafka producers, consumers, and topics.
 * Sets up both Spring Kafka and Reactor Kafka components.
 */
@Configuration
@EnableKafka
class KafkaConfig(
    @Value("\${spring.kafka.bootstrap-servers}") private val bootstrapServers: String,
    @Value("\${spring.kafka.consumer.group-id}") private val groupId: String,
    @Value("\${spring.kafka.consumer.auto-offset-reset}") private val autoOffsetReset: String
) {

    // Common Kafka topics
    companion object {
        const val MATCHES_TOPIC = "tinder-matches"
        const val MESSAGES_TOPIC = "tinder-messages"
        const val PROFILES_TOPIC = "tinder-profiles"
    }

    /**
     * Kafka admin client configuration.
     */
    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val configs: MutableMap<String, Any> = HashMap()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        return KafkaAdmin(configs)
    }

    /**
     * Creates the matches topic.
     */
    @Bean
    fun matchesTopic(): NewTopic {
        return TopicBuilder.name(MATCHES_TOPIC)
            .partitions(3)
            .replicas(1)
            .build()
    }

    /**
     * Creates the messages topic.
     */
    @Bean
    fun messagesTopic(): NewTopic {
        return TopicBuilder.name(MESSAGES_TOPIC)
            .partitions(3)
            .replicas(1)
            .build()
    }

    /**
     * Creates the profiles topic.
     */
    @Bean
    fun profilesTopic(): NewTopic {
        return TopicBuilder.name(PROFILES_TOPIC)
            .partitions(3)
            .replicas(1)
            .build()
    }

    /**
     * Producer configuration for Spring Kafka.
     */
    @Bean
    fun producerConfigs(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.ACKS_CONFIG] = "all"
        return props
    }

    /**
     * Producer factory for Spring Kafka.
     */
    @Bean
    fun producerFactory(): ProducerFactory<String, String> {
        return DefaultKafkaProducerFactory(producerConfigs())
    }

    /**
     * Kafka template for Spring Kafka.
     */
    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(producerFactory())
    }

    /**
     * Consumer configuration for Spring Kafka.
     */
    @Bean
    fun consumerConfigs(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = autoOffsetReset
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        return props
    }

    /**
     * Consumer factory for Spring Kafka.
     */
    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> {
        return DefaultKafkaConsumerFactory(consumerConfigs())
    }

    /**
     * Kafka listener container factory for Spring Kafka.
     */
    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory()
        factory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL_IMMEDIATE
        return factory
    }

    /**
     * Reactor Kafka sender options.
     */
    @Bean
    fun senderOptions(): SenderOptions<String, String> {
        return SenderOptions.create(producerConfigs())
    }

    /**
     * Reactor Kafka sender.
     */
    @Bean
    fun kafkaSender(senderOptions: SenderOptions<String, String>): KafkaSender<String, String> {
        return KafkaSender.create(senderOptions)
    }

    /**
     * Reactor Kafka receiver options.
     */
    @Bean
    fun receiverOptions(): ReceiverOptions<String, String> {
        return ReceiverOptions.create<String, String>(consumerConfigs())
    }

    /**
     * Reactor Kafka receiver for a specific topic.
     */
    @Bean
    fun kafkaReceiver(receiverOptions: ReceiverOptions<String, String>, topic: String): KafkaReceiver<String, String> {
        return KafkaReceiver.create(receiverOptions.subscription(Collections.singleton(topic)))
    }
}
