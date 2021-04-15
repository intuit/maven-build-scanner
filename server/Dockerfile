FROM node:14.16.1-buster-slim
WORKDIR /app
ENV NODE_ENV=production
COPY . .
RUN npm install --production
CMD [ "node", "start.js" ]
