package learn.ai.tinder.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.CassandraContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestCassandraConfig {

    companion object {
        val cassandra: CassandraContainer<*> = CassandraContainer(DockerImageName.parse("cassandra:4.1"))
            .withExposedPorts(9042)
            .withReuse(true)
            .waitingFor(Wait.forLogMessage(".*Startup complete.*\n", 1))
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    fun cassandraContainer(): CassandraContainer<*> = cassandra
}
