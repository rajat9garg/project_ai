package learn.ai.tinder.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration
import org.springframework.data.cassandra.config.CqlSessionFactoryBean
import org.springframework.data.cassandra.config.SchemaAction
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CassandraProperties::class)
@EnableCassandraRepositories(basePackages = ["learn.ai.tinder.repository.cassandra"])
class CassandraConfig(
    private val properties: CassandraProperties
) : AbstractCassandraConfiguration() {

    override fun getKeyspaceName(): String = properties.keyspaceName
    override fun getContactPoints(): String = properties.contactPoints
    override fun getLocalDataCenter(): String = properties.localDatacenter
    override fun getPort(): Int = properties.port
    override fun getSchemaAction(): SchemaAction = SchemaAction.CREATE_IF_NOT_EXISTS

    override fun getKeyspaceCreations(): List<CreateKeyspaceSpecification> = listOf(
        CreateKeyspaceSpecification.createKeyspace(properties.keyspaceName)
            .ifNotExists()
            .with(KeyspaceOption.DURABLE_WRITES, true)
            .withSimpleReplication(1)
    )

    @Bean
    override fun cassandraSession(): CqlSessionFactoryBean {
        return super.cassandraSession().apply {
            // Any additional session configuration can go here
        }
    }
}

@ConfigurationProperties(prefix = "spring.cassandra")
data class CassandraProperties(
    val keyspaceName: String = "tinder",
    val contactPoints: String = "localhost",
    val localDatacenter: String = "datacenter1",
    val port: Int = 9042
)
