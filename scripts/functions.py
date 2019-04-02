from numpy import random, pi
import sys

def generate_dynamic_file(number_of_particles, area_length, name):
    with open(name, 'w') as f:
        f.write('0\n')
        for x in range(0, number_of_particles):
            randomAngle = random.uniform()*2*pi
            f.write('{} {} {}\n'.format(random.uniform(0, area_length), random.uniform(0, area_length), randomAngle))

def generate_files(number_of_particles, area_length):
    generate_dynamic_file(number_of_particles, area_length, 'Dynamic-N=' + str(number_of_particles) + '.txt')

def generate_multiple_files(number_of_particles, area_length):
    generate_dynamic_file(number_of_particles, area_length, 'Dynamic-N=' + str(number_of_particles) + '.txt')

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
