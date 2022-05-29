/*
 * generated by Xtext 2.25.0
 */
package dsl.scoping

import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.scoping.Scopes
import org.eclipse.xtext.EcoreUtil2
import dsl.greenhouse.Action
import dsl.greenhouse.State
import dsl.greenhouse.RowSensor
import dsl.greenhouse.RowActuator
import dsl.greenhouse.RowRuleSet
import dsl.greenhouse.SettingSensor
import dsl.greenhouse.SettingActuator
import dsl.greenhouse.Trigger
import dsl.greenhouse.SettingAction
import dsl.greenhouse.GreenhouseActuator
import dsl.greenhouse.GreenhouseSensor
/**
 * This class contains custom scoping description.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 */

import dsl.greenhouse.GreenhouseRuleSet
import dsl.greenhouse.Controller
import java.util.List
import java.util.ArrayList

/**
 * This class contains custom scoping description.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 */
class GreenhouseScopeProvider extends AbstractGreenhouseScopeProvider {
	
	
	override getScope(EObject context, EReference reference) {
		return context.scopeForEObject(reference)
	}
	
	def dispatch scopeForEObject(EObject context, EReference reference) {
		return super.getScope(context.eContainer, reference)
	}
	
	def dispatch scopeForEObject(RowActuator context, EReference reference) {
		
		
		val root = EcoreUtil2.getRootContainer(context);
		val allControllers = EcoreUtil2.getAllContentsOfType(root, Controller)
		val allActuators = EcoreUtil2.getAllContentsOfType(root, SettingActuator)
		val allSettingActions = EcoreUtil2.getAllContentsOfType(root, SettingAction)
		Scopes.scopeFor(#[context], Scopes.scopeFor(allActuators, Scopes.scopeFor(allControllers, Scopes.scopeFor(allSettingActions))))
	}
	
	def dispatch scopeForEObject(GreenhouseActuator context, EReference reference) {
		
		
		val root = EcoreUtil2.getRootContainer(context);
		
		val allActuators = EcoreUtil2.getAllContentsOfType(root, SettingActuator)
		val allControllers = EcoreUtil2.getAllContentsOfType(root, Controller)
		val allSettingActions = EcoreUtil2.getAllContentsOfType(root, SettingAction)
		
		System.out.println(allActuators)
		Scopes.scopeFor(#[context], Scopes.scopeFor(allActuators, Scopes.scopeFor(allControllers, Scopes.scopeFor(allSettingActions))))
	}
	
	def dispatch scopeForEObject(RowSensor context, EReference reference) {
		
		
		val root = EcoreUtil2.getRootContainer(context);
		
		val allSensors = EcoreUtil2.getAllContentsOfType(root, SettingSensor)
		val allControllers = EcoreUtil2.getAllContentsOfType(root, Controller)
		System.out.println(allSensors)
		Scopes.scopeFor(#[context], Scopes.scopeFor(allSensors, Scopes.scopeFor(allControllers)))
	}
	
	def dispatch scopeForEObject(GreenhouseSensor context, EReference reference) {
		
		
		val root = EcoreUtil2.getRootContainer(context);
		
		val allSensors = EcoreUtil2.getAllContentsOfType(root, SettingSensor)
		val allControllers = EcoreUtil2.getAllContentsOfType(root, Controller)
		System.out.println(allSensors)
		Scopes.scopeFor(#[context], Scopes.scopeFor(allSensors, Scopes.scopeFor(allControllers)))
	}
	
	def dispatch scopeForEObject(Action context, EReference reference) {
		
		
		val root = EcoreUtil2.getRootContainer(context);
		
		val allValues = EcoreUtil2.getAllContentsOfType(root, SettingAction)
		
		
		val filtered = allValues.filter[
			switch(context.eContainer){
				RowActuator: (context.eContainer as RowActuator).type == (it.eContainer as SettingActuator)
				GreenhouseActuator: (context.eContainer as GreenhouseActuator).type == (it.eContainer as SettingActuator) 
				default: false
		}]
		Scopes.scopeFor(#[context], Scopes.scopeFor(filtered))
	}
	
	def dispatch scopeForEObject(RowRuleSet rule, EReference reference) {	
		
		val row = rule.eContainer
		val allTrigger = EcoreUtil2.getAllContentsOfType(row, Trigger)
		val allSensor = EcoreUtil2.getAllContentsOfType(row, RowSensor)
		val allStates = EcoreUtil2.getAllContentsOfType(row, State)
		val allActuators = EcoreUtil2.getAllContentsOfType(row, RowActuator)
		
		System.out.println(allTrigger)
		Scopes.scopeFor(
			allSensor, 
			Scopes.scopeFor(
				allActuators,
				Scopes.scopeFor(
					allTrigger, 
					Scopes.scopeFor(
						allStates
					)
				)
			)
		)
	}
	
	
	
	def dispatch scopeForEObject(GreenhouseRuleSet rule, EReference reference) {	
		
        val root = EcoreUtil2.getRootContainer(rule);
        val allTriggers = EcoreUtil2.getAllContentsOfType(root, Trigger).filter([
        	it.eContainer.eContainer instanceof GreenhouseActuator 
        ])
        val allSettingValue = EcoreUtil2.getAllContentsOfType(root, SettingAction)
        val allSensor = EcoreUtil2.getAllContentsOfType(root, GreenhouseSensor)
        val allStates = EcoreUtil2.getAllContentsOfType(root, State)
        val allActuators = EcoreUtil2.getAllContentsOfType(root, GreenhouseActuator)
		//System.out.println(allTriggers)
		
		
        Scopes.scopeFor(
            allSensor, 
            Scopes.scopeFor(
                allTriggers,
                Scopes.scopeFor(
                    allActuators,
                    Scopes.scopeFor(
                        allSettingValue,
                            Scopes.scopeFor(
                                allStates
                        )
                    )
                )
            )
        )
	}
	
		
	


}
