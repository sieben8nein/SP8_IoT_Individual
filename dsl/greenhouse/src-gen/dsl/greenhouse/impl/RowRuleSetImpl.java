/**
 * generated by Xtext 2.25.0
 */
package dsl.greenhouse.impl;

import dsl.greenhouse.GreenhousePackage;
import dsl.greenhouse.RowActuator;
import dsl.greenhouse.RowRuleSet;
import dsl.greenhouse.RowSensor;
import dsl.greenhouse.State;
import dsl.greenhouse.Trigger;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Row Rule Set</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link dsl.greenhouse.impl.RowRuleSetImpl#getActuator <em>Actuator</em>}</li>
 *   <li>{@link dsl.greenhouse.impl.RowRuleSetImpl#getTrigger <em>Trigger</em>}</li>
 *   <li>{@link dsl.greenhouse.impl.RowRuleSetImpl#getSensor <em>Sensor</em>}</li>
 *   <li>{@link dsl.greenhouse.impl.RowRuleSetImpl#getState <em>State</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RowRuleSetImpl extends RowElementImpl implements RowRuleSet
{
  /**
   * The cached value of the '{@link #getActuator() <em>Actuator</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getActuator()
   * @generated
   * @ordered
   */
  protected RowActuator actuator;

  /**
   * The cached value of the '{@link #getTrigger() <em>Trigger</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTrigger()
   * @generated
   * @ordered
   */
  protected Trigger trigger;

  /**
   * The cached value of the '{@link #getSensor() <em>Sensor</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSensor()
   * @generated
   * @ordered
   */
  protected RowSensor sensor;

  /**
   * The cached value of the '{@link #getState() <em>State</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getState()
   * @generated
   * @ordered
   */
  protected State state;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RowRuleSetImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return GreenhousePackage.Literals.ROW_RULE_SET;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public RowActuator getActuator()
  {
    if (actuator != null && actuator.eIsProxy())
    {
      InternalEObject oldActuator = (InternalEObject)actuator;
      actuator = (RowActuator)eResolveProxy(oldActuator);
      if (actuator != oldActuator)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, GreenhousePackage.ROW_RULE_SET__ACTUATOR, oldActuator, actuator));
      }
    }
    return actuator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RowActuator basicGetActuator()
  {
    return actuator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setActuator(RowActuator newActuator)
  {
    RowActuator oldActuator = actuator;
    actuator = newActuator;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, GreenhousePackage.ROW_RULE_SET__ACTUATOR, oldActuator, actuator));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Trigger getTrigger()
  {
    if (trigger != null && trigger.eIsProxy())
    {
      InternalEObject oldTrigger = (InternalEObject)trigger;
      trigger = (Trigger)eResolveProxy(oldTrigger);
      if (trigger != oldTrigger)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, GreenhousePackage.ROW_RULE_SET__TRIGGER, oldTrigger, trigger));
      }
    }
    return trigger;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Trigger basicGetTrigger()
  {
    return trigger;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setTrigger(Trigger newTrigger)
  {
    Trigger oldTrigger = trigger;
    trigger = newTrigger;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, GreenhousePackage.ROW_RULE_SET__TRIGGER, oldTrigger, trigger));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public RowSensor getSensor()
  {
    if (sensor != null && sensor.eIsProxy())
    {
      InternalEObject oldSensor = (InternalEObject)sensor;
      sensor = (RowSensor)eResolveProxy(oldSensor);
      if (sensor != oldSensor)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, GreenhousePackage.ROW_RULE_SET__SENSOR, oldSensor, sensor));
      }
    }
    return sensor;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RowSensor basicGetSensor()
  {
    return sensor;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setSensor(RowSensor newSensor)
  {
    RowSensor oldSensor = sensor;
    sensor = newSensor;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, GreenhousePackage.ROW_RULE_SET__SENSOR, oldSensor, sensor));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public State getState()
  {
    if (state != null && state.eIsProxy())
    {
      InternalEObject oldState = (InternalEObject)state;
      state = (State)eResolveProxy(oldState);
      if (state != oldState)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, GreenhousePackage.ROW_RULE_SET__STATE, oldState, state));
      }
    }
    return state;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public State basicGetState()
  {
    return state;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setState(State newState)
  {
    State oldState = state;
    state = newState;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, GreenhousePackage.ROW_RULE_SET__STATE, oldState, state));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case GreenhousePackage.ROW_RULE_SET__ACTUATOR:
        if (resolve) return getActuator();
        return basicGetActuator();
      case GreenhousePackage.ROW_RULE_SET__TRIGGER:
        if (resolve) return getTrigger();
        return basicGetTrigger();
      case GreenhousePackage.ROW_RULE_SET__SENSOR:
        if (resolve) return getSensor();
        return basicGetSensor();
      case GreenhousePackage.ROW_RULE_SET__STATE:
        if (resolve) return getState();
        return basicGetState();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case GreenhousePackage.ROW_RULE_SET__ACTUATOR:
        setActuator((RowActuator)newValue);
        return;
      case GreenhousePackage.ROW_RULE_SET__TRIGGER:
        setTrigger((Trigger)newValue);
        return;
      case GreenhousePackage.ROW_RULE_SET__SENSOR:
        setSensor((RowSensor)newValue);
        return;
      case GreenhousePackage.ROW_RULE_SET__STATE:
        setState((State)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case GreenhousePackage.ROW_RULE_SET__ACTUATOR:
        setActuator((RowActuator)null);
        return;
      case GreenhousePackage.ROW_RULE_SET__TRIGGER:
        setTrigger((Trigger)null);
        return;
      case GreenhousePackage.ROW_RULE_SET__SENSOR:
        setSensor((RowSensor)null);
        return;
      case GreenhousePackage.ROW_RULE_SET__STATE:
        setState((State)null);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case GreenhousePackage.ROW_RULE_SET__ACTUATOR:
        return actuator != null;
      case GreenhousePackage.ROW_RULE_SET__TRIGGER:
        return trigger != null;
      case GreenhousePackage.ROW_RULE_SET__SENSOR:
        return sensor != null;
      case GreenhousePackage.ROW_RULE_SET__STATE:
        return state != null;
    }
    return super.eIsSet(featureID);
  }

} //RowRuleSetImpl
