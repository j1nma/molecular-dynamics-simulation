import os
import subprocess
import csv
import numpy
import matplotlib.pyplot as plt
import math
from numpy import vstack, zeros, array, ones, linalg, transpose, delete, mean
from pylab import plot,show
from oct2py import octave
octave.addpath('./scripts/')

def squared_error(y, modelY):
    return sum((modelY - y) * (modelY - y))

def coefficient_of_determination(y, modelY):
    yMeans = [mean(y) for x in y]
    squaredErrorRegression = squared_error(y, modelY)
    squaredErrorYMean = squared_error(y, yMeans)
    return 1 - (squaredErrorRegression/squaredErrorYMean)

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

d_values = []

# Print current parameters
print('N = {N}, limitTime = {limitTime}'.format(N = N, limitTime = limitTime))

for k in range(0, times):
	# Generate a file with set of parameters
	os.system('python3 ./scripts/generate.py {N} {L} {max_velocity_module} {small_radius} {small_mass} {big_radius} {big_mass}'.format(
		N = N, 
		L = L,
		max_velocity_module = max_velocity_module,
		small_radius = small_radius,
		small_mass = small_mass,
		big_radius = big_radius,
		big_mass = big_mass
		));

	# Execute simulation
	command = 'java -jar ./target/molecular-dynamics-simulation-1.0-SNAPSHOT.jar --dynamicFile=./data/Dynamic-N={N}.txt --staticFile=./data/Static-N={N}.txt --time={limitTime} --boxSize={L}'.format(
						N = N,
						limitTime = limitTime,
						L = L,
						)
	print('Iteration {k}'.format(k=k + 1))
	p = subprocess.check_output(command, shell=True)

	# Parse particle diffusion file
	bpd = open('{dirName}/big_particle_diffusion.txt'.format(dirName = bigParticleDiffusionDirName), 'r');
	simulationTime = float(next(bpd));
	initialPosition = []
	initialPosition.append([float(x) for x in next(bpd).split()])
	d = [0.0]
	for line in bpd:
		position = [float(x) for x in line.split()]
		d.append(numpy.linalg.norm(position[0]-position[1]))
	d_values.append(d)

	# Plot dynamically the MSD for each run
	func = 'particleDiffusion(' + str(k) + ')';
	octave.eval(func)

# Calculate mean and std for each time (columns)
mean_values = numpy.mean(d_values, axis = 0)
std_values = numpy.std(d_values, axis = 0)

# Save value to file for later processing if needed
with open('{dirName}/Mean-and-Std-N={N}-times={times}.txt'.format(
	dirName = bigParticleDiffusionDirName,
	N = N,
	times = times
	), 'w') as f:
	for j in range(0, len(mean_values)):
	    f.write(str(mean_values[j]) + ' ')
	f.write('\n')
	for j in range(0, len(std_values)):
	    f.write(str(std_values[j]) + ' ')
	f.write('\n')

# Ignore first values close to 0
relevantIndex = 0
for mean_value in mean_values:
    if mean_value < 0.0001:
        relevantIndex = relevantIndex + 1

# Prepare MSD plot
f, ax = plt.subplots(1)

# Calculate x range and step
step = math.floor(limitTime * 0.1)
xRange = numpy.arange(start=0, stop=limitTime + step, step=step)

# Calculate various slopes and its curves
m = numpy.arange(start=0.0001, stop=0.0015, step=0.0001)
y = m * xRange[:, numpy.newaxis]

# Calculate sum of squared fit errors
y = transpose(y)
e = []
for yi in y:
    e.append(mean_values - yi)
e2 = [x**2 for x in e]
sums = [sum(x) for x in e2]

# Get best fit slope
from operator import itemgetter
minIndex = min(enumerate(sums), key=itemgetter(1))[0]
bestM = m[minIndex]

# Calculate best fit curve for relevant values of x
relevantxRange = xRange[relevantIndex:]
bestY = bestM * relevantxRange[:, numpy.newaxis]

# Plot data and best fit curve
ax.errorbar(xRange, mean_values, std_values, linestyle='None', marker='^')
ax.grid()
ax.set_ylim(bottom=0)
ax.set_xlim(0, limitTime + step)
plt.xlabel("Tiempo [s]")
plt.ylabel("Desplazamiento cuadrático medio [m$^2$]")
plt.xticks(xRange)
plt.plot(relevantxRange, bestY)
plt.legend(['Ajuste modelo lineal', 'Datos (promedios)'], loc=2)

# Add other slope curves to plot data
delete(m, minIndex)
for slope in m:
    y = slope * relevantxRange[:, numpy.newaxis]
    plt.plot(relevantxRange, y, alpha = 0.25)

# Save plot
plt.savefig('./output/bigParticleDiffusion/FullBigParticleDiffusion-Time={limitTime}'.format(limitTime = limitTime))

# Plot sum of squared fit errors
g, ay = plt.subplots(1)
ay.grid()
ay.set_ylim(bottom=0, top=max(sums))
ay.set_xlim(0, 0.0015)
plt.xlabel("Pendiente (coeficiente) [m$^2$/s]")
plt.ylabel("Error del ajuste [m$^4$]")
plt.xticks(m)
plt.ticklabel_format(style='sci', axis='x', scilimits=(0,0))
plt.plot(m, sums, linestyle='None', marker='*')
plt.show(g)
g.savefig('./output/bigParticleDiffusion/SquaredError-Time={limitTime}'.format(limitTime = limitTime))

# Calculate and print diffusion coefficient and R squared
print("Coeficiente de difusión (D) = %f [m²/s]" % (bestM))
print("R² = %f" % (coefficient_of_determination(mean_values[relevantIndex:], [j for i in bestY for j in i])))