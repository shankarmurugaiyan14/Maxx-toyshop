# Maxx Toy Shop - MySQL Database Container

This folder runs the MySQL database for Maxx Toy Shop in Docker.

## Start

From this folder:

    docker compose up -d --build

## Check

    docker ps

Connect from your host:

    mysql -h 127.0.0.1 -P 3306 -u root -p

Password:

    root

Database:

    maxx_toy_shop

## Important for Spring Boot

When Spring Boot runs in another Docker container on the same Docker network,
do NOT use localhost in the JDBC URL.

Use:

    jdbc:mysql://mysql:3306/maxx_toy_shop

The hostname `mysql` is the Docker Compose service name.

## Data persistence

MySQL data is stored in the Docker volume:

    mysql_data

Therefore, restarting the container does not delete the database.

To completely remove the database volume:

    docker compose down -v

WARNING: `docker compose down -v` deletes the stored MySQL data.
