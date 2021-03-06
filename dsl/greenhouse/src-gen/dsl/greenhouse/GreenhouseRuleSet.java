/**
 * generated by Xtext 2.25.0
 */
package dsl.greenhouse;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Rule Set</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link dsl.greenhouse.GreenhouseRuleSet#getActuator <em>Actuator</em>}</li>
 *   <li>{@link dsl.greenhouse.GreenhouseRuleSet#getTrigger <em>Trigger</em>}</li>
 *   <li>{@link dsl.greenhouse.GreenhouseRuleSet#getSensor <em>Sensor</em>}</li>
 *   <li>{@link dsl.greenhouse.GreenhouseRuleSet#getState <em>State</em>}</li>
 * </ul>
 *
 * @see dsl.greenhouse.GreenhousePackage#getGreenhouseRuleSet()
 * @model
 * @generated
 */
public interface GreenhouseRuleSet extends GreenhouseElement
{
  /**
   * Returns the value of the '<em><b>Actuator</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Actuator</em>' reference.
   * @see #setActuator(GreenhouseActuator)
   * @see dsl.greenhouse.GreenhousePackage#getGreenhouseRuleSet_Actuator()
   * @model
   * @generated
   */
  GreenhouseActuator getActuator();

  /**
   * Sets the value of the '{@link dsl.greenhouse.GreenhouseRuleSet#getActuator <em>Actuator</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Actuator</em>' reference.
   * @see #getActuator()
   * @generated
   */
  void setActuator(GreenhouseActuator value);

  /**
   * Returns the value of the '<em><b>Trigger</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trigger</em>' reference.
   * @see #setTrigger(Trigger)
   * @see dsl.greenhouse.GreenhousePackage#getGreenhouseRuleSet_Trigger()
   * @model
   * @generated
   */
  Trigger getTrigger();

  /**
   * Sets the value of the '{@link dsl.greenhouse.GreenhouseRuleSet#getTrigger <em>Trigger</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger</em>' reference.
   * @see #getTrigger()
   * @generated
   */
  void setTrigger(Trigger value);

  /**
   * Returns the value of the '<em><b>Sensor</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sensor</em>' reference.
   * @see #setSensor(GreenhouseSensor)
   * @see dsl.greenhouse.GreenhousePackage#getGreenhouseRuleSet_Sensor()
   * @model
   * @generated
   */
  GreenhouseSensor getSensor();

  /**
   * Sets the value of the '{@link dsl.greenhouse.GreenhouseRuleSet#getSensor <em>Sensor</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sensor</em>' reference.
   * @see #getSensor()
   * @generated
   */
  void setSensor(GreenhouseSensor value);

  /**
   * Returns the value of the '<em><b>State</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>State</em>' reference.
   * @see #setState(State)
   * @see dsl.greenhouse.GreenhousePackage#getGreenhouseRuleSet_State()
   * @model
   * @generated
   */
  State getState();

  /**
   * Sets the value of the '{@link dsl.greenhouse.GreenhouseRuleSet#getState <em>State</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>State</em>' reference.
   * @see #getState()
   * @generated
   */
  void setState(State value);

} // GreenhouseRuleSet
