#!/bin/bash         

echo "Start to process DA-DAG experiment with 500-size input"

echo ""
echo "Start: DA-DAG COMMU with 300 iterations"
java -jar da-dag-project-1.0.1-SNAPSHOT-full.jar /home/pwangsom/java-program/run4-3workflows 300 da commu

echo ""
echo "Start: DA-DAG COMMU with 600 iterations"
java -jar da-dag-project-1.0.1-SNAPSHOT-full.jar /home/pwangsom/java-program/run4-3workflows 600 da commu

