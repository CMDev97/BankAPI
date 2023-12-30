# BANK API

## Description
Bank API is an application for retrieving information related to a test account. The information retrieved includes:
* Account Balance
* Account Transactions
* Creation of money transfer

## Prerequisites
Before you begin, ensure you have met the following requirements:
* Java JDK 17 or higher
* Maven 3.8.1 or higher

## Setting Up
To set up the project, follow these steps:

1. Clone the repo:
   ```shell
   git clone https://github.com/CMDev97/BankAPI.git
2. Added env props: 
   ```shell
   export FABRICK_API_KEY={your_value}
3. Build application:
    
    ```shell
   # Without tests running 
   mvn clean install -DskipTests=true

   # With tests running
   mvn clean install
4. Run application:
   ```shell
   java -jar target/bank-0.0.1-SNAPSHOT.jar

## Using Postman with OpenAPI
To interact with the API using Postman, follow these steps to import the OpenAPI specification:
1. **Launch Postman**: Start postman in your machine
2. **Import OpenAPI Specification**: 
   * In Postman, click on the 'Import' button in the top left corner.
   * Choose the 'Link' tab and paste the URL of your OpenAPI specification. Alternatively, if you have the OpenAPI file locally, switch to the 'File' tab and upload the file.
   * Click 'Continue' and then 'Import' to load your API specification into Postman
3. **Test API**: Select an API request from the collection. You may need to set up environment variables in Postman, such as the base URL of your API
4. **Using Environment Variables**: For more dynamic and reusable requests, you can set up environment variables in Postman.
   * Go to the 'Environment' tab in the top right, create a new environment, and add your variables such as baseUrl, in local = http://localhost:8080

If you choose 'Link', the url of OpenAPI specification is https://raw.githubusercontent.com/CMDev97/BankAPI/main/docs/openapi/bank-api-v1.yaml