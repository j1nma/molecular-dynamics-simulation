from functions import is_int_string, generate_files
import sys

if len(sys.argv) != 8:
    sys.exit("Arguments missing. Exit.")

number_of_small_particles = sys.argv[1]
area_length = sys.argv[2]
max_velocity_module = sys.argv[3]
particle_radius = sys.argv[4]
particle_mass = sys.argv[5]
large_particle_radius = sys.argv[6]
large_particle_mass = sys.argv[7]

if not is_int_string(number_of_small_particles):
    sys.exit("Must be integer. Exit.")

generate_files(int(number_of_small_particles),
float(area_length),
float(max_velocity_module),
float(particle_radius),
float(particle_mass),
float(large_particle_radius),
float(large_particle_mass))
