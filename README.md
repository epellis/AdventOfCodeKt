## Build executable and run

```
mvn package
java -jar target/whoami-1.0-SNAPSHOT-jar-with-dependencies.jar

# Or...

mvn scala:run -DmainClass=com.nedellis.AppServerMain
```

## Debug

```
mvn package
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=127.0.0.1:8000 -jar target/whoami-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Interact

```
curl -i --header "Content-Type: application/json" \
  --request POST \
  --data '{"heartbeats":[{"address": "localhost:8888", "count": "1", "epoch": "0"}]}' \
  http://localhost:8888/heartbeat
```

## Docker

```
docker build -t epelesis/whoami:latest .
docker run -p 8888:8888 epelesis/whoami:latest
```
