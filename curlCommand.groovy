import groovy.json.JsonSlurper

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
println(output.toString())

def jsonSlurper = new JsonSlurper()
def jsonData = jsonSlurper.parseText(output.toString())

println(jsonData.id)

// Close the input stream
inputStream.close()

// Wait for the process to complete
process.waitFor()

// Check for errors
if (process.exitValue() != 0) {
    println("Error occurred: ${process.errorStream.text}")
    System.exit(1)
}
