#!/bin/bash         

echo "Start to process DA-DAG experiment with 500-size input"
echo ""
echo "Start: NONE IDLE with 300 iterations"

java -jar da-dag-project-1.0.1-SNAPSHOT-full.jar /home/pwangsom/java-program/run3 300 none idle

echo ""
echo "Start: NONE IDLE with 600 iterations"
java -jar da-dag-project-1.0.1-SNAPSHOT-full.jar /home/pwangsom/java-program/run3 600 none idle

echo ""
echo "Start: NONE IDLE with 900 iterations"
java -jar da-dag-project-1.0.1-SNAPSHOT-full.jar /home/pwangsom/java-program/run3 900 none idle
