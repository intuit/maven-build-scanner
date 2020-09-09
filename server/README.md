Docker Container for App Server

1. build container from dockerfile - cd to /server (where the Dockerfile is) then run:
   - docker build -t <tagname> . --build-arg=HTTP_PROXY=<http://yourproxy:port>  --build-arg=HTTPS_PROXY=<http://yourproxy:port>

2. run the docker container:
   - docker run -it -d -p 3000:3000 --name <imagename>  <tagname>
   
   
Example:
 > docker build -t build-scan/app-server . --build-arg=HTTP_PROXY=http://myproxy:9100  --build-arg=HTTPS_PROXY=http://myproxy:9100
 > docker run -it -d -p 3000:3000 --name build-app-server  build-scan/app-server
 
 
Docker logs:
 - docker log -f build-app-server
 
Run a command / check out whats in the container:
 - docker exec -it build-app-server /bin/sh
