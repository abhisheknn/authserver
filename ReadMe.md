# Docker Image 
sudo docker run --name redis -p 6379:6379 -d redis

#Redis Command Used:

1) hmset(String key,Map<String,String>)
2) Map<String,String> hgetall(String key)
3) del(String key)

Using Lettuce for redis connection and operation .
https://github.com/lettuce-io/lettuce-core


For kafka 
docker run -p 2181:2181 -p 9092:9092 --env ADVERTISED_HOST=127.0.0.1 --env ADVERTISED_PORT=9092 spotify/kafka


For Kafka Manager
 sheepkiller/kafka-manager
