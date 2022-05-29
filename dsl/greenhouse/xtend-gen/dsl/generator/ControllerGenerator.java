package dsl.generator;

import com.google.common.base.Objects;
import dsl.greenhouse.Controller;
import dsl.greenhouse.Greenhouse;
import dsl.greenhouse.GreenhouseRuleSet;
import dsl.greenhouse.GreenhouseSensor;
import dsl.greenhouse.Model;
import dsl.greenhouse.Row;
import dsl.greenhouse.RowRuleSet;
import dsl.greenhouse.RowSensor;
import dsl.greenhouse.State;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.EcoreUtil2;

@SuppressWarnings("all")
public class ControllerGenerator {
  private MathGenerator mathGenerator = new MathGenerator();
  
  public CharSequence compileController(final Model model) {
    CharSequence _xblockexpression = null;
    {
      final EObject root = EcoreUtil2.getRootContainer(model);
      final List<RowSensor> allRowSensors = EcoreUtil2.<RowSensor>getAllContentsOfType(root, RowSensor.class);
      final List<GreenhouseSensor> allGreenhouseSensors = EcoreUtil2.<GreenhouseSensor>getAllContentsOfType(root, GreenhouseSensor.class);
      final List<RowRuleSet> allRowRuleset = EcoreUtil2.<RowRuleSet>getAllContentsOfType(root, RowRuleSet.class);
      final List<GreenhouseRuleSet> allGreenhouseRuleset = EcoreUtil2.<GreenhouseRuleSet>getAllContentsOfType(root, GreenhouseRuleSet.class);
      final List<Controller> allControllers = EcoreUtil2.<Controller>getAllContentsOfType(root, Controller.class);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("from paho.mqtt import client as mqtt_client");
      _builder.newLine();
      _builder.append("import time");
      _builder.newLine();
      _builder.append("class Sensor:");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("currentState = \"\"");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("def __init__(self, name, states, variable, actuator):");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("self.name = name");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("self.states = states");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("self.variable = variable");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("self.actuator = actuator");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("def updateSensor(self, variable, client):");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("self.variable = variable");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("ruleCheck(variable, self, client, self.states)");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("def updateSensorState(self, state, client):");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("theKey = next(iter(state))");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("self.currentState = theKey");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("publish(client, self.actuator, state.get(self.currentState))");
      _builder.newLine();
      _builder.append("class Microcontroller:");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("lastheartbeat = time.time()");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("healthy = True");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("def __init__(self, name, delay):");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("self.name = name");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("self.delay = delay");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("def isHealthy(self):");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("self.updateHealth()");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("return self.healthy");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("def updateHeartbeat(self):");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("print(\"updating heartbeat for: \" + self.name + \", \" + str(time.time() - self.lastheartbeat) + \" seconds since the last heartbeat\" )");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("self.updateHealth()");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("self.lastheartbeat = time.time()");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("def updateHealth(self):");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("if(self.lastheartbeat + self.delay < (time.time())):");
      _builder.newLine();
      _builder.append("            ");
      _builder.append("print(\"microcontroller \" + self.name + \" not healthy\")");
      _builder.newLine();
      _builder.append("            ");
      _builder.append("self.healthy = False");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("else:");
      _builder.newLine();
      _builder.append("            ");
      _builder.append("print(\"microcontroller \" + self.name + \" healthy\")");
      _builder.newLine();
      _builder.append("            ");
      _builder.append("self.healthy = True");
      _builder.newLine();
      _builder.append("broker = \'localhost\'");
      _builder.newLine();
      _builder.append("port = 1883");
      _builder.newLine();
      _builder.append("client_id = \'python-mqtt-controller\'");
      _builder.newLine();
      _builder.append("username = \'my_user\'");
      _builder.newLine();
      _builder.append("password = \'bendevictor\'");
      _builder.newLine();
      _builder.append("manual = 0");
      _builder.newLine();
      _builder.append("sensors = []");
      _builder.newLine();
      _builder.append("microcontrollers = []");
      _builder.newLine();
      _builder.append("def connect_mqtt() -> mqtt_client:");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("def on_connect(client, userdata, flags, rc):");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("if rc == 0:");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("print(\"Connected to MQTT Broker!\")");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("else:");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("print(\"Failed to connect, return code %d\\n\", rc)");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t");
      _builder.append("client = mqtt_client.Client(client_id)");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("client.username_pw_set(username, password)");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("client.on_connect = on_connect");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("client.connect(broker, port)");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("return client");
      _builder.newLine();
      _builder.newLine();
      _builder.newLine();
      _builder.append("def subscribe(client: mqtt_client, sensor):");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("def on_message(client, userdata, msg):");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("print(f\"Received `{msg.payload.decode()}` from `{msg.topic}` topic\")");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("for s in sensors:");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("if s.name == msg.topic:");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("s.updateSensor(msg.payload.decode(), client)");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("for mc in microcontrollers:");
      _builder.newLine();
      _builder.append("\t\t            ");
      _builder.append("if mc.name == msg.topic:");
      _builder.newLine();
      _builder.append("\t\t                ");
      _builder.append("mc.updateHeartbeat()");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("client.subscribe(sensor.name)");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("client.on_message = on_message");
      _builder.newLine();
      _builder.newLine();
      _builder.append("def publish(client,topic, message):");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("msg = message");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("if manual == 0:");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("result = client.publish(topic, msg)");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("# result: [0, 1]");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("status = result[0]");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("if status == 0:");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("print(f\"Send `{msg}` to topic `{topic}`\")");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("else:");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("print(f\"Failed to send message to topic {topic}\")");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.newLine();
      _builder.append("def ruleCheck(value, sensor, client,states):");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("if sensor.name == \"manual\":");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("global manual ");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("manual = int(value)");
      _builder.newLine();
      {
        for(final RowSensor sensor : allRowSensors) {
          _builder.append("\t");
          _builder.append("if sensor.name == \"");
          EObject _eContainer = sensor.eContainer().eContainer();
          String _name = ((Greenhouse) _eContainer).getName();
          _builder.append(_name, "\t");
          _builder.append("/");
          EObject _eContainer_1 = sensor.eContainer();
          String _name_1 = ((Row) _eContainer_1).getName();
          _builder.append(_name_1, "\t");
          _builder.append("/");
          String _name_2 = sensor.getName();
          _builder.append(_name_2, "\t");
          _builder.append("\" and microcontrollers[microcontrollers.index(\'");
          String _name_3 = sensor.getController().getName();
          _builder.append(_name_3, "\t");
          _builder.append("\')].isHealthy:");
          _builder.newLineIfNotEmpty();
          {
            EList<State> _states = sensor.getStates();
            for(final State state : _states) {
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("if float(value) ");
              String _op = state.getOp();
              _builder.append(_op, "\t\t");
              _builder.append(" ");
              String _computeExpression = this.mathGenerator.computeExpression(state.getThreshold());
              _builder.append(_computeExpression, "\t\t");
              _builder.append(":");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("sensor.updateSensorState(states[");
              int _indexOf = sensor.getStates().indexOf(state);
              _builder.append(_indexOf, "\t\t\t");
              _builder.append("],client)");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      {
        for(final GreenhouseSensor sensor_1 : allGreenhouseSensors) {
          _builder.append("\t");
          _builder.append("if sensor.name == \"");
          EObject _eContainer_2 = sensor_1.eContainer();
          String _name_4 = ((Greenhouse) _eContainer_2).getName();
          _builder.append(_name_4, "\t");
          _builder.append("/");
          String _name_5 = sensor_1.getName();
          _builder.append(_name_5, "\t");
          _builder.append("\":");
          _builder.newLineIfNotEmpty();
          {
            EList<State> _states_1 = sensor_1.getStates();
            for(final State state_1 : _states_1) {
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("if float(value) ");
              String _op_1 = state_1.getOp();
              _builder.append(_op_1, "\t\t");
              _builder.append(" ");
              String _computeExpression_1 = this.mathGenerator.computeExpression(state_1.getThreshold());
              _builder.append(_computeExpression_1, "\t\t");
              _builder.append(":");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("sensor.updateSensorState(states[");
              int _indexOf_1 = sensor_1.getStates().indexOf(state_1);
              _builder.append(_indexOf_1, "\t\t\t");
              _builder.append("],client)");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      _builder.append("\t");
      _builder.append("return");
      _builder.newLine();
      _builder.newLine();
      _builder.append("def run():");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("client = connect_mqtt()");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("manualState = Sensor(\"manual\", None, 0, None)");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("sensors.append(manualState)");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      {
        for(final Controller controller : allControllers) {
          _builder.append("\t");
          String _name_6 = controller.getName();
          _builder.append(_name_6, "\t");
          _builder.append(" = Microcontroller(\"");
          String _name_7 = controller.getName();
          _builder.append(_name_7, "\t");
          _builder.append("\", ");
          int _freqUnitTime = this.getFreqUnitTime(controller.getHeartBeat().getFreq().getUnit());
          _builder.append(_freqUnitTime, "\t");
          _builder.append("/");
          String _computeExpression_2 = this.mathGenerator.computeExpression(controller.getHeartBeat().getFreq().getFreq());
          _builder.append(_computeExpression_2, "\t");
          _builder.append(")");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("microcontrollers.append(");
          String _name_8 = controller.getName();
          _builder.append(_name_8, "\t");
          _builder.append(")");
          _builder.newLineIfNotEmpty();
        }
      }
      {
        for(final RowSensor sensor_2 : allRowSensors) {
          _builder.append("\t");
          _builder.append("sr");
          int _indexOf_2 = allRowSensors.indexOf(sensor_2);
          _builder.append(_indexOf_2, "\t");
          _builder.append(" = Sensor(\"");
          EObject _eContainer_3 = sensor_2.eContainer().eContainer();
          String _name_9 = ((Greenhouse) _eContainer_3).getName();
          _builder.append(_name_9, "\t");
          _builder.append("/");
          EObject _eContainer_4 = sensor_2.eContainer();
          String _name_10 = ((Row) _eContainer_4).getName();
          _builder.append(_name_10, "\t");
          _builder.append("/");
          String _name_11 = sensor_2.getName();
          _builder.append(_name_11, "\t");
          _builder.append("\",[");
          {
            EList<State> _states_2 = sensor_2.getStates();
            for(final State state_2 : _states_2) {
              _builder.append("{\"");
              String _name_12 = state_2.getName();
              _builder.append(_name_12, "\t");
              _builder.append("\":\"");
              {
                for(final RowRuleSet rule : allRowRuleset) {
                  {
                    if ((Objects.equal(rule.getSensor().getName(), sensor_2.getName()) && Objects.equal(rule.getState().getName(), state_2.getName()))) {
                      String _name_13 = rule.getTrigger().getName();
                      _builder.append(_name_13, "\t");
                    }
                  }
                }
              }
              _builder.append("\"},");
            }
          }
          _builder.append("],0,\"");
          EObject _eContainer_5 = sensor_2.eContainer().eContainer();
          String _name_14 = ((Greenhouse) _eContainer_5).getName();
          _builder.append(_name_14, "\t");
          _builder.append("/");
          EObject _eContainer_6 = sensor_2.eContainer();
          String _name_15 = ((Row) _eContainer_6).getName();
          _builder.append(_name_15, "\t");
          _builder.append("/");
          String _rowActuatorName = this.getRowActuatorName(model, sensor_2);
          _builder.append(_rowActuatorName, "\t");
          _builder.append("\")");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("sensors.append(sr");
          int _indexOf_3 = allRowSensors.indexOf(sensor_2);
          _builder.append(_indexOf_3, "\t");
          _builder.append(")");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("subscribe(client, sr");
          int _indexOf_4 = allRowSensors.indexOf(sensor_2);
          _builder.append(_indexOf_4, "\t");
          _builder.append(")");
          _builder.newLineIfNotEmpty();
        }
      }
      {
        for(final GreenhouseSensor sensor_3 : allGreenhouseSensors) {
          _builder.append("\t");
          _builder.append("sg");
          int _indexOf_5 = allGreenhouseSensors.indexOf(sensor_3);
          _builder.append(_indexOf_5, "\t");
          _builder.append(" = Sensor(\"");
          EObject _eContainer_7 = sensor_3.eContainer();
          String _name_16 = ((Greenhouse) _eContainer_7).getName();
          _builder.append(_name_16, "\t");
          _builder.append("/");
          String _name_17 = sensor_3.getName();
          _builder.append(_name_17, "\t");
          _builder.append("\",[");
          {
            EList<State> _states_3 = sensor_3.getStates();
            for(final State state_3 : _states_3) {
              _builder.append("{\"");
              String _name_18 = state_3.getName();
              _builder.append(_name_18, "\t");
              _builder.append("\":\"");
              {
                for(final GreenhouseRuleSet rule_1 : allGreenhouseRuleset) {
                  {
                    if ((Objects.equal(rule_1.getSensor().getName(), sensor_3.getName()) && Objects.equal(rule_1.getState().getName(), state_3.getName()))) {
                      String _name_19 = rule_1.getTrigger().getName();
                      _builder.append(_name_19, "\t");
                    }
                  }
                }
              }
              _builder.append("\"},");
            }
          }
          _builder.append("],0,\"");
          EObject _eContainer_8 = sensor_3.eContainer();
          String _name_20 = ((Greenhouse) _eContainer_8).getName();
          _builder.append(_name_20, "\t");
          _builder.append("/");
          String _greenhouseActuatorName = this.getGreenhouseActuatorName(model, sensor_3);
          _builder.append(_greenhouseActuatorName, "\t");
          _builder.append("\")");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("sensors.append(sg");
          int _indexOf_6 = allGreenhouseSensors.indexOf(sensor_3);
          _builder.append(_indexOf_6, "\t");
          _builder.append(")");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("subscribe(client, sg");
          int _indexOf_7 = allGreenhouseSensors.indexOf(sensor_3);
          _builder.append(_indexOf_7, "\t");
          _builder.append(")");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.append("client.loop_start()");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("while(True):");
      _builder.newLine();
      _builder.append("\t        ");
      _builder.append("time.sleep(1)");
      _builder.newLine();
      _builder.append("\t        ");
      _builder.append("for controller in microcontrollers:");
      _builder.newLine();
      _builder.append("\t            ");
      _builder.append("controller.isHealthy()");
      _builder.newLine();
      _builder.newLine();
      _builder.append("if __name__ == \'__main__\':");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("run()");
      _builder.newLine();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  public String getRowActuatorName(final Model model, final RowSensor sensor) {
    final EObject root = EcoreUtil2.getRootContainer(model);
    final List<RowRuleSet> allRowRuleset = EcoreUtil2.<RowRuleSet>getAllContentsOfType(root, RowRuleSet.class);
    for (final RowRuleSet rule : allRowRuleset) {
      String _name = rule.getSensor().getName();
      String _name_1 = sensor.getName();
      boolean _equals = Objects.equal(_name, _name_1);
      if (_equals) {
        StringConcatenation _builder = new StringConcatenation();
        String _name_2 = rule.getActuator().getName();
        _builder.append(_name_2);
        return _builder.toString();
      }
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    return _builder_1.toString();
  }
  
  public String getGreenhouseActuatorName(final Model model, final GreenhouseSensor sensor) {
    final EObject root = EcoreUtil2.getRootContainer(model);
    final List<GreenhouseRuleSet> allGreenhouseRuleset = EcoreUtil2.<GreenhouseRuleSet>getAllContentsOfType(root, GreenhouseRuleSet.class);
    for (final GreenhouseRuleSet rule : allGreenhouseRuleset) {
      String _name = rule.getSensor().getName();
      String _name_1 = sensor.getName();
      boolean _equals = Objects.equal(_name, _name_1);
      if (_equals) {
        StringConcatenation _builder = new StringConcatenation();
        String _name_2 = rule.getActuator().getName();
        _builder.append(_name_2);
        return _builder.toString();
      }
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    return _builder_1.toString();
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
