#Docker Image 
sudo docker run --name redis -p 6379:6379 -d redis

#Redis Command Used:

1) hmset(String key,Map<String,String>)
2) Map<String,String> hgetall(String key)

Using Lettuce for redis connection and operation .
https://github.com/lettuce-io/lettuce-core