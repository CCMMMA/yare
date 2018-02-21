package it.uniparthenope.ccmmma.yare;




import java.util.UUID;

import mjson.Json;

/**
 * Created by raffaelemontella on 24/01/2017.
 */

public class Event {
    public static final String LOG_TAG="RULE_EVENT";
    private Engine engine;
    private UUID uuid= UUID.randomUUID();
    private String name;
    private String desc;
    private String type;
    private Json params=Json.object();


    public Event() {
        init();
    }
    public Event(String name, String desc) {
        this.name=name;
        this.desc=desc;
        init();
    }

    public Event(Json json) {
        init();
        byJson(json);
        LoggerUtils.debug(LOG_TAG,"name:"+name);
    }

    public String getName() { return name; }
    public String getDesc() { return desc; }
    public String getType() { return type; }
    public String getParamAsString(String key) { return params.at(key,"").asString().replace("\"",""); }
    public Double getParamAsDouble(String key) { return params.at(key).asDouble(); }
    public Integer getParamAsInteger(String key) { return params.at(key).asInteger(); }
    public Long getParamAsLong(String key) { return params.at(key).asLong(); }


    private void init() {

    }

    public void byJson(Json json) {
        try {
            uuid = UUID.fromString(json.at("uuid", UUID.randomUUID().toString()).asString().replace("\"", ""));
        } catch (IllegalArgumentException ex) {

        }
        name=json.at("name","").asString().replace("\"","");
        desc=json.at("desc","").asString().replace("\"","");
        type=json.at("type","").asString().replace("\"","");
        params=json.at("params",null);
        Json jsonParams=json.at("params",null);
    }

    public Json asJson() {
        Json json=Json.object();
        json.set("uuid",uuid.toString());
        json.set("name",name);
        json.set("desc",desc);
        json.set("type",type);
        json.set("params",params);
        return json;
    }

    public UUID getUuid() {return uuid;}
}
