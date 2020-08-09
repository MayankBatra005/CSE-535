from sqlalchemy.ext.automap import automap_base
from sqlalchemy.orm import Session
from sqlalchemy import create_engine
from math import sin, cos, sqrt, atan2, radians
import pprint
import datetime
import sys
import os

current_location = os.path.dirname(os.path.abspath(__file__))

class Person:

    def __init__(self, db_id, start_date, end_date, is_target=False):
        self.db_id = db_id
        self.start_date = start_date
        self.end_date = end_date
        self.is_target = is_target
        self.Base = automap_base()
        self.get_db()
        self.possible_times = self.get_possible_times()

    def get_db(self):
        self.engine = create_engine(f'sqlite:///{current_location}/LifeMap_GS{self.db_id}.db')
        self.session = Session(self.engine)

        self.Base.prepare(self.engine, reflect=True)
        self.apTable = self.Base.classes.apTable
        self.batteryTable = self.Base.classes.batteryTable
        self.categorySetTable = self.Base.classes.categoryTable
        self.cellEdgeTable = self.Base.classes.cellEdgeTable
        self.cellNodeTable = self.Base.classes.cellNodeTable
        self.cellTable = self.Base.classes.cellTable
        self.configureTable = self.Base.classes.configureTable
        self.edgeTable = self.Base.classes.edgeTable
        self.locationTable = self.Base.classes.locationTable
        self.noRadioTable = self.Base.classes.noRadioTable
        self.sensorUsageTable = self.Base.classes.sensorUsageTable
        self.stayCommentTable = self.Base.classes.stayCommentTable
        self.stayTable = self.Base.classes.stayTable

    def convert_to_date(self, date):
        return datetime.datetime.strptime(date[:-3], '%Y%m%d%H%M%S')
    
    def get_possible_times(self):
        possible_times = []
        for stay in self.session.query(self.stayTable).all():
            stay_start_time = self.convert_to_date(stay._stay_start_time)
            if self.start_date <= stay_start_time and stay_start_time < self.end_date:
                possible_times.append(stay)
        return possible_times
    
    def is_within_n_miles(self, t_lat, t_lon, s_lat, s_lon, miles=5):
        R = 6373.0 #Earth Radius in km
        lat1 = radians(t_lat)
        lon1 = radians(t_lon)
        lat2 = radians(s_lat)
        lon2 = radians(s_lon)

        dlon = lon2 - lon1
        dlat = lat2 - lat1

        a = sin(dlat/2)**2 + cos(lat1) * cos(lat2) * sin(dlon/2)**2
        c = 2*atan2(sqrt(a), sqrt(1-a))

        distance = R*c
        distance /= 1.609344 #Convert to miles
        return distance < miles

    
    def has_been_in_contact(self, person):
        time_intersections = []
        for target_time in self.possible_times:
            for stay_time in person.possible_times:
                t_start = self.convert_to_date(target_time._stay_start_time) - datetime.timedelta(minutes=30)
                t_end = self.convert_to_date(target_time._time_stay) + datetime.timedelta(minutes=30)

                s_start = self.convert_to_date(stay_time._stay_start_time)
                s_end = self.convert_to_date(stay_time._time_stay)

                latest_start = max(t_start, s_start)
                earliest_end = min(t_end, s_end)
                delta = (earliest_end - latest_start)
                #print(earliest_end, latest_start, delta.total_seconds())
                if delta.total_seconds() > 0:
                    time_intersections.append((target_time, stay_time))
                    target_loc = self.session.query(self.locationTable).filter(self.locationTable._node_id == target_time._node_id).first()
                    stay_loc = person.session.query(person.locationTable).filter(person.locationTable._node_id == stay_time._node_id).first()
                    t_lat, t_lon = target_loc._latitude/10**6, target_loc._longitude/10**6
                    s_lat, s_lon = stay_loc._latitude/10**6, stay_loc._longitude/10**6
                    if self.is_within_n_miles(t_lat, t_lon, s_lat, s_lon, miles=5):
                        return True
        return False





if __name__ == '__main__':
    db_id = int(sys.argv[1])
    date = datetime.datetime.strptime(sys.argv[2], '%m/%d/%Y')
    start_date = date + datetime.timedelta(days=-7) 
    end_date = date + datetime.timedelta(days=7) 
    target = Person(db_id, start_date, end_date, is_target=True)
    people = [Person(x, start_date, end_date) for x in range(1,13) if x != db_id]

    adj_matrix = [[0]*(len(people)+1) for x in range(len(people)+1)]
    for person in people:
        adj_matrix[person.db_id - 1][target.db_id-1] = int(target.has_been_in_contact(person))

    adj_matrix[target.db_id-1][target.db_id-1] = 1
    pprint.pprint(adj_matrix)