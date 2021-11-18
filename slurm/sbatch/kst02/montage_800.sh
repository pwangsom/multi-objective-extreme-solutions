#!/bin/bash -l
#SBATCH -J DAG-Scheduling
#SBATCH --mail-type=end,fail
#SBATCH --mail-user=peerasak.w@cattelecom.com
#SBATCH -N 1
#SBATCH --ntasks-per-node=1 
#SBATCH -c 4
#SBATCH --mem=48gb
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

srun java -jar da-dag-project-4.0.0-SNAPSHOT-full.jar /home/users/pwangsom/kst02/kst2019.properties montage 800

echo "== Finish run at $(date)"