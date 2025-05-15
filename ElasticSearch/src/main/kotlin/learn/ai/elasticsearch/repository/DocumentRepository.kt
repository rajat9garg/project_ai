package learn.ai.elasticsearch.repository

import learn.ai.elasticsearch.model.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface DocumentRepository : ReactiveMongoRepository<Document, String> {
    fun findByTitleContaining(title: String): Flux<Document>
    fun findByTagsContaining(tag: String): Flux<Document>
}
