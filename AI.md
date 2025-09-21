# AI-Assisted code generation 

## added javadoc comments 
### in sigmaBot package:
- Parser.java
- SigmaBot.java
- Task.java
- TaskStub.java
### in UI componenets:
- DialogBox.java

## general prompts
- remove dead code or commented code out from any part of the code base
- help to modularize methods in Parser.java to reduce method length 
- help move related methods to be next to each other in the file for Parser.java.
- help rename all occurances of duke and Duke to sigmaBot and SigmaBot for the JavaFX related classes
- generate Regex for DeadlineTask and EventTask
- generate README.md as per github official specifications
- generate JUnit testcases for Storage.java
- generate SigmaBotReadSaveException to handle malformed lines when loading saves