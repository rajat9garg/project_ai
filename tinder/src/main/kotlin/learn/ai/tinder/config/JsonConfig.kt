package learn.ai.tinder.config

import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration for JSON serialization/deserialization.
 * Provides a centralized Json instance for consistent handling across the application.
 */
@Configuration
class JsonConfig {

    /**
     * Creates a Json instance with specific configuration settings.
     * This bean can be injected wherever JSON serialization/deserialization is needed.
     */
    @Bean
    fun json(): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = false
            encodeDefaults = true
            coerceInputValues = true
        }
    }
}
