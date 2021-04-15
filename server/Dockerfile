FROM node:14.16.1-buster-slim
WORKDIR /app
ENV NODE_ENV=production
COPY . .
RUN npm update && npm install --production
CMD [ "node", "start.js" ]
