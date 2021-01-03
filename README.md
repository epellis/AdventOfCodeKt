## Build executable and run

```
mvn package
java -jar target/whoami-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Debug

```
mvn package
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=127.0.0.1:8000 -jar target/whoami-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Interact

```
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"address": "localhost:8888"}' \
  http://localhost:8888/advertise
  ```
