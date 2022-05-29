package dsl.generator

import dsl.greenhouse.Model
import org.eclipse.xtext.EcoreUtil2
import dsl.greenhouse.RowSensor
import dsl.greenhouse.RowActuator
import dsl.greenhouse.GreenhouseActuator
import dsl.greenhouse.GreenhouseSensor
import dsl.greenhouse.Greenhouse
import dsl.greenhouse.Row
import dsl.greenhouse.RowRuleSet
import dsl.greenhouse.GreenhouseRuleSet

import dsl.greenhouse.SettingSensor
import dsl.greenhouse.SettingActuator

class VerificationGenerator {
	
	MathGenerator mathGenerator = new MathGenerator()
	
	def getAllClocks(Model model){
		val root = EcoreUtil2.getRootContainer(model);
        val allSettingSensors = EcoreUtil2.getAllContentsOfType(root, SettingSensor)
    	val allSettingActuators = EcoreUtil2.getAllContentsOfType(root, SettingActuator)
    	
    	'''
    	«FOR settingSensor : allSettingSensors»
    		clock «settingSensor.name»Clock;
    	«ENDFOR»
    	«FOR settingActuator : allSettingActuators»
    		clock «settingActuator.name»Clock;
    	«ENDFOR»
    	'''
	}
	
	def getAllVariables(Model model){
		val root = EcoreUtil2.getRootContainer(model);
        val allRowSensors = EcoreUtil2.getAllContentsOfType(root, RowSensor)
    	val allGreenhouseSensors = EcoreUtil2.getAllContentsOfType(root, GreenhouseSensor)
    	
    	'''
    	«FOR rowSensor : allRowSensors»
    		«FOR state : rowSensor.states»
    			«IF state.name.contains('optimal')»
    				int «rowSensor.variable.name» := «mathGenerator.computeExpression(state.threshold)»+1;
    			«ENDIF»
    		«ENDFOR»
    	«ENDFOR»
    	
    	«FOR greenhouseSensor : allGreenhouseSensors»
    		«FOR state : greenhouseSensor.states»
    			«IF state.name.contains('optimal')»
					int «greenhouseSensor.variable.name» := «mathGenerator.computeExpression(state.threshold)»+1;
				«ENDIF»
    		«ENDFOR»
    	«ENDFOR»
    	'''
    }	
		
	
	def getTopics(Model model){
		val root = EcoreUtil2.getRootContainer(model);
        val allSensors = EcoreUtil2.getAllContentsOfType(root, RowSensor).filter[it.controller.name == controller.name]
        val allGlobalSensors = EcoreUtil2.getAllContentsOfType(root, GreenhouseSensor).filter[it.controller.name == controller.name]
        return '''

        «IF !allSensors.empty»
        «FOR sensor: allSensors»
        chan «(sensor.eContainer.eContainer as Greenhouse).name»_«(sensor.eContainer as Row).name»_«sensor.name»;
        «ENDFOR»
        «ENDIF»
        «IF !allGlobalSensors.empty»
        «FOR sensor: allGlobalSensors»
        chan «(sensor.eContainer as Greenhouse).name»_«sensor.name»;
        «ENDFOR»
        «ENDIF»
        '''
		
	}
	
	def getAllActuators(Model model){
        val root = EcoreUtil2.getRootContainer(model);
        val allRowActuators = EcoreUtil2.getAllContentsOfType(root, RowActuator);
        val allGreenhouseActuators = EcoreUtil2.getAllContentsOfType(root, GreenhouseActuator)
        val allGreenhouseSensors = EcoreUtil2.getAllContentsOfType(root, GreenhouseSensor);
		val allRowSensors = EcoreUtil2.getAllContentsOfType(root, RowSensor);
        val allRowRules = EcoreUtil2.getAllContentsOfType(root, RowRuleSet);
        val allGreenhouseRules = EcoreUtil2.getAllContentsOfType(root, GreenhouseRuleSet);
        val allSettingActuator = EcoreUtil2.getAllContentsOfType(root, SettingActuator);
        

        
        return '''

        «IF !allRowActuators.isEmpty»
        	«FOR rowActuator : allRowActuators SEPARATOR '\n'»
        		process «rowActuator.name.toUpperCase»(){
        		state
        			«FOR action : rowActuator.action SEPARATOR ',\n'»«action.value.name»«ENDFOR»;
        		init
        			idle;
        		«IF allRowRules.filter[it.actuator.name == rowActuator.name].size > 0»
				trans
					«FOR action1 : rowActuator.action SEPARATOR ','»«FOR action2 : rowActuator.action»«IF action1.value.name.contains('idle') && action1.value.name != action2.value.name»«action1.value.name» -> «action2.value.name»«FOR sensor : allRowSensors»«IF !allRowRules.filter[it.actuator.name ==rowActuator.name].empty»{sync «(sensor.eContainer.eContainer as Greenhouse).name»_«(sensor.eContainer as Row).name»_«sensor.name»?;«FOR settingActuator : allSettingActuator»«IF settingActuator.name == rowActuator.type.name» assign «settingActuator.name»Clock:=0;«ENDIF»«ENDFOR»},«ENDIF»«ENDFOR»
					«ENDIF»
					«IF action1.value != action2.value»«action2.value» -> «action1.value.name»{}«ENDIF»«ENDFOR»«ENDFOR»;
				«ENDIF»
        		}
        	«ENDFOR»
        «ENDIF»
        
        «IF !allGreenhouseActuators.isEmpty»
           «FOR greenhouseActuator : allGreenhouseActuators SEPARATOR '\n'»
                process «greenhouseActuator.name.toUpperCase»(){
                state
                    «FOR action : greenhouseActuator.action SEPARATOR ',\n'»«action.value.name»«ENDFOR»;
                init
                    idle;
                «IF allGreenhouseRules.filter[it.actuator.name == greenhouseActuator.name].size > 0»
                trans
					«FOR action1 : greenhouseActuator.action SEPARATOR ','»«FOR action2 : greenhouseActuator.action»«IF action1.value.name.contains('idle') && action1.value.name != action2.value.name»«action1.value.name» -> «action2.value.name»«FOR sensor : allRowSensors»«IF !allRowRules.filter[it.actuator.name ==greenhouseActuator.name].empty»{sync «(sensor.eContainer.eContainer as Greenhouse).name»_«(sensor.eContainer as Row).name»_«sensor.name»?;«FOR settingActuator : allSettingActuator»«IF settingActuator.name == greenhouseActuator.type.name» assign «settingActuator.name»Clock:=0;«ENDIF»«ENDFOR»},«ENDIF»«ENDFOR»
					«ENDIF»
					«IF action1.value.name != action2.value.name»«action2.value.name» -> «action1.value.name»{}«ENDIF»«ENDFOR»«ENDFOR»;
				«ENDIF»
                }
           «ENDFOR»
        «ENDIF»
        
        '''
    }
    
    def getAllSensors(Model model){
    	val root = EcoreUtil2.getRootContainer(model);
        val allRowSensors = EcoreUtil2.getAllContentsOfType(root, RowSensor);
        val allGreenhouseSensors = EcoreUtil2.getAllContentsOfType(root, GreenhouseSensor);
        val allRowRules = EcoreUtil2.getAllContentsOfType(root, RowRuleSet);
        val allGreenhouseRules = EcoreUtil2.getAllContentsOfType(root, GreenhouseRuleSet);
        val allSettingSensors = EcoreUtil2.getAllContentsOfType(root, SettingSensor);
        
        return '''

        «IF !allRowSensors.isEmpty»
        	«FOR rowSensor : allRowSensors SEPARATOR '\n'»
        		process «rowSensor.name.toUpperCase»(){
        		state
        			«FOR state : rowSensor.states SEPARATOR ',\n'»«state.name»«IF state.threshold !== null»{«rowSensor.variable.name» «state.op» «mathGenerator.computeExpression(state.threshold)»}«ENDIF»«ENDFOR»;
        		init
        			optimal;
                «IF allRowRules.filter[it.sensor.name == rowSensor.name].size > 0»
        		trans
        			«FOR rowRule : allRowRules SEPARATOR ','»«FOR state : rowSensor.states»«IF state.name.contains('optimal')»«state.name» -> «rowRule.state.name»«FOR sensor : allRowSensors»«IF sensor.name == rowSensor.name && rowRule.sensor.name == sensor.name»{sync «(sensor.eContainer.eContainer as Greenhouse).name»_«(sensor.eContainer as Row).name»_«sensor.name»!;«FOR settingSensor : allSettingSensors»«IF settingSensor.name == sensor.type.name» assign «settingSensor.name»Clock:=0;«ENDIF»«ENDFOR»}«ENDIF»,
        			«ENDFOR»«rowRule.state.name» -> «state.name»{}«ENDIF»«ENDFOR»«ENDFOR»;
        		«ENDIF»
        		}
        	«ENDFOR»
        «ENDIF»
        
        «IF !allGreenhouseSensors.isEmpty»
           «FOR greenhouseSensor : allGreenhouseSensors SEPARATOR '\n'»
                process «greenhouseSensor.name.toUpperCase»(){
                state
                    «FOR state : greenhouseSensor.states SEPARATOR ',\n'»«state.name»«IF state.threshold !== null»{«greenhouseSensor.variable.name»«state.op»«mathGenerator.computeExpression(state.threshold)»}«ENDIF»«ENDFOR»;
                init
                    optimal;
                «IF allGreenhouseRules.filter[it.sensor.name == greenhouseSensor.name].size > 0»
                trans
                	«FOR greenhouseRule : allGreenhouseRules SEPARATOR ','»«FOR state : greenhouseSensor.states»«IF state.name.contains('optimal')»
        			«state.name» -> «greenhouseRule.state.name»
        			«FOR sensor : allRowSensors»
        			«IF sensor.name == greenhouseSensor.name»
        			«IF greenhouseRule.sensor.name == sensor.name»{sync «(sensor.eContainer.eContainer as Greenhouse).name»_«(sensor.eContainer as Row).name»_«sensor.name»!;«FOR settingSensor : allSettingSensors»«IF settingSensor.name == sensor.type.name» assign «settingSensor.name»Clock:=0;«ENDIF»«ENDFOR»}«ENDIF»,
        			«ENDIF»
        			«ENDFOR»«greenhouseRule.state.name» -> «state.name»{}«ENDIF»«ENDFOR»«ENDFOR»;
                «ENDIF»
                }
           	«ENDFOR»
        «ENDIF»
        
        '''
    }
    
    def instantiateVerificationModels(Model model){
    	val root = EcoreUtil2.getRootContainer(model);
        val allRowSensors = EcoreUtil2.getAllContentsOfType(root, RowSensor);
        val allGreenhouseSensors = EcoreUtil2.getAllContentsOfType(root, GreenhouseSensor)
        val allRowActuators = EcoreUtil2.getAllContentsOfType(root, RowActuator);
        val allGreenhouseActuators = EcoreUtil2.getAllContentsOfType(root, GreenhouseActuator)
    	
    	'''
    	«IF !allRowSensors.isEmpty»
    	    «FOR rowSensor : allRowSensors»
    	    «rowSensor.name.toLowerCase» := «rowSensor.name.toUpperCase»();
    	    «ENDFOR»
    	«ENDIF»
    	
    	«IF !allGreenhouseSensors.isEmpty»
    	    «FOR greenhouseSensor : allGreenhouseSensors»
    	    «greenhouseSensor.name.toLowerCase» := «greenhouseSensor.name.toUpperCase»();
    	    «ENDFOR»
    	«ENDIF»
    	
    	«IF !allRowActuators.isEmpty»
    	    «FOR rowActuators : allRowActuators»
    	    «rowActuators.name.toLowerCase» := «rowActuators.name.toUpperCase»();
    	    «ENDFOR»
    	«ENDIF»
    	
    	«IF !allGreenhouseActuators.isEmpty»
    	    «FOR greenhouseActuators : allGreenhouseActuators»
    	    «greenhouseActuators.name.toLowerCase» := «greenhouseActuators.name.toUpperCase»();
    	    «ENDFOR»
    	«ENDIF»
    	
    	system «IF !allRowSensors.isEmpty»«FOR rowSensor : allRowSensors SEPARATOR ', '»«rowSensor.name.toLowerCase»«ENDFOR»«ENDIF», «IF !allGreenhouseSensors.isEmpty»«FOR greenhouseSensor : allGreenhouseSensors SEPARATOR ', '»«greenhouseSensor.name.toLowerCase»«ENDFOR»«ENDIF», «IF !allRowActuators.isEmpty»«FOR rowActuators : allRowActuators SEPARATOR ', '»«rowActuators.name.toLowerCase»«ENDFOR»«ENDIF», «IF !allGreenhouseActuators.isEmpty»«FOR greenhouseActuators : allGreenhouseActuators SEPARATOR ', '»«greenhouseActuators.name.toLowerCase»«ENDFOR»«ENDIF»;
    	'''
    }
    
    
	
	
	
	def compileVerification(Model model)
	'''
	«model.getAllClocks»
	«model.getAllVariables»
	«model.getTopics»
	«model.getAllActuators»
	«model.getAllSensors»
	«model.instantiateVerificationModels»
	'''
	
	def int getFreqUnitTime(String unit){
		switch unit{
			case 'second': return 1000
			case 'minute': return 60*1000
			case 'hour': return 60*60*1000
			case 'day': return 24*60*60*1000
			case 'week': return 7*24*60*60*1000
			default: return 1000
		}	
	}
	
}