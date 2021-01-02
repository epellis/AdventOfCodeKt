```
mvn package
```

```
mvn scala:run -DmainClass=com.nedellis.AppServerMain

curl -D - localhost:8888
```

```
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"address": "localhost:8888"}' \
  http://localhost:8888/advertise
  ```
