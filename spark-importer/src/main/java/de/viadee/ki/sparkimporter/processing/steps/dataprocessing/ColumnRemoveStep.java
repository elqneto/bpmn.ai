package de.viadee.ki.sparkimporter.processing.steps.dataprocessing;

import de.viadee.ki.sparkimporter.annotation.PreprocessingStepDescription;
import de.viadee.ki.sparkimporter.configuration.Configuration;
import de.viadee.ki.sparkimporter.configuration.preprocessing.ColumnConfiguration;
import de.viadee.ki.sparkimporter.configuration.preprocessing.PreprocessingConfiguration;
import de.viadee.ki.sparkimporter.configuration.util.ConfigurationUtils;
import de.viadee.ki.sparkimporter.processing.interfaces.PreprocessingStepInterface;
import de.viadee.ki.sparkimporter.runner.config.SparkRunnerConfig;
import de.viadee.ki.sparkimporter.util.SparkImporterUtils;
import de.viadee.ki.sparkimporter.util.SparkImporterVariables;
import de.viadee.ki.sparkimporter.util.logging.SparkImporterLogger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@PreprocessingStepDescription(name = "Remove column", description = "In this step input columns are removed accoording to the configuration before the processing to the data is done.")
public class ColumnRemoveStep implements PreprocessingStepInterface {
    @Override
    public Dataset<Row> runPreprocessingStep(Dataset<Row> dataSet, Map<String, Object> parameters, SparkRunnerConfig config) {

        //these columns have to stay in in order to do the processing
        List<String> columnsToKeep = new ArrayList<>();
        columnsToKeep.add(SparkImporterVariables.VAR_PROCESS_INSTANCE_ID);
        columnsToKeep.add(SparkImporterVariables.VAR_PROCESS_INSTANCE_VARIABLE_NAME);
        columnsToKeep.add(SparkImporterVariables.VAR_PROCESS_INSTANCE_VARIABLE_TYPE);
        columnsToKeep.add(SparkImporterVariables.VAR_PROCESS_INSTANCE_VARIABLE_REVISION);
        columnsToKeep.add(SparkImporterVariables.VAR_STATE);
        columnsToKeep.add(SparkImporterVariables.VAR_LONG);
        columnsToKeep.add(SparkImporterVariables.VAR_DOUBLE);
        columnsToKeep.add(SparkImporterVariables.VAR_TEXT);
        columnsToKeep.add(SparkImporterVariables.VAR_TEXT2);
        columnsToKeep.add(SparkImporterVariables.VAR_DATA_SOURCE);

        List<String> columnsToRemove = new ArrayList<>();

        Configuration configuration = ConfigurationUtils.getInstance().getConfiguration(config);
        if(configuration != null) {
            PreprocessingConfiguration preprocessingConfiguration = configuration.getPreprocessingConfiguration();
            if(preprocessingConfiguration != null) {
                for(ColumnConfiguration cc : preprocessingConfiguration.getColumnConfiguration()) {
                    if(!cc.isUseColumn()) {
                        if(columnsToKeep.contains(cc.getColumnName())) {
                            SparkImporterLogger.getInstance().writeWarn("The column '" + cc.getColumnName() + "' has to stay in in order to do the processing. It will not be removed. Comment: " + cc.getComment());
                        } else {
                            columnsToRemove.add(cc.getColumnName());
                            SparkImporterLogger.getInstance().writeInfo("The column '" + cc.getColumnName() + "' will be removed. Comment: " + cc.getComment());
                        }
                    }
                }
            }
        }

        //check if all variables that should be filtered actually exist, otherwise log a warning
        List<String> existingColumns = new ArrayList<>(Arrays.asList(dataSet.columns()));

        columnsToRemove
                .stream()
                .forEach(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        if(!existingColumns.contains(s)) {
                            // log the fact that a variable that should be filtered does not exist
                            SparkImporterLogger.getInstance().writeWarn("The column '" + s + "' is configured to be filtered, but does not exist in the data.");
                        }
                    }
                });

        dataSet = dataSet.drop(SparkImporterUtils.getInstance().asSeq(columnsToRemove));

        return dataSet;
    }
}