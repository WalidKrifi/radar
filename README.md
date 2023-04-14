### Introduction
Radar is a lightweight java agent with objective is to instrument the code to report execution metrics.
The project is always under building and many features ar coming soon.
### Getting started
- Build Radar locally with maven :
> mvn clean install
- Instrument your JVM by adding the agent to your java command
> -javaagent:/[CUSTOM_PATH]/radar/radar-agent/target/radar-agent-[version]-jar-with-dependencies.jar
- Customize your config
> -Dradar.performance.active=true -Dradar.performance.classes=org/radar/agent/.*

| Agent config              | Values                         | Description                                     |
|---------------------------|--------------------------------|-------------------------------------------------|
| radar.performance.active  | `'true or false'`              | activate the performance plugin                 |
| radar.performance.classes | `'Array of String sep with ;'` | RegExp to identifie which classes to instrument |

### Sample output
<code>
    org.radar.agent.performance.DetectLeakPerformance.doConnectWithoutParam(int,int) : 8ms
    org.radar.agent.performance.DetectLeakPerformance.doConnectWithoutParam(int,int,int) : 1ms
    org.radar.agent.performance.DetectLeakPerformance.doConnectWithoutParam() : 0ms
    org.radar.agent.Main.main(java.lang.String[]) : 14ms
</code>
