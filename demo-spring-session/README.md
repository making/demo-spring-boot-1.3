# Spring Session Demo

## Run Redis

``` console
$ docker-compose up
```

## Make jar

```
$ mvn package
```

## Run App1

```
$ java -jar target/demo-0.0.1-SNAPSHOT.jar --server.port=8080
```

## Run App2

```
$ java -jar target/demo-0.0.1-SNAPSHOT.jar --server.port=8081
```