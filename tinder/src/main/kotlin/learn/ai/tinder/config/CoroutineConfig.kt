package learn.ai.tinder.config

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration for Kotlin coroutines in the application.
 * Provides beans for application-wide coroutine scopes and exception handlers.
 */
@Configuration
class CoroutineConfig {
    
    private val logger = LoggerFactory.getLogger(CoroutineConfig::class.java)
    
    /**
     * Creates a global exception handler for coroutines that logs exceptions.
     */
    @Bean
    fun coroutineExceptionHandler() = CoroutineExceptionHandler { _, throwable ->
        logger.error("Uncaught coroutine exception", throwable)
    }
    
    /**
     * Creates an application-wide CoroutineScope for IO-bound operations.
     * Uses a SupervisorJob to prevent child coroutine failures from affecting siblings.
     */
    @Bean
    fun ioCoroutineScope(exceptionHandler: CoroutineExceptionHandler): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO + exceptionHandler)
    
    /**
     * Creates an application-wide CoroutineScope for CPU-bound operations.
     * Uses a SupervisorJob to prevent child coroutine failures from affecting siblings.
     */
    @Bean
    fun defaultCoroutineScope(exceptionHandler: CoroutineExceptionHandler): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default + exceptionHandler)
}
