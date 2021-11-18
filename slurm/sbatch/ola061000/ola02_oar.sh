#!/bin/bash -l
#OAR -n mokp
#OAR -p cputype='xeon-haswell'
#OAR -l nodes=1/core=4,walltime=120:00:00

echo "== Starting run at $(date)"
echo "== Job ID: ${SLURM_JOBID}"
echo "== Node list: ${SLURM_NODELIST}"
echo "== Submit dir. : ${SLURM_SUBMIT_DIR}"
# Your more useful application can be started below!

module load lang/Java

java -version

java -jar da-dag-project-5.1.0-SNAPSHOT-full.jar /home/users/pwangsom/ola061000/ola2019.properties

echo "== Finish run at $(date)"