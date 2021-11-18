#!/bin/bash -l
#SBATCH -J DAG-Scheduling
#SBATCH --mail-type=end,fail
#SBATCH --mail-user=peerasak.w@cattelecom.com
#SBATCH -N 1
#SBATCH --ntasks-per-node=1 
#SBATCH -c 8
#SBATCH --mem=64gb
#SBATCH --time=5-0:0:0
#SBATCH -p batch
#SBATCH --qos=qos-batch

echo "== Starting run at $(date)"
echo "== Job ID: ${SLURM_JOBID}"
echo "== Node list: ${SLURM_NODELIST}"
echo "== Submit dir. : ${SLURM_SUBMIT_DIR}"
# Your more useful application can be started below!

module load lang/Java

java -version

srun java -jar da-dag-project-4.0.0-SNAPSHOT-full.jar /home/users/pwangsom/icmla01900/icmla2018.properties cybershake 50

echo "== Finish run at $(date)"