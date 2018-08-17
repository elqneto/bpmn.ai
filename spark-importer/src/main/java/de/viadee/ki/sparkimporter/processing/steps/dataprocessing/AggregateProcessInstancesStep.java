package de.viadee.ki.sparkimporter.processing.steps.dataprocessing;

import de.viadee.ki.sparkimporter.processing.interfaces.PreprocessingStepInterface;
import de.viadee.ki.sparkimporter.util.SparkImporterUtils;
import de.viadee.ki.sparkimporter.util.SparkImporterVariables;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.spark.sql.functions.isnull;
import static org.apache.spark.sql.functions.not;

public class AggregateProcessInstancesStep implements PreprocessingStepInterface {

    @Override
    public Dataset<Row> runPreprocessingStep(Dataset<Row> dataset, boolean writeStepResultIntoFile) {

        //apply first and processState aggregator
        Map<String, String> aggregationMap = new HashMap<>();
        for(String column : dataset.columns()) {
            if(column.equals(SparkImporterVariables.VAR_STATE)) {
                aggregationMap.put(column, "ProcessState");
            } else {
                aggregationMap.put(column, "first");
            }
        }

        //first aggregation
        //take only processInstance rows
        Dataset<Row> datasetPIAgg = dataset
                .filter(not(isnull(dataset.col(SparkImporterVariables.VAR_STATE))))
                .groupBy(SparkImporterVariables.VAR_PROCESS_INSTANCE_ID)
                .agg(aggregationMap);

        //cleanup, so renaming columns and dropping not used ones
        datasetPIAgg = datasetPIAgg.drop(SparkImporterVariables.VAR_PROCESS_INSTANCE_ID);
        datasetPIAgg = datasetPIAgg.drop(SparkImporterVariables.VAR_PROCESS_INSTANCE_VARIABLE_NAME);

        //rename back columns after aggregation
        String pattern = "(first|processstate)\\((.+)\\)";
        Pattern r = Pattern.compile(pattern);

        for(String columnName : datasetPIAgg.columns()) {
            Matcher m = r.matcher(columnName);
            if(m.find()) {
                String newColumnName = m.group(2);
                datasetPIAgg = datasetPIAgg.withColumnRenamed(columnName, newColumnName);
            }
        }

        //union again with processInstance rows
        dataset = dataset
                .filter(not(isnull(dataset.col(SparkImporterVariables.VAR_PROCESS_INSTANCE_VARIABLE_NAME))))
                .union(datasetPIAgg);

        if(writeStepResultIntoFile) {
            SparkImporterUtils.getInstance().writeDatasetToCSV(dataset, "agg_of_process_instances");
        }

        //return preprocessed data
        return dataset;
    }
}
