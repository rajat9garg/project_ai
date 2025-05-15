package learn.ai.elasticsearch.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "documents")
data class Document(
    @Id
    val id: String? = null,
    val title: String,
    val content: String,
    val tags: List<String> = emptyList(),
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)
