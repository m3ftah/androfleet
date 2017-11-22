#!/usr/bin/env python
import web
import xml.etree.ElementTree as ET
import json
from pprint import pprint

with open('tasks.json') as data_file:
    data = json.load(data_file)

#pprint(json.dumps(data))

urls = (
    '/checkin','checkin',
    '/postmeasurement', 'postmeasurement'
)

app = web.application(urls, globals())

class postmeasurement:
    def POST(self):
        message = json.loads(web.data())
        pprint(json.dumps(message))
        output = '{ "success": true }'
        return output
class checkin:
    def POST(self):
        output = json.dumps(data)
        return output

if __name__ == "__main__":
    app.run()
