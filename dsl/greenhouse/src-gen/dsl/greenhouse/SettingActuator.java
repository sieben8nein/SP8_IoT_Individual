/**
 * generated by Xtext 2.25.0
 */
package dsl.greenhouse;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Setting Actuator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link dsl.greenhouse.SettingActuator#getSettingAction <em>Setting Action</em>}</li>
 * </ul>
 *
 * @see dsl.greenhouse.GreenhousePackage#getSettingActuator()
 * @model
 * @generated
 */
public interface SettingActuator extends Hardware
{
  /**
   * Returns the value of the '<em><b>Setting Action</b></em>' containment reference list.
   * The list contents are of type {@link dsl.greenhouse.SettingAction}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Setting Action</em>' containment reference list.
   * @see dsl.greenhouse.GreenhousePackage#getSettingActuator_SettingAction()
   * @model containment="true"
   * @generated
   */
  EList<SettingAction> getSettingAction();

} // SettingActuator
