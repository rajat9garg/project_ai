# Data Generation Scripts

This directory contains scripts for generating test data for the dating application.

## Generate Users

The `GenerateUsers.kt` script generates 5000 random user profiles with the following characteristics:

- 50% male, 50% female distribution
- Realistic names and email addresses
- Ages between 18 and 45
- Locations from popular cities worldwide
- Random preferences for age range and distance
- 80% of users have a default password (hashed "password")
- 30% of users have geolocation coordinates

### Prerequisites

- Kotlin 1.6.0 or later
- Java 11 or later

### Running the Script

1. Navigate to the project root directory
2. Run the script using Kotlin Script:
   ```bash
   kotlin data-store/GenerateUsers.kt
   ```

### Output

The script will generate a file named `generated_users.json` in the `data-store` directory.

### Importing to MongoDB

To import the generated users into MongoDB:

```bash
mongoimport --db datingApp --collection users --file data-store/generated_users.json --jsonArray
```

### Customization

You can modify the script to:
- Change the number of users generated
- Adjust the distribution of genders
- Add more cities or countries
- Modify the age ranges
- Change the password hashing algorithm

### Notes

- The script uses a fixed random seed for reproducibility
- All generated emails are fake but follow standard email formats
- Location data includes a mix of popular cities and random locations
