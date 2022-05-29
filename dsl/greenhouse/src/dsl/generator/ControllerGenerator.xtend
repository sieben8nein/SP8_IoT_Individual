package dsl.generator

import dsl.greenhouse.Model
import org.eclipse.xtext.EcoreUtil2
import dsl.greenhouse.RowSensor
import dsl.greenhouse.GreenhouseSensor
import dsl.greenhouse.RowRuleSet
import dsl.greenhouse.GreenhouseRuleSet
import dsl.greenhouse.Greenhouse
import dsl.greenhouse.Row
import dsl.greenhouse.Controller

class ControllerGenerator {
	
	MathGenerator mathGenerator = new MathGenerator()
	
	def compileController(Model model){
		val root = EcoreUtil2.getRootContainer(model);
	    val allRowSensors = EcoreUtil2.getAllContentsOfType(root, RowSensor);
	    val allGreenhouseSensors = EcoreUtil2.getAllContentsOfType(root, GreenhouseSensor)
	    val allRowRuleset = EcoreUtil2.getAllContentsOfType(root, RowRuleSet)
	    val allGreenhouseRuleset = EcoreUtil2.getAllContentsOfType(root, GreenhouseRuleSet)
	    val allControllers = EcoreUtil2.getAllContentsOfType(root, Controller)
	    
	    '''
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
			«FOR sensor : allRowSensors»
			if sensor.name == "«(sensor.eContainer.eContainer as Greenhouse).name»/«(sensor.eContainer as Row).name»/«sensor.name»" and microcontrollers[microcontrollers.index('«sensor.controller.name»')].isHealthy:
				«FOR state : sensor.states»
				if float(value) «state.op» «mathGenerator.computeExpression(state.threshold)»:
					sensor.updateSensorState(states[«sensor.states.indexOf(state)»],client)
				«ENDFOR»
			«ENDFOR»
			«FOR sensor : allGreenhouseSensors»
			if sensor.name == "«(sensor.eContainer as Greenhouse).name»/«sensor.name»":
				«FOR state : sensor.states»
				if float(value) «state.op» «mathGenerator.computeExpression(state.threshold)»:
					sensor.updateSensorState(states[«sensor.states.indexOf(state)»],client)
				«ENDFOR»
			«ENDFOR»
			return
		
		def run():
			client = connect_mqtt()
			manualState = Sensor("manual", None, 0, None)
			sensors.append(manualState)
			
			«FOR controller : allControllers»
			«controller.name» = Microcontroller("«controller.name»", «getFreqUnitTime(controller.heartBeat.freq.unit)»/«mathGenerator.computeExpression(controller.heartBeat.freq.freq)»)
			microcontrollers.append(«controller.name»)
			«ENDFOR»
			«FOR sensor : allRowSensors»
			sr«allRowSensors.indexOf(sensor)» = Sensor("«(sensor.eContainer.eContainer as Greenhouse).name»/«(sensor.eContainer as Row).name»/«sensor.name»",[«FOR state : sensor.states»{"«state.name»":"«FOR rule : allRowRuleset»«IF rule.sensor.name == sensor.name && rule.state.name == state.name»«rule.trigger.name»«ENDIF»«ENDFOR»"},«ENDFOR»],0,"«(sensor.eContainer.eContainer as Greenhouse).name»/«(sensor.eContainer as Row).name»/«getRowActuatorName(model, sensor)»")
			sensors.append(sr«allRowSensors.indexOf(sensor)»)
			subscribe(client, sr«allRowSensors.indexOf(sensor)»)
			«ENDFOR»
			«FOR sensor : allGreenhouseSensors»
			sg«allGreenhouseSensors.indexOf(sensor)» = Sensor("«(sensor.eContainer as Greenhouse).name»/«sensor.name»",[«FOR state : sensor.states»{"«state.name»":"«FOR rule : allGreenhouseRuleset»«IF rule.sensor.name == sensor.name && rule.state.name == state.name»«rule.trigger.name»«ENDIF»«ENDFOR»"},«ENDFOR»],0,"«(sensor.eContainer as Greenhouse).name»/«getGreenhouseActuatorName(model, sensor)»")
			sensors.append(sg«allGreenhouseSensors.indexOf(sensor)»)
			subscribe(client, sg«allGreenhouseSensors.indexOf(sensor)»)
			«ENDFOR»
			client.loop_start()
			
			
			while(True):
			        time.sleep(1)
			        for controller in microcontrollers:
			            controller.isHealthy()
		
		if __name__ == '__main__':
			run()
		'''
	}
	
	def getRowActuatorName(Model model, RowSensor sensor){
		val root = EcoreUtil2.getRootContainer(model);
		val allRowRuleset = EcoreUtil2.getAllContentsOfType(root, RowRuleSet);
    	for (rule : allRowRuleset){
    		if(rule.sensor.name == sensor.name){
    			return '''«rule.actuator.name»'''
    		}
    	}
		return ''''''
	}
	def getGreenhouseActuatorName(Model model, GreenhouseSensor sensor){
		val root = EcoreUtil2.getRootContainer(model);
    	val allGreenhouseRuleset = EcoreUtil2.getAllContentsOfType(root, GreenhouseRuleSet);
    	for (rule : allGreenhouseRuleset){
    		if(rule.sensor.name == sensor.name){
    			return '''«rule.actuator.name»'''
    		}
    	}
		return ''''''
	}
	
	def int getFreqUnitTime(String unit){
		switch unit{
			case 'second': return 1000
			case 'minute': return 60*1000
			case 'hour': return 60*60*1000
			case 'day': return 24*60*60*1000
			case 'week': return 7*24*60*60*1000
			default: return 1000
		}	
	}
}