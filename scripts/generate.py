from functions import is_int_string, get_radius, get_area_length, generate_files
import sys

number_of_particles = input("Enter number of particles N: ")
area_length = get_area_length()

if not is_int_string(number_of_particles) or not is_int_string(area_length):
    sys.exit("All arguments must be integers. Exit.")

generate_files(int(number_of_particles), area_length)
