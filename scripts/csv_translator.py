import csv
from io import StringIO
import requests
import json

d = {}

id = "10zt1QXvCbeWGicHwQNelYk3qXUy1hLBQccDPsx1aApE"

r = "https://docs.google.com/spreadsheets/export?id={}&exportFormat=csv".format(id)

data = requests.get(r).text

reader = csv.reader(StringIO(data, newline=''), delimiter=',')

n = 1
for row in reader:
    if n >= 11:
        l = []
        i = 0
        for modifier in row[2:]:
            if not modifier:
                break

            if len(l) == 0 or row[3].startswith(row[2]):
                l.append(modifier)
            else:
                l.append(l[-1] + " " + modifier)
            i += 1
        if l != []:
            d[row[0]] = l
    n += 1

print(d)

final = []

with open("../src/main/resources/assets/nimipona/toki/tok.json", 'w') as f:
    json.dump(d, f)
    