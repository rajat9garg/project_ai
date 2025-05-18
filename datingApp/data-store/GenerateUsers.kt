@file:DependsOn("com.github.javafaker:javafaker:1.0.2")
@file:DependsOn("org.mongodb:mongodb-driver-sync:4.10.2")
@file:DependsOn("org.slf4j:slf4j-simple:2.0.7")

import com.github.javafaker.Faker
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import java.time.LocalDateTime
import java.util.*
import java.util.UUID
import kotlin.random.Random
import org.bson.json.JsonWriterSettings
import org.bson.BsonDocument
import org.bson.BsonString
import org.bson.BsonInt32
import org.bson.BsonArray
import org.bson.BsonDouble
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("GenerateUsers")

data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val password: String? = null,
    val age: Int,
    val gender: String,
    val location: Location,
    val preferences: Preferences,
    val createdAt: String = LocalDateTime.now().toString()
)

data class Location(
    val city: String,
    val country: String,
    val coordinates: Coordinates? = null
)

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)

data class Preferences(
    val interestedIn: List<String>,
    val ageRange: IntRange,
    val maxDistance: Int? = null
)

fun main() {
    // MongoDB connection
    val mongoClient = MongoClients.create("mongodb://localhost:27017")
    val database: MongoDatabase = mongoClient.getDatabase("datingApp")
    val collection: MongoCollection<Document> = database.getCollection("users")
    
    // Clear existing data
    collection.deleteMany(Document())
    
    // Create indexes
    collection.createIndex(Document("email", 1), Document("unique", true))
    collection.createIndex(Document("location.coordinates", "2dsphere"))
    
    val faker = Faker(Locale.ENGLISH, Random(42))
    val batchSize = 1000
    val batch = mutableListOf<Document>()
    var totalInserted = 0
    
    // Popular cities for better distribution
    val popularCities = listOf(
        "New York" to "USA",
        "London" to "UK",
        "Tokyo" to "Japan",
        "Sydney" to "Australia",
        "Berlin" to "Germany",
        "Paris" to "France",
        "Toronto" to "Canada",
        "Mumbai" to "India",
        "Sao Paulo" to "Brazil",
        "Cape Town" to "South Africa"
    )
    
    // Generate 5000 users
    repeat(5000) { index ->
        val isMale = index % 2 == 0
        val gender = if (isMale) "MALE" else "FEMALE"
        val firstName = if (isMale) faker.name().firstNameMale() else faker.name().firstNameFemale()
        val lastName = faker.name().lastName()
        val fullName = "$firstName $lastName"
        val email = "${firstName.lowercase()}.${lastName.lowercase()}@${faker.internet().domainName()}"
        val age = (18..45).random()
        
        // Select a random city or generate a random one
        val (city, country) = if (Random.nextDouble() < 0.7) {
            popularCities.random()
        } else {
            faker.address().city() to faker.address().country()
        }
        
        // Generate location with 30% chance of having coordinates
        val coordinates = if (Random.nextDouble() < 0.3) {
            Coordinates(
                latitude = faker.address().latitude().toDouble(),
                longitude = faker.address().longitude().toDouble()
            )
        } else {
            null
        }
        
        val location = Location(
            city = city,
            country = country,
            coordinates = coordinates
        )
        
        // Generate preferences
        val minAge = maxOf(18, age - 5)
        val maxAge = minOf(120, age + 5)
        val preferences = Preferences(
            interestedIn = if (isMale) listOf("FEMALE") else listOf("MALE"),
            ageRange = IntRange(minAge, maxAge),
            maxDistance = listOf(10, 25, 50, 100, 250, 500, null).random()
        )
        
        // Create user
        val user = User(
            name = fullName,
            email = email,
            password = if (Random.nextDouble() < 0.8) "\$2a\$10\$D4OLKI6yy68cAeYqHZUYwOOMQssrGt8QpB3JAYPSJIELUdzG3pjDy" else null, // "password"
            age = age,
            gender = gender,
            location = location,
            preferences = preferences
        )
        
        // Convert user to BSON document
        val userDoc = Document()
            .append("_id", user.id)
            .append("name", user.name)
            .append("email", user.email)
            .append("age", user.age)
            .append("gender", user.gender)
            .append("createdAt", user.createdAt)
        
        // Add password if exists
        user.password?.let { userDoc.append("password", it) }
        
        // Add location
        val locationDoc = Document()
            .append("city", user.location.city)
            .append("country", user.location.country)
        
        user.location.coordinates?.let { coords ->
            locationDoc.append("coordinates", 
                Document()
                    .append("type", "Point")
                    .append("coordinates", listOf(coords.longitude, coords.latitude))
            )
        }
        userDoc.append("location", locationDoc)
        
        // Add preferences
        val prefsDoc = Document()
            .append("interestedIn", user.preferences.interestedIn)
            .append("ageRange", mapOf(
                "min" to user.preferences.ageRange.first,
                "max" to user.preferences.ageRange.last
            ))
        user.preferences.maxDistance?.let { prefsDoc.append("maxDistance", it) }
        userDoc.append("preferences", prefsDoc)
        
        batch.add(userDoc)
        
        // Insert in batches
        if (batch.size >= batchSize) {
            collection.insertMany(batch)
            totalInserted += batch.size
            logger.info("Inserted $totalInserted users...")
            batch.clear()
        }
    }
    
    // Insert remaining documents
    if (batch.isNotEmpty()) {
        collection.insertMany(batch)
        totalInserted += batch.size
    }
    
    logger.info("Successfully inserted $totalInserted users into MongoDB")
    mongoClient.close()
}
