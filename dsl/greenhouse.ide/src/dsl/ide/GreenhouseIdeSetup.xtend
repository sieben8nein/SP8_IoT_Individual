/*
 * generated by Xtext 2.25.0
 */
package dsl.ide

import com.google.inject.Guice
import dsl.GreenhouseRuntimeModule
import dsl.GreenhouseStandaloneSetup
import org.eclipse.xtext.util.Modules2

/**
 * Initialization support for running Xtext languages as language servers.
 */
class GreenhouseIdeSetup extends GreenhouseStandaloneSetup {

	override createInjector() {
		Guice.createInjector(Modules2.mixin(new GreenhouseRuntimeModule, new GreenhouseIdeModule))
	}
	
}
