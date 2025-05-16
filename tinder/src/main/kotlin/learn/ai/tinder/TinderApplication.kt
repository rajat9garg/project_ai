package learn.ai.tinder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TinderApplication

fun main(args: Array<String>) {
	runApplication<TinderApplication>(*args)
}

