**Docker**

Run following maven command for creating docker image.
`mvn clean install`

Run following command for running the image

docker run \
        -e MONGO_HOST=<your-mongo-host> \
        -e MONGO_PORT=<your-mongo-port> \
        -p 8080:8080\
        -d bilyoner:1.0