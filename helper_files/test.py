import csv

input_file = 'events_speed.csv'  # Replace with your file name
output_file = 'events_speed_out.csv'  # Replace with your desired output file name

with open(input_file, 'r') as csv_file, open(output_file, 'w', newline='') as output:
    reader = csv.reader(csv_file)
    writer = csv.writer(output)

    for row in reader:
        modified_row = [col.replace(',', ':') for col in row]
        writer.writerow(modified_row)

print("CSV file processed successfully!")