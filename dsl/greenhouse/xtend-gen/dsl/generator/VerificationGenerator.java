package dsl.generator;

import com.google.common.base.Objects;
import dsl.greenhouse.Action;
import dsl.greenhouse.Expression;
import dsl.greenhouse.Greenhouse;
import dsl.greenhouse.GreenhouseActuator;
import dsl.greenhouse.GreenhouseRuleSet;
import dsl.greenhouse.GreenhouseSensor;
import dsl.greenhouse.Model;
import dsl.greenhouse.Row;
import dsl.greenhouse.RowActuator;
import dsl.greenhouse.RowRuleSet;
import dsl.greenhouse.RowSensor;
import dsl.greenhouse.SettingAction;
import dsl.greenhouse.SettingActuator;
import dsl.greenhouse.SettingSensor;
import dsl.greenhouse.State;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class VerificationGenerator {
  private MathGenerator mathGenerator = new MathGenerator();
  
  public CharSequence getAllClocks(final Model model) {
    CharSequence _xblockexpression = null;
    {
      final EObject root = EcoreUtil2.getRootContainer(model);
      final List<SettingSensor> allSettingSensors = EcoreUtil2.<SettingSensor>getAllContentsOfType(root, SettingSensor.class);
      final List<SettingActuator> allSettingActuators = EcoreUtil2.<SettingActuator>getAllContentsOfType(root, SettingActuator.class);
      StringConcatenation _builder = new StringConcatenation();
      {
        for(final SettingSensor settingSensor : allSettingSensors) {
          _builder.append("clock ");
          String _name = settingSensor.getName();
          _builder.append(_name);
          _builder.append("Clock;");
          _builder.newLineIfNotEmpty();
        }
      }
      {
        for(final SettingActuator settingActuator : allSettingActuators) {
          _builder.append("clock ");
          String _name_1 = settingActuator.getName();
          _builder.append(_name_1);
          _builder.append("Clock;");
          _builder.newLineIfNotEmpty();
        }
      }
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  public CharSequence getAllVariables(final Model model) {
    CharSequence _xblockexpression = null;
    {
      final EObject root = EcoreUtil2.getRootContainer(model);
      final List<RowSensor> allRowSensors = EcoreUtil2.<RowSensor>getAllContentsOfType(root, RowSensor.class);
      final List<GreenhouseSensor> allGreenhouseSensors = EcoreUtil2.<GreenhouseSensor>getAllContentsOfType(root, GreenhouseSensor.class);
      StringConcatenation _builder = new StringConcatenation();
      {
        for(final RowSensor rowSensor : allRowSensors) {
          {
            EList<State> _states = rowSensor.getStates();
            for(final State state : _states) {
              {
                boolean _contains = state.getName().contains("optimal");
                if (_contains) {
                  _builder.append("int ");
                  String _name = rowSensor.getVariable().getName();
                  _builder.append(_name);
                  _builder.append(" := ");
                  String _computeExpression = this.mathGenerator.computeExpression(state.getThreshold());
                  _builder.append(_computeExpression);
                  _builder.append("+1;");
                  _builder.newLineIfNotEmpty();
                }
              }
            }
          }
        }
      }
      _builder.newLine();
      {
        for(final GreenhouseSensor greenhouseSensor : allGreenhouseSensors) {
          {
            EList<State> _states_1 = greenhouseSensor.getStates();
            for(final State state_1 : _states_1) {
              {
                boolean _contains_1 = state_1.getName().contains("optimal");
                if (_contains_1) {
                  _builder.append("int ");
                  String _name_1 = greenhouseSensor.getVariable().getName();
                  _builder.append(_name_1);
                  _builder.append(" := ");
                  String _computeExpression_1 = this.mathGenerator.computeExpression(state_1.getThreshold());
                  _builder.append(_computeExpression_1);
                  _builder.append("+1;");
                  _builder.newLineIfNotEmpty();
                }
              }
            }
          }
        }
      }
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  public String getTopics(final Model model) {
    final EObject root = EcoreUtil2.getRootContainer(model);
    final Function1<RowSensor, Boolean> _function = (RowSensor it) -> {
      String _name = it.getController().getName();
      String _name_1 = it.getController().getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<RowSensor> allSensors = IterableExtensions.<RowSensor>filter(EcoreUtil2.<RowSensor>getAllContentsOfType(root, RowSensor.class), _function);
    final Function1<GreenhouseSensor, Boolean> _function_1 = (GreenhouseSensor it) -> {
      String _name = it.getController().getName();
      String _name_1 = it.getController().getName();
      return Boolean.valueOf(Objects.equal(_name, _name_1));
    };
    final Iterable<GreenhouseSensor> allGlobalSensors = IterableExtensions.<GreenhouseSensor>filter(EcoreUtil2.<GreenhouseSensor>getAllContentsOfType(root, GreenhouseSensor.class), _function_1);
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    {
      boolean _isEmpty = IterableExtensions.isEmpty(allSensors);
      boolean _not = (!_isEmpty);
      if (_not) {
        {
          for(final RowSensor sensor : allSensors) {
            _builder.append("chan ");
            EObject _eContainer = sensor.eContainer().eContainer();
            String _name = ((Greenhouse) _eContainer).getName();
            _builder.append(_name);
            _builder.append("_");
            EObject _eContainer_1 = sensor.eContainer();
            String _name_1 = ((Row) _eContainer_1).getName();
            _builder.append(_name_1);
            _builder.append("_");
            String _name_2 = sensor.getName();
            _builder.append(_name_2);
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      boolean _isEmpty_1 = IterableExtensions.isEmpty(allGlobalSensors);
      boolean _not_1 = (!_isEmpty_1);
      if (_not_1) {
        {
          for(final GreenhouseSensor sensor_1 : allGlobalSensors) {
            _builder.append("chan ");
            EObject _eContainer_2 = sensor_1.eContainer();
            String _name_3 = ((Greenhouse) _eContainer_2).getName();
            _builder.append(_name_3);
            _builder.append("_");
            String _name_4 = sensor_1.getName();
            _builder.append(_name_4);
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder.toString();
  }
  
  public String getAllActuators(final Model model) {
    final EObject root = EcoreUtil2.getRootContainer(model);
    final List<RowActuator> allRowActuators = EcoreUtil2.<RowActuator>getAllContentsOfType(root, RowActuator.class);
    final List<GreenhouseActuator> allGreenhouseActuators = EcoreUtil2.<GreenhouseActuator>getAllContentsOfType(root, GreenhouseActuator.class);
    final List<GreenhouseSensor> allGreenhouseSensors = EcoreUtil2.<GreenhouseSensor>getAllContentsOfType(root, GreenhouseSensor.class);
    final List<RowSensor> allRowSensors = EcoreUtil2.<RowSensor>getAllContentsOfType(root, RowSensor.class);
    final List<RowRuleSet> allRowRules = EcoreUtil2.<RowRuleSet>getAllContentsOfType(root, RowRuleSet.class);
    final List<GreenhouseRuleSet> allGreenhouseRules = EcoreUtil2.<GreenhouseRuleSet>getAllContentsOfType(root, GreenhouseRuleSet.class);
    final List<SettingActuator> allSettingActuator = EcoreUtil2.<SettingActuator>getAllContentsOfType(root, SettingActuator.class);
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    {
      boolean _isEmpty = allRowActuators.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        {
          boolean _hasElements = false;
          for(final RowActuator rowActuator : allRowActuators) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate("\n", "");
            }
            _builder.append("process ");
            String _upperCase = rowActuator.getName().toUpperCase();
            _builder.append(_upperCase);
            _builder.append("(){");
            _builder.newLineIfNotEmpty();
            _builder.append("state");
            _builder.newLine();
            _builder.append("\t");
            {
              EList<Action> _action = rowActuator.getAction();
              boolean _hasElements_1 = false;
              for(final Action action : _action) {
                if (!_hasElements_1) {
                  _hasElements_1 = true;
                } else {
                  _builder.appendImmediate(",\n", "\t");
                }
                String _name = action.getValue().getName();
                _builder.append(_name, "\t");
              }
            }
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("init");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("idle;");
            _builder.newLine();
            {
              final Function1<RowRuleSet, Boolean> _function = (RowRuleSet it) -> {
                String _name_1 = it.getActuator().getName();
                String _name_2 = rowActuator.getName();
                return Boolean.valueOf(Objects.equal(_name_1, _name_2));
              };
              int _size = IterableExtensions.size(IterableExtensions.<RowRuleSet>filter(allRowRules, _function));
              boolean _greaterThan = (_size > 0);
              if (_greaterThan) {
                _builder.append("trans");
                _builder.newLine();
                _builder.append("\t");
                {
                  EList<Action> _action_1 = rowActuator.getAction();
                  boolean _hasElements_2 = false;
                  for(final Action action1 : _action_1) {
                    if (!_hasElements_2) {
                      _hasElements_2 = true;
                    } else {
                      _builder.appendImmediate(",", "\t");
                    }
                    {
                      EList<Action> _action_2 = rowActuator.getAction();
                      for(final Action action2 : _action_2) {
                        {
                          if ((action1.getValue().getName().contains("idle") && (!Objects.equal(action1.getValue().getName(), action2.getValue().getName())))) {
                            String _name_1 = action1.getValue().getName();
                            _builder.append(_name_1, "\t");
                            _builder.append(" -> ");
                            String _name_2 = action2.getValue().getName();
                            _builder.append(_name_2, "\t");
                            {
                              for(final RowSensor sensor : allRowSensors) {
                                {
                                  final Function1<RowRuleSet, Boolean> _function_1 = (RowRuleSet it) -> {
                                    String _name_3 = it.getActuator().getName();
                                    String _name_4 = rowActuator.getName();
                                    return Boolean.valueOf(Objects.equal(_name_3, _name_4));
                                  };
                                  boolean _isEmpty_1 = IterableExtensions.isEmpty(IterableExtensions.<RowRuleSet>filter(allRowRules, _function_1));
                                  boolean _not_1 = (!_isEmpty_1);
                                  if (_not_1) {
                                    _builder.append("{sync ");
                                    EObject _eContainer = sensor.eContainer().eContainer();
                                    String _name_3 = ((Greenhouse) _eContainer).getName();
                                    _builder.append(_name_3, "\t");
                                    _builder.append("_");
                                    EObject _eContainer_1 = sensor.eContainer();
                                    String _name_4 = ((Row) _eContainer_1).getName();
                                    _builder.append(_name_4, "\t");
                                    _builder.append("_");
                                    String _name_5 = sensor.getName();
                                    _builder.append(_name_5, "\t");
                                    _builder.append("?;");
                                    {
                                      for(final SettingActuator settingActuator : allSettingActuator) {
                                        {
                                          String _name_6 = settingActuator.getName();
                                          String _name_7 = rowActuator.getType().getName();
                                          boolean _equals = Objects.equal(_name_6, _name_7);
                                          if (_equals) {
                                            _builder.append(" assign ");
                                            String _name_8 = settingActuator.getName();
                                            _builder.append(_name_8, "\t");
                                            _builder.append("Clock:=0;");
                                          }
                                        }
                                      }
                                    }
                                    _builder.append("},");
                                  }
                                }
                              }
                            }
                            _builder.newLineIfNotEmpty();
                          }
                        }
                        _builder.append("\t");
                        {
                          SettingAction _value = action1.getValue();
                          SettingAction _value_1 = action2.getValue();
                          boolean _notEquals = (!Objects.equal(_value, _value_1));
                          if (_notEquals) {
                            SettingAction _value_2 = action2.getValue();
                            _builder.append(_value_2, "\t");
                            _builder.append(" -> ");
                            String _name_9 = action1.getValue().getName();
                            _builder.append(_name_9, "\t");
                            _builder.append("{}");
                          }
                        }
                      }
                    }
                  }
                }
                _builder.append(";");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    _builder.newLine();
    {
      boolean _isEmpty_2 = allGreenhouseActuators.isEmpty();
      boolean _not_2 = (!_isEmpty_2);
      if (_not_2) {
        {
          boolean _hasElements_3 = false;
          for(final GreenhouseActuator greenhouseActuator : allGreenhouseActuators) {
            if (!_hasElements_3) {
              _hasElements_3 = true;
            } else {
              _builder.appendImmediate("\n", "");
            }
            _builder.append("process ");
            String _upperCase_1 = greenhouseActuator.getName().toUpperCase();
            _builder.append(_upperCase_1);
            _builder.append("(){");
            _builder.newLineIfNotEmpty();
            _builder.append("state");
            _builder.newLine();
            _builder.append("    ");
            {
              EList<Action> _action_3 = greenhouseActuator.getAction();
              boolean _hasElements_4 = false;
              for(final Action action_1 : _action_3) {
                if (!_hasElements_4) {
                  _hasElements_4 = true;
                } else {
                  _builder.appendImmediate(",\n", "    ");
                }
                String _name_10 = action_1.getValue().getName();
                _builder.append(_name_10, "    ");
              }
            }
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("init");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("idle;");
            _builder.newLine();
            {
              final Function1<GreenhouseRuleSet, Boolean> _function_2 = (GreenhouseRuleSet it) -> {
                String _name_11 = it.getActuator().getName();
                String _name_12 = greenhouseActuator.getName();
                return Boolean.valueOf(Objects.equal(_name_11, _name_12));
              };
              int _size_1 = IterableExtensions.size(IterableExtensions.<GreenhouseRuleSet>filter(allGreenhouseRules, _function_2));
              boolean _greaterThan_1 = (_size_1 > 0);
              if (_greaterThan_1) {
                _builder.append("                ");
                _builder.append("trans");
                _builder.newLine();
                {
                  EList<Action> _action_4 = greenhouseActuator.getAction();
                  boolean _hasElements_5 = false;
                  for(final Action action1_1 : _action_4) {
                    if (!_hasElements_5) {
                      _hasElements_5 = true;
                    } else {
                      _builder.appendImmediate(",", "");
                    }
                    {
                      EList<Action> _action_5 = greenhouseActuator.getAction();
                      for(final Action action2_1 : _action_5) {
                        {
                          if ((action1_1.getValue().getName().contains("idle") && (!Objects.equal(action1_1.getValue().getName(), action2_1.getValue().getName())))) {
                            String _name_11 = action1_1.getValue().getName();
                            _builder.append(_name_11);
                            _builder.append(" -> ");
                            String _name_12 = action2_1.getValue().getName();
                            _builder.append(_name_12);
                            {
                              for(final RowSensor sensor_1 : allRowSensors) {
                                {
                                  final Function1<RowRuleSet, Boolean> _function_3 = (RowRuleSet it) -> {
                                    String _name_13 = it.getActuator().getName();
                                    String _name_14 = greenhouseActuator.getName();
                                    return Boolean.valueOf(Objects.equal(_name_13, _name_14));
                                  };
                                  boolean _isEmpty_3 = IterableExtensions.isEmpty(IterableExtensions.<RowRuleSet>filter(allRowRules, _function_3));
                                  boolean _not_3 = (!_isEmpty_3);
                                  if (_not_3) {
                                    _builder.append("{sync ");
                                    EObject _eContainer_2 = sensor_1.eContainer().eContainer();
                                    String _name_13 = ((Greenhouse) _eContainer_2).getName();
                                    _builder.append(_name_13);
                                    _builder.append("_");
                                    EObject _eContainer_3 = sensor_1.eContainer();
                                    String _name_14 = ((Row) _eContainer_3).getName();
                                    _builder.append(_name_14);
                                    _builder.append("_");
                                    String _name_15 = sensor_1.getName();
                                    _builder.append(_name_15);
                                    _builder.append("?;");
                                    {
                                      for(final SettingActuator settingActuator_1 : allSettingActuator) {
                                        {
                                          String _name_16 = settingActuator_1.getName();
                                          String _name_17 = greenhouseActuator.getType().getName();
                                          boolean _equals_1 = Objects.equal(_name_16, _name_17);
                                          if (_equals_1) {
                                            _builder.append(" assign ");
                                            String _name_18 = settingActuator_1.getName();
                                            _builder.append(_name_18);
                                            _builder.append("Clock:=0;");
                                          }
                                        }
                                      }
                                    }
                                    _builder.append("},");
                                  }
                                }
                              }
                            }
                            _builder.newLineIfNotEmpty();
                          }
                        }
                        {
                          String _name_19 = action1_1.getValue().getName();
                          String _name_20 = action2_1.getValue().getName();
                          boolean _notEquals_1 = (!Objects.equal(_name_19, _name_20));
                          if (_notEquals_1) {
                            String _name_21 = action2_1.getValue().getName();
                            _builder.append(_name_21);
                            _builder.append(" -> ");
                            String _name_22 = action1_1.getValue().getName();
                            _builder.append(_name_22);
                            _builder.append("{}");
                          }
                        }
                      }
                    }
                  }
                }
                _builder.append(";");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    _builder.newLine();
    return _builder.toString();
  }
  
  public String getAllSensors(final Model model) {
    final EObject root = EcoreUtil2.getRootContainer(model);
    final List<RowSensor> allRowSensors = EcoreUtil2.<RowSensor>getAllContentsOfType(root, RowSensor.class);
    final List<GreenhouseSensor> allGreenhouseSensors = EcoreUtil2.<GreenhouseSensor>getAllContentsOfType(root, GreenhouseSensor.class);
    final List<RowRuleSet> allRowRules = EcoreUtil2.<RowRuleSet>getAllContentsOfType(root, RowRuleSet.class);
    final List<GreenhouseRuleSet> allGreenhouseRules = EcoreUtil2.<GreenhouseRuleSet>getAllContentsOfType(root, GreenhouseRuleSet.class);
    final List<SettingSensor> allSettingSensors = EcoreUtil2.<SettingSensor>getAllContentsOfType(root, SettingSensor.class);
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    {
      boolean _isEmpty = allRowSensors.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        {
          boolean _hasElements = false;
          for(final RowSensor rowSensor : allRowSensors) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate("\n", "");
            }
            _builder.append("process ");
            String _upperCase = rowSensor.getName().toUpperCase();
            _builder.append(_upperCase);
            _builder.append("(){");
            _builder.newLineIfNotEmpty();
            _builder.append("state");
            _builder.newLine();
            _builder.append("\t");
            {
              EList<State> _states = rowSensor.getStates();
              boolean _hasElements_1 = false;
              for(final State state : _states) {
                if (!_hasElements_1) {
                  _hasElements_1 = true;
                } else {
                  _builder.appendImmediate(",\n", "\t");
                }
                String _name = state.getName();
                _builder.append(_name, "\t");
                {
                  Expression _threshold = state.getThreshold();
                  boolean _tripleNotEquals = (_threshold != null);
                  if (_tripleNotEquals) {
                    _builder.append("{");
                    String _name_1 = rowSensor.getVariable().getName();
                    _builder.append(_name_1, "\t");
                    _builder.append(" ");
                    String _op = state.getOp();
                    _builder.append(_op, "\t");
                    _builder.append(" ");
                    String _computeExpression = this.mathGenerator.computeExpression(state.getThreshold());
                    _builder.append(_computeExpression, "\t");
                    _builder.append("}");
                  }
                }
              }
            }
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("init");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("optimal;");
            _builder.newLine();
            {
              final Function1<RowRuleSet, Boolean> _function = (RowRuleSet it) -> {
                String _name_2 = it.getSensor().getName();
                String _name_3 = rowSensor.getName();
                return Boolean.valueOf(Objects.equal(_name_2, _name_3));
              };
              int _size = IterableExtensions.size(IterableExtensions.<RowRuleSet>filter(allRowRules, _function));
              boolean _greaterThan = (_size > 0);
              if (_greaterThan) {
                _builder.append("trans");
                _builder.newLine();
                _builder.append("\t");
                {
                  boolean _hasElements_2 = false;
                  for(final RowRuleSet rowRule : allRowRules) {
                    if (!_hasElements_2) {
                      _hasElements_2 = true;
                    } else {
                      _builder.appendImmediate(",", "\t");
                    }
                    {
                      EList<State> _states_1 = rowSensor.getStates();
                      for(final State state_1 : _states_1) {
                        {
                          boolean _contains = state_1.getName().contains("optimal");
                          if (_contains) {
                            String _name_2 = state_1.getName();
                            _builder.append(_name_2, "\t");
                            _builder.append(" -> ");
                            String _name_3 = rowRule.getState().getName();
                            _builder.append(_name_3, "\t");
                            {
                              for(final RowSensor sensor : allRowSensors) {
                                {
                                  if ((Objects.equal(sensor.getName(), rowSensor.getName()) && Objects.equal(rowRule.getSensor().getName(), sensor.getName()))) {
                                    _builder.append("{sync ");
                                    EObject _eContainer = sensor.eContainer().eContainer();
                                    String _name_4 = ((Greenhouse) _eContainer).getName();
                                    _builder.append(_name_4, "\t");
                                    _builder.append("_");
                                    EObject _eContainer_1 = sensor.eContainer();
                                    String _name_5 = ((Row) _eContainer_1).getName();
                                    _builder.append(_name_5, "\t");
                                    _builder.append("_");
                                    String _name_6 = sensor.getName();
                                    _builder.append(_name_6, "\t");
                                    _builder.append("!;");
                                    {
                                      for(final SettingSensor settingSensor : allSettingSensors) {
                                        {
                                          String _name_7 = settingSensor.getName();
                                          String _name_8 = sensor.getType().getName();
                                          boolean _equals = Objects.equal(_name_7, _name_8);
                                          if (_equals) {
                                            _builder.append(" assign ");
                                            String _name_9 = settingSensor.getName();
                                            _builder.append(_name_9, "\t");
                                            _builder.append("Clock:=0;");
                                          }
                                        }
                                      }
                                    }
                                    _builder.append("}");
                                  }
                                }
                                _builder.append(",");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                              }
                            }
                            String _name_10 = rowRule.getState().getName();
                            _builder.append(_name_10, "\t");
                            _builder.append(" -> ");
                            String _name_11 = state_1.getName();
                            _builder.append(_name_11, "\t");
                            _builder.append("{}");
                          }
                        }
                      }
                    }
                  }
                }
                _builder.append(";");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    _builder.newLine();
    {
      boolean _isEmpty_1 = allGreenhouseSensors.isEmpty();
      boolean _not_1 = (!_isEmpty_1);
      if (_not_1) {
        {
          boolean _hasElements_3 = false;
          for(final GreenhouseSensor greenhouseSensor : allGreenhouseSensors) {
            if (!_hasElements_3) {
              _hasElements_3 = true;
            } else {
              _builder.appendImmediate("\n", "");
            }
            _builder.append("process ");
            String _upperCase_1 = greenhouseSensor.getName().toUpperCase();
            _builder.append(_upperCase_1);
            _builder.append("(){");
            _builder.newLineIfNotEmpty();
            _builder.append("state");
            _builder.newLine();
            _builder.append("    ");
            {
              EList<State> _states_2 = greenhouseSensor.getStates();
              boolean _hasElements_4 = false;
              for(final State state_2 : _states_2) {
                if (!_hasElements_4) {
                  _hasElements_4 = true;
                } else {
                  _builder.appendImmediate(",\n", "    ");
                }
                String _name_12 = state_2.getName();
                _builder.append(_name_12, "    ");
                {
                  Expression _threshold_1 = state_2.getThreshold();
                  boolean _tripleNotEquals_1 = (_threshold_1 != null);
                  if (_tripleNotEquals_1) {
                    _builder.append("{");
                    String _name_13 = greenhouseSensor.getVariable().getName();
                    _builder.append(_name_13, "    ");
                    String _op_1 = state_2.getOp();
                    _builder.append(_op_1, "    ");
                    String _computeExpression_1 = this.mathGenerator.computeExpression(state_2.getThreshold());
                    _builder.append(_computeExpression_1, "    ");
                    _builder.append("}");
                  }
                }
              }
            }
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("init");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("optimal;");
            _builder.newLine();
            {
              final Function1<GreenhouseRuleSet, Boolean> _function_1 = (GreenhouseRuleSet it) -> {
                String _name_14 = it.getSensor().getName();
                String _name_15 = greenhouseSensor.getName();
                return Boolean.valueOf(Objects.equal(_name_14, _name_15));
              };
              int _size_1 = IterableExtensions.size(IterableExtensions.<GreenhouseRuleSet>filter(allGreenhouseRules, _function_1));
              boolean _greaterThan_1 = (_size_1 > 0);
              if (_greaterThan_1) {
                _builder.append("trans");
                _builder.newLine();
                {
                  boolean _hasElements_5 = false;
                  for(final GreenhouseRuleSet greenhouseRule : allGreenhouseRules) {
                    if (!_hasElements_5) {
                      _hasElements_5 = true;
                    } else {
                      _builder.appendImmediate(",", "\t");
                    }
                    {
                      EList<State> _states_3 = greenhouseSensor.getStates();
                      for(final State state_3 : _states_3) {
                        {
                          boolean _contains_1 = state_3.getName().contains("optimal");
                          if (_contains_1) {
                            String _name_14 = state_3.getName();
                            _builder.append(_name_14);
                            _builder.append(" -> ");
                            String _name_15 = greenhouseRule.getState().getName();
                            _builder.append(_name_15);
                            _builder.newLineIfNotEmpty();
                            {
                              for(final RowSensor sensor_1 : allRowSensors) {
                                {
                                  String _name_16 = sensor_1.getName();
                                  String _name_17 = greenhouseSensor.getName();
                                  boolean _equals_1 = Objects.equal(_name_16, _name_17);
                                  if (_equals_1) {
                                    {
                                      String _name_18 = greenhouseRule.getSensor().getName();
                                      String _name_19 = sensor_1.getName();
                                      boolean _equals_2 = Objects.equal(_name_18, _name_19);
                                      if (_equals_2) {
                                        _builder.append("{sync ");
                                        EObject _eContainer_2 = sensor_1.eContainer().eContainer();
                                        String _name_20 = ((Greenhouse) _eContainer_2).getName();
                                        _builder.append(_name_20);
                                        _builder.append("_");
                                        EObject _eContainer_3 = sensor_1.eContainer();
                                        String _name_21 = ((Row) _eContainer_3).getName();
                                        _builder.append(_name_21);
                                        _builder.append("_");
                                        String _name_22 = sensor_1.getName();
                                        _builder.append(_name_22);
                                        _builder.append("!;");
                                        {
                                          for(final SettingSensor settingSensor_1 : allSettingSensors) {
                                            {
                                              String _name_23 = settingSensor_1.getName();
                                              String _name_24 = sensor_1.getType().getName();
                                              boolean _equals_3 = Objects.equal(_name_23, _name_24);
                                              if (_equals_3) {
                                                _builder.append(" assign ");
                                                String _name_25 = settingSensor_1.getName();
                                                _builder.append(_name_25);
                                                _builder.append("Clock:=0;");
                                              }
                                            }
                                          }
                                        }
                                        _builder.append("}");
                                      }
                                    }
                                    _builder.append(",");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                            String _name_26 = greenhouseRule.getState().getName();
                            _builder.append(_name_26);
                            _builder.append(" -> ");
                            String _name_27 = state_3.getName();
                            _builder.append(_name_27);
                            _builder.append("{}");
                          }
                        }
                      }
                    }
                  }
                }
                _builder.append(";");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    _builder.newLine();
    return _builder.toString();
  }
  
  public CharSequence instantiateVerificationModels(final Model model) {
    CharSequence _xblockexpression = null;
    {
      final EObject root = EcoreUtil2.getRootContainer(model);
      final List<RowSensor> allRowSensors = EcoreUtil2.<RowSensor>getAllContentsOfType(root, RowSensor.class);
      final List<GreenhouseSensor> allGreenhouseSensors = EcoreUtil2.<GreenhouseSensor>getAllContentsOfType(root, GreenhouseSensor.class);
      final List<RowActuator> allRowActuators = EcoreUtil2.<RowActuator>getAllContentsOfType(root, RowActuator.class);
      final List<GreenhouseActuator> allGreenhouseActuators = EcoreUtil2.<GreenhouseActuator>getAllContentsOfType(root, GreenhouseActuator.class);
      StringConcatenation _builder = new StringConcatenation();
      {
        boolean _isEmpty = allRowSensors.isEmpty();
        boolean _not = (!_isEmpty);
        if (_not) {
          {
            for(final RowSensor rowSensor : allRowSensors) {
              String _lowerCase = rowSensor.getName().toLowerCase();
              _builder.append(_lowerCase);
              _builder.append(" := ");
              String _upperCase = rowSensor.getName().toUpperCase();
              _builder.append(_upperCase);
              _builder.append("();");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      _builder.newLine();
      {
        boolean _isEmpty_1 = allGreenhouseSensors.isEmpty();
        boolean _not_1 = (!_isEmpty_1);
        if (_not_1) {
          {
            for(final GreenhouseSensor greenhouseSensor : allGreenhouseSensors) {
              String _lowerCase_1 = greenhouseSensor.getName().toLowerCase();
              _builder.append(_lowerCase_1);
              _builder.append(" := ");
              String _upperCase_1 = greenhouseSensor.getName().toUpperCase();
              _builder.append(_upperCase_1);
              _builder.append("();");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      _builder.newLine();
      {
        boolean _isEmpty_2 = allRowActuators.isEmpty();
        boolean _not_2 = (!_isEmpty_2);
        if (_not_2) {
          {
            for(final RowActuator rowActuators : allRowActuators) {
              String _lowerCase_2 = rowActuators.getName().toLowerCase();
              _builder.append(_lowerCase_2);
              _builder.append(" := ");
              String _upperCase_2 = rowActuators.getName().toUpperCase();
              _builder.append(_upperCase_2);
              _builder.append("();");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      _builder.newLine();
      {
        boolean _isEmpty_3 = allGreenhouseActuators.isEmpty();
        boolean _not_3 = (!_isEmpty_3);
        if (_not_3) {
          {
            for(final GreenhouseActuator greenhouseActuators : allGreenhouseActuators) {
              String _lowerCase_3 = greenhouseActuators.getName().toLowerCase();
              _builder.append(_lowerCase_3);
              _builder.append(" := ");
              String _upperCase_3 = greenhouseActuators.getName().toUpperCase();
              _builder.append(_upperCase_3);
              _builder.append("();");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      _builder.newLine();
      _builder.append("system ");
      {
        boolean _isEmpty_4 = allRowSensors.isEmpty();
        boolean _not_4 = (!_isEmpty_4);
        if (_not_4) {
          {
            boolean _hasElements = false;
            for(final RowSensor rowSensor_1 : allRowSensors) {
              if (!_hasElements) {
                _hasElements = true;
              } else {
                _builder.appendImmediate(", ", "");
              }
              String _lowerCase_4 = rowSensor_1.getName().toLowerCase();
              _builder.append(_lowerCase_4);
            }
          }
        }
      }
      _builder.append(", ");
      {
        boolean _isEmpty_5 = allGreenhouseSensors.isEmpty();
        boolean _not_5 = (!_isEmpty_5);
        if (_not_5) {
          {
            boolean _hasElements_1 = false;
            for(final GreenhouseSensor greenhouseSensor_1 : allGreenhouseSensors) {
              if (!_hasElements_1) {
                _hasElements_1 = true;
              } else {
                _builder.appendImmediate(", ", "");
              }
              String _lowerCase_5 = greenhouseSensor_1.getName().toLowerCase();
              _builder.append(_lowerCase_5);
            }
          }
        }
      }
      _builder.append(", ");
      {
        boolean _isEmpty_6 = allRowActuators.isEmpty();
        boolean _not_6 = (!_isEmpty_6);
        if (_not_6) {
          {
            boolean _hasElements_2 = false;
            for(final RowActuator rowActuators_1 : allRowActuators) {
              if (!_hasElements_2) {
                _hasElements_2 = true;
              } else {
                _builder.appendImmediate(", ", "");
              }
              String _lowerCase_6 = rowActuators_1.getName().toLowerCase();
              _builder.append(_lowerCase_6);
            }
          }
        }
      }
      _builder.append(", ");
      {
        boolean _isEmpty_7 = allGreenhouseActuators.isEmpty();
        boolean _not_7 = (!_isEmpty_7);
        if (_not_7) {
          {
            boolean _hasElements_3 = false;
            for(final GreenhouseActuator greenhouseActuators_1 : allGreenhouseActuators) {
              if (!_hasElements_3) {
                _hasElements_3 = true;
              } else {
                _builder.appendImmediate(", ", "");
              }
              String _lowerCase_7 = greenhouseActuators_1.getName().toLowerCase();
              _builder.append(_lowerCase_7);
            }
          }
        }
      }
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  public CharSequence compileVerification(final Model model) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _allClocks = this.getAllClocks(model);
    _builder.append(_allClocks);
    _builder.newLineIfNotEmpty();
    CharSequence _allVariables = this.getAllVariables(model);
    _builder.append(_allVariables);
    _builder.newLineIfNotEmpty();
    String _topics = this.getTopics(model);
    _builder.append(_topics);
    _builder.newLineIfNotEmpty();
    String _allActuators = this.getAllActuators(model);
    _builder.append(_allActuators);
    _builder.newLineIfNotEmpty();
    String _allSensors = this.getAllSensors(model);
    _builder.append(_allSensors);
    _builder.newLineIfNotEmpty();
    CharSequence _instantiateVerificationModels = this.instantiateVerificationModels(model);
    _builder.append(_instantiateVerificationModels);
    _builder.newLineIfNotEmpty();
    return _builder;
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
