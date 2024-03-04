import pandas as pd

# Read the CSV file
df = pd.read_csv('./../football/events.csv')

# Calculate the number of rows for the split
total_rows = len(df)
split_point = int(total_rows * 0.99)
split_point += 60

# Split the DataFrame
events_batch_data = df.iloc[:split_point]
events_speed = df.iloc[split_point:]

# Write the split dataframes to separate CSV files
events_batch_data.to_csv('events_batch_data.csv', index=False)
events_speed.to_csv('events_speed.csv', index=False)
