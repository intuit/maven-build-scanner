Docker Container for App Server

1. Edit mongo db connect string in /server/routes/index.js to the ip-adress your mongo db is running on:
   - mongoose.connect("mongodb://<IP-ADRESS>:27017/build_scans", { useNewUrlParser: true });
   
1. build container from /server/Dockerfile:
   - docker build -t \<tagname> . --build-arg=HTTP_PROXY=\<http://yourproxy:port>  --build-arg=HTTPS_PROXY=\<http://yourproxy:port>

2. run the docker container:
   - docker run -it -d -p 3000:3000 --name \<imagename>  \<tagname>
   
   
Example:
 - docker build -t build-scan/app-server . --build-arg=HTTP_PROXY=http://myproxy:9100  --build-arg=HTTPS_PROXY=http://myproxy:9100
 - docker run -it -d -p 3000:3000 --name build-app-server  build-scan/app-server
 
 
Docker logs:
 - docker logs -f build-app-server
 
Run a command / check out whats in the container:
 - docker exec -it build-app-server /bin/sh
