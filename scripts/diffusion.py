import os
import subprocess
import csv
import numpy
import matplotlib.pyplot as plt
import math
from numpy import vstack
from numpy import zeros
from oct2py import octave
octave.addpath('./scripts/')

N = 100
L = 0.5
max_velocity_module = 0.1
small_radius = 0.005
small_mass = 0.0001
big_radius = 0.05
big_mass = 0.1

limitTime = 120;

dirName = './output';
bigParticleDiffusionDirName = dirName + '/bigParticleDiffusion';

if not os.path.exists(dirName):
        os.mkdir(dirName)
        print("Directory ", dirName, " created.")

if not os.path.exists(bigParticleDiffusionDirName):
        os.mkdir(bigParticleDiffusionDirName)
        print("Directory ", bigParticleDiffusionDirName, " created.")

times = 10
time_samples = 10

values = zeros(times);
d_values = [[0 for x in range(time_samples)] for y in range(times)];

# Generate a file with set of parameters
for k in range(0, times):
	os.system('python3 ./scripts/generate.py {N} {L} {max_velocity_module} {small_radius} {small_mass} {big_radius} {big_mass}'.format(
		N = N, 
		L = L,
		max_velocity_module = max_velocity_module,
		small_radius = small_radius,
		small_mass = small_mass,
		big_radius = big_radius,
		big_mass = big_mass
		));

	command = 'java -jar ./target/molecular-dynamics-simulation-1.0-SNAPSHOT.jar --dynamicFile=./data/Dynamic-N={N}.txt --staticFile=./data/Static-N={N}.txt --time={limitTime} --boxSize={L}'.format(
						N = N,
						limitTime = limitTime,
						L = L,
						)
	print(command)
	p = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, bufsize=0)
	number = None;
	p.stdout.readline(); # Results line
	p.stdout.readline(); # Execution time line
	line = p.stdout.readlines() # Average time btw collisions line
	number = line[0].decode()
	number = number.split('\t')
	number = number[1]
	number = number.replace('\n', '')
	values[k] = float(number);
	d_value = numpy.array(octave.eval("particleDiffusion")[0]);
	for j in range(0, time_samples):
	    d_values[k][j] = d_value[j]

with open('{dirName}/Mean-and-Std-N={N}-times={times}.txt'.format(
	dirName = bigParticleDiffusionDirName,
	N = N,
	times = times
	), 'w') as f:

	mean_values = numpy.mean(d_values, axis = 1);
	for j in range(0, time_samples):
	    f.write(str(mean_values[j]))
	    f.write('\n')

	f.write('\n')

	std_values = numpy.std(d_values, axis = 1);
	for j in range(0, time_samples):
	    f.write(str(std_values[j]))
	    f.write('\n')