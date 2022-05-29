#include <Statistical.h>
#include <PubSubClient.h>
#include <analogWrite.h>
#include <WiFi.h>
const int arrSize = 10;
// preamble for row sensors
#define moistSensorTopic "greenify/tomatoes/moistSensor"
float moistSensorValueArray[arrSize];
int moistSensorCounter = 0;
// preamble for greenhouse sensors
#define lightSensorTopic "greenify/lightSensor"
float lightSensorValueArray[arrSize];
int lightSensorCounter = 0;

// timers for row sensorss
long int moistSensorTimer = millis();
// timers for greenhouse sensors
long int lightSensorTimer = millis();
#define LocalControllerTopic "LocalController"
long int LocalControllerTimer = millis();

long int sleepMicroSeconds = min(60000/3, min(3600000/5, 5));
		long int wakeMilliSeconds = 20000;
		long int sleepTimer = millis();

// topics for row actuators
#define pumpTopic "greenify/tomatoes/pump"
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
    if (client.connect("LocalController", "my_user", "bendevictor")) {
      Serial.println("connected");
      // Subscribe
// topics for row actuators
		  		subscribe(pumpTopic);
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
// topics for row actuators

if(String(topic) == String(pumpTopic)){
  handlepumpMessage(msg);
}
}





// methods for row sensors
void setupmoistSensor(){
	// insert code to setup sensor here
}

float getmoistSensorValue(){
	// insert code to get value for sensor here
	// remember to return a float!
	// example: 
	// float lightValue = analogRead(lightPin);
	// Serial.println(lightValue);
}
// methods for greenhouse sensors
void setuplightSensor(){
	// insert code to setup sensor here
}

float getlightSensorValue(){
	// insert code to get value for sensor here
	// remember to return a float!
	// example: 
	// float lightValue = analogRead(lightPin);
}

// methods for row actuators
void setuppump(){
	// insert code to setup actuator here
}
void handlepumpMessage(String msg){
	if(msg == "start"){
		// handle message
		String strValue = "messageReceived";
		int str_len = strValue.length() + 1;
		char char_array[str_len];
		strValue.toCharArray(char_array, str_len);
		publish(pumpTopic, char_array);
	}
	if(msg == "stop"){
		// handle message
		String strValue = "messageReceived";
		int str_len = strValue.length() + 1;
		char char_array[str_len];
		strValue.toCharArray(char_array, str_len);
		publish(pumpTopic, char_array);
	}
}

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
initWiFi();
client.setServer(mqtt_server, 1883);
client.setCallback(callback);
// setup for row sensors
setupmoistSensor();
	// setup for for greenhouse sensors
	setuplightSensor();
	// setup for for row actuators
	setuppump();		
}
void loop(){
	
	if (!client.connected()) {
	    reconnect();
	}
	client.loop();
	
	
	if(millis() >= (LocalControllerTimer + (60000/(3)))){
		
		String strValue = String(millis());
		int str_len = strValue.length() + 1;
		char char_array[str_len];
		strValue.toCharArray(char_array, str_len);
		publish(LocalControllerTopic, char_array);
		LocalControllerTimer = millis();
		
	}

	// publishing for row sensors
	
	if(millis() >= (moistSensorTimer + (3600000/(5)))){
		moistSensorValueArray[moistSensorCounter%arrSize] = getmoistSensorValue();
		moistSensorCounter += 1;
		Array_Stats<float> Data_Array(moistSensorValueArray, sizeof(moistSensorValueArray) / sizeof(moistSensorValueArray[0]));
		float value = 0;
		value = Data_Array.Average(Data_Array.Arithmetic_Avg);
		String strValue = String(value);
		int str_len = strValue.length() + 1;
		char char_array[str_len];
	    strValue.toCharArray(char_array, str_len);
		publish(moistSensorTopic, char_array);
		moistSensorTimer = millis();
	}
	// publishing for global sensors
	
	if(millis() >= (lightSensorTimer + (60000/(5)))){
		lightSensorValueArray[lightSensorCounter%arrSize] = getlightSensorValue();
		lightSensorCounter += 1;
		Array_Stats<float> Data_Array(lightSensorValueArray, sizeof(lightSensorValueArray) / sizeof(lightSensorValueArray[0]));
		float value = 0;
		value = Data_Array.Quartile(2);
		String strValue = String(value);
		int str_len = strValue.length() + 1;
		char char_array[str_len];
	    strValue.toCharArray(char_array, str_len);
		publish(lightSensorTopic, char_array);
		lightSensorTimer = millis();
	}
}
