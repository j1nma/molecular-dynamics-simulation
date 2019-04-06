import os
import subprocess
import csv
import numpy
from numpy import vstack
from numpy import zeros

duration=2000;

dirName='../output/duration={duration}'.format(duration=duration)

defaultVelocity = 0.03;
rc=1
data_values_1 = [40, 3.1, 3]
data_values_2 = [100, 5, 4]
data_values_3 = [400, 10, 9]
simulation_data_values = [data_values_1, data_values_2, data_values_3]

etha_values=[x * 0.5 for x in range(0, 11)]

if not os.path.exists(dirName):
        os.mkdir(dirName)
        print("Directory " , dirName ,  " Created ")

# For each set of parameters
for i in range(0, len(simulation_data_values)):
	N=simulation_data_values[i][0]
	L=simulation_data_values[i][1]
	M=simulation_data_values[i][2]

	times = 3

	file_values = zeros([times, len(etha_values)]);
	# Generate a file with set of parameters
	for k in range(0, times):
		os.system('python3 ./generate_for_va.py {N} {L}'.format(N = N, L = L));
		values = []
		# Generate array of all noises
		for e in etha_values:
			command = 'java -jar ../target/tp2-1.0-SNAPSHOT.jar --dynamicFile=Dynamic-N={n}.txt --radius={rc} --matrix={matrix} --noise={noise} --speed={defaultVelocity} --time={time} --boxSize={L}'.format(
								n=N,
								rc= rc,
								matrix=M,
								noise=e,
								time=duration,
								L=L,
								defaultVelocity=defaultVelocity
								)
			print(command)
			p = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
			number = None;
			p.stdout.readline();
			line = p.stdout.readlines()
			number = line[0].decode()
			number = number.replace('\n', '')
			values.append(float(number))
		file_values[k] = values;
	with open('{dirName}/N={n}-L={boxSize}-M={matrix}.txt'.format(
		dirName=dirName,
		n=N,
		boxSize=L,
		matrix=M), 'w') as f:
		f.write(' '.join([str(x) for x in numpy.mean(file_values, axis=0)]))
		f.write('\n')
		f.write(' '.join([str(round(x, 5)) for x in numpy.std(file_values, axis=0)]))
		f.write('\n')