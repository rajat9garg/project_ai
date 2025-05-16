package learn.ai.tinder

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import learn.ai.tinder.config.TestCassandraConfig
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestCassandraConfig::class)
@Testcontainers(disabledWithoutDocker = true)
class TinderApplicationTests {

    @Test
    fun contextLoads() {
        // Test will verify that the application context loads
    }
}
