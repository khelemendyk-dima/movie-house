# Use official Node.js 21 image for building the app
FROM node:22 AS build

# Set the working directory in the container
WORKDIR /app

# Copy dependency files
COPY package.json package-lock.json ./

# Install dependencies
RUN npm install

# Copy the rest of the app's code
COPY . .

# Build the Vite app
RUN npm run build

# Use nginx to serve the app
FROM nginx:alpine

# Copy the build folder to nginx's default public directory
COPY --from=build /app/dist /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
