@Grab('com.moandjiezana.toml:toml4j:0.7.2')

import java.nio.file.*
import com.moandjiezana.toml.Toml
import groovy.json.JsonSlurper


// Get the current directory
Path currentDir = Paths.get(".")

// List all files in the current directory
Files.walk(currentDir).forEach { path ->
    // Check if it's a file and ends with ".toml"
    if (Files.isRegularFile(path) && path.getFileName().toString().toLowerCase().endsWith(".toml")) {
        // println("TOML File: " + path)

        //convert path to String
        String tomlPath = path

        // Path to the TOML file
        Path tomlFilePath = Paths.get(tomlPath)

        // Read the content of the TOML file
        String tomlContent = tomlFilePath.toFile().text

        // Parse the TOML content
        Toml toml = new Toml().read(tomlContent)

        // Get the value associated with the "sha" key
        String shaValue = toml.getString("artifactory.sha")

        // Print the SHA value
        println("SHA Value: $shaValue")

        // Define the cURL command
        def curlCommand = ['curl', 'https://jsonplaceholder.typicode.com/posts/1']

        // Execute the cURL command
        def process = curlCommand.execute()
        def inputStream = process.inputStream

        // Read and print the JSON output
        def output = new StringBuilder()
        inputStream.eachLine { line ->
            output.append(line)
        }
        // println(output.toString())

        def jsonSlurper = new JsonSlurper()
        def jsonData = jsonSlurper.parseText(output.toString())

        // println(jsonData.id)

        // Close the input stream
        inputStream.close()

        // Wait for the process to complete
        process.waitFor()

        // Check for errors
        if (process.exitValue() != 0) {
            println("Error occurred: ${process.errorStream.text}")
            System.exit(1)
        }


        // println(jsonData)

        if (jsonData.id.toString() == shaValue) {
            println("It's a Match ")
        } else {
            println("No Match Found")
        }

        

    }
}
