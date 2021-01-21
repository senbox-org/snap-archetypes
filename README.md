SNAP Maven Archetypes
==========================

A project gathering maven archetypes useful in creating new SNAP modules for readers and operators.

The project page of SNAP, and the Sentinel toolboxes can be found at http://step.esa.int.
There you can find tutorials, developer guides, a user forum and other interesting things.


Building SNAP Archetypes from the source
------------------------------

The following gives a brief introduction how to build the SNAP Archetypes.
More information can be found in the [Developer Guide](https://senbox.atlassian.net/wiki/display/SNAP/Developer+Guide).


Download and install the required build tools

* Install Java 8 JDK and set JAVA_HOME accordingly. A distribution of OpenJDK is suggested. 
Several distributions are available, for example
  * [Azul Zulu](https://www.azul.com/downloads/zulu-community)  
  * [AdoptOpenJDK](https://adoptopenjdk.net)   
  * [Amazon Corretto](https://aws.amazon.com/de/corretto)	  
* Install Maven and set MAVEN_HOME accordingly. 
* Install git

Add $JAVA_HOME/bin, $MAVEN_HOME/bin to your PATH.

Clone the SNAP Archetypes source code into a local directory referred to a ${snap-archetypes} from here on

    cd ${snap-archetypes}
    git clone https://github.com/senbox-org/snap-archetypes.git
    
Build SNAP-Archetypes:

    mvn clean install


Setting up IntelliJ IDEA
------------------------

1. Create a new project with the option "Project from existing sources", by choosing the pom.xml from ${snap-archetypes}

2. Set the used JDK for the project.


Usage of SNAP Archetypes
------------------------
After making sure that snap-archetypes are successfully build, these can be used when creating new SNAP modules.

Assuming that snap-engine, snap-desktop and the preferred toolboxes (s1tbx, s2tbx, s3tbx) are locally cloned and build under ${snap} directory,
one can chose a project (among the above enumerated) where a new module using a SNAP archetypes will be added.

From IntelliJ, being positioned on the parent project where a new module will be added, the steps are the following:
- File -> New Module
- check the "Create from Archetype" checkbox
- first register the needed SNAP archetype by Add Archetype button (make sure to write here the details that are in the specific pom.xml of the chosen achetype (found under ${snap-archetypes})
- select the newly added archetype from the list, as the archetype to be used for the new module


Enjoy developing!


