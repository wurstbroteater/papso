#!/bin/bash
for dim in {1,16,256,4096} #dimensions determine the difficulty to calculate a single perticle
do
    rm -f "papso_scaling_${dim}_dimensions.txt"
    echo "dimensions: $dim"
    for cores in {0..23} #run the test for 1-24 cores
    do
	echo $(date +"%T") benchmarking for 0.5 minutes
	echo $(taskset -c 0-$cores java -jar $1 240000 $dim) | tee -a "${1}_${dim}_dimensions.txt" #runs the test for 7 minutes
	echo $(date +"%T") sleeping for 0.5 minutes
	sleep 60
    done
done
