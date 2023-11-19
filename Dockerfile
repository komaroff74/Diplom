java "./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"FROM node:lts-alpine3.12
WORKDIR /opt/app
COPY gate-simulator .
RUN npm install
CMD ["npm", "start"]
EXPOSE 9999