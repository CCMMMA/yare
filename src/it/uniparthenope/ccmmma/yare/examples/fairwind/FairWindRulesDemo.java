package it.uniparthenope.ccmmma.yare.examples.fairwind;

import it.uniparthenope.ccmmma.yare.Engine;
import it.uniparthenope.ccmmma.yare.Event;
import it.uniparthenope.ccmmma.yare.FactAdapter;
import it.uniparthenope.ccmmma.yare.LoggerUtils;
import mjson.Json;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.Vector;

public class FairWindRulesDemo implements FactAdapter {
    public static final String LOG_TAG="FairWindRulesDemo";

    private HashMap<String, Object> data;

    public static void main(String[] args) throws Exception {
        ConsoleAppender console = new ConsoleAppender(); //create appender
        //configure the appender
        String PATTERN = "%d [%p|%c|%C{1}] %m%n";
        console.setLayout(new PatternLayout(PATTERN));
        console.setThreshold(Level.DEBUG);
        console.activateOptions();
        Logger.getRootLogger().addAppender(console);

        FairWindRulesDemo app=new FairWindRulesDemo();
        app.run();
    }



    public void run() throws IOException {
        data= new HashMap<String, Object>();
        data.put("/vessels/self/navigation/speedThroughWater/value",5.1);
        data.put("/vessels/self/environment/depth/belowTransducer/value",-2.5);

        Json rules=Json.read(FairWindRulesDemo.getRules());
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
                "    \"name\":\"Shallow water 1\",\n" +
                "    \"desc\":\"Set the alarm on if the speed is greater than 5knt and the depth is less than 4.5m\",\n" +
                "    \"conditions\":{\"all\": [\n" +
                "      {\n" +
                "        \"fact\": \"/vessels/self/navigation/speedThroughWater/value\",\n" +
                "        \"operator\": \"greaterThan\",\n" +
                "        \"value\": 5,\n" +
                "        \"scales\": 1.0,\n" +
                "        \"unit\": \"knot\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"fact\": \"/vessels/self/environment/depth/belowTransducer/value\",\n" +
                "        \"operator\": \"greaterThan\",\n" +
                "        \"value\": -4.5,\n" +
                "        \"scale\": 1.0,\n" +
                "        \"units\": \"m\"\n" +
                "      }\n" +
                "\n" +
                "    ]\n" +
                "    },\n" +
                "    \"events\":[{\n" +
                "      \"uuid\": \"1d21a07c-e270-11e6-bf01-fe55135034f3\",\n" +
                "      \"name\":\"Shallow water\",\n" +
                "      \"desc\":\"Activate an alert if the depth is lower than 4.5 meters and the speed is higher than 5 knt\",\n" +
                "      \"type\":\"popup\",\n" +
                "      \"params\":{\n" +
                "        \"span\": 30000,\n" +
                "        \"timeout\": 15000,\n" +
                "        \"title\": \"Shallow water!\",\n" +
                "        \"message\":\"You are in a shallow water area, reduce your speed to 5knt or less.\",\n" +
                "        \"alarm\": true\n" +
                "      }\n" +
                "    }]\n" +
                "  },\n" +
                "  \"b5660928-e24c-11e6-bf01-fe55135034f3\": {\n" +
                "    \"uuid\":\"b5660928-e24c-11e6-bf01-fe55135034f3\",\n" +
                "    \"name\":\"Heavy wind 1\",\n" +
                "    \"desc\":\"Set the alarm on if the speed of the wind is more than 20knts from NE\",\n" +
                "    \"conditions\":{\"all\": [\n" +
                "      {\n" +
                "        \"fact\": \"/vessels/self/environment/wind/directionTrue/value\",\n" +
                "        \"operator\": \"greaterThanInclusive\",\n" +
                "        \"value\": 35,\n" +
                "        \"units\": \"deg\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"fact\": \"/vessels/self/environment/wind/directionTrue/value\",\n" +
                "        \"operator\": \"lessThanInclusive\",\n" +
                "        \"value\": 55,\n" +
                "        \"units\": \"deg\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"fact\": \"/vessels/self/environment/wind/speedTrue/value\",\n" +
                "        \"operator\": \"greaterThanInclusive\",\n" +
                "        \"value\": 20.0,\n" +
                "        \"units\": \"knot\"\n" +
                "      }\n" +
                "\n" +
                "    ]\n" +
                "    },\n" +
                "    \"events\":[{\n" +
                "      \"uuid\": \"ee91264c-e26f-11e6-bf01-fe55135034f3\",\n" +
                "      \"name\": \"Heavy Winds\",\n" +
                "      \"desc\": \"Activate an alert if the winds are strong blowing in the NE sector\",\n" +
                "      \"type\":\"popup\",\n" +
                "      \"params\":{\n" +
                "        \"span\": 30000,\n" +
                "        \"timeout\": 15000,\n" +
                "        \"title\": \"Heavy winds\",\n" +
                "        \"message\":\"Heavy winds from NE!\",\n" +
                "        \"alarm\": true\n" +
                "      }\n" +
                "    }]\n" +
                "  }\n" +
                "}";
    }
}
