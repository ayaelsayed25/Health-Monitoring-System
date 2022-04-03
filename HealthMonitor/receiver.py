import socket
import os
import subprocess
from threading import Thread
import time


def write():
    log = open("logs.log", "a")
    for element in records:
        log.write(element + "\n")
    log.close()

    from datetime import datetime

    timestamp = datetime.today().strftime('%Y-%m-%d')
    path = "/user/data/" + timestamp + ".log"

    create_file = "hdfs dfs -touchz " + path
    os.system(create_file)
    start = time.time()
    append_to_file = "hadoop fs -appendToFile logs.log " + path
    os.system(append_to_file)
    stop = time.time()
    writing_speed = thres / (stop - start)
    print("Writing speed", writing_speed)
    with open("logs.log", 'r+') as f:
        f.truncate(0)


# receiver
UDP_IP = "206.189.108.184"
UDP_PORT = 4000
records = []
counter = 0
thres = 1024
sock = socket.socket(socket.AF_INET,  # Internet
                     socket.SOCK_DGRAM)  # UDP
sock.bind((UDP_IP, UDP_PORT))
receive_time = []
start_sys = time.time()
num_batches = 10
BATCHES = 10
while True:
    json_obj, addr = sock.recvfrom(4000)  # buffer size is 4000 bytes
    receive_time.append(time.time())
    records.append(json_obj.decode())
    counter += 1
    if counter == thres:
        # write to file
        thread = Thread(target=write)
        thread.start()
        thread.join()
        end_time = time.time()
        num_batches = num_batches - 1
        if num_batches == 0:
            throughput = BATCHES * thres / (end_time - start_sys)
            num_batches = BATCHES
            print("Throughput ", throughput)
        diff = [end_time - x for x in receive_time]
        avg_end_end = sum(diff) / thres
        print("Average end to end", avg_end_end)
        counter = 0
        records = []
        receive_time = []

