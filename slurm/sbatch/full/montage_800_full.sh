#!/bin/bash -l
#SBATCH -J DAG-Scheduling
#SBATCH --mail-type=end,fail
#SBATCH --mail-user=peerasak.w@cattelecom.com
#SBATCH -N 1
#SBATCH --ntasks-per-node=1 
#SBATCH -c 12
#SBATCH --mem=96gb
#SBATCH --time=30-0:0:0
#SBATCH -p long
#SBATCH --qos=qos-long

echo "== Starting run at $(date)"
echo "== Job ID: ${SLURM_JOBID}"
echo "== Node list: ${SLURM_NODELIST}"
echo "== Submit dir. : ${SLURM_SUBMIT_DIR}"
# Your more useful application can be started below!

module load lang/Java

java -version

srun java -jar -Xmx64g da-dag-project-3.3.0-SNAPSHOT-full.jar /home/users/pwangsom/java-program/experiment0 montage 800 commu 30 900 300

echo "== Finish run at $(date)"