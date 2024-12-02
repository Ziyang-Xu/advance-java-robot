#!/bin/bash

# Start Zookeeper
/opt/kafka/bin/zookeeper-server-start.sh /opt/kafka/config/zookeeper.properties &

# Start Kafka
/opt/kafka/bin/kafka-server-start.sh /opt/kafka/config/server.properties

