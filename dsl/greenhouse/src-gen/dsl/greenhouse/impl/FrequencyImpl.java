/**
 * generated by Xtext 2.25.0
 */
package dsl.greenhouse.impl;

import dsl.greenhouse.Expression;
import dsl.greenhouse.Frequency;
import dsl.greenhouse.GreenhousePackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Frequency</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link dsl.greenhouse.impl.FrequencyImpl#getFreq <em>Freq</em>}</li>
 *   <li>{@link dsl.greenhouse.impl.FrequencyImpl#getUnit <em>Unit</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FrequencyImpl extends MinimalEObjectImpl.Container implements Frequency
{
  /**
   * The cached value of the '{@link #getFreq() <em>Freq</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFreq()
   * @generated
   * @ordered
   */
  protected Expression freq;

  /**
   * The default value of the '{@link #getUnit() <em>Unit</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUnit()
   * @generated
   * @ordered
   */
  protected static final String UNIT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getUnit() <em>Unit</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUnit()
   * @generated
   * @ordered
   */
  protected String unit = UNIT_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected FrequencyImpl()
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
    return GreenhousePackage.Literals.FREQUENCY;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Expression getFreq()
  {
    return freq;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetFreq(Expression newFreq, NotificationChain msgs)
  {
    Expression oldFreq = freq;
    freq = newFreq;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GreenhousePackage.FREQUENCY__FREQ, oldFreq, newFreq);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setFreq(Expression newFreq)
  {
    if (newFreq != freq)
    {
      NotificationChain msgs = null;
      if (freq != null)
        msgs = ((InternalEObject)freq).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - GreenhousePackage.FREQUENCY__FREQ, null, msgs);
      if (newFreq != null)
        msgs = ((InternalEObject)newFreq).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - GreenhousePackage.FREQUENCY__FREQ, null, msgs);
      msgs = basicSetFreq(newFreq, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, GreenhousePackage.FREQUENCY__FREQ, newFreq, newFreq));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getUnit()
  {
    return unit;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setUnit(String newUnit)
  {
    String oldUnit = unit;
    unit = newUnit;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, GreenhousePackage.FREQUENCY__UNIT, oldUnit, unit));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case GreenhousePackage.FREQUENCY__FREQ:
        return basicSetFreq(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
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
      case GreenhousePackage.FREQUENCY__FREQ:
        return getFreq();
      case GreenhousePackage.FREQUENCY__UNIT:
        return getUnit();
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
      case GreenhousePackage.FREQUENCY__FREQ:
        setFreq((Expression)newValue);
        return;
      case GreenhousePackage.FREQUENCY__UNIT:
        setUnit((String)newValue);
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
      case GreenhousePackage.FREQUENCY__FREQ:
        setFreq((Expression)null);
        return;
      case GreenhousePackage.FREQUENCY__UNIT:
        setUnit(UNIT_EDEFAULT);
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
      case GreenhousePackage.FREQUENCY__FREQ:
        return freq != null;
      case GreenhousePackage.FREQUENCY__UNIT:
        return UNIT_EDEFAULT == null ? unit != null : !UNIT_EDEFAULT.equals(unit);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (unit: ");
    result.append(unit);
    result.append(')');
    return result.toString();
  }

} //FrequencyImpl