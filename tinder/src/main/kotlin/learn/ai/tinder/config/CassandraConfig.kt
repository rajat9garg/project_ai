package learn.ai.tinder.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration
import org.springframework.data.cassandra.config.SchemaAction
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories
import org.springframework.beans.factory.annotation.Value

/**
 * Configuration for Cassandra connection and repositories.
 * Extends AbstractReactiveCassandraConfiguration to provide reactive Cassandra support.
 */
@Configuration
@EnableReactiveCassandraRepositories(basePackages = ["learn.ai.tinder.repository"])
class CassandraConfig(
    @Value("\${spring.data.cassandra.keyspace-name}") private val keyspaceName: String,
    @Value("\${spring.data.cassandra.contact-points}") private val contactPoints: String,
    @Value("\${spring.data.cassandra.port}") private val port: Int,
    @Value("\${spring.data.cassandra.local-datacenter}") private val localDatacenter: String,
    @Value("\${spring.data.cassandra.schema-action}") private val schemaAction: String
) : AbstractReactiveCassandraConfiguration() {

    override fun getKeyspaceName(): String = keyspaceName

    override fun getContactPoints(): String = contactPoints

    override fun getPort(): Int = port

    override fun getLocalDataCenter(): String = localDatacenter

    override fun getSchemaAction(): SchemaAction = SchemaAction.valueOf(schemaAction)

    /**
     * Creates the keyspace if it doesn't exist.
     */
    override fun getKeyspaceCreations(): List<CreateKeyspaceSpecification> {
        return listOf(
            CreateKeyspaceSpecification
                .createKeyspace(keyspaceName)
                .ifNotExists()
                .withSimpleReplication(1)
        )
    }
}
