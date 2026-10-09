FROM mysql:8.0

ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATABASE=maxx_toy_shop

COPY init.sql /docker-entrypoint-initdb.d/01-init.sql

EXPOSE 3306