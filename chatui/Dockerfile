# Use the latest LTS version of Node.js
FROM node:18-alpine
 
# Set the working directory inside the container
WORKDIR /app
 
# Copy package.json and package-lock.json
COPY package*.json ./
 
RUN npm install
ARG REACT_APP_API_URL 
ENV REACT_APP_API_URL $REACT_APP_API_URL
 
# Copy the rest of your application files
COPY . .
 
# Expose the port your app runs on
EXPOSE $PORT_IP
 
# Define the command to run your app
CMD ["npm", "start"]