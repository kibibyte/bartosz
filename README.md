# Hotel helper application 

## Modules
* app
* data
* domain

## Build

```shell script
$ ./gradlew build
```

## Test
```shell script
$ ./gradlew test
```

## Run
```shell script
$ ./gradlew run
```
Example request: 
```shell script
curl --request POST \
  --url http://localhost:8080/occupancy/optimizer \
  --header 'content-type: application/json' \
  --data '{
	"premiumRoomsCount" : 7,
	"economyRoomsCount" : 1
}'
```