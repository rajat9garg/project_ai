package learn.ai.tinder.config

import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.CredentialsProvider
import org.apache.http.impl.client.BasicCredentialsProvider
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.elc.ElasticsearchClient
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

/**
 * Configuration for Elasticsearch connection and repositories.
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = ["learn.ai.tinder.repository"])
class ElasticsearchConfig(
    @Value("${elasticsearch.host:localhost}") private val host: String,
    @Value("${elasticsearch.port:9200}") private val port: Int,
    @Value("${elasticsearch.username:}") private val username: String,
    @Value("${elasticsearch.password:}") private val password: String
) {
    @Bean
    fun elasticsearchRestClient(): RestClient {
        val credentialsProvider = BasicCredentialsProvider()
        if (username.isNotBlank() && password.isNotBlank()) {
            credentialsProvider.setCredentials(
                AuthScope.ANY,
                UsernamePasswordCredentials(username, password)
            )
        }
        
        return RestClient.builder(HttpHost(host, port, "http"))
            .setHttpClientConfigCallback { httpClientBuilder ->
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
            }
            .build()
    }

    @Bean
    fun elasticsearchConfiguration(restClient: RestClient): ElasticsearchConfiguration {
        val configuration = ElasticsearchConfiguration { client ->
            client.restClient(restClient)
        }
        configuration.mappingContext = SimpleElasticsearchMappingContext()
        configuration.converter = MappingElasticsearchConverter(configuration.mappingContext)
        return configuration
    }

    @Bean
    fun elasticsearchOperations(configuration: ElasticsearchConfiguration): ElasticsearchOperations {
        return configuration.createElasticsearchOperations()
    }
}    @Value("\${spring.data.elasticsearch.uris}") private val elasticsearchUris: String,
    @Value("\${spring.elasticsearch.rest.username:}") private val username: String,
    @Value("\${spring.elasticsearch.rest.password:}") private val password: String
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(javaClass)

    /**
     * Creates a low-level REST client for Elasticsearch.
     */
    @Bean
    fun restClient(): RestClient {
        val uri = URI.create(elasticsearchUris)
        logger.info("Creating Elasticsearch RestClient for URI: $uri")
        
        val httpHost = HttpHost(uri.host, uri.port, uri.scheme)
        val builder = RestClient.builder(httpHost)
        
        // Add basic auth if credentials are provided
        if (username.isNotBlank() && password.isNotBlank()) {
            val credentialsProvider = BasicCredentialsProvider()
            credentialsProvider.setCredentials(
                AuthScope.ANY,
                UsernamePasswordCredentials(username, password)
            )
            
            builder.setHttpClientConfigCallback { httpClientBuilder: HttpAsyncClientBuilder ->
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
            }
        }
        
        return builder.build()
    }

    /**
     * Creates an Elasticsearch client using the new API.
     */
    @Bean(destroyMethod = "close")
    fun elasticsearchClient(restClient: RestClient): ElasticsearchClient {
        val transport = RestClientTransport(restClient, JacksonJsonpMapper())
        return ElasticsearchClient(transport)
    }

    /**
     * Creates an Elasticsearch mapping context.
     */
    @Bean
    fun elasticsearchMappingContext(): SimpleElasticsearchMappingContext {
        return SimpleElasticsearchMappingContext()
    }

    /**
     * Creates an Elasticsearch converter.
     */
    @Bean
    fun elasticsearchConverter(mappingContext: SimpleElasticsearchMappingContext): ElasticsearchConverter {
        return MappingElasticsearchConverter(mappingContext)
    }

    /**
     * Creates a synchronous Elasticsearch template.
     */
    @Bean
    fun elasticsearchTemplate(
        restClient: RestClient,
        converter: ElasticsearchConverter
    ): ElasticsearchOperations {
        val restHighLevelClient = RestHighLevelClient.builder(restClient)
            .setApiCompatibilityMode(true)
            .build()
        return ElasticsearchRestTemplate(restHighLevelClient, converter)
    }
}

/**
 * Configuration for Elasticsearch connection and repositories.
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = ["learn.ai.tinder.repository"])
class ElasticsearchConfig(
    @Value("\${spring.data.elasticsearch.uris}") private val elasticsearchUris: String,
    @Value("\${spring.elasticsearch.rest.username:}") private val username: String,
    @Value("\${spring.elasticsearch.rest.password:}") private val password: String
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(javaClass)

    /**
     * Creates a low-level REST client for Elasticsearch.
     */
    @Bean
    fun restClient(): RestClient {
        val uri = URI.create(elasticsearchUris)
        logger.info("Creating Elasticsearch RestClient for URI: $uri")
        
        val httpHost = HttpHost(uri.host, uri.port, uri.scheme)
        val builder = RestClient.builder(httpHost)
        
        // Add basic auth if credentials are provided
        if (username.isNotBlank() && password.isNotBlank()) {
            val credentialsProvider = BasicCredentialsProvider()
            credentialsProvider.setCredentials(
                AuthScope.ANY,
                UsernamePasswordCredentials(username, password)
            )
            
            builder.setHttpClientConfigCallback { httpClientBuilder: HttpAsyncClientBuilder ->
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
            }
        }
        
        return builder.build()
    }

    /**
     * Creates a high-level REST client for Elasticsearch.
     */
    @Bean(destroyMethod = "close")
    fun restHighLevelClient(restClient: RestClient): RestHighLevelClient {
        return RestHighLevelClient.builder(restClient)
            .setApiCompatibilityMode(true)
            .build()
    }

    /**
     * Creates an Elasticsearch mapping context.
     */
    @Bean
    fun elasticsearchMappingContext(): SimpleElasticsearchMappingContext {
        return SimpleElasticsearchMappingContext()
    }

    /**
     * Creates an Elasticsearch converter.
     */
    @Bean
    fun elasticsearchConverter(mappingContext: SimpleElasticsearchMappingContext): ElasticsearchConverter {
        return MappingElasticsearchConverter(mappingContext)
    }

    /**
     * Creates a synchronous Elasticsearch template.
     */
    @Bean
    fun elasticsearchTemplate(
        restHighLevelClient: RestHighLevelClient,
        converter: ElasticsearchConverter
    ): ElasticsearchOperations {
        return ElasticsearchRestTemplate(restHighLevelClient, converter)
    }
}
