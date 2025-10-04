# Use the latest openjdk image
FROM openjdk:latest
# Copy jar file to the working directory of the container
COPY ./target/GROUP-7-DEV-OP-25---26-0.1.0.2-jar-with-dependencies.jar /tmp
# Set the working directory of the container
WORKDIR /tmp
# Run the jar file
ENTRYPOINT ["java", "-jar", "GROUP-7-DEV-OP-25---26-0.1.0.2-jar-with-dependencies.jar"]