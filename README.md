# FromMdbToCsv
Convert Microsoft Access data to CSV in Leva style

## Description
Transforms data from a Microsoft Access database into a CSV file. Details such as the input and output file names and paths are stored in a configuration file.

## Prerequisites

### Development
* Java Development Kit
* Maven

### Runtime
* Java Runtime Environment

## Build
Run:

```bash
mvn install
```

The **target** folder will contain the files **fromMdbToCsv-0.5.jar** and **fromMdbToCsv-0.5-jar-with-dependencies.jar**, where **0.5** is the version number.

## Execution
Run the JAR with:

```bash
java -jar fromMdbToCsv-0.5-jar-with-dependencies.jar
```

Be sure to fill out the **app.config** file and place it in the same directory as the JAR.
