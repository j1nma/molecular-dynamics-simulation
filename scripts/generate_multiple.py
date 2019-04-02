from functions import generate_multiple_files

numbers = [40,100,4000,10000]

i = 0
for x in numbers:
    i += 1
    generate_multiple_files(numbers[i-1], 20.0)
