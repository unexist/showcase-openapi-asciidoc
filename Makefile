 .DEFAULT_GOAL := build

define JSON_TODO
curl -X 'POST' \
  'http://localhost:8080/todo' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "description": "Todo string",
  "title": "Todo string"
}'
endef
export JSON_TODO

# Tools
todo:
	@echo $$JSON_TODO | bash

list:
	@curl -X 'GET' 'http://localhost:8080/todo' -H 'accept: */*' | jq .

hurl:
	@hurl ./todo.hurl

# Build & run
build:
	mvn package

run:
	java -jar todo-service/target/todo-service-0.1-jar-with-dependencies.jar
