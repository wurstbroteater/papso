#!/bin/bash
for i in {0,1,2,3}
do
    echo $(taskset -c 0-"$i" java -jar papso_scaling.jar 420 $1)
done
