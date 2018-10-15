package de.viadee.ki.servicecaller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import java.util.logging.Logger;

/**
 * Formatieren der Daten in meta und content für die Vorhersage im JSON Format
 */
public class JSONUtils {

    private final static Logger LOGGER = Logger.getLogger("LOAN-REQUESTS");

    public String genJSONStr (DelegateExecution execution) throws Exception {


        JsonObject processData = new JsonObject();

        JsonObject meta = new JsonObject();
        processData.add("meta", meta);

        meta.addProperty("procDefID", execution.getProcessDefinitionId());
        meta.addProperty("procInstID", execution.getProcessInstanceId());

        ObjectMapper mapper = new ObjectMapper();
        String processVariables = mapper.writeValueAsString(execution.getVariables());
        JsonParser parser = new JsonParser();

        JsonObject content = parser.parse(processVariables).getAsJsonObject();
        processData.add("content", content);

        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();

        return gson.toJson(processData);
    }

}