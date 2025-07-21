# Order-StreamHub
sample kafka project

Application Functionalities:

API Request Validation
Validates incoming order requests for completeness and correctness.

Topic Routing Based on Address
Routes valid data to Kafka topics based on the order’s destination (US or UK).

Data Serialization
Serializes each order into both JSON and XML formats before sending to topics.

Dead Letter Queue Handling
Sends failed messages to Dead Letter Topics after exhausting all retry attempts.

Kafka Consumer Processing
Listens to each topic, processes JSON messages, and saves them to corresponding country-specific databases (us_orders, uk_orders).

XML Data Handling
XML messages are consumed and logged (not persisted).

Stream-Based Order Segregation
Kafka Streams processes data from us_orders and uk_orders to categorize into high-value and low-value orders, storing them in respective databases.


