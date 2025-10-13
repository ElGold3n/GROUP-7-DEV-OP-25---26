# Use the official MySQL 8 image
FROM mysql:8.0

# Set root password and default database
ENV MYSQL_ROOT_PASSWORD=P@ssw0rd!
ENV MYSQL_DATABASE=world

# Copy the world.sql file into the container's init directory
# MySQL will automatically execute any .sql files in /docker-entrypoint-initdb.d
COPY world.sql /docker-entrypoint-initdb.d/

# Expose MySQL port
EXPOSE 3306
