package dsl.generator
import dsl.greenhouse.Model
import org.eclipse.xtext.EcoreUtil2
import dsl.greenhouse.RowSensor
import dsl.greenhouse.RowActuator
import dsl.greenhouse.GreenhouseActuator
import dsl.greenhouse.GreenhouseSensor
import dsl.greenhouse.Greenhouse
import dsl.greenhouse.Row
import dsl.greenhouse.Controller

class PeripheralGenerator {
	
	MathGenerator mathGenerator = new MathGenerator()
	
	
	def compilePeripheral(Model model, Controller controller){
		val root = EcoreUtil2.getRootContainer(model);
		val allSensors = EcoreUtil2.getAllContentsOfType(root, RowSensor).filter[it.controller.name == controller.name]
		val allGlobalSensors = EcoreUtil2.getAllContentsOfType(root, GreenhouseSensor).filter[it.controller.name == controller.name]
		
		return
		
		'''
		#include <Statistical.h>
		«IF controller.type.name == "ESP32"»
		#include <PubSubClient.h>
		#include <analogWrite.h>
		#include <WiFi.h>
		«ENDIF»
		«model.getAllSensorPreamble(controller)»
		«model.getAllSensorTimers(controller)»
		«getControllerTimer(controller)»
		
		long int sleepMicroSeconds = «getMaximumSleepTime(model, controller)»
				long int wakeMilliSeconds = 20000;
				long int sleepTimer = millis();
		«model.getAllActuatorTopics(controller)»
		«IF controller.type.name == "ESP32"»
			«setupWifi_ESP32()»
			«model.setupMQTT_ESP32(controller)»
			
	    «ENDIF»
		«IF controller.type.name == "ESP8266"»
			«setupWifi_ESP8266()»
			«model.setupMQTT_ESP8266(controller)»
	    «ENDIF»
		
		
		«model.getSensorMethods(controller)»
		«model.getActuatorMethods(controller)»
		«model.getSetup(controller)»
		«model.getLoop(controller)»
		
		'''
		
	}
	
	def getMaximumSleepTime(Model model, Controller controller){
		val root = EcoreUtil2.getRootContainer(model);
		val allSensors = EcoreUtil2.getAllContentsOfType(root, RowSensor).filter[it.controller.name == controller.name]
		val heartbeat = controller.heartBeat.freq.unit.getFreqUnitTime + "/" + mathGenerator.computeExpression(controller.heartBeat.freq.freq)
		return '''«IF allSensors.size > 0»min(«heartbeat», «FOR sensor : allSensors»min(«sensor.type.frequency.unit.getFreqUnitTime + "/" + mathGenerator.computeExpression(sensor.type.frequency.freq)», «ENDFOR»«mathGenerator.computeExpression(allSensors.findFirst[1 == 1].type.frequency.freq)»«FOR sensor : allSensors»)«ENDFOR»)«ENDIF»;'''
	}
	
	
	def getAllSensorPreamble(Model model, Controller controller){
		val root = EcoreUtil2.getRootContainer(model);
		val allSensors = EcoreUtil2.getAllContentsOfType(root, RowSensor).filter[it.controller.name == controller.name]
		val allGlobalSensors = EcoreUtil2.getAllContentsOfType(root, GreenhouseSensor).filter[it.controller.name == controller.name]
		return '''
		const int arrSize = 10;
		«IF allSensors.length > 0»
		// preamble for row sensors
		«FOR sensor: allSensors»
		#define «sensor.name»Topic "«(sensor.eContainer.eContainer as Greenhouse).name»/«(sensor.eContainer as Row).name»/«sensor.name»"
		float «sensor.name»ValueArray[arrSize];
		int «sensor.name»Counter = 0;
		«ENDFOR»
		«ENDIF»
		«IF allGlobalSensors.length > 0»
		// preamble for greenhouse sensors
		«FOR sensor: allGlobalSensors»
		#define «sensor.name»Topic "«(sensor.eContainer as Greenhouse).name»/«sensor.name»"
		float «sensor.name»ValueArray[arrSize];
		int «sensor.name»Counter = 0;
		«ENDFOR»
		«ENDIF»
		'''
	}
	
	def getControllerTimer(Controller controller){
		return '''
		#define «controller.name»Topic "«controller.name»"
		long int «controller.name»Timer = millis();
		'''
	}
	
	def getAllActuatorTopics(Model model, Controller controller){
		val root = EcoreUtil2.getRootContainer(model);
		val allRowActuators = EcoreUtil2.getAllContentsOfType(root, RowActuator).filter[it.controller.name == controller.name]
		val allGlobalActuators = EcoreUtil2.getAllContentsOfType(root, GreenhouseActuator).filter[it.controller.name == controller.name]
		return '''
		
		«IF allRowActuators.length > 0»
		// topics for row actuators
		«FOR actuator: allRowActuators»
		#define «actuator.name»Topic "«(actuator.eContainer.eContainer as Greenhouse).name»/«(actuator.eContainer as Row).name»/«actuator.name»"
		«ENDFOR»
		«ENDIF»
		«IF allGlobalActuators.length > 0»
		// topics for greenhouse actuators
		«FOR actuator: allGlobalActuators»
		#define «actuator.name»Topic "«(actuator.eContainer as Greenhouse).name»/«actuator.name»"
		«ENDFOR»
		«ENDIF»
		'''
	}
	
	
	def getAllSensorTimers(Model model, Controller controller){
		val root = EcoreUtil2.getRootContainer(model);
		val allSensors = EcoreUtil2.getAllContentsOfType(root, RowSensor).filter[it.controller.name == controller.name]
		val allGlobalSensors = EcoreUtil2.getAllContentsOfType(root, GreenhouseSensor).filter[it.controller.name == controller.name]
		return '''
		
		«IF allSensors.length > 0»
		// timers for row sensors
		«FOR sensor: allSensors»
		long int «sensor.name»Timer = millis();
		«ENDFOR»
		«ENDIF»
		«IF allGlobalSensors.length > 0»
		// timers for greenhouse sensors
		«FOR sensor: allGlobalSensors»
		long int «sensor.name»Timer = millis();
		«ENDFOR»
		«ENDIF»
		'''
	}
	
	def getSetup(Model model, Controller controller){
		val root = EcoreUtil2.getRootContainer(model);
		val allSensors = EcoreUtil2.getAllContentsOfType(root, RowSensor).filter[it.controller.name == controller.name]
		val allGlobalSensors = EcoreUtil2.getAllContentsOfType(root, GreenhouseSensor).filter[it.controller.name == controller.name]
		val allRowActuators = EcoreUtil2.getAllContentsOfType(root, RowActuator).filter[it.controller.name == controller.name]
		val allGlobalActuators = EcoreUtil2.getAllContentsOfType(root, GreenhouseActuator).filter[it.controller.name == controller.name]
		return	'''
		
		void debug(const char *s)
		{
		  Serial.print (millis());
		  Serial.print (" ");
		  Serial.println(s);
		}
		
		void setup() {
		  Serial.begin(115200);
	  	  while (!Serial) {
	          delay(100);
	      }
	  	  delay(10);
	  	  «IF controller.type.name == "ESP32"»
		  initWiFi();
		  client.setServer(mqtt_server, 1883);
		  client.setCallback(callback);
		  «ENDIF»
		  «IF controller.type.name == "ESP8266"»
		  connectToWifi();
		  mqtt_connect();
		  «ENDIF»
		  «IF allSensors.length > 0»
			// setup for row sensors
			«FOR sensor: allSensors»
			setup«sensor.name»();
			«ENDFOR»
			«ENDIF»
			«IF allGlobalSensors.length > 0»
			// setup for for greenhouse sensors
			«FOR sensor: allGlobalSensors»
			setup«sensor.name»();
			«ENDFOR»
			«ENDIF»
			«IF allRowActuators.length > 0»
			// setup for for row actuators
			«FOR actuator: allRowActuators»
			setup«actuator.name»();		«ENDFOR»
			«ENDIF»
			«IF allGlobalActuators.length > 0»
			// setup for greenhouse actuators
			«FOR actuator: allGlobalActuators»
			setup«actuator.name»();
			«ENDFOR»
			«ENDIF»
		}
		'''
	}

	
	def setupWifi_ESP32()'''
		//wifi
		const char* ssid = "LEO1_TEAM_06";
		const char* password = "embeddedlinux";
		void initWiFi() {
		  WiFi.mode(WIFI_STA);
		  WiFi.begin(ssid, password);
		  Serial.print("Connecting to WiFi ..");
		  while (WiFi.status() != WL_CONNECTED) {
		    Serial.print('.');
		    delay(1000);
		  }
		  Serial.println(WiFi.localIP());
		}
		'''
	
	def setupWifi_ESP8266()'''
		//wifi
		#define WIFI_SSID       "LEO1_TEAM_06"
		#define WIFI_PASSWORD    "embeddedlinux"
		#include <ESP8266WiFiMulti.h>
		#include <ESP8266HTTPClient.h>
		ESP8266WiFiMulti WiFiMulti;
		const uint32_t conn_tout_ms = 5000;
		WiFiClient wifi_client;
		void print_wifi_status()
		{
		  Serial.print (millis());
		  Serial.print(" WiFi connected: ");
		  Serial.print(WiFi.SSID());
		  Serial.print(" ");
		  Serial.print(WiFi.localIP());
		  Serial.print(" RSSI: ");
		  Serial.print(WiFi.RSSI());
		  Serial.println(" dBm");
		}
		
		void connectToWifi(){
		  // wifi
		  WiFi.persistent(false);
		  WiFi.mode(WIFI_STA);
		  WiFiMulti.addAP(WIFI_SSID, WIFI_PASSWORD);
		  if(WiFiMulti.run(conn_tout_ms) == WL_CONNECTED)
		  {
		    print_wifi_status();
		  }
		  else
		  {
		    debug("Unable to connect");
		  }
		  
		}
		'''
	
	def setupMQTT_ESP32(Model model, Controller controller){
		val root = EcoreUtil2.getRootContainer(model);
		val allRowActuators = EcoreUtil2.getAllContentsOfType(root, RowActuator).filter[it.controller.name == controller.name]
		val allGlobalActuators = EcoreUtil2.getAllContentsOfType(root, GreenhouseActuator).filter[it.controller.name == controller.name]
		
		return '''
		// MQTT setup
		const char* mqtt_server =  "192.168.10.1";
		WiFiClient espClient;
		PubSubClient client(espClient);
		void publish(const char* topic, const char* content){
			client.publish(topic, content);
		}
		
		void subscribe(const char* topic){
			client.subscribe(topic);
		}
		void reconnect() {
		  // Loop until we're reconnected
		  while (!client.connected()) {
		    Serial.print("Attempting MQTT connection...");
		    // Attempt to connect
		    if (client.connect("«controller.name»", "my_user", "bendevictor")) {
		      Serial.println("connected");
		      // Subscribe
		      «IF allRowActuators.length > 0»
				// topics for row actuators
		  		«FOR actuator: allRowActuators»
		  			subscribe(«actuator.name»Topic);
		  		«ENDFOR»
		  		«ENDIF»
		  		«IF allGlobalActuators.length > 0»
		  		// topics for greenhouse actuators
		  		«FOR actuator: allGlobalActuators»
		  			subscribe(«actuator.name»Topic);
		  		«ENDFOR»
		  		«ENDIF»
		    } else {
		      Serial.print("failed, rc=");
		      Serial.print(client.state());
		      Serial.println(" try again in 5 seconds");
		      // Wait 5 seconds before retrying
		      delay(5000);
		    }
		  }
		}
		
		void callback(char* topic, byte* message, unsigned int length) {
		  String msg = "";
		  for(int i = 0; i< length; i++){
		    msg = msg+(char)message[i];
		  }
		  «IF allRowActuators.length > 0»
			// topics for row actuators
	  		«FOR actuator: allRowActuators»
			  
			  if(String(topic) == String(«actuator.name»Topic)){
			    handle«actuator.name»Message(msg);
			  }
	  		«ENDFOR»
	  		«ENDIF»
	  		«IF allGlobalActuators.length > 0»
	  		// topics for greenhouse actuators
	  		«FOR actuator: allGlobalActuators»
	  			if(String(topic) == String(«actuator.name»Topic)){
				    handle«actuator.name»Message(msg);
				  }
	  		«ENDFOR»
	  		«ENDIF»
		}
		
		'''
	}
	
	def setupMQTT_ESP8266(Model model, Controller controller){
		val root = EcoreUtil2.getRootContainer(model);
		val allRowActuators = EcoreUtil2.getAllContentsOfType(root, RowActuator).filter[it.controller.name == controller.name]
		val allGlobalActuators = EcoreUtil2.getAllContentsOfType(root, GreenhouseActuator).filter[it.controller.name == controller.name]
		
		return '''
		// MQTT setup
		#define MQTT_SERVER      "192.168.10.1"
		#define MQTT_SERVERPORT  1883 
		#define MQTT_USERNAME    "my_user"
		#define MQTT_KEY         "bendevictor"
		#define MQTT_TOPIC        "mqtt"
		#include "Adafruit_MQTT.h"
		#include "Adafruit_MQTT_Client.h"
		
		Adafruit_MQTT_Client mqtt(&wifi_client, MQTT_SERVER, MQTT_SERVERPORT, MQTT_USERNAME, MQTT_KEY);
		
		void publish(const char* topic, const char* content){
			if((WiFiMulti.run(conn_tout_ms) == WL_CONNECTED))
			  {
			    print_wifi_status();
			
			    mqtt_connect();
			    char charBuf[50];
			    Adafruit_MQTT_Publish publish_topic = Adafruit_MQTT_Publish(&mqtt, topic);
			    Serial.println("connect success");
			    if (! publish_topic.publish(content))
			    {
			      debug("MQTT failed");
			    }
			    else
			    {
			      debug("MQTT ok");
			    }
			  }
		}
		
		
		«IF allRowActuators.length > 0»
		// topics for row actuators
	  		«FOR actuator: allRowActuators»
		Adafruit_MQTT_Subscribe «actuator.name»SubscribeTopic = Adafruit_MQTT_Subscribe(&mqtt, "«(actuator.eContainer.eContainer as Greenhouse).name»/«(actuator.eContainer as Row).name»/«actuator.name»");
	  		«ENDFOR»
  		«ENDIF»
  		«IF allGlobalActuators.length > 0»
		// topics for greenhouse actuators
	  		«FOR actuator: allGlobalActuators»
		Adafruit_MQTT_Subscribe «actuator.name»SubscribeTopic = Adafruit_MQTT_Subscribe(&mqtt, "«(actuator.eContainer as Greenhouse).name»/«actuator.name»");
	  		«ENDFOR»
  		«ENDIF»
		
		
		void mqtt_connect()
		{
		  int8_t ret;
		
		  // Stop if already connected.
		  if (! mqtt.connected())
		  {
		    debug("Connecting to MQTT... ");
		    while ((ret = mqtt.connect()) != 0)
		    { // connect will return 0 for connected
		         Serial.println(mqtt.connectErrorString(ret));
		         debug("Retrying MQTT connection in 5 seconds...");
		         mqtt.disconnect();
		         delay(5000);  // wait 5 seconds
		    }
		    debug("MQTT Connected");
		    «IF allRowActuators.length > 0»
		    	// subscribe to topics for row actuators
		    	  		«FOR actuator: allRowActuators»
		    	mqtt.subscribe(&«actuator.name»SubscribeTopic);
		    	  		«ENDFOR»
		      		«ENDIF»
		      		«IF allGlobalActuators.length > 0»
		    	// subscribe to topics for greenhouse actuators
		    	  		«FOR actuator: allGlobalActuators»
		    	mqtt.subscribe(&«actuator.name»SubscribeTopic);
		    	  		«ENDFOR»
		      		«ENDIF»
		  }
		}
		
		void recievedMessage(){
		   Adafruit_MQTT_Subscribe *subscription;
		   while ((subscription = mqtt.readSubscription(15000))) {
		   	«IF allRowActuators.length > 0»
			// topics for row actuators
	  		«FOR actuator: allRowActuators»
			  if(subscription == &«actuator.name»SubscribeTopic)){
			  	String msg = (char *)«actuator.name»SubscribeTopic.lastread;
			    handle«actuator.name»Message(msg);
			  }
	  		«ENDFOR»
	  		«ENDIF»
	  		«IF allGlobalActuators.length > 0»
	  		// topics for greenhouse actuators
	  		«FOR actuator: allGlobalActuators»
			if(subscription == &«actuator.name»SubscribeTopic){
				String msg = (char *)«actuator.name»SubscribeTopic.lastread;
				handle«actuator.name»Message(msg);
			}
	  		«ENDFOR»
	  		«ENDIF»
		
		  }
		}
		
		'''
	}
	
	
	
	def getSensorMethods(Model model, Controller controller){
		val root = EcoreUtil2.getRootContainer(model);
		val allSensors = EcoreUtil2.getAllContentsOfType(root, RowSensor).filter[it.controller.name == controller.name]
		val allGlobalSensors = EcoreUtil2.getAllContentsOfType(root, GreenhouseSensor).filter[it.controller.name == controller.name]
		return '''
		
		«IF allSensors.length > 0»
		// methods for row sensors
		«FOR sensor: allSensors»
		void setup«sensor.name»(){
			// insert code to setup sensor here
		}
		
		float get«sensor.name»Value(){
			// insert code to get value for sensor here
			// remember to return a float!
			// example: 
			// float lightValue = analogRead(lightPin);
			// Serial.println(lightValue);
		}
		«ENDFOR»
		«ENDIF»
		«IF allGlobalSensors.length > 0»
		// methods for greenhouse sensors
		«FOR sensor: allGlobalSensors»
		void setup«sensor.name»(){
			// insert code to setup sensor here
		}
		
		float get«sensor.name»Value(){
			// insert code to get value for sensor here
			// remember to return a float!
			// example: 
			// float lightValue = analogRead(lightPin);
		}
		«ENDFOR»
		«ENDIF»
		'''
	}
	
	
	def getActuatorMethods(Model model, Controller controller){
		val root = EcoreUtil2.getRootContainer(model);
		val allRowActuators = EcoreUtil2.getAllContentsOfType(root, RowActuator).filter[it.controller.name == controller.name]
		val allGlobalActuators = EcoreUtil2.getAllContentsOfType(root, GreenhouseActuator).filter[it.controller.name == controller.name]
		return '''
		
		«IF allRowActuators.length > 0»
		// methods for row actuators
		«FOR actuator: allRowActuators»
		void setup«actuator.name»(){
			// insert code to setup actuator here
		}
		void handle«actuator.name»Message(String msg){
			«FOR action: actuator.action»
			if(msg == "«action.trigger.name»"){
				// handle message
				String strValue = "messageReceived";
				int str_len = strValue.length() + 1;
				char char_array[str_len];
				strValue.toCharArray(char_array, str_len);
				publish(«actuator.name»Topic, char_array);
			}
			«ENDFOR»
		}
		«ENDFOR»
		«ENDIF»
		«IF allGlobalActuators.length > 0»
		// methods for greenhouse actuators
		«FOR actuator: allGlobalActuators»
		void setup«actuator.name»(){
			// insert code to setup actuator here
		}
		void handle«actuator.name»Message(String msg){
			«FOR action: actuator.action»
			if(msg == "«action.trigger.name»"){
				// handle message
				String strValue = "messageReceived";
				int str_len = strValue.length() + 1;
				char char_array[str_len];
				strValue.toCharArray(char_array, str_len);
				publish(«actuator.name»Topic, char_array);
			}
			«ENDFOR»
		}
		«ENDFOR»
		«ENDIF»
		'''
	}
	
	
	
	def getLoop(Model model, Controller controller){
		val root = EcoreUtil2.getRootContainer(model);
		val allSensors = EcoreUtil2.getAllContentsOfType(root, RowSensor).filter[it.controller.name == controller.name]
		val allGlobalSensors = EcoreUtil2.getAllContentsOfType(root, GreenhouseSensor).filter[it.controller.name == controller.name]
		return '''
		void loop(){
			
			«IF controller.type.name == "ESP32"»
			if (!client.connected()) {
			    reconnect();
			}
			client.loop();
			
	        «ENDIF»
	        «IF controller.type.name == "ESP8266"»
            recievedMessage();
	        «ENDIF»
			
			«getHeartbeat(controller)»

			«IF allSensors.length > 0»
			// publishing for row sensors
			«FOR sensor: allSensors»
			«sensor.getRowSensorLoop()»
			«ENDFOR»
			«ENDIF»
			«IF allGlobalSensors.length > 0»
			// publishing for global sensors
			«FOR sensor: allGlobalSensors»
			«sensor.getGreenhouseSensorLoop()»
			«ENDFOR»
			«ENDIF»
			«IF allSensors.size <= 0 && allGlobalSensors.size <= 0»
			«IF controller.type.name == "ESP32"»
			if(millis() >= (sleepTimer + wakeMilliSeconds)){
				esp_sleep_enable_timer_wakeup(sleepMicroSeconds);
			}
	        «ENDIF»
	        «IF controller.type.name == "ESP8266"»
			if(millis() >= (sleepTimer + wakeMilliSeconds)){
				ESP.deepSleep(30e6);
			}
	        «ENDIF»
	        «ENDIF»
		}
		'''
	}
	
	def getRowSensorLoop(RowSensor sensor){
		return 
		'''
		
		if(millis() >= («sensor.name»Timer + («sensor.type.frequency.unit.getFreqUnitTime»/(«mathGenerator.computeExpression(sensor.type.frequency.freq)»)))){
			«sensor.name»ValueArray[«sensor.name»Counter%arrSize] = get«sensor.name»Value();
			«sensor.name»Counter += 1;
			Array_Stats<float> Data_Array(«sensor.name»ValueArray, sizeof(«sensor.name»ValueArray) / sizeof(«sensor.name»ValueArray[0]));
			float value = 0;
			«IF sensor.type.reducer.name == "average"»
			value = Data_Array.Average(Data_Array.Arithmetic_Avg);
			«ENDIF»
			«IF sensor.type.reducer.name == "median"»
			value = Data_Array.Quartile(2);
			«ENDIF»
			String strValue = String(value);
			int str_len = strValue.length() + 1;
			char char_array[str_len];
		    strValue.toCharArray(char_array, str_len);
			publish(«sensor.name»Topic, char_array);
			«sensor.name»Timer = millis();
		}
		'''
	}
	
	def getGreenhouseSensorLoop(GreenhouseSensor sensor){
		return 
		'''
		
		if(millis() >= («sensor.name»Timer + («sensor.type.frequency.unit.getFreqUnitTime»/(«mathGenerator.computeExpression(sensor.type.frequency.freq)»)))){
			«sensor.name»ValueArray[«sensor.name»Counter%arrSize] = get«sensor.name»Value();
			«sensor.name»Counter += 1;
			Array_Stats<float> Data_Array(«sensor.name»ValueArray, sizeof(«sensor.name»ValueArray) / sizeof(«sensor.name»ValueArray[0]));
			float value = 0;
			«IF sensor.type.reducer.name == "average"»
			value = Data_Array.Average(Data_Array.Arithmetic_Avg);
			«ENDIF»
			«IF sensor.type.reducer.name == "median"»
			value = Data_Array.Quartile(2);
			«ENDIF»
			String strValue = String(value);
			int str_len = strValue.length() + 1;
			char char_array[str_len];
		    strValue.toCharArray(char_array, str_len);
			publish(«sensor.name»Topic, char_array);
			«sensor.name»Timer = millis();
		}
		'''
	}
	
	def getHeartbeat(Controller controller){
		return '''
		if(millis() >= («controller.name»Timer + («controller.heartBeat.freq.unit.getFreqUnitTime»/(«mathGenerator.computeExpression(controller.heartBeat.freq.freq)»)))){
			
			String strValue = String(millis());
			int str_len = strValue.length() + 1;
			char char_array[str_len];
			strValue.toCharArray(char_array, str_len);
			publish(«controller.name»Topic, char_array);
			«controller.name»Timer = millis();
			
		}
		'''
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