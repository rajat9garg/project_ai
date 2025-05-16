package learn.ai.tinder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class TinderApplication

fun main(args: Array<String>) {
	runApplication<TinderApplication>(*args)
}
