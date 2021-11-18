#!/bin/bash         

echo "Start to process DA-DAG experiment with 500-size input"
echo ""
echo "Start: NONE COMMU with 300 iterations"

java -jar da-dag-project-1.0.0-SNAPSHOT-full.jar /home/pwangsom/java-program/run1-500-size 300 none commu

echo ""
echo "Start: NONE COMMU with 600 iterations"
java -jar da-dag-project-1.0.0-SNAPSHOT-full.jar /home/pwangsom/java-program/run1-500-size 600 none commu

echo ""
echo "Start: NONE COMMU with 900 iterations"
java -jar da-dag-project-1.0.0-SNAPSHOT-full.jar /home/pwangsom/java-program/run1-500-size 900 none commu
