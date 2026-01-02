# Charging Session Service

Kotlin, Springboot Microservice for managing EV charging sessions with async Rabbitmq-based authorization.

## Quick Start

### Prerequisites
- Docker 20+
- Kafka (included in Docker)

### Run Locally

# 1. Clone repo
git clone https://github.com/charithellawala/sample-project-async-rabbitmq.git

# 2. Start services (Kafka + Apps)
docker-compose up --build

# 3. Verify services
docker-compose ps

# 4. Verify Api 

Through swagger "http://localhost:8082/swagger-ui/index.html#/"
Or through Postman, http://localhost:8082/api/v1/charging/start-charging

# 5. Curl for Success Scenario

curl -X 'POST' \
  'http://localhost:8082/api/v1/charging/start-charging' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "stationId": "123e4567-e89b-12d3-a456-426614174000",
    "driverToken": "Adriver_TEST_00112efef23ef2",
    "callbackUrl": "http://localhost:8082/api/v1/callback/get-callback"
}'

# 6. WorkFlow Scenario
1.)User sending a charging vehicle charging request through charging-service as producer.
2.)Auth-service get the request as consumer and validate the request.
3.)Auth-service use ready in cache memory(redis) avoiding repeatedly hiting database.
4.)using internal async call result will trigger a generating report through a independent service.
5.)After validation results will be send to again charging-service as the producer.
6.)Charging-service receive the result as consumer and save the final request in db while sending it to a sample callback service.



