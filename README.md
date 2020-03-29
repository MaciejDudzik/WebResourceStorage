# Web resource storage

Web resource storage is service capable of storing resources from given url. Service allows to: 
- Request to store some resource by giving its URL
- Get all resources metadata
- Search for resource giving searched phrase
- Get resource by its unique id 

The things that are worth mentioning:
- As resources in internet are mutable web resource is defined as: {resourceURL, recourseDownloadDate}.
- A consequence of the above assumption is that storing the same URL will end up in two resources stored in service.
- Service is downloading one resource at time so if two or more store requests are made they will wait till first one is finished.
- There are no any retires strategies on resource downloading failure.

### API

Here is listed API of 'Web resource storage' service.


| Method | Endpoint |  Description | Path parameters | Request Parameters | Body parameters |
| ------------- | ------------- | ------------- | ------------- | ------------- | ------------- |
| POST | /resources/store | Store resource request | | | resourceUrl |
| GET | /resources | Get all resources metadata | | | |
| GET | /resources?like=pdf | Search for resources metadata like given parameter | | like | |
| GET | /resources/{resourceId} | Get resource by its unique id | resourceId | | |

### Run it

Useful commands to run service and database containers.

```
# build and test the project
./gradlew build

# build service docker image
docker-compose build 

# start service and database containers
docker-compose up 

# check logs of service
docker-compose logs -f backend

# stop conatiners if needed
docker-compose down
```
### Test it

Here are given some helpful commands to test service.

```
curl --request POST \
  --url http://localhost:8090/resources/store \
  --header 'content-type: application/json' \
  --data '{
	"resourceUrl" : "http://www.africau.edu/images/default/sample.pdf"
}'

curl --request POST \
  --url http://localhost:8090/resources/store \
  --header 'content-type: application/json' \
  --data '{
	"resourceUrl" : "https://stackoverflow.com/"
}'

curl --request POST \
  --url http://localhost:8090/resources/store \
  --header 'content-type: application/json' \
  --data '{
	"resourceUrl" : "https://www.google.com/"
}'

curl --request GET \
  --url http://localhost:8090/resources

curl --request GET \
  --url 'http://localhost:8090/resources?like=pdf'

curl --request GET \
  --url http://localhost:8090/resources/16e96279-f3b9-42c4-973f-58a0a9bf176f
```
