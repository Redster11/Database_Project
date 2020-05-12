This Program is using ucanaccess make sure that the .jar files are added to the project. 
This can be done in Eclipes by:
1. right clicking on the Eclipes project
2. going to the "Java Build Path" 
3. then Clicking on the "Libraries" Tab and then Clicking on the "Add External JARs..."
4. add the jars need from ucanaccess: 
	jackcess.jar
	ucanaccess.jar
	hsqldb.jar
	commons-lang.jar
	commons-logging.jar
These can be downloaded on ucanaccess Site https://sourceforge.net/projects/ucanaccess/ 


When trying to use this code make sure that in Main the file path to the String databaseURL is pointing to the Access Database file.
 