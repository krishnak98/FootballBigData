import csv

# Expected schema for the CSV file
expected_schema = [
    'id_odsp', 'id_event', 'sort_order', 'time', 'text', 'event_type', 'event_type2',
    'side', 'event_team', 'opponent', 'player', 'player2', 'player_in', 'player_out',
    'shot_place', 'shot_outcome', 'is_goal', 'location', 'bodypart', 'assist_method',
    'situation', 'fast_break'
]

def check_empty_rows(file_path):
    with open(file_path, 'r', newline='') as csvfile:
        csv_reader = csv.reader(csvfile)
        empty_rows = []
        for row_num, row in enumerate(csv_reader, start=1):
            if not any(row):
                empty_rows.append(row_num)
        return empty_rows

def validate_schema(file_path):
    with open(file_path, 'r', newline='') as csvfile:
        csv_reader = csv.reader(csvfile)
        headers = next(csv_reader)  # Read the first row as headers

        # Check if the headers in CSV match the expected schema
        if headers != expected_schema:
            print("CSV headers do not match the expected schema.")
            return False
        
        # Check the number of columns (commas) in each row
        for row_num, row in enumerate(csv_reader, start=2):  # Starting from the second row
            if len(row) != len(expected_schema):
                print(f"Row {row_num} has an incorrect number of columns.")
                return False
            
            # Check for missing data (empty fields)
            empty_indices = [idx for idx, val in enumerate(row) if val == '']
            if empty_indices:
                print(f"Row {row_num} has missing data at indices: {empty_indices}")
                print(expected_schema[10])
                # return False
        return False
        return True  # Return True if CSV data matches schema expectations

# Replace 'file_path' with the path to your CSV file
file_path = '../football/events.csv'

# Check for empty rows
empty_rows = check_empty_rows(file_path)
if empty_rows:
    print(f"The following rows are empty: {empty_rows}")
else:
    print("No empty rows found.")

# Validate schema, number of columns, and missing data
validation_result = validate_schema(file_path)
if validation_result:
    print("CSV data matches the expected schema, and there are no missing data.")
else:
    print("There are issues with the CSV data.")
