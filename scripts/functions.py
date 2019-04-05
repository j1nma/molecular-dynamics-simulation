from numpy import random, pi, power, concatenate
import sys

def is_valid_position(otherX, otherY, otherRadius, newX, newY, newRadius):
    return (power(otherX - newX, 2) + pow(otherY - newY, 2)) > pow(otherRadius + newRadius, 2)

def generate_static_file(name, number_of_small_particles, area_length, particle_radius, particle_mass, large_particle_radius, large_particle_mass):
    with open(name, 'w') as f:
        one_value = '{}\n'
        f.write(one_value.format(number_of_small_particles + 1))
        for x in range(0, number_of_small_particles):
            f.write('{} {}\n'.format(particle_radius, particle_mass))
        # Large particle
        f.write('{} {}\n'.format(large_particle_radius, large_particle_mass))

def generate_dynamic_file(name, number_of_small_particles, area_length, max_velocity_module, particle_radius, large_particle_radius):
    with open(name, 'w') as f:
        f.write('{}\n{}\n'.format(number_of_small_particles, area_length))

        # Large particle
        particles = [[area_length/2, area_length/2, large_particle_radius]]
        f.write('1\t{}\t{}\t{}\t{}\n'.format(area_length/2, area_length/2, 0, 0))

        # Small particles
        for i in range(0, number_of_small_particles):
            validPosition = False
            x = 0
            y = 0
            while not validPosition:
                x = random.uniform() * (area_length - 2*particle_radius) + particle_radius
                y = random.uniform() * (area_length - 2*particle_radius) + particle_radius
                j = 0
                validPosition = True
                while j < len(particles) and validPosition:
                    validPosition = is_valid_position(particles[j][0], particles[j][1], particles[j][2], x, y, particle_radius)
                    j = j + 1
            vx = random.uniform() * (max_velocity_module - (-max_velocity_module)) + (-max_velocity_module)
            vy = random.uniform() * (max_velocity_module - (-max_velocity_module)) + (-max_velocity_module)
            particles = concatenate((particles, [[x, y, particle_radius]]), axis=0)
            f.write('{}\t{}\t{}\t{}\t{}\n'.format(i + 2, x, y, vx, vy))

def generate_files(number_of_small_particles, area_length, max_velocity_module, particle_radius, particle_mass, large_particle_radius, large_particle_mass):
    generate_static_file('Static-N=' + str(number_of_small_particles) + '.txt', number_of_small_particles, area_length, particle_radius, particle_mass, large_particle_radius, large_particle_mass)
    generate_dynamic_file('Dynamic-N=' + str(number_of_small_particles) + '.txt', number_of_small_particles, area_length, max_velocity_module, particle_radius, large_particle_radius)

def generate_multiple_files(index, number_of_small_particles, area_length, particle_radius, particle_mass, large_particle_radius, large_particle_mass):
    generate_static_file('Static-' + str(index) + '.txt', number_of_small_particles, area_length, particle_radius, particle_mass, large_particle_radius, large_particle_mass)
    generate_dynamic_file('Dynamic-N=' + str(number_of_small_particles) + '.txt', number_of_small_particles, area_length, max_velocity_module, particle_radius, large_particle_radius)

def is_int_string(s):
    try:
        int(s)
        return True
    except ValueError:
        return False

def get_radius():
    while True:
        try:
            return float(input("Enter particle radius r: "))
        except ValueError:
            print("Number not a float.")

def get_area_length():
    while True:
        try:
            return float(input("Enter area length L: "))
        except ValueError:
            print("Number not a float.")
