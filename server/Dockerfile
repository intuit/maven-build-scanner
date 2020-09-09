FROM node:12

WORKDIR /usr/src/app

COPY package*.json ./

#In case your corp firewall gives you troubles building, try this 
#RUN npm config set registry http://registry.npmjs.org/
#RUN npm config set strict-ssl false

RUN npm install
# If you are building your code for production
# RUN npm ci --only=production

COPY . .

EXPOSE 3000

RUN npm start &
CMD [ "node", "./bin/www.js" ]
