/**
 * generated by Xtext 2.25.0
 */
package dsl.greenhouse;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Actuator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link dsl.greenhouse.GreenhouseActuator#getType <em>Type</em>}</li>
 *   <li>{@link dsl.greenhouse.GreenhouseActuator#getName <em>Name</em>}</li>
 *   <li>{@link dsl.greenhouse.GreenhouseActuator#getController <em>Controller</em>}</li>
 *   <li>{@link dsl.greenhouse.GreenhouseActuator#getAction <em>Action</em>}</li>
 *   <li>{@link dsl.greenhouse.GreenhouseActuator#getSafeState <em>Safe State</em>}</li>
 * </ul>
 *
 * @see dsl.greenhouse.GreenhousePackage#getGreenhouseActuator()
 * @model
 * @generated
 */
public interface GreenhouseActuator extends GreenhouseElement
{
  /**
   * Returns the value of the '<em><b>Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' reference.
   * @see #setType(SettingActuator)
   * @see dsl.greenhouse.GreenhousePackage#getGreenhouseActuator_Type()
   * @model
   * @generated
   */
  SettingActuator getType();

  /**
   * Sets the value of the '{@link dsl.greenhouse.GreenhouseActuator#getType <em>Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' reference.
   * @see #getType()
   * @generated
   */
  void setType(SettingActuator value);

  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see dsl.greenhouse.GreenhousePackage#getGreenhouseActuator_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link dsl.greenhouse.GreenhouseActuator#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Controller</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Controller</em>' reference.
   * @see #setController(Controller)
   * @see dsl.greenhouse.GreenhousePackage#getGreenhouseActuator_Controller()
   * @model
   * @generated
   */
  Controller getController();

  /**
   * Sets the value of the '{@link dsl.greenhouse.GreenhouseActuator#getController <em>Controller</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Controller</em>' reference.
   * @see #getController()
   * @generated
   */
  void setController(Controller value);

  /**
   * Returns the value of the '<em><b>Action</b></em>' containment reference list.
   * The list contents are of type {@link dsl.greenhouse.Action}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Action</em>' containment reference list.
   * @see dsl.greenhouse.GreenhousePackage#getGreenhouseActuator_Action()
   * @model containment="true"
   * @generated
   */
  EList<Action> getAction();

  /**
   * Returns the value of the '<em><b>Safe State</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Safe State</em>' reference.
   * @see #setSafeState(SettingAction)
   * @see dsl.greenhouse.GreenhousePackage#getGreenhouseActuator_SafeState()
   * @model
   * @generated
   */
  SettingAction getSafeState();

  /**
   * Sets the value of the '{@link dsl.greenhouse.GreenhouseActuator#getSafeState <em>Safe State</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Safe State</em>' reference.
   * @see #getSafeState()
   * @generated
   */
  void setSafeState(SettingAction value);

} // GreenhouseActuator