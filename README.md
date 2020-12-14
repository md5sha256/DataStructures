# Compilation

This project utilizes [Apache Maven](https://maven.apache.org/) as it's build tool. To compile the project into a JAR executable, please run `./mvnw package`. The executable can be found under the `target` folder.
<br>

<strong>This project require Java 8 or above</strong>

# Usage
The program will run "as is" by executing the compiled JAR file after compilation. No UI window will be generated (therefore, if you would like to see some sort of an output, please
execute the program from a command line environment); However, a `csv` file of the results will be generated upon the benchmark's completion.
Test parameters are *hardcoded* inside of `Main`, `ArrayBenchmark` and `BaseBenchmark`. 

# Third-party Libraries
The JMH ([Java Micro-Bench Harness](https://github.com/openjdk/jmh)) was used (GNU GPL-v2)
