#include <Statistical.h>
const int arrSize = 10;
// preamble for greenhouse sensors
#define tempSensorTopic "greenify/tempSensor"
float tempSensorValueArray[arrSize];
int tempSensorCounter = 0;
#define humSensorTopic "greenify/humSensor"
float humSensorValueArray[arrSize];
int humSensorCounter = 0;
#define co2SensorTopic "greenify/co2Sensor"
float co2SensorValueArray[arrSize];
int co2SensorCounter = 0;

// timers for greenhouse sensors
long int tempSensorTimer = millis();
long int humSensorTimer = millis();
long int co2SensorTimer = millis();
#define GlobalControllerTopic "GlobalController"
long int GlobalControllerTimer = millis();

long int sleepMicroSeconds = ;
		long int wakeMilliSeconds = 20000;
		long int sleepTimer = millis();

// topics for greenhouse actuators
#define windowopenerTopic "greenify/windowopener"
#define fanTopic "greenify/fan"
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


// topics for greenhouse actuators
Adafruit_MQTT_Subscribe windowopenerSubscribeTopic = Adafruit_MQTT_Subscribe(&mqtt, "greenify/windowopener");
Adafruit_MQTT_Subscribe fanSubscribeTopic = Adafruit_MQTT_Subscribe(&mqtt, "greenify/fan");


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
// subscribe to topics for greenhouse actuators
mqtt.subscribe(&windowopenerSubscribeTopic);
mqtt.subscribe(&fanSubscribeTopic);
  }
}

void recievedMessage(){
   Adafruit_MQTT_Subscribe *subscription;
   while ((subscription = mqtt.readSubscription(15000))) {
	  		// topics for greenhouse actuators
if(subscription == &windowopenerSubscribeTopic){
	String msg = (char *)windowopenerSubscribeTopic.lastread;
	handlewindowopenerMessage(msg);
}
if(subscription == &fanSubscribeTopic){
	String msg = (char *)fanSubscribeTopic.lastread;
	handlefanMessage(msg);
}

  }
}




// methods for greenhouse sensors
void setuptempSensor(){
	// insert code to setup sensor here
}

float gettempSensorValue(){
	// insert code to get value for sensor here
	// remember to return a float!
	// example: 
	// float lightValue = analogRead(lightPin);
}
void setuphumSensor(){
	// insert code to setup sensor here
}

float gethumSensorValue(){
	// insert code to get value for sensor here
	// remember to return a float!
	// example: 
	// float lightValue = analogRead(lightPin);
}
void setupco2Sensor(){
	// insert code to setup sensor here
}

float getco2SensorValue(){
	// insert code to get value for sensor here
	// remember to return a float!
	// example: 
	// float lightValue = analogRead(lightPin);
}

// methods for greenhouse actuators
void setupwindowopener(){
	// insert code to setup actuator here
}
void handlewindowopenerMessage(String msg){
	if(msg == "min"){
		// handle message
		String strValue = "messageReceived";
		int str_len = strValue.length() + 1;
		char char_array[str_len];
		strValue.toCharArray(char_array, str_len);
		publish(windowopenerTopic, char_array);
	}
	if(msg == "close"){
		// handle message
		String strValue = "messageReceived";
		int str_len = strValue.length() + 1;
		char char_array[str_len];
		strValue.toCharArray(char_array, str_len);
		publish(windowopenerTopic, char_array);
	}
}
void setupfan(){
	// insert code to setup actuator here
}
void handlefanMessage(String msg){
	if(msg == "max"){
		// handle message
		String strValue = "messageReceived";
		int str_len = strValue.length() + 1;
		char char_array[str_len];
		strValue.toCharArray(char_array, str_len);
		publish(fanTopic, char_array);
	}
	if(msg == "stop"){
		// handle message
		String strValue = "messageReceived";
		int str_len = strValue.length() + 1;
		char char_array[str_len];
		strValue.toCharArray(char_array, str_len);
		publish(fanTopic, char_array);
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
  connectToWifi();
  mqtt_connect();
	// setup for for greenhouse sensors
	setuptempSensor();
	setuphumSensor();
	setupco2Sensor();
	// setup for greenhouse actuators
	setupwindowopener();
	setupfan();
}
void loop(){
	
recievedMessage();
	
	if(millis() >= (GlobalControllerTimer + (3600000/(((9/3)*3))))){
		
		String strValue = String(millis());
		int str_len = strValue.length() + 1;
		char char_array[str_len];
		strValue.toCharArray(char_array, str_len);
		publish(GlobalControllerTopic, char_array);
		GlobalControllerTimer = millis();
		
	}

	// publishing for global sensors
	
	if(millis() >= (tempSensorTimer + (60000/(4)))){
		tempSensorValueArray[tempSensorCounter%arrSize] = gettempSensorValue();
		tempSensorCounter += 1;
		Array_Stats<float> Data_Array(tempSensorValueArray, sizeof(tempSensorValueArray) / sizeof(tempSensorValueArray[0]));
		float value = 0;
		value = Data_Array.Average(Data_Array.Arithmetic_Avg);
		String strValue = String(value);
		int str_len = strValue.length() + 1;
		char char_array[str_len];
	    strValue.toCharArray(char_array, str_len);
		publish(tempSensorTopic, char_array);
		tempSensorTimer = millis();
	}
	
	if(millis() >= (humSensorTimer + (3600000/(9)))){
		humSensorValueArray[humSensorCounter%arrSize] = gethumSensorValue();
		humSensorCounter += 1;
		Array_Stats<float> Data_Array(humSensorValueArray, sizeof(humSensorValueArray) / sizeof(humSensorValueArray[0]));
		float value = 0;
		value = Data_Array.Average(Data_Array.Arithmetic_Avg);
		String strValue = String(value);
		int str_len = strValue.length() + 1;
		char char_array[str_len];
	    strValue.toCharArray(char_array, str_len);
		publish(humSensorTopic, char_array);
		humSensorTimer = millis();
	}
	
	if(millis() >= (co2SensorTimer + (1000/(1)))){
		co2SensorValueArray[co2SensorCounter%arrSize] = getco2SensorValue();
		co2SensorCounter += 1;
		Array_Stats<float> Data_Array(co2SensorValueArray, sizeof(co2SensorValueArray) / sizeof(co2SensorValueArray[0]));
		float value = 0;
		value = Data_Array.Average(Data_Array.Arithmetic_Avg);
		String strValue = String(value);
		int str_len = strValue.length() + 1;
		char char_array[str_len];
	    strValue.toCharArray(char_array, str_len);
		publish(co2SensorTopic, char_array);
		co2SensorTimer = millis();
	}
}
