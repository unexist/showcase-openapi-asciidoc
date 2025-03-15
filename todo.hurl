# Create
POST http://localhost:8080/todo
{
    "description": "string",
    "title": "string"
}
HTTP 201
[Asserts]
header "Content-Type" contains "json"
jsonpath "$.title" == "string"
jsonpath "$.description" == "string"
[Captures]
todoId: jsonpath "$.id"

# Read
GET http://localhost:8080/todo/{{todoId}}
HTTP 200

# Update
PUT http://localhost:8080/todo/{{todoId}}
{
    "title": "new string"
}
HTTP 200
[Asserts]
header "Content-Type" contains "json"
jsonpath "$.title" == "new string"

# Delete
DELETE http://localhost:8080/todo/{{todoId}}
HTTP 204

# List all
GET http://localhost:8080/todo
HTTP 200
