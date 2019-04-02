from functions import is_int_string, generate_files
import sys

if len(sys.argv) == 1 or len(sys.argv) == 2:
    sys.exit("Arguments missing. Exit.")

number_of_particles = sys.argv[1]
area_length = sys.argv[2]

if not is_int_string(number_of_particles):
    sys.exit("Must be integer. Exit.")

generate_files(int(number_of_particles), float(sys.argv[2]))