package de.viadee.bpmnai.core.configuration.preprocessing;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PreprocessingConfiguration {

    @SerializedName("data_level")
    private String dataLevel = "";

    @SerializedName("variable_configuration")
    private List<VariableConfiguration> variableConfiguration = new ArrayList<>();

    @SerializedName("variable_name_mapping")
    private List<VariableNameMapping> variableNameMappings = new ArrayList<>();

    @SerializedName("column_configuration")
    private List<ColumnConfiguration> columnConfiguration = new ArrayList<>();

    @SerializedName("column_hash_configuration")
    private List<ColumnHashConfiguration> columnHashConfiguration = new ArrayList<>();

    @SerializedName("pipeline_step_configuration")
    private PipelineStepConfiguration pipelineStepConfiguration = new PipelineStepConfiguration();

    public String getDataLevel() {
        return dataLevel;
    }

    public void setDataLevel(String dataLevel) {
        this.dataLevel = dataLevel;
    }

    public List<VariableConfiguration> getVariableConfiguration() {
        return variableConfiguration;
    }

    public List<VariableNameMapping> getVariableNameMappings() {
        return variableNameMappings;
    }

    public List<ColumnConfiguration> getColumnConfiguration() { return columnConfiguration; }

    public List<ColumnHashConfiguration> getColumnHashConfiguration() { return columnHashConfiguration; }

    public PipelineStepConfiguration getPipelineStepConfiguration() {
        return pipelineStepConfiguration;
    }
}
