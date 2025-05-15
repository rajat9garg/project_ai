package learn.ai.elasticsearch

import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ElasticSearchApplication

fun main(args: Array<String>) = runBlocking {
    runApplication<ElasticSearchApplication>(*args)
}
