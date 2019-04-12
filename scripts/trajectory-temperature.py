import os
import subprocess
import csv
import numpy
from numpy import vstack
from numpy import zeros
from oct2py import octave
octave.addpath('./scripts/')

N = 100
L = 0.5

max_velocity_module_1 = 0.1
max_velocity_module_2 = 0.5
max_velocity_module_3 = 1.0
max_velocity_modules = [max_velocity_module_1, max_velocity_module_2, max_velocity_module_3]

small_radius = 0.005
small_mass = 0.0001
big_radius = 0.05
big_mass = 0.1

limitTime = 60

dirName = './output';
bigParticleTrajectoryDirName = dirName + '/bigParticleTrajectory';

if not os.path.exists(dirName):
        os.mkdir(dirName)
        print("Directory ", dirName, " created.")

if not os.path.exists(bigParticleTrajectoryDirName):
        os.mkdir(bigParticleTrajectoryDirName)
        print("Directory ", bigParticleTrajectoryDirName, " created.")

times = len(max_velocity_modules);
values = zeros(times);

os.system('python3 ./scripts/generate_for_temperatures.py {N} {L} {velocity_modules} {small_radius} {small_mass} {big_radius} {big_mass}'.format(
		N = N,
		L = L,
		velocity_modules = str(max_velocity_modules).replace(']','').replace('[','').replace(' ',''),
		small_radius = small_radius,
		small_mass = small_mass,
		big_radius = big_radius,
		big_mass = big_mass
		));

for k in range(0, times):
	command = 'java -jar ./target/molecular-dynamics-simulation-1.0-SNAPSHOT.jar --dynamicFile=./data/Dynamic-N={N}-V={max_velocity_module}.txt --staticFile=./data/Static-N={N}.txt --time={limitTime} --boxSize={L}'.format(
						N = N,
						max_velocity_module = max_velocity_modules[k],
						limitTime = limitTime,
						L = L,
						)
	print(command)
	p = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, bufsize=0)
	number = None;
	line = p.stdout.readlines() # Temperature line
	number = line[0].decode()
	number = number.split('\t')
	number = number[1]
	number = number.replace('\n', '')
	values[k] = float(number)
	print(values[k])
	func = 'bigParticleTrajectory(' + str(k) + ',' + str(values[k]) + ')';
	octave.eval(func)