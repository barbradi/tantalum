## PRE REQUISITES
Consul must be running on port 8500, this is configurable through properties files

## Running the message app

Move to the app message module folder and run

```
gradle bootRun
```

## Running the uuid app

Move to the app uuid module folder and run

```
gradle bootRun
```


## Managing the messages
### Create a message

perform a POST to localhost:8080/message with the payload

```json
{
	"message": "blah blah"
}
```

you should get a 201 response containing a payload similar to:

```json
{
    "uuid": "aprefix-c5bd2247-f635-41f7-b2a7-abd21d2dbf61-asuffix",
    "message": "blah blah",
    "creationDate": "2018-06-26@21:54:15"
 }
```

### Get a message

perform a GET to localhost:8080/message/id

### Get all messages

perform a GET to localhost:8080/message

### Get top messages

perform a GET to localhost:8080/message/top?number={number of messages}

### modify a message

perform a PUT to localhost:8080/message/id with the payload

```json
{
	"message": "blah blah"
}
```

### delete a message

perform a DELETE to localhost:8080/message/id
