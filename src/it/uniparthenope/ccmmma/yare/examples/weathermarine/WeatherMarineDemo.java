package it.uniparthenope.ccmmma.yare.examples.weathermarine;

import it.uniparthenope.ccmmma.yare.Engine;
import it.uniparthenope.ccmmma.yare.Event;
import it.uniparthenope.ccmmma.yare.FactAdapter;
import it.uniparthenope.ccmmma.yare.LoggerUtils;
import mjson.Json;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public class WeatherMarineDemo implements FactAdapter {
    public static final String LOG_TAG="WeatherMarineDemo";

    private HashMap<String, Object> data;

    public static void main(String[] args) throws Exception {
        ConsoleAppender console = new ConsoleAppender(); //create appender
        //configure the appender
        String PATTERN = "%d [%p|%c|%C{1}] %m%n";
        console.setLayout(new PatternLayout(PATTERN));
        console.setThreshold(Level.DEBUG);
        console.activateOptions();
        Logger.getRootLogger().addAppender(console);

        WeatherMarineDemo app=new WeatherMarineDemo();
        app.run();
    }

    public void run() throws IOException {
        data= new HashMap<String, Object>();

        data.put("/places/VET001/conc/10m/value",662);
        data.put("/places/VET002/conc/10m/value",521);
        data.put("/places/VET003/conc/10m/value",211);
        data.put("/places/VET004/conc/10m/value",32);
        data.put("/places/VET005/conc/10m/value",797);

        Json rules=Json.read(WeatherMarineDemo.getRules());
        Engine engine=new Engine(this, rules);
        LoggerUtils.info("Engine started...");
        engine.run().then();
        LoggerUtils.info("...engine ended.");

    }

    @Override
    public Object onGetFact(String key) {
        LoggerUtils.debug(LOG_TAG,"Key="+key);
        Object obj=data.get(key);
        return obj;
    }

    @Override
    public void onEvents(Vector<Event> events) {
        LoggerUtils.debug(LOG_TAG,"onEvent!");
        for(Event event:events) {
            LoggerUtils.info(event.getName()+":"+event.getDesc());
        }
    }

    @Override
    public double onGetUnitsConvertedValue(double value, String key, String srcUnits) {
        return value;
    }

    private static String getRules() {
        return "{\n" +
                "  \"930d8838-ddba-11e6-bf26-cec0c932ce01\": {\n" +
                "    \"uuid\":\"930d8838-ddba-11e6-bf26-cec0c932ce01\",\n" +
                "    \"name\":\"Concentration alert\",\n" +
                "    \"desc\":\"Set the alarm on if the concentration overcame a treshold.\",\n" +
                "    \"conditions\":{\"all\": [\n" +
                "      {\n" +
                "        \"fact\": \"/places/VET001/conc/10m/value\",\n" +
                "        \"operator\": \"greaterThan\",\n" +
                "        \"value\": 500,\n" +
                "        \"scales\": 1.0,\n" +
                "        \"unit\": \"number\"\n" +
                "      }\n" +
                "    ]\n" +
                "    },\n" +
                "    \"events\":[{\n" +
                "      \"uuid\": \"1d21a07c-e270-11e6-bf01-fe55135034f3\",\n" +
                "      \"name\":\"High concentration\",\n" +
                "      \"desc\":\"Activate if concentration overcame the treshold\",\n" +
                "      \"type\":\"email\",\n" +
                "      \"params\":{\n" +
                "        \"to\": \"raffaele.montella@uniparthenope.it\",\n" +
                "        \"subject\": \"Concentration alert!\",\n" +
                "        \"message\":\"Check the area %s at the time %s\"\n" +
                "      }\n" +
                "    }]\n" +
                "  }\n" +
                "}";
    }
}
