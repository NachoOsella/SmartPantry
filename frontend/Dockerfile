# Build stage
FROM node:22-alpine AS build
WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm install

# Copy source code and build the application
COPY . .
RUN npm run build -- --configuration production

# Runtime stage
FROM nginx:mainline-alpine-slim
WORKDIR /usr/share/nginx/html

# Copy the built application from the build stage
# Angular 19+ with @angular/build:application outputs to dist/frontend/browser
COPY --from=build /app/dist/frontend/browser ./

# Copy custom nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
