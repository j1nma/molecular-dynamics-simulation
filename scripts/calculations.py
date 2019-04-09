import os
import subprocess
import csv
import numpy
from numpy import vstack
from numpy import zeros

N = 100
L = 0.5
max_velocity_module = 0.1
small_radius = 0.005
small_mass = 0.0001
big_radius = 0.05
big_mass = 0.1

limitTime = 60

dirName = './output';
collisionFrequencyDirName = dirName + '/collisionFrequency';
lastThirdSpeedsDirName = dirName + '/lastThirdSpeeds';

if not os.path.exists(dirName):
        os.mkdir(dirName)
        print("Directory ", dirName, " created.")

if not os.path.exists(lastThirdSpeedsDirName):
        os.mkdir(lastThirdSpeedsDirName)
        print("Directory ", lastThirdSpeedsDirName, " created.")

if not os.path.exists(collisionFrequencyDirName):
        os.mkdir(collisionFrequencyDirName)
        print("Directory ", collisionFrequencyDirName, " created.")

times = 3

values = zeros(times);

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
	values[k] = float(number)
	print(values[k])

with open('{dirName}/Mean-and-Std-N={N}-times={times}.txt'.format(
	dirName = collisionFrequencyDirName,
	N = N,
	times = times
	), 'w') as f:
	f.write(str(1 / numpy.mean(values)))
	f.write('\n')
	f.write(str(numpy.mean(values)))
	f.write('\n')
	f.write(str(numpy.std(values)))
	f.write('\n')