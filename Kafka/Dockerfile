# Use an official lightweight Linux base image
FROM ubuntu:22.04

# Set environment variables for Kafka
ENV KAFKA_VERSION=3.5.2 \
    SCALA_VERSION=2.13

# Update packages and install necessary tools
RUN apt-get update && apt-get install -y \
    wget \
    openjdk-17-jdk \
    net-tools \
    curl \
    unzip \
    tar && \
    rm -rf /var/lib/apt/lists/*

# Set JAVA_HOME
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH="$JAVA_HOME/bin:$PATH"

# Install Kafka
RUN wget https://archive.apache.org/dist/kafka/$KAFKA_VERSION/kafka_$SCALA_VERSION-$KAFKA_VERSION.tgz && \
    tar -xvzf kafka_$SCALA_VERSION-$KAFKA_VERSION.tgz && \
    mv kafka_$SCALA_VERSION-$KAFKA_VERSION /opt/kafka && \
    rm kafka_$SCALA_VERSION-$KAFKA_VERSION.tgz

# Expose Kafka ports
EXPOSE 9092 2181

# Set up Kafka entrypoint script
COPY kafka-entrypoint.sh /usr/bin/
RUN chmod +x /usr/bin/kafka-entrypoint.sh

# Set working directory
WORKDIR /opt/kafka

# Set default command
CMD ["kafka-entrypoint.sh"]

