/**
 * generated by Xtext 2.25.0
 */
package dsl.scoping;

import com.google.common.base.Objects;
import dsl.greenhouse.Action;
import dsl.greenhouse.Controller;
import dsl.greenhouse.GreenhouseActuator;
import dsl.greenhouse.GreenhouseRuleSet;
import dsl.greenhouse.GreenhouseSensor;
import dsl.greenhouse.RowActuator;
import dsl.greenhouse.RowRuleSet;
import dsl.greenhouse.RowSensor;
import dsl.greenhouse.SettingAction;
import dsl.greenhouse.SettingActuator;
import dsl.greenhouse.SettingSensor;
import dsl.greenhouse.State;
import dsl.greenhouse.Trigger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

/**
 * This class contains custom scoping description.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 */
@SuppressWarnings("all")
public class GreenhouseScopeProvider extends AbstractGreenhouseScopeProvider {
  @Override
  public IScope getScope(final EObject context, final EReference reference) {
    return this.scopeForEObject(context, reference);
  }
  
  protected IScope _scopeForEObject(final EObject context, final EReference reference) {
    return super.getScope(context.eContainer(), reference);
  }
  
  protected IScope _scopeForEObject(final RowActuator context, final EReference reference) {
    IScope _xblockexpression = null;
    {
      final EObject root = EcoreUtil2.getRootContainer(context);
      final List<Controller> allControllers = EcoreUtil2.<Controller>getAllContentsOfType(root, Controller.class);
      final List<SettingActuator> allActuators = EcoreUtil2.<SettingActuator>getAllContentsOfType(root, SettingActuator.class);
      final List<SettingAction> allSettingActions = EcoreUtil2.<SettingAction>getAllContentsOfType(root, SettingAction.class);
      _xblockexpression = Scopes.scopeFor(Collections.<EObject>unmodifiableList(CollectionLiterals.<EObject>newArrayList(context)), Scopes.scopeFor(allActuators, Scopes.scopeFor(allControllers, Scopes.scopeFor(allSettingActions))));
    }
    return _xblockexpression;
  }
  
  protected IScope _scopeForEObject(final GreenhouseActuator context, final EReference reference) {
    IScope _xblockexpression = null;
    {
      final EObject root = EcoreUtil2.getRootContainer(context);
      final List<SettingActuator> allActuators = EcoreUtil2.<SettingActuator>getAllContentsOfType(root, SettingActuator.class);
      final List<Controller> allControllers = EcoreUtil2.<Controller>getAllContentsOfType(root, Controller.class);
      final List<SettingAction> allSettingActions = EcoreUtil2.<SettingAction>getAllContentsOfType(root, SettingAction.class);
      System.out.println(allActuators);
      _xblockexpression = Scopes.scopeFor(Collections.<EObject>unmodifiableList(CollectionLiterals.<EObject>newArrayList(context)), Scopes.scopeFor(allActuators, Scopes.scopeFor(allControllers, Scopes.scopeFor(allSettingActions))));
    }
    return _xblockexpression;
  }
  
  protected IScope _scopeForEObject(final RowSensor context, final EReference reference) {
    IScope _xblockexpression = null;
    {
      final EObject root = EcoreUtil2.getRootContainer(context);
      final List<SettingSensor> allSensors = EcoreUtil2.<SettingSensor>getAllContentsOfType(root, SettingSensor.class);
      final List<Controller> allControllers = EcoreUtil2.<Controller>getAllContentsOfType(root, Controller.class);
      System.out.println(allSensors);
      _xblockexpression = Scopes.scopeFor(Collections.<EObject>unmodifiableList(CollectionLiterals.<EObject>newArrayList(context)), Scopes.scopeFor(allSensors, Scopes.scopeFor(allControllers)));
    }
    return _xblockexpression;
  }
  
  protected IScope _scopeForEObject(final GreenhouseSensor context, final EReference reference) {
    IScope _xblockexpression = null;
    {
      final EObject root = EcoreUtil2.getRootContainer(context);
      final List<SettingSensor> allSensors = EcoreUtil2.<SettingSensor>getAllContentsOfType(root, SettingSensor.class);
      final List<Controller> allControllers = EcoreUtil2.<Controller>getAllContentsOfType(root, Controller.class);
      System.out.println(allSensors);
      _xblockexpression = Scopes.scopeFor(Collections.<EObject>unmodifiableList(CollectionLiterals.<EObject>newArrayList(context)), Scopes.scopeFor(allSensors, Scopes.scopeFor(allControllers)));
    }
    return _xblockexpression;
  }
  
  protected IScope _scopeForEObject(final Action context, final EReference reference) {
    IScope _xblockexpression = null;
    {
      final EObject root = EcoreUtil2.getRootContainer(context);
      final List<SettingAction> allValues = EcoreUtil2.<SettingAction>getAllContentsOfType(root, SettingAction.class);
      final Function1<SettingAction, Boolean> _function = (SettingAction it) -> {
        boolean _switchResult = false;
        EObject _eContainer = context.eContainer();
        boolean _matched = false;
        if (_eContainer instanceof RowActuator) {
          _matched=true;
          EObject _eContainer_1 = context.eContainer();
          SettingActuator _type = ((RowActuator) _eContainer_1).getType();
          EObject _eContainer_2 = it.eContainer();
          _switchResult = Objects.equal(_type, ((SettingActuator) _eContainer_2));
        }
        if (!_matched) {
          if (_eContainer instanceof GreenhouseActuator) {
            _matched=true;
            EObject _eContainer_1 = context.eContainer();
            SettingActuator _type = ((GreenhouseActuator) _eContainer_1).getType();
            EObject _eContainer_2 = it.eContainer();
            _switchResult = Objects.equal(_type, ((SettingActuator) _eContainer_2));
          }
        }
        if (!_matched) {
          _switchResult = false;
        }
        return Boolean.valueOf(_switchResult);
      };
      final Iterable<SettingAction> filtered = IterableExtensions.<SettingAction>filter(allValues, _function);
      _xblockexpression = Scopes.scopeFor(Collections.<EObject>unmodifiableList(CollectionLiterals.<EObject>newArrayList(context)), Scopes.scopeFor(filtered));
    }
    return _xblockexpression;
  }
  
  protected IScope _scopeForEObject(final RowRuleSet rule, final EReference reference) {
    IScope _xblockexpression = null;
    {
      final EObject row = rule.eContainer();
      final List<Trigger> allTrigger = EcoreUtil2.<Trigger>getAllContentsOfType(row, Trigger.class);
      final List<RowSensor> allSensor = EcoreUtil2.<RowSensor>getAllContentsOfType(row, RowSensor.class);
      final List<State> allStates = EcoreUtil2.<State>getAllContentsOfType(row, State.class);
      final List<RowActuator> allActuators = EcoreUtil2.<RowActuator>getAllContentsOfType(row, RowActuator.class);
      System.out.println(allTrigger);
      _xblockexpression = Scopes.scopeFor(allSensor, 
        Scopes.scopeFor(allActuators, 
          Scopes.scopeFor(allTrigger, 
            Scopes.scopeFor(allStates))));
    }
    return _xblockexpression;
  }
  
  protected IScope _scopeForEObject(final GreenhouseRuleSet rule, final EReference reference) {
    IScope _xblockexpression = null;
    {
      final EObject root = EcoreUtil2.getRootContainer(rule);
      final Function1<Trigger, Boolean> _function = (Trigger it) -> {
        EObject _eContainer = it.eContainer().eContainer();
        return Boolean.valueOf((_eContainer instanceof GreenhouseActuator));
      };
      final Iterable<Trigger> allTriggers = IterableExtensions.<Trigger>filter(EcoreUtil2.<Trigger>getAllContentsOfType(root, Trigger.class), _function);
      final List<SettingAction> allSettingValue = EcoreUtil2.<SettingAction>getAllContentsOfType(root, SettingAction.class);
      final List<GreenhouseSensor> allSensor = EcoreUtil2.<GreenhouseSensor>getAllContentsOfType(root, GreenhouseSensor.class);
      final List<State> allStates = EcoreUtil2.<State>getAllContentsOfType(root, State.class);
      final List<GreenhouseActuator> allActuators = EcoreUtil2.<GreenhouseActuator>getAllContentsOfType(root, GreenhouseActuator.class);
      _xblockexpression = Scopes.scopeFor(allSensor, 
        Scopes.scopeFor(allTriggers, 
          Scopes.scopeFor(allActuators, 
            Scopes.scopeFor(allSettingValue, 
              Scopes.scopeFor(allStates)))));
    }
    return _xblockexpression;
  }
  
  public IScope scopeForEObject(final EObject context, final EReference reference) {
    if (context instanceof GreenhouseActuator) {
      return _scopeForEObject((GreenhouseActuator)context, reference);
    } else if (context instanceof GreenhouseRuleSet) {
      return _scopeForEObject((GreenhouseRuleSet)context, reference);
    } else if (context instanceof GreenhouseSensor) {
      return _scopeForEObject((GreenhouseSensor)context, reference);
    } else if (context instanceof RowActuator) {
      return _scopeForEObject((RowActuator)context, reference);
    } else if (context instanceof RowRuleSet) {
      return _scopeForEObject((RowRuleSet)context, reference);
    } else if (context instanceof RowSensor) {
      return _scopeForEObject((RowSensor)context, reference);
    } else if (context instanceof Action) {
      return _scopeForEObject((Action)context, reference);
    } else if (context != null) {
      return _scopeForEObject(context, reference);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(context, reference).toString());
    }
  }
}
