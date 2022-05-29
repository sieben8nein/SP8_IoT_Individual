from paho.mqtt import client as mqtt_client
import time
class Sensor:
	currentState = ""
	def __init__(self, name, states, variable, actuator):
		self.name = name
		self.states = states
		self.variable = variable
		self.actuator = actuator
	def updateSensor(self, variable, client):
		self.variable = variable
		ruleCheck(variable, self, client, self.states)
	def updateSensorState(self, state, client):
		theKey = next(iter(state))
		self.currentState = theKey
		publish(client, self.actuator, state.get(self.currentState))
class Microcontroller:
    lastheartbeat = time.time()
    healthy = True
    def __init__(self, name, delay):
        self.name = name
        self.delay = delay
    def isHealthy(self):
        self.updateHealth()
        return self.healthy
    def updateHeartbeat(self):
        print("updating heartbeat for: " + self.name + ", " + str(time.time() - self.lastheartbeat) + " seconds since the last heartbeat" )
        self.updateHealth()
        self.lastheartbeat = time.time()
    def updateHealth(self):
        if(self.lastheartbeat + self.delay < (time.time())):
            print("microcontroller " + self.name + " not healthy")
            self.healthy = False
        else:
            print("microcontroller " + self.name + " healthy")
            self.healthy = True
broker = 'localhost'
port = 1883
client_id = 'python-mqtt-controller'
username = 'my_user'
password = 'bendevictor'
manual = 0
sensors = []
microcontrollers = []
def connect_mqtt() -> mqtt_client:
	def on_connect(client, userdata, flags, rc):
		if rc == 0:
			print("Connected to MQTT Broker!")
		else:
			print("Failed to connect, return code %d\n", rc)

	client = mqtt_client.Client(client_id)
	client.username_pw_set(username, password)
	client.on_connect = on_connect
	client.connect(broker, port)
	return client


def subscribe(client: mqtt_client, sensor):
    def on_message(client, userdata, msg):
        print(f"Received `{msg.payload.decode()}` from `{msg.topic}` topic")
        for s in sensors:
            if s.name == msg.topic:
                s.updateSensor(msg.payload.decode(), client)
        for mc in microcontrollers:
            if mc.name == msg.topic:
                mc.updateHeartbeat()
    client.subscribe(sensor.name)
    client.on_message = on_message
def publish(client,topic, message):
	msg = message
	if manual == 0:
		result = client.publish(topic, msg)
		# result: [0, 1]
		status = result[0]
		if status == 0:
			print(f"Send `{msg}` to topic `{topic}`")
		else:
			print(f"Failed to send message to topic {topic}")
			
def ruleCheck(value, sensor, client,states):
	if sensor.name == "manual":
		global manual 
		manual = int(value)
	if sensor.name == "greenify/tomatoes/moistSensor" and microcontrollers[microcontrollers.index('LocalController')].isHealthy:
		if float(value) > 1000:
			sensor.updateSensorState(states[0],client)
		if float(value) > 500:
			sensor.updateSensorState(states[1],client)
		if float(value) < 500:
			sensor.updateSensorState(states[2],client)
	if sensor.name == "greenify/tempSensor":
		if float(value) > 40:
			sensor.updateSensorState(states[0],client)
		if float(value) > 25:
			sensor.updateSensorState(states[1],client)
		if float(value) < 25:
			sensor.updateSensorState(states[2],client)
	if sensor.name == "greenify/humSensor":
		if float(value) > 40:
			sensor.updateSensorState(states[0],client)
		if float(value) > 30:
			sensor.updateSensorState(states[1],client)
		if float(value) < 30:
			sensor.updateSensorState(states[2],client)
	if sensor.name == "greenify/co2Sensor":
		if float(value) > 1000:
			sensor.updateSensorState(states[0],client)
		if float(value) > 800:
			sensor.updateSensorState(states[1],client)
		if float(value) < 800:
			sensor.updateSensorState(states[2],client)
	if sensor.name == "greenify/lightSensor":
		if float(value) > 1000:
			sensor.updateSensorState(states[0],client)
		if float(value) > 800:
			sensor.updateSensorState(states[1],client)
		if float(value) < 800:
			sensor.updateSensorState(states[2],client)
	return

def run():
	client = connect_mqtt()
	manualState = Sensor("manual", None, 0, None)
	sensors.append(manualState)
	
	LocalController = Microcontroller("LocalController", 60000/3)
	microcontrollers.append(LocalController)
	GlobalController = Microcontroller("GlobalController", 3600000/((9/3)*3)
	microcontrollers.append(GlobalController)
	sr0 = Sensor("greenify/tomatoes/moistSensor",[{"moist":""},{"optimal":""},{"dry":"start"},],0,"greenify/tomatoes/pump")
	sensors.append(sr0)
	subscribe(client, sr0)
	sg0 = Sensor("greenify/tempSensor",[{"hot":""},{"optimal":""},{"cold":""},],0,"greenify/")
	sensors.append(sg0)
	subscribe(client, sg0)
	sg1 = Sensor("greenify/humSensor",[{"humid":"min"},{"optimal":""},{"dry":""},],0,"greenify/windowopener")
	sensors.append(sg1)
	subscribe(client, sg1)
	sg2 = Sensor("greenify/co2Sensor",[{"tooMuch":""},{"optimal":""},{"tooLittle":""},],0,"greenify/")
	sensors.append(sg2)
	subscribe(client, sg2)
	sg3 = Sensor("greenify/lightSensor",[{"tooMuch":""},{"optimal":""},{"tooLittle":""},],0,"greenify/")
	sensors.append(sg3)
	subscribe(client, sg3)
	client.loop_start()
	
	
	while(True):
	        time.sleep(1)
	        for controller in microcontrollers:
	            controller.isHealthy()

if __name__ == '__main__':
	run()
