/*
* Copyright 2013 JUGChennai
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package in.jugchennai.forge.android;

import java.io.InputStream;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.forge.project.Project;
import org.jboss.forge.project.facets.DependencyFacet;
import org.jboss.forge.project.facets.JavaSourceFacet;
import org.jboss.forge.project.facets.events.InstallFacets;
import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.resources.FileResource;
import org.jboss.forge.shell.ShellColor;
import org.jboss.forge.shell.ShellPrintWriter;
import org.jboss.forge.shell.ShellPrompt;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.Command;
import org.jboss.forge.shell.plugins.DefaultCommand;
import org.jboss.forge.shell.plugins.Help;
import org.jboss.forge.shell.plugins.Option;
import org.jboss.forge.shell.plugins.PipeOut;
import org.jboss.forge.shell.plugins.Plugin;
import org.jboss.forge.shell.plugins.RequiresFacet;
import org.jboss.forge.shell.plugins.RequiresProject;
import org.jboss.forge.shell.plugins.SetupCommand;

/**
 * 
 * <b>Contributors</b>
 * 
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 *
 */

@Alias("android")
@Help("A Forge plugin to enable and work on Android development.")
@RequiresFacet({DependencyFacet.class, JavaSourceFacet.class})
@RequiresProject
public class AndroidPlugin implements Plugin  {
	
    /** The shell. */
    @Inject
    private ShellPrompt shell;

    /** The project. */
    @Inject
    private Project project;

    /** The install. */
    @Inject
    private Event<InstallFacets> install;

    /** The writer. */
    @Inject
    private ShellPrintWriter writer;
	
    /**
     * The setup command for Android. This adds dependency to the current project
     * 
     * @param out the out
     * @param moduleName the module name
     */
	@SetupCommand(help = "Installs basic setup to work with Android application.")
	 public void setup(final PipeOut out) {
		//, @Option(name = "module", shortName = "m", required = true, help = "The Module name to be installed.") final String moduleName
		if (!this.project.hasFacet(AndroidFacet.class)) {
		    this.install.fire(new InstallFacets(AndroidFacet.class));
		}
		DirectoryResource projectRoot = this.project.getProjectRoot();
		projectRoot.getOrCreateChildDirectory("assets");
		DirectoryResource resDirectory = projectRoot.getOrCreateChildDirectory("res");
		
		// res inner directories
		DirectoryResource layoutDirectory = resDirectory.getOrCreateChildDirectory("layout");
		resDirectory.getOrCreateChildDirectory("drawable-hdpi");
		resDirectory.getOrCreateChildDirectory("drawable-ldpi");
		resDirectory.getOrCreateChildDirectory("drawable-mdpi");
		DirectoryResource valuesDirectory = resDirectory.getOrCreateChildDirectory("values");
		
		// manifest and default.properties file
		InputStream stream = null;
		FileResource<?> manifestFile = (FileResource<?>) projectRoot.getChild("AndroidManifest.xml");
		if (!manifestFile.exists()) {
			stream = AndroidPlugin.class.getResourceAsStream("/templates/TemplateManifest.ftl");
			manifestFile.setContents(stream);
		}
		
		FileResource<?> defaultPropFile = (FileResource<?>) projectRoot.getChild("default.properties");
		if (!defaultPropFile.exists()) {
			stream = AndroidPlugin.class.getResourceAsStream("/templates/TemplateProperties.ftl");
			defaultPropFile.setContents(stream);
		}
		
		FileResource<?> layoutMainFile = (FileResource<?>) layoutDirectory.getChild("main.xml");
		if (!layoutMainFile.exists()) {
			stream = AndroidPlugin.class.getResourceAsStream("/templates/TemplateLayoutMain.ftl");
			layoutMainFile.setContents(stream);	
		}
		
		FileResource<?> valuesStringsFile = (FileResource<?>) valuesDirectory.getChild("strings.xml");
		if (!valuesStringsFile.exists()) {
			stream = AndroidPlugin.class.getResourceAsStream("/templates/TemplateStrings.ftl");
			valuesStringsFile.setContents(stream);
		}
		
		if (this.project.hasFacet(AndroidFacet.class)) {
		    this.writer.println(ShellColor.GREEN, "Android is configured.");
		}
	}
	
    /**
     * If android command is not executed with any argument this method will be called.
     * 
     * @param out the out
     */
	@DefaultCommand
	private void defaultCommand(final PipeOut out) {
        if (this.project.hasFacet(AndroidFacet.class)) {
            out.println("Android is installed.");
        } else {
            out.println("Android is not installed. Use 'android setup' to install.");
        }
	}
	
	
    /**
     * Creates the activity.
     * 
     * @param out the out
     * @param name the activity name
     */
    @Command(value = "activity-create", help = "Create a activity for the given name")
    public void createActivity(
            final PipeOut out,
            @Option(name = "name", shortName = "n", required = true, help = "Name of the activity to be created.")
            final String name) {
    }
    
    /**
     * Creates the android project.
     * 
     * @param out the out
     * @param name the project name
     */
    @Command(value = "project-create", help = "Create a android application with the given name")
    public void createProject(
            final PipeOut out,
            @Option(name = "name", shortName = "n", required = true, help = "Name of the application to be created.")
            final String name) {
    }
    
    /**
     * Change the emulator.
     * 
     * @param out the out
     * @param number the emulator number
     */
    @Command(value = "change-emulator", help = "Change the emulator")
    public void changeEmulator(
            final PipeOut out,
            @Option(name = "number", shortName = "n", required = true, help = "Emulator number.")
            final String number) {
    }
}
