#!/bin/bash         

echo "Start to process DA-DAG experiment with 500-size input"
echo ""
echo "Start: DA-DAG COMMU with 300 iterations"

java -jar da-dag-project-1.0.0-SNAPSHOT-full.jar /home/pwangsom/java-program/run1-500-size 300 da commu

echo ""
echo "Start: DA-DAG COMMU with 600 iterations"
java -jar da-dag-project-1.0.0-SNAPSHOT-full.jar /home/pwangsom/java-program/run1-500-size 600 da commu

echo ""
echo "Start: DA-DAG COMMU with 900 iterations"
java -jar da-dag-project-1.0.0-SNAPSHOT-full.jar /home/pwangsom/java-program/run1-500-size 900 da commu
