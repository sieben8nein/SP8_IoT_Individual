package dsl.generator;

import com.google.common.base.Objects;
import dsl.greenhouse.Action;
import dsl.greenhouse.Controller;
import dsl.greenhouse.Greenhouse;
import dsl.greenhouse.GreenhouseActuator;
import dsl.greenhouse.GreenhouseSensor;
import dsl.greenhouse.Model;
import dsl.greenhouse.Row;
import dsl.greenhouse.RowActuator;
import dsl.greenhouse.RowSensor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class PeripheralGenerator {
  private MathGenerator mathGenerator = new MathGenerator();
  
  public String compilePeripheral(final Model model, final Controller controller) {
    final EObject root = EcoreUtil2.getRootContainer(model);
    final Function1<RowSensor, Boolean> _function = (RowSensor it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<RowSensor> allSensors = IterableExtensions.<RowSensor>filter(EcoreUtil2.<RowSensor>getAllContentsOfType(root, RowSensor.class), _function);
    final Function1<GreenhouseSensor, Boolean> _function_1 = (GreenhouseSensor it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<GreenhouseSensor> allGlobalSensors = IterableExtensions.<GreenhouseSensor>filter(EcoreUtil2.<GreenhouseSensor>getAllContentsOfType(root, GreenhouseSensor.class), _function_1);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("#include <Statistical.h>");
    _builder.newLine();
    {
      String _name = controller.getType().getName();
      boolean _equals = Objects.equal(_name, "ESP32");
      if (_equals) {
        _builder.append("#include <PubSubClient.h>");
        _builder.newLine();
        _builder.append("#include <analogWrite.h>");
        _builder.newLine();
        _builder.append("#include <WiFi.h>");
        _builder.newLine();
      }
    }
    String _allSensorPreamble = this.getAllSensorPreamble(model, controller);
    _builder.append(_allSensorPreamble);
    _builder.newLineIfNotEmpty();
    String _allSensorTimers = this.getAllSensorTimers(model, controller);
    _builder.append(_allSensorTimers);
    _builder.newLineIfNotEmpty();
    String _controllerTimer = this.getControllerTimer(controller);
    _builder.append(_controllerTimer);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("long int sleepMicroSeconds = ");
    String _maximumSleepTime = this.getMaximumSleepTime(model, controller);
    _builder.append(_maximumSleepTime);
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("long int wakeMilliSeconds = 20000;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("long int sleepTimer = millis();");
    _builder.newLine();
    String _allActuatorTopics = this.getAllActuatorTopics(model, controller);
    _builder.append(_allActuatorTopics);
    _builder.newLineIfNotEmpty();
    {
      String _name_1 = controller.getType().getName();
      boolean _equals_1 = Objects.equal(_name_1, "ESP32");
      if (_equals_1) {
        CharSequence _setupWifi_ESP32 = this.setupWifi_ESP32();
        _builder.append(_setupWifi_ESP32);
        _builder.newLineIfNotEmpty();
        String _setupMQTT_ESP32 = this.setupMQTT_ESP32(model, controller);
        _builder.append(_setupMQTT_ESP32);
        _builder.newLineIfNotEmpty();
        _builder.newLine();
      }
    }
    {
      String _name_2 = controller.getType().getName();
      boolean _equals_2 = Objects.equal(_name_2, "ESP8266");
      if (_equals_2) {
        CharSequence _setupWifi_ESP8266 = this.setupWifi_ESP8266();
        _builder.append(_setupWifi_ESP8266);
        _builder.newLineIfNotEmpty();
        String _setupMQTT_ESP8266 = this.setupMQTT_ESP8266(model, controller);
        _builder.append(_setupMQTT_ESP8266);
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.newLine();
    String _sensorMethods = this.getSensorMethods(model, controller);
    _builder.append(_sensorMethods);
    _builder.newLineIfNotEmpty();
    String _actuatorMethods = this.getActuatorMethods(model, controller);
    _builder.append(_actuatorMethods);
    _builder.newLineIfNotEmpty();
    String _setup = this.getSetup(model, controller);
    _builder.append(_setup);
    _builder.newLineIfNotEmpty();
    String _loop = this.getLoop(model, controller);
    _builder.append(_loop);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    return _builder.toString();
  }
  
  public String getMaximumSleepTime(final Model model, final Controller controller) {
    final EObject root = EcoreUtil2.getRootContainer(model);
    final Function1<RowSensor, Boolean> _function = (RowSensor it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<RowSensor> allSensors = IterableExtensions.<RowSensor>filter(EcoreUtil2.<RowSensor>getAllContentsOfType(root, RowSensor.class), _function);
    int _freqUnitTime = this.getFreqUnitTime(controller.getHeartBeat().getFreq().getUnit());
    String _plus = (Integer.valueOf(_freqUnitTime) + "/");
    String _computeExpression = this.mathGenerator.computeExpression(controller.getHeartBeat().getFreq().getFreq());
    final String heartbeat = (_plus + _computeExpression);
    StringConcatenation _builder = new StringConcatenation();
    {
      int _size = IterableExtensions.size(allSensors);
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        _builder.append("min(");
        _builder.append(heartbeat);
        _builder.append(", ");
        {
          for(final RowSensor sensor : allSensors) {
            _builder.append("min(");
            int _freqUnitTime_1 = this.getFreqUnitTime(sensor.getType().getFrequency().getUnit());
            String _plus_1 = (Integer.valueOf(_freqUnitTime_1) + "/");
            String _computeExpression_1 = this.mathGenerator.computeExpression(sensor.getType().getFrequency().getFreq());
            String _plus_2 = (_plus_1 + _computeExpression_1);
            _builder.append(_plus_2);
            _builder.append(", ");
          }
        }
        final Function1<RowSensor, Boolean> _function_1 = (RowSensor it) -> {
          return Boolean.valueOf((1 == 1));
        };
        String _computeExpression_2 = this.mathGenerator.computeExpression(IterableExtensions.<RowSensor>findFirst(allSensors, _function_1).getType().getFrequency().getFreq());
        _builder.append(_computeExpression_2);
        {
          for(final RowSensor sensor_1 : allSensors) {
            _builder.append(")");
          }
        }
        _builder.append(")");
      }
    }
    _builder.append(";");
    return _builder.toString();
  }
  
  public String getAllSensorPreamble(final Model model, final Controller controller) {
    final EObject root = EcoreUtil2.getRootContainer(model);
    final Function1<RowSensor, Boolean> _function = (RowSensor it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<RowSensor> allSensors = IterableExtensions.<RowSensor>filter(EcoreUtil2.<RowSensor>getAllContentsOfType(root, RowSensor.class), _function);
    final Function1<GreenhouseSensor, Boolean> _function_1 = (GreenhouseSensor it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<GreenhouseSensor> allGlobalSensors = IterableExtensions.<GreenhouseSensor>filter(EcoreUtil2.<GreenhouseSensor>getAllContentsOfType(root, GreenhouseSensor.class), _function_1);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("const int arrSize = 10;");
    _builder.newLine();
    {
      int _length = ((Object[])Conversions.unwrapArray(allSensors, Object.class)).length;
      boolean _greaterThan = (_length > 0);
      if (_greaterThan) {
        _builder.append("// preamble for row sensors");
        _builder.newLine();
        {
          for(final RowSensor sensor : allSensors) {
            _builder.append("#define ");
            String _name = sensor.getName();
            _builder.append(_name);
            _builder.append("Topic \"");
            EObject _eContainer = sensor.eContainer().eContainer();
            String _name_1 = ((Greenhouse) _eContainer).getName();
            _builder.append(_name_1);
            _builder.append("/");
            EObject _eContainer_1 = sensor.eContainer();
            String _name_2 = ((Row) _eContainer_1).getName();
            _builder.append(_name_2);
            _builder.append("/");
            String _name_3 = sensor.getName();
            _builder.append(_name_3);
            _builder.append("\"");
            _builder.newLineIfNotEmpty();
            _builder.append("float ");
            String _name_4 = sensor.getName();
            _builder.append(_name_4);
            _builder.append("ValueArray[arrSize];");
            _builder.newLineIfNotEmpty();
            _builder.append("int ");
            String _name_5 = sensor.getName();
            _builder.append(_name_5);
            _builder.append("Counter = 0;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      int _length_1 = ((Object[])Conversions.unwrapArray(allGlobalSensors, Object.class)).length;
      boolean _greaterThan_1 = (_length_1 > 0);
      if (_greaterThan_1) {
        _builder.append("// preamble for greenhouse sensors");
        _builder.newLine();
        {
          for(final GreenhouseSensor sensor_1 : allGlobalSensors) {
            _builder.append("#define ");
            String _name_6 = sensor_1.getName();
            _builder.append(_name_6);
            _builder.append("Topic \"");
            EObject _eContainer_2 = sensor_1.eContainer();
            String _name_7 = ((Greenhouse) _eContainer_2).getName();
            _builder.append(_name_7);
            _builder.append("/");
            String _name_8 = sensor_1.getName();
            _builder.append(_name_8);
            _builder.append("\"");
            _builder.newLineIfNotEmpty();
            _builder.append("float ");
            String _name_9 = sensor_1.getName();
            _builder.append(_name_9);
            _builder.append("ValueArray[arrSize];");
            _builder.newLineIfNotEmpty();
            _builder.append("int ");
            String _name_10 = sensor_1.getName();
            _builder.append(_name_10);
            _builder.append("Counter = 0;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder.toString();
  }
  
  public String getControllerTimer(final Controller controller) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("#define ");
    String _name = controller.getName();
    _builder.append(_name);
    _builder.append("Topic \"");
    String _name_1 = controller.getName();
    _builder.append(_name_1);
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("long int ");
    String _name_2 = controller.getName();
    _builder.append(_name_2);
    _builder.append("Timer = millis();");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String getAllActuatorTopics(final Model model, final Controller controller) {
    final EObject root = EcoreUtil2.getRootContainer(model);
    final Function1<RowActuator, Boolean> _function = (RowActuator it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<RowActuator> allRowActuators = IterableExtensions.<RowActuator>filter(EcoreUtil2.<RowActuator>getAllContentsOfType(root, RowActuator.class), _function);
    final Function1<GreenhouseActuator, Boolean> _function_1 = (GreenhouseActuator it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<GreenhouseActuator> allGlobalActuators = IterableExtensions.<GreenhouseActuator>filter(EcoreUtil2.<GreenhouseActuator>getAllContentsOfType(root, GreenhouseActuator.class), _function_1);
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    {
      int _length = ((Object[])Conversions.unwrapArray(allRowActuators, Object.class)).length;
      boolean _greaterThan = (_length > 0);
      if (_greaterThan) {
        _builder.append("// topics for row actuators");
        _builder.newLine();
        {
          for(final RowActuator actuator : allRowActuators) {
            _builder.append("#define ");
            String _name = actuator.getName();
            _builder.append(_name);
            _builder.append("Topic \"");
            EObject _eContainer = actuator.eContainer().eContainer();
            String _name_1 = ((Greenhouse) _eContainer).getName();
            _builder.append(_name_1);
            _builder.append("/");
            EObject _eContainer_1 = actuator.eContainer();
            String _name_2 = ((Row) _eContainer_1).getName();
            _builder.append(_name_2);
            _builder.append("/");
            String _name_3 = actuator.getName();
            _builder.append(_name_3);
            _builder.append("\"");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      int _length_1 = ((Object[])Conversions.unwrapArray(allGlobalActuators, Object.class)).length;
      boolean _greaterThan_1 = (_length_1 > 0);
      if (_greaterThan_1) {
        _builder.append("// topics for greenhouse actuators");
        _builder.newLine();
        {
          for(final GreenhouseActuator actuator_1 : allGlobalActuators) {
            _builder.append("#define ");
            String _name_4 = actuator_1.getName();
            _builder.append(_name_4);
            _builder.append("Topic \"");
            EObject _eContainer_2 = actuator_1.eContainer();
            String _name_5 = ((Greenhouse) _eContainer_2).getName();
            _builder.append(_name_5);
            _builder.append("/");
            String _name_6 = actuator_1.getName();
            _builder.append(_name_6);
            _builder.append("\"");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder.toString();
  }
  
  public String getAllSensorTimers(final Model model, final Controller controller) {
    final EObject root = EcoreUtil2.getRootContainer(model);
    final Function1<RowSensor, Boolean> _function = (RowSensor it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<RowSensor> allSensors = IterableExtensions.<RowSensor>filter(EcoreUtil2.<RowSensor>getAllContentsOfType(root, RowSensor.class), _function);
    final Function1<GreenhouseSensor, Boolean> _function_1 = (GreenhouseSensor it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<GreenhouseSensor> allGlobalSensors = IterableExtensions.<GreenhouseSensor>filter(EcoreUtil2.<GreenhouseSensor>getAllContentsOfType(root, GreenhouseSensor.class), _function_1);
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    {
      int _length = ((Object[])Conversions.unwrapArray(allSensors, Object.class)).length;
      boolean _greaterThan = (_length > 0);
      if (_greaterThan) {
        _builder.append("// timers for row sensors");
        _builder.newLine();
        {
          for(final RowSensor sensor : allSensors) {
            _builder.append("long int ");
            String _name = sensor.getName();
            _builder.append(_name);
            _builder.append("Timer = millis();");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      int _length_1 = ((Object[])Conversions.unwrapArray(allGlobalSensors, Object.class)).length;
      boolean _greaterThan_1 = (_length_1 > 0);
      if (_greaterThan_1) {
        _builder.append("// timers for greenhouse sensors");
        _builder.newLine();
        {
          for(final GreenhouseSensor sensor_1 : allGlobalSensors) {
            _builder.append("long int ");
            String _name_1 = sensor_1.getName();
            _builder.append(_name_1);
            _builder.append("Timer = millis();");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder.toString();
  }
  
  public String getSetup(final Model model, final Controller controller) {
    final EObject root = EcoreUtil2.getRootContainer(model);
    final Function1<RowSensor, Boolean> _function = (RowSensor it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<RowSensor> allSensors = IterableExtensions.<RowSensor>filter(EcoreUtil2.<RowSensor>getAllContentsOfType(root, RowSensor.class), _function);
    final Function1<GreenhouseSensor, Boolean> _function_1 = (GreenhouseSensor it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<GreenhouseSensor> allGlobalSensors = IterableExtensions.<GreenhouseSensor>filter(EcoreUtil2.<GreenhouseSensor>getAllContentsOfType(root, GreenhouseSensor.class), _function_1);
    final Function1<RowActuator, Boolean> _function_2 = (RowActuator it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<RowActuator> allRowActuators = IterableExtensions.<RowActuator>filter(EcoreUtil2.<RowActuator>getAllContentsOfType(root, RowActuator.class), _function_2);
    final Function1<GreenhouseActuator, Boolean> _function_3 = (GreenhouseActuator it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<GreenhouseActuator> allGlobalActuators = IterableExtensions.<GreenhouseActuator>filter(EcoreUtil2.<GreenhouseActuator>getAllContentsOfType(root, GreenhouseActuator.class), _function_3);
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    _builder.append("void debug(const char *s)");
    _builder.newLine();
    _builder.append("{");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("Serial.print (millis());");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("Serial.print (\" \");");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("Serial.println(s);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("void setup() {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("Serial.begin(115200);");
    _builder.newLine();
    _builder.append("\t  \t  ");
    _builder.append("while (!Serial) {");
    _builder.newLine();
    _builder.append("\t          ");
    _builder.append("delay(100);");
    _builder.newLine();
    _builder.append("\t      ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t  \t  ");
    _builder.append("delay(10);");
    _builder.newLine();
    {
      String _name = controller.getType().getName();
      boolean _equals = Objects.equal(_name, "ESP32");
      if (_equals) {
        _builder.append("initWiFi();");
        _builder.newLine();
        _builder.append("client.setServer(mqtt_server, 1883);");
        _builder.newLine();
        _builder.append("client.setCallback(callback);");
        _builder.newLine();
      }
    }
    {
      String _name_1 = controller.getType().getName();
      boolean _equals_1 = Objects.equal(_name_1, "ESP8266");
      if (_equals_1) {
        _builder.append("  ");
        _builder.append("connectToWifi();");
        _builder.newLine();
        _builder.append("  ");
        _builder.append("mqtt_connect();");
        _builder.newLine();
      }
    }
    {
      int _length = ((Object[])Conversions.unwrapArray(allSensors, Object.class)).length;
      boolean _greaterThan = (_length > 0);
      if (_greaterThan) {
        _builder.append("// setup for row sensors");
        _builder.newLine();
        {
          for(final RowSensor sensor : allSensors) {
            _builder.append("setup");
            String _name_2 = sensor.getName();
            _builder.append(_name_2);
            _builder.append("();");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      int _length_1 = ((Object[])Conversions.unwrapArray(allGlobalSensors, Object.class)).length;
      boolean _greaterThan_1 = (_length_1 > 0);
      if (_greaterThan_1) {
        _builder.append("\t");
        _builder.append("// setup for for greenhouse sensors");
        _builder.newLine();
        {
          for(final GreenhouseSensor sensor_1 : allGlobalSensors) {
            _builder.append("\t");
            _builder.append("setup");
            String _name_3 = sensor_1.getName();
            _builder.append(_name_3, "\t");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      int _length_2 = ((Object[])Conversions.unwrapArray(allRowActuators, Object.class)).length;
      boolean _greaterThan_2 = (_length_2 > 0);
      if (_greaterThan_2) {
        _builder.append("\t");
        _builder.append("// setup for for row actuators");
        _builder.newLine();
        {
          for(final RowActuator actuator : allRowActuators) {
            _builder.append("\t");
            _builder.append("setup");
            String _name_4 = actuator.getName();
            _builder.append(_name_4, "\t");
            _builder.append("();\t\t");
          }
        }
        _builder.newLineIfNotEmpty();
      }
    }
    {
      int _length_3 = ((Object[])Conversions.unwrapArray(allGlobalActuators, Object.class)).length;
      boolean _greaterThan_3 = (_length_3 > 0);
      if (_greaterThan_3) {
        _builder.append("\t");
        _builder.append("// setup for greenhouse actuators");
        _builder.newLine();
        {
          for(final GreenhouseActuator actuator_1 : allGlobalActuators) {
            _builder.append("\t");
            _builder.append("setup");
            String _name_5 = actuator_1.getName();
            _builder.append(_name_5, "\t");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public CharSequence setupWifi_ESP32() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//wifi");
    _builder.newLine();
    _builder.append("const char* ssid = \"LEO1_TEAM_06\";");
    _builder.newLine();
    _builder.append("const char* password = \"embeddedlinux\";");
    _builder.newLine();
    _builder.append("void initWiFi() {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("WiFi.mode(WIFI_STA);");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("WiFi.begin(ssid, password);");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("Serial.print(\"Connecting to WiFi ..\");");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("while (WiFi.status() != WL_CONNECTED) {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("Serial.print(\'.\');");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("delay(1000);");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("Serial.println(WiFi.localIP());");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence setupWifi_ESP8266() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//wifi");
    _builder.newLine();
    _builder.append("#define WIFI_SSID       \"LEO1_TEAM_06\"");
    _builder.newLine();
    _builder.append("#define WIFI_PASSWORD    \"embeddedlinux\"");
    _builder.newLine();
    _builder.append("#include <ESP8266WiFiMulti.h>");
    _builder.newLine();
    _builder.append("#include <ESP8266HTTPClient.h>");
    _builder.newLine();
    _builder.append("ESP8266WiFiMulti WiFiMulti;");
    _builder.newLine();
    _builder.append("const uint32_t conn_tout_ms = 5000;");
    _builder.newLine();
    _builder.append("WiFiClient wifi_client;");
    _builder.newLine();
    _builder.append("void print_wifi_status()");
    _builder.newLine();
    _builder.append("{");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("Serial.print (millis());");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("Serial.print(\" WiFi connected: \");");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("Serial.print(WiFi.SSID());");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("Serial.print(\" \");");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("Serial.print(WiFi.localIP());");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("Serial.print(\" RSSI: \");");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("Serial.print(WiFi.RSSI());");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("Serial.println(\" dBm\");");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("void connectToWifi(){");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("// wifi");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("WiFi.persistent(false);");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("WiFi.mode(WIFI_STA);");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("WiFiMulti.addAP(WIFI_SSID, WIFI_PASSWORD);");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("if(WiFiMulti.run(conn_tout_ms) == WL_CONNECTED)");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("{");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("print_wifi_status();");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("else");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("{");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("debug(\"Unable to connect\");");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("  ");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public String setupMQTT_ESP32(final Model model, final Controller controller) {
    final EObject root = EcoreUtil2.getRootContainer(model);
    final Function1<RowActuator, Boolean> _function = (RowActuator it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<RowActuator> allRowActuators = IterableExtensions.<RowActuator>filter(EcoreUtil2.<RowActuator>getAllContentsOfType(root, RowActuator.class), _function);
    final Function1<GreenhouseActuator, Boolean> _function_1 = (GreenhouseActuator it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<GreenhouseActuator> allGlobalActuators = IterableExtensions.<GreenhouseActuator>filter(EcoreUtil2.<GreenhouseActuator>getAllContentsOfType(root, GreenhouseActuator.class), _function_1);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// MQTT setup");
    _builder.newLine();
    _builder.append("const char* mqtt_server =  \"192.168.10.1\";");
    _builder.newLine();
    _builder.append("WiFiClient espClient;");
    _builder.newLine();
    _builder.append("PubSubClient client(espClient);");
    _builder.newLine();
    _builder.append("void publish(const char* topic, const char* content){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("client.publish(topic, content);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("void subscribe(const char* topic){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("client.subscribe(topic);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("void reconnect() {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("// Loop until we\'re reconnected");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("while (!client.connected()) {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("Serial.print(\"Attempting MQTT connection...\");");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// Attempt to connect");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("if (client.connect(\"");
    String _name = controller.getName();
    _builder.append(_name, "    ");
    _builder.append("\", \"my_user\", \"bendevictor\")) {");
    _builder.newLineIfNotEmpty();
    _builder.append("      ");
    _builder.append("Serial.println(\"connected\");");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("// Subscribe");
    _builder.newLine();
    {
      int _length = ((Object[])Conversions.unwrapArray(allRowActuators, Object.class)).length;
      boolean _greaterThan = (_length > 0);
      if (_greaterThan) {
        _builder.append("// topics for row actuators");
        _builder.newLine();
        {
          for(final RowActuator actuator : allRowActuators) {
            _builder.append("\t\t  \t\t");
            _builder.append("subscribe(");
            String _name_1 = actuator.getName();
            _builder.append(_name_1, "\t\t  \t\t");
            _builder.append("Topic);");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      int _length_1 = ((Object[])Conversions.unwrapArray(allGlobalActuators, Object.class)).length;
      boolean _greaterThan_1 = (_length_1 > 0);
      if (_greaterThan_1) {
        _builder.append("  \t\t");
        _builder.append("// topics for greenhouse actuators");
        _builder.newLine();
        {
          for(final GreenhouseActuator actuator_1 : allGlobalActuators) {
            _builder.append("  \t\t");
            _builder.append("subscribe(");
            String _name_2 = actuator_1.getName();
            _builder.append(_name_2, "  \t\t");
            _builder.append("Topic);");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("    ");
    _builder.append("} else {");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("Serial.print(\"failed, rc=\");");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("Serial.print(client.state());");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("Serial.println(\" try again in 5 seconds\");");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("// Wait 5 seconds before retrying");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("delay(5000);");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("void callback(char* topic, byte* message, unsigned int length) {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("String msg = \"\";");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("for(int i = 0; i< length; i++){");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("msg = msg+(char)message[i];");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("}");
    _builder.newLine();
    {
      int _length_2 = ((Object[])Conversions.unwrapArray(allRowActuators, Object.class)).length;
      boolean _greaterThan_2 = (_length_2 > 0);
      if (_greaterThan_2) {
        _builder.append("// topics for row actuators");
        _builder.newLine();
        {
          for(final RowActuator actuator_2 : allRowActuators) {
            _builder.newLine();
            _builder.append("if(String(topic) == String(");
            String _name_3 = actuator_2.getName();
            _builder.append(_name_3);
            _builder.append("Topic)){");
            _builder.newLineIfNotEmpty();
            _builder.append("  ");
            _builder.append("handle");
            String _name_4 = actuator_2.getName();
            _builder.append(_name_4, "  ");
            _builder.append("Message(msg);");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    {
      int _length_3 = ((Object[])Conversions.unwrapArray(allGlobalActuators, Object.class)).length;
      boolean _greaterThan_3 = (_length_3 > 0);
      if (_greaterThan_3) {
        _builder.append("\t  \t\t");
        _builder.append("// topics for greenhouse actuators");
        _builder.newLine();
        {
          for(final GreenhouseActuator actuator_3 : allGlobalActuators) {
            _builder.append("\t  \t\t");
            _builder.append("if(String(topic) == String(");
            String _name_5 = actuator_3.getName();
            _builder.append(_name_5, "\t  \t\t");
            _builder.append("Topic)){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t\t\t    ");
            _builder.append("handle");
            String _name_6 = actuator_3.getName();
            _builder.append(_name_6, "\t\t\t\t    ");
            _builder.append("Message(msg);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t\t\t  ");
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    return _builder.toString();
  }
  
  public String setupMQTT_ESP8266(final Model model, final Controller controller) {
    final EObject root = EcoreUtil2.getRootContainer(model);
    final Function1<RowActuator, Boolean> _function = (RowActuator it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<RowActuator> allRowActuators = IterableExtensions.<RowActuator>filter(EcoreUtil2.<RowActuator>getAllContentsOfType(root, RowActuator.class), _function);
    final Function1<GreenhouseActuator, Boolean> _function_1 = (GreenhouseActuator it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<GreenhouseActuator> allGlobalActuators = IterableExtensions.<GreenhouseActuator>filter(EcoreUtil2.<GreenhouseActuator>getAllContentsOfType(root, GreenhouseActuator.class), _function_1);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// MQTT setup");
    _builder.newLine();
    _builder.append("#define MQTT_SERVER      \"192.168.10.1\"");
    _builder.newLine();
    _builder.append("#define MQTT_SERVERPORT  1883 ");
    _builder.newLine();
    _builder.append("#define MQTT_USERNAME    \"my_user\"");
    _builder.newLine();
    _builder.append("#define MQTT_KEY         \"bendevictor\"");
    _builder.newLine();
    _builder.append("#define MQTT_TOPIC        \"mqtt\"");
    _builder.newLine();
    _builder.append("#include \"Adafruit_MQTT.h\"");
    _builder.newLine();
    _builder.append("#include \"Adafruit_MQTT_Client.h\"");
    _builder.newLine();
    _builder.newLine();
    _builder.append("Adafruit_MQTT_Client mqtt(&wifi_client, MQTT_SERVER, MQTT_SERVERPORT, MQTT_USERNAME, MQTT_KEY);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("void publish(const char* topic, const char* content){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if((WiFiMulti.run(conn_tout_ms) == WL_CONNECTED))");
    _builder.newLine();
    _builder.append("\t  ");
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t    ");
    _builder.append("print_wifi_status();");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t    ");
    _builder.append("mqtt_connect();");
    _builder.newLine();
    _builder.append("\t    ");
    _builder.append("char charBuf[50];");
    _builder.newLine();
    _builder.append("\t    ");
    _builder.append("Adafruit_MQTT_Publish publish_topic = Adafruit_MQTT_Publish(&mqtt, topic);");
    _builder.newLine();
    _builder.append("\t    ");
    _builder.append("Serial.println(\"connect success\");");
    _builder.newLine();
    _builder.append("\t    ");
    _builder.append("if (! publish_topic.publish(content))");
    _builder.newLine();
    _builder.append("\t    ");
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t      ");
    _builder.append("debug(\"MQTT failed\");");
    _builder.newLine();
    _builder.append("\t    ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t    ");
    _builder.append("else");
    _builder.newLine();
    _builder.append("\t    ");
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t      ");
    _builder.append("debug(\"MQTT ok\");");
    _builder.newLine();
    _builder.append("\t    ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t  ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.newLine();
    {
      int _length = ((Object[])Conversions.unwrapArray(allRowActuators, Object.class)).length;
      boolean _greaterThan = (_length > 0);
      if (_greaterThan) {
        _builder.append("// topics for row actuators");
        _builder.newLine();
        {
          for(final RowActuator actuator : allRowActuators) {
            _builder.append("Adafruit_MQTT_Subscribe ");
            String _name = actuator.getName();
            _builder.append(_name);
            _builder.append("SubscribeTopic = Adafruit_MQTT_Subscribe(&mqtt, \"");
            EObject _eContainer = actuator.eContainer().eContainer();
            String _name_1 = ((Greenhouse) _eContainer).getName();
            _builder.append(_name_1);
            _builder.append("/");
            EObject _eContainer_1 = actuator.eContainer();
            String _name_2 = ((Row) _eContainer_1).getName();
            _builder.append(_name_2);
            _builder.append("/");
            String _name_3 = actuator.getName();
            _builder.append(_name_3);
            _builder.append("\");");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      int _length_1 = ((Object[])Conversions.unwrapArray(allGlobalActuators, Object.class)).length;
      boolean _greaterThan_1 = (_length_1 > 0);
      if (_greaterThan_1) {
        _builder.append("// topics for greenhouse actuators");
        _builder.newLine();
        {
          for(final GreenhouseActuator actuator_1 : allGlobalActuators) {
            _builder.append("Adafruit_MQTT_Subscribe ");
            String _name_4 = actuator_1.getName();
            _builder.append(_name_4);
            _builder.append("SubscribeTopic = Adafruit_MQTT_Subscribe(&mqtt, \"");
            EObject _eContainer_2 = actuator_1.eContainer();
            String _name_5 = ((Greenhouse) _eContainer_2).getName();
            _builder.append(_name_5);
            _builder.append("/");
            String _name_6 = actuator_1.getName();
            _builder.append(_name_6);
            _builder.append("\");");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    _builder.newLine();
    _builder.append("void mqtt_connect()");
    _builder.newLine();
    _builder.append("{");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("int8_t ret;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("  ");
    _builder.append("// Stop if already connected.");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("if (! mqtt.connected())");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("{");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("debug(\"Connecting to MQTT... \");");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("while ((ret = mqtt.connect()) != 0)");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("{ // connect will return 0 for connected");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("Serial.println(mqtt.connectErrorString(ret));");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("debug(\"Retrying MQTT connection in 5 seconds...\");");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("mqtt.disconnect();");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("delay(5000);  // wait 5 seconds");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("debug(\"MQTT Connected\");");
    _builder.newLine();
    {
      int _length_2 = ((Object[])Conversions.unwrapArray(allRowActuators, Object.class)).length;
      boolean _greaterThan_2 = (_length_2 > 0);
      if (_greaterThan_2) {
        _builder.append("    ");
        _builder.append("// subscribe to topics for row actuators");
        _builder.newLine();
        {
          for(final RowActuator actuator_2 : allRowActuators) {
            _builder.append("mqtt.subscribe(&");
            String _name_7 = actuator_2.getName();
            _builder.append(_name_7);
            _builder.append("SubscribeTopic);");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      int _length_3 = ((Object[])Conversions.unwrapArray(allGlobalActuators, Object.class)).length;
      boolean _greaterThan_3 = (_length_3 > 0);
      if (_greaterThan_3) {
        _builder.append("// subscribe to topics for greenhouse actuators");
        _builder.newLine();
        {
          for(final GreenhouseActuator actuator_3 : allGlobalActuators) {
            _builder.append("mqtt.subscribe(&");
            String _name_8 = actuator_3.getName();
            _builder.append(_name_8);
            _builder.append("SubscribeTopic);");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("  ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("void recievedMessage(){");
    _builder.newLine();
    _builder.append("   ");
    _builder.append("Adafruit_MQTT_Subscribe *subscription;");
    _builder.newLine();
    _builder.append("   ");
    _builder.append("while ((subscription = mqtt.readSubscription(15000))) {");
    _builder.newLine();
    {
      int _length_4 = ((Object[])Conversions.unwrapArray(allRowActuators, Object.class)).length;
      boolean _greaterThan_4 = (_length_4 > 0);
      if (_greaterThan_4) {
        _builder.append("// topics for row actuators");
        _builder.newLine();
        {
          for(final RowActuator actuator_4 : allRowActuators) {
            _builder.append("if(subscription == &");
            String _name_9 = actuator_4.getName();
            _builder.append(_name_9);
            _builder.append("SubscribeTopic)){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("String msg = (char *)");
            String _name_10 = actuator_4.getName();
            _builder.append(_name_10, "\t");
            _builder.append("SubscribeTopic.lastread;");
            _builder.newLineIfNotEmpty();
            _builder.append("  ");
            _builder.append("handle");
            String _name_11 = actuator_4.getName();
            _builder.append(_name_11, "  ");
            _builder.append("Message(msg);");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    {
      int _length_5 = ((Object[])Conversions.unwrapArray(allGlobalActuators, Object.class)).length;
      boolean _greaterThan_5 = (_length_5 > 0);
      if (_greaterThan_5) {
        _builder.append("\t  \t\t");
        _builder.append("// topics for greenhouse actuators");
        _builder.newLine();
        {
          for(final GreenhouseActuator actuator_5 : allGlobalActuators) {
            _builder.append("if(subscription == &");
            String _name_12 = actuator_5.getName();
            _builder.append(_name_12);
            _builder.append("SubscribeTopic){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("String msg = (char *)");
            String _name_13 = actuator_5.getName();
            _builder.append(_name_13, "\t");
            _builder.append("SubscribeTopic.lastread;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("handle");
            String _name_14 = actuator_5.getName();
            _builder.append(_name_14, "\t");
            _builder.append("Message(msg);");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("  ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    return _builder.toString();
  }
  
  public String getSensorMethods(final Model model, final Controller controller) {
    final EObject root = EcoreUtil2.getRootContainer(model);
    final Function1<RowSensor, Boolean> _function = (RowSensor it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<RowSensor> allSensors = IterableExtensions.<RowSensor>filter(EcoreUtil2.<RowSensor>getAllContentsOfType(root, RowSensor.class), _function);
    final Function1<GreenhouseSensor, Boolean> _function_1 = (GreenhouseSensor it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<GreenhouseSensor> allGlobalSensors = IterableExtensions.<GreenhouseSensor>filter(EcoreUtil2.<GreenhouseSensor>getAllContentsOfType(root, GreenhouseSensor.class), _function_1);
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    {
      int _length = ((Object[])Conversions.unwrapArray(allSensors, Object.class)).length;
      boolean _greaterThan = (_length > 0);
      if (_greaterThan) {
        _builder.append("// methods for row sensors");
        _builder.newLine();
        {
          for(final RowSensor sensor : allSensors) {
            _builder.append("void setup");
            String _name = sensor.getName();
            _builder.append(_name);
            _builder.append("(){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("// insert code to setup sensor here");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            _builder.append("float get");
            String _name_1 = sensor.getName();
            _builder.append(_name_1);
            _builder.append("Value(){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("// insert code to get value for sensor here");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("// remember to return a float!");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("// example: ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("// float lightValue = analogRead(lightPin);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("// Serial.println(lightValue);");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    {
      int _length_1 = ((Object[])Conversions.unwrapArray(allGlobalSensors, Object.class)).length;
      boolean _greaterThan_1 = (_length_1 > 0);
      if (_greaterThan_1) {
        _builder.append("// methods for greenhouse sensors");
        _builder.newLine();
        {
          for(final GreenhouseSensor sensor_1 : allGlobalSensors) {
            _builder.append("void setup");
            String _name_2 = sensor_1.getName();
            _builder.append(_name_2);
            _builder.append("(){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("// insert code to setup sensor here");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            _builder.append("float get");
            String _name_3 = sensor_1.getName();
            _builder.append(_name_3);
            _builder.append("Value(){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("// insert code to get value for sensor here");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("// remember to return a float!");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("// example: ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("// float lightValue = analogRead(lightPin);");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    return _builder.toString();
  }
  
  public String getActuatorMethods(final Model model, final Controller controller) {
    final EObject root = EcoreUtil2.getRootContainer(model);
    final Function1<RowActuator, Boolean> _function = (RowActuator it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<RowActuator> allRowActuators = IterableExtensions.<RowActuator>filter(EcoreUtil2.<RowActuator>getAllContentsOfType(root, RowActuator.class), _function);
    final Function1<GreenhouseActuator, Boolean> _function_1 = (GreenhouseActuator it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<GreenhouseActuator> allGlobalActuators = IterableExtensions.<GreenhouseActuator>filter(EcoreUtil2.<GreenhouseActuator>getAllContentsOfType(root, GreenhouseActuator.class), _function_1);
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    {
      int _length = ((Object[])Conversions.unwrapArray(allRowActuators, Object.class)).length;
      boolean _greaterThan = (_length > 0);
      if (_greaterThan) {
        _builder.append("// methods for row actuators");
        _builder.newLine();
        {
          for(final RowActuator actuator : allRowActuators) {
            _builder.append("void setup");
            String _name = actuator.getName();
            _builder.append(_name);
            _builder.append("(){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("// insert code to setup actuator here");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
            _builder.append("void handle");
            String _name_1 = actuator.getName();
            _builder.append(_name_1);
            _builder.append("Message(String msg){");
            _builder.newLineIfNotEmpty();
            {
              EList<Action> _action = actuator.getAction();
              for(final Action action : _action) {
                _builder.append("\t");
                _builder.append("if(msg == \"");
                String _name_2 = action.getTrigger().getName();
                _builder.append(_name_2, "\t");
                _builder.append("\"){");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("// handle message");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("String strValue = \"messageReceived\";");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("int str_len = strValue.length() + 1;");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("char char_array[str_len];");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("strValue.toCharArray(char_array, str_len);");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("publish(");
                String _name_3 = actuator.getName();
                _builder.append(_name_3, "\t\t");
                _builder.append("Topic, char_array);");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
              }
            }
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    {
      int _length_1 = ((Object[])Conversions.unwrapArray(allGlobalActuators, Object.class)).length;
      boolean _greaterThan_1 = (_length_1 > 0);
      if (_greaterThan_1) {
        _builder.append("// methods for greenhouse actuators");
        _builder.newLine();
        {
          for(final GreenhouseActuator actuator_1 : allGlobalActuators) {
            _builder.append("void setup");
            String _name_4 = actuator_1.getName();
            _builder.append(_name_4);
            _builder.append("(){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("// insert code to setup actuator here");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
            _builder.append("void handle");
            String _name_5 = actuator_1.getName();
            _builder.append(_name_5);
            _builder.append("Message(String msg){");
            _builder.newLineIfNotEmpty();
            {
              EList<Action> _action_1 = actuator_1.getAction();
              for(final Action action_1 : _action_1) {
                _builder.append("\t");
                _builder.append("if(msg == \"");
                String _name_6 = action_1.getTrigger().getName();
                _builder.append(_name_6, "\t");
                _builder.append("\"){");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("// handle message");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("String strValue = \"messageReceived\";");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("int str_len = strValue.length() + 1;");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("char char_array[str_len];");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("strValue.toCharArray(char_array, str_len);");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("publish(");
                String _name_7 = actuator_1.getName();
                _builder.append(_name_7, "\t\t");
                _builder.append("Topic, char_array);");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
              }
            }
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    return _builder.toString();
  }
  
  public String getLoop(final Model model, final Controller controller) {
    final EObject root = EcoreUtil2.getRootContainer(model);
    final Function1<RowSensor, Boolean> _function = (RowSensor it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<RowSensor> allSensors = IterableExtensions.<RowSensor>filter(EcoreUtil2.<RowSensor>getAllContentsOfType(root, RowSensor.class), _function);
    final Function1<GreenhouseSensor, Boolean> _function_1 = (GreenhouseSensor it) -> {
      String _name = it.getController().getName();
      String _name_1 = controller.getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<GreenhouseSensor> allGlobalSensors = IterableExtensions.<GreenhouseSensor>filter(EcoreUtil2.<GreenhouseSensor>getAllContentsOfType(root, GreenhouseSensor.class), _function_1);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("void loop(){");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    {
      String _name = controller.getType().getName();
      boolean _equals = Objects.equal(_name, "ESP32");
      if (_equals) {
        _builder.append("\t");
        _builder.append("if (!client.connected()) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("    ");
        _builder.append("reconnect();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("client.loop();");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
      }
    }
    {
      String _name_1 = controller.getType().getName();
      boolean _equals_1 = Objects.equal(_name_1, "ESP8266");
      if (_equals_1) {
        _builder.append("recievedMessage();");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    String _heartbeat = this.getHeartbeat(controller);
    _builder.append(_heartbeat, "\t");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      int _length = ((Object[])Conversions.unwrapArray(allSensors, Object.class)).length;
      boolean _greaterThan = (_length > 0);
      if (_greaterThan) {
        _builder.append("\t");
        _builder.append("// publishing for row sensors");
        _builder.newLine();
        {
          for(final RowSensor sensor : allSensors) {
            _builder.append("\t");
            String _rowSensorLoop = this.getRowSensorLoop(sensor);
            _builder.append(_rowSensorLoop, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      int _length_1 = ((Object[])Conversions.unwrapArray(allGlobalSensors, Object.class)).length;
      boolean _greaterThan_1 = (_length_1 > 0);
      if (_greaterThan_1) {
        _builder.append("\t");
        _builder.append("// publishing for global sensors");
        _builder.newLine();
        {
          for(final GreenhouseSensor sensor_1 : allGlobalSensors) {
            _builder.append("\t");
            String _greenhouseSensorLoop = this.getGreenhouseSensorLoop(sensor_1);
            _builder.append(_greenhouseSensorLoop, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      if (((IterableExtensions.size(allSensors) <= 0) && (IterableExtensions.size(allGlobalSensors) <= 0))) {
        {
          String _name_2 = controller.getType().getName();
          boolean _equals_2 = Objects.equal(_name_2, "ESP32");
          if (_equals_2) {
            _builder.append("\t");
            _builder.append("if(millis() >= (sleepTimer + wakeMilliSeconds)){");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("esp_sleep_enable_timer_wakeup(sleepMicroSeconds);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
        {
          String _name_3 = controller.getType().getName();
          boolean _equals_3 = Objects.equal(_name_3, "ESP8266");
          if (_equals_3) {
            _builder.append("if(millis() >= (sleepTimer + wakeMilliSeconds)){");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("ESP.deepSleep(30e6);");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public String getRowSensorLoop(final RowSensor sensor) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    _builder.append("if(millis() >= (");
    String _name = sensor.getName();
    _builder.append(_name);
    _builder.append("Timer + (");
    int _freqUnitTime = this.getFreqUnitTime(sensor.getType().getFrequency().getUnit());
    _builder.append(_freqUnitTime);
    _builder.append("/(");
    String _computeExpression = this.mathGenerator.computeExpression(sensor.getType().getFrequency().getFreq());
    _builder.append(_computeExpression);
    _builder.append(")))){");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _name_1 = sensor.getName();
    _builder.append(_name_1, "\t");
    _builder.append("ValueArray[");
    String _name_2 = sensor.getName();
    _builder.append(_name_2, "\t");
    _builder.append("Counter%arrSize] = get");
    String _name_3 = sensor.getName();
    _builder.append(_name_3, "\t");
    _builder.append("Value();");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _name_4 = sensor.getName();
    _builder.append(_name_4, "\t");
    _builder.append("Counter += 1;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("Array_Stats<float> Data_Array(");
    String _name_5 = sensor.getName();
    _builder.append(_name_5, "\t");
    _builder.append("ValueArray, sizeof(");
    String _name_6 = sensor.getName();
    _builder.append(_name_6, "\t");
    _builder.append("ValueArray) / sizeof(");
    String _name_7 = sensor.getName();
    _builder.append(_name_7, "\t");
    _builder.append("ValueArray[0]));");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("float value = 0;");
    _builder.newLine();
    {
      String _name_8 = sensor.getType().getReducer().getName();
      boolean _equals = Objects.equal(_name_8, "average");
      if (_equals) {
        _builder.append("\t");
        _builder.append("value = Data_Array.Average(Data_Array.Arithmetic_Avg);");
        _builder.newLine();
      }
    }
    {
      String _name_9 = sensor.getType().getReducer().getName();
      boolean _equals_1 = Objects.equal(_name_9, "median");
      if (_equals_1) {
        _builder.append("\t");
        _builder.append("value = Data_Array.Quartile(2);");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("String strValue = String(value);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("int str_len = strValue.length() + 1;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("char char_array[str_len];");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("strValue.toCharArray(char_array, str_len);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("publish(");
    String _name_10 = sensor.getName();
    _builder.append(_name_10, "\t");
    _builder.append("Topic, char_array);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _name_11 = sensor.getName();
    _builder.append(_name_11, "\t");
    _builder.append("Timer = millis();");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public String getGreenhouseSensorLoop(final GreenhouseSensor sensor) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    _builder.append("if(millis() >= (");
    String _name = sensor.getName();
    _builder.append(_name);
    _builder.append("Timer + (");
    int _freqUnitTime = this.getFreqUnitTime(sensor.getType().getFrequency().getUnit());
    _builder.append(_freqUnitTime);
    _builder.append("/(");
    String _computeExpression = this.mathGenerator.computeExpression(sensor.getType().getFrequency().getFreq());
    _builder.append(_computeExpression);
    _builder.append(")))){");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _name_1 = sensor.getName();
    _builder.append(_name_1, "\t");
    _builder.append("ValueArray[");
    String _name_2 = sensor.getName();
    _builder.append(_name_2, "\t");
    _builder.append("Counter%arrSize] = get");
    String _name_3 = sensor.getName();
    _builder.append(_name_3, "\t");
    _builder.append("Value();");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _name_4 = sensor.getName();
    _builder.append(_name_4, "\t");
    _builder.append("Counter += 1;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("Array_Stats<float> Data_Array(");
    String _name_5 = sensor.getName();
    _builder.append(_name_5, "\t");
    _builder.append("ValueArray, sizeof(");
    String _name_6 = sensor.getName();
    _builder.append(_name_6, "\t");
    _builder.append("ValueArray) / sizeof(");
    String _name_7 = sensor.getName();
    _builder.append(_name_7, "\t");
    _builder.append("ValueArray[0]));");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("float value = 0;");
    _builder.newLine();
    {
      String _name_8 = sensor.getType().getReducer().getName();
      boolean _equals = Objects.equal(_name_8, "average");
      if (_equals) {
        _builder.append("\t");
        _builder.append("value = Data_Array.Average(Data_Array.Arithmetic_Avg);");
        _builder.newLine();
      }
    }
    {
      String _name_9 = sensor.getType().getReducer().getName();
      boolean _equals_1 = Objects.equal(_name_9, "median");
      if (_equals_1) {
        _builder.append("\t");
        _builder.append("value = Data_Array.Quartile(2);");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("String strValue = String(value);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("int str_len = strValue.length() + 1;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("char char_array[str_len];");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("strValue.toCharArray(char_array, str_len);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("publish(");
    String _name_10 = sensor.getName();
    _builder.append(_name_10, "\t");
    _builder.append("Topic, char_array);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _name_11 = sensor.getName();
    _builder.append(_name_11, "\t");
    _builder.append("Timer = millis();");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public String getHeartbeat(final Controller controller) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("if(millis() >= (");
    String _name = controller.getName();
    _builder.append(_name);
    _builder.append("Timer + (");
    int _freqUnitTime = this.getFreqUnitTime(controller.getHeartBeat().getFreq().getUnit());
    _builder.append(_freqUnitTime);
    _builder.append("/(");
    String _computeExpression = this.mathGenerator.computeExpression(controller.getHeartBeat().getFreq().getFreq());
    _builder.append(_computeExpression);
    _builder.append(")))){");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("String strValue = String(millis());");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("int str_len = strValue.length() + 1;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("char char_array[str_len];");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("strValue.toCharArray(char_array, str_len);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("publish(");
    String _name_1 = controller.getName();
    _builder.append(_name_1, "\t");
    _builder.append("Topic, char_array);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _name_2 = controller.getName();
    _builder.append(_name_2, "\t");
    _builder.append("Timer = millis();");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public int getFreqUnitTime(final String unit) {
    if (unit != null) {
      switch (unit) {
        case "second":
          return 1000;
        case "minute":
          return (60 * 1000);
        case "hour":
          return ((60 * 60) * 1000);
        case "day":
          return (((24 * 60) * 60) * 1000);
        case "week":
          return ((((7 * 24) * 60) * 60) * 1000);
        default:
          return 1000;
      }
    } else {
      return 1000;
    }
  }
}
