package learn.ai.tinder.service

import kotlinx.coroutines.future.await
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import learn.ai.tinder.config.KafkaConfig
import learn.ai.tinder.dto.UserProfileDto
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord

/**
 * Service for producing Kafka messages.
 * Uses both Spring Kafka and Reactor Kafka for different use cases.
 */
@Service
class KafkaProducerService(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val kafkaSender: KafkaSender<String, String>,
    private val json: Json
) {
    private val logger = LoggerFactory.getLogger(KafkaProducerService::class.java)

    /**
     * Send a user profile update event to Kafka.
     * Uses Spring Kafka for simple sending.
     *
     * @param userProfileDto The user profile data to send
     * @return True if the message was sent successfully
     */
    suspend fun sendProfileUpdate(userProfileDto: UserProfileDto): Boolean {
        return try {
            val profileJson = json.encodeToString(userProfileDto)
            kafkaTemplate.send(
                KafkaConfig.PROFILES_TOPIC,
                userProfileDto.userId,
                profileJson
            ).await()
            logger.info("Sent profile update for user: ${userProfileDto.userId}")
            true
        } catch (e: Exception) {
            logger.error("Error sending profile update for user: ${userProfileDto.userId}", e)
            false
        }
    }

    /**
     * Send a match event to Kafka.
     * Uses Reactor Kafka for reactive sending.
     *
     * @param userId1 The first user ID in the match
     * @param userId2 The second user ID in the match
     * @return True if the message was sent successfully
     */
    suspend fun sendMatchEvent(userId1: String, userId2: String): Boolean {
        val matchData = mapOf(
            "matchId" to "${userId1}_${userId2}",
            "userId1" to userId1,
            "userId2" to userId2,
            "timestamp" to System.currentTimeMillis()
        )
        
        return try {
            val matchJson = json.encodeToString(matchData)
            val record = SenderRecord.create(
                KafkaConfig.MATCHES_TOPIC,
                0,
                null,
                "${userId1}_${userId2}",
                matchJson,
                null
            )
            
            kafkaSender.send(listOf(record).toIterable())
                .next()
                .awaitSingle()
                
            logger.info("Sent match event for users: $userId1 and $userId2")
            true
        } catch (e: Exception) {
            logger.error("Error sending match event for users: $userId1 and $userId2", e)
            false
        }
    }

    /**
     * Send a message event to Kafka.
     * Uses Spring Kafka for simple sending.
     *
     * @param senderId The sender's user ID
     * @param recipientId The recipient's user ID
     * @param content The message content
     * @return True if the message was sent successfully
     */
    suspend fun sendMessageEvent(senderId: String, recipientId: String, content: String): Boolean {
        val messageData = mapOf(
            "messageId" to "${System.currentTimeMillis()}_${senderId}_${recipientId}",
            "senderId" to senderId,
            "recipientId" to recipientId,
            "content" to content,
            "timestamp" to System.currentTimeMillis()
        )
        
        return try {
            val messageJson = json.encodeToString(messageData)
            kafkaTemplate.send(
                KafkaConfig.MESSAGES_TOPIC,
                "$senderId-$recipientId",
                messageJson
            ).await()
            logger.info("Sent message from $senderId to $recipientId")
            true
        } catch (e: Exception) {
            logger.error("Error sending message from $senderId to $recipientId", e)
            false
        }
    }
}
