import paho.mqtt.client as mqtt
import time
import random

broker = 'localhost'
port = 1883
topic = 'SENSOR_A'
client_id = 'SENSOR_A'

refresh_rate = 1

def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc))
    client.subscribe("CONTROL")

def on_message(client, userdata, msg):
    print(msg.topic+" "+str(msg.payload))
    try:
        x = int(msg.payload)     
        global refresh_rate
        refresh_rate = x 
    except ValueError:
         print("The received delay was not a number")


def update_server(client):
    global refresh_rate
    while True:
        time.sleep(refresh_rate)
        msg = f"New value: {random.randint(0, 1000000)}"
        publish_message(client, msg)
        

def publish_message(client, msg):
    result = client.publish(topic, msg)
    status = result[0]
    if status == 0:
        print(f"Sent `{msg}` to topic `{topic}`")
    else:
        print(f"Failed to send message to topic {topic}")


client = mqtt.Client(client_id)
client.on_connect = on_connect
client.on_message = on_message

client.connect(broker, port, 60)
client.loop_start()
update_server(client)