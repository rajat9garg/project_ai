package learn.ai.elasticsearch.controller

import kotlinx.coroutines.flow.Flow
import learn.ai.elasticsearch.model.Document
import learn.ai.elasticsearch.service.DocumentService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/documents")
class DocumentController(private val documentService: DocumentService) {

    @GetMapping("/{id}")
    suspend fun getDocumentById(@PathVariable id: String): Document {
        return documentService.findById(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "Document with id $id not found"
        )
    }

    @GetMapping
    fun getAllDocuments(): Flow<Document> {
        return documentService.findAll()
    }

    @GetMapping("/search/title")
    fun getDocumentsByTitle(@RequestParam title: String): Flow<Document> {
        return documentService.findByTitle(title)
    }

    @GetMapping("/search/tag")
    fun getDocumentsByTag(@RequestParam tag: String): Flow<Document> {
        return documentService.findByTag(tag)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createDocument(@RequestBody document: Document): Document {
        if (document.id != null) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Document ID must be null for creation"
            )
        }
        return documentService.save(document)
    }

    @PutMapping("/{id}")
    suspend fun updateDocument(
        @PathVariable id: String,
        @RequestBody document: Document
    ): Document {
        if (document.id != id) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Document ID in path and body must match"
            )
        }
        return documentService.save(document)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deleteDocument(@PathVariable id: String) {
        if (!documentService.delete(id)) {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND, "Document with id $id not found"
            )
        }
    }
}
