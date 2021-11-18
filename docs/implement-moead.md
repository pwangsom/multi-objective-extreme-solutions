# Reference paper

# Cloud Data Center Performance Model

2014-Deadline Based Resource Provisioning and Scheduling Algorithm for Scientific Workflows on Clouds
EC2 Model
- VM boot time = 97 seconds
- VM billing interval = 1 hour
- VM performance degradation; normal distribution = 24%, mean 12%, SDV = 10%

Network Model
- Network performance degradation; normal distribution = 19%, mean 9.5%, SDV = 5%

2017-Budget-Driven Resource Provisioning and Scheduling of Scientific Workflow in IaaS Clouds with Fine-Grained Billing Periods
EC2 Model
- VM boot time = 97 seconds
- VM billing interval = 1 minute
- VM performance degradation; normal distribution = 24%, mean 12%, SDV = 10%

Network Model
- Network performance degradation; normal distribution = 19%, mean 9.5%, SDV = 5%


2018-A Hybrid Metaheuristic for Multi-Objective Scientific Workflow Scheduling in a Cloud Environment
EC2 Model
- VM boot time = 97 seconds
- VM billing interval = 1 hour
- VM performance degradation; normal distribution = 24%, mean 12%, SDV = 10%

# Google Compute Engine Model

2017-Robust Deadline-Constrained Resource Provisioning and Workflow Scheduling Algorithm for Handling Performance Uncertainty in IaaS Clouds
- Table 1: VM Types Based on Google Compute Engine Oﬀerings

Type			vCPUs	Memory(GB)	Price/Hr.($)	MIPs
f1-micro 		0.2 	0.6 		0.0092 			880
g1-small 		0.5 	1.7 		0.0322 			2200
n1-standard-1 	1 		3.75 		0.0610 			4400
n1-standard-2 	2 		7.5 		0.1220 			8800
n1-standard-4 	4		15 			0.2440 			17600
n1-standard-8 	8 		30 			0.4880 			35200


2017-Budget-Driven Resource Provisioning and Scheduling of Scientific Workflow in IaaS Clouds with Fine-Grained Billing Periods
- Table I. VM Types Based on Google Compute Engine Offerings

# Five Scientific Workflows

2016-Evolutionary Multi-Objective Workflow Scheduling in Cloud
- Table 2. Characteristics of the real-workd DAGs.

2018-A Hybrid Metaheuristic for Multi-Objective Scientific Workflow Scheduling in a Cloud Environment
- Table 4. Characteristics of the benchmark workflows.
Workflow		Number_of_Nodes	Number_of_Edges	Mean_Data_Size_(MB)
Montage_25		25				95				3.43
Montage_50		50				206				3.36
Montage_100 	100				433				3.23
Montage_1000	1000			4485			3.21
CyberShake_30	30				112				747.48
CyberShake_50	50				188				864.74
CyberShake_100	100				380				849.60
CyberShake_100	1000			3988			102.29
Epigenomics_24	24				75				116.20
Epigenomics_46	46				148				104.81
Epigenomics_100	100				322				395.10
Epigenomics_997	997				3228			388.59
LIGO_30			30				95				9.00
LIGO_50			50				160				9.16
LIGO_100		100				319				8.93
LIGO_1000		1000			3246			8.90
SIPHT_30		30				91				7.73
SIPHT_60		60				198				6.95
SIPHT_100		100				335				6.27
SIPHT_1000		1000			3528			5.91


2018-Elastic scheduling of scientific workflows under deadline constraints in cloud
- Table 2. Characteristics of the benchmark workflows. CU: Compute Unit.
Workflow		Number_of_Nodes	Number_of_Edges	Mean_Data_Size_(MB)	Mean_Runtime_(CU = 1)
Montage_1000	1000			4485			3.21				11.36 s
CyberShake_1000	1000			3988			102.29				22.71 s
Epigenomics_997	997				3228			388.59				3858.67 s
LIGO_1000		1000			3246			8.90				2227.25 s
SIPHT_1000		1000			3528			5.91				179.05 s

# MOEA/D Parameters

2014-An Evolutionary Many-Objective Optimization Algorithm Using Reference-Point-Based Nondominated Sorting Approach Part I
MOEA/D-PBI(*) and MOEA/D-TCH
- SBX probability = 1
- Polynomial mutation probability = 1/n
- SBX Normal distribution = 20
- Polynomial Normal distribution = 20

[The neighborhood size T is set as 20 for both
approaches and, additionally, the penalty parameter θ for the
MOEA/D-PBI approach is set as 5.]

2016-An improved NSGA-III algorithm based on objective space decomposition for many-objective optimization

- Neighborhood size T = 20
[Parameter setting in MOEA/D: Neighborhood size T =
20 and probability used to select in the neighborhood
δ = 0.9. Since PBI function is involved in MOEA/D, the
penalty parameter θ needs to be set for it. In this study, θ
is just set to 5 for MOEA/D as suggested in (Zhang and
Li 2007)]

2018-Evolutionary Many-Objective Optimization; A Comparative Study of the State-of-the-Art
- Table 4: MOEA/D: The penalty parameter Zeta of the PBI function: 5, neighbourhood size T: N/10

