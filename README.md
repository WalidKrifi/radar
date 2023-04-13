### Introdcution
Radar is a java agent, its objective is to instrument the code to report execution metrics.
Based on his pre-main Radar will inject byte-code when loading classes.
### Getting started
- Build Radar localy with maven :
> mvn clean install
- Instrument your JVM
>javaagent:/CUSTOM_PATH/radar/radar-agent/target/radar-agent-[version]-jar-with-dependencies.jar
- Customize your config
>-Dradar.performance.active=true -Dradar.performance.classes=org/radar/agent/.*
> 
| Agent config     | Values            | Description                     |
|------------------|-------------------|---------------------------------|
| radar.performance.active | `'true or false'` | activate the performance plugin |
### Sample output
<code>
org.radar.agent.performance.DetectLeakPerformance.doConnectWithoutParam(int,int) : 8ms
org.radar.agent.performance.DetectLeakPerformance.doConnectWithoutParam(int,int,int) : 1ms
org.radar.agent.performance.DetectLeakPerformance.doConnectWithoutParam() : 0ms
org.radar.agent.Main.main(java.lang.String[]) : 14ms
</code>