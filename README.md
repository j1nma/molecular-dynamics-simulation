# Event Driven Molecular Dynamics Simulation of Hard Spheres: Brownian Movement

Molecular dynamics simulation of hard spheres governed by events with elastic collisions.
Particles follow a uniform rectilinear motion between collisions.
N is the number of particles, and the maximum that allows simulations in reasonable times.

Different behaviors of the system are studied varying N.

## Directed by Events
Only if an event happens, the state of the simulation is updated.

The simulations have an intrinsic variable dt, depending on when the events happen. Therefore a dt2 constant and 
independent of the previous one is considered to print the state of the system (positions and velocities of the 
particles) to then make animations with a uniform temporal step.

The simulation generates an output in text file format. Then the animation module runs independently taking these text 
files as input. In this way the speed of the animation is not subject to the speed of the simulation.


## Brownian Movement
Consider a square domain of side L = 0.5 m.
Inside it place N small particles of radius R1 = 0.005 m and mass m1 = 0.1 g 
and a large particle of radius R2 = 0.05 and mass m2 = 100 g.

### Initial conditions
The positions of all particles must be random with uniform distribution within the domain. 
Small particles must have velocities with a uniform distribution in the range: | v | <0.1 m / s. 
The large particle must have initial velocity v2 = 0 and its initial position at x = L / 2, y = L / 2

### Contour Conditions:
Confined system, that is, all walls are rigid.

### Evolution of the system and calculations:
   * Collision frequency (number of collisions per unit of time); Average and probability distribution 
    (or alternatively, PDF) of collision times.
   * Probability distribution (or alternatively, PDF) of the velocity module in the last third of the simulation. 
    Comparison with the PDF of the initial state of the system (at t = 0).
   * Graph of the trajectory of the large particle for different temperatures. 
    How to change the temperature in the simulated system?
   * Estimate the diffusion coefficient of the large particle (and some of the small ones) by calculating the mean 
    square displacement as a function of time. For the particle that is studied, 
    only trajectories are considered until they hit the walls.

## Compilation

```
mvn clean package
```

## Execution
### Generation of dynamic file
[TODO]
```
python [TODO]
```

[TODO]
```
python [TODO]
```

### Running simulation

```
java -jar [TODO]
```

Options [TODO]:

* **-h, --help**: Prints usage infp.
* **-M, --matrix &lt;size>**: Size of the squared matrix.
* **-r, --radius &lt;double>**: Interaction radius.
* **--pbc**: Enable periodic boundary conditions.
* **--bf**: Enable brute force algorithm.
* **--dynamicFile &lt;path>**: Path to dynamic file.

The simulation's results [TODO] to `ovito_file.txt`.

### Run script to [TODO]

```
python3 scripts/[TODO].py
```

### (Octave) Run script to graph [TODO]

```
run("[TODO].m")
```
