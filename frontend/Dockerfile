# Build Angular application
FROM node:22-alpine AS build

WORKDIR /app

COPY package*.json ./

RUN npm ci

COPY . .

RUN npm run build


# Run Angular with Nginx
FROM nginx:alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf

# CHANGE THIS PATH TO YOUR ACTUAL DIST PATH
COPY --from=build /app/dist/maxx-toy-shop/browser /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]