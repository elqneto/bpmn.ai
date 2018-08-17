package de.viadee.ki.sparkimporter.processing.steps.output;

import de.viadee.ki.sparkimporter.processing.interfaces.PreprocessingStepInterface;
import de.viadee.ki.sparkimporter.util.SparkImporterVariables;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;

public class WriteToDataSinkStep implements PreprocessingStepInterface {
    @Override
    public Dataset<Row> runPreprocessingStep(Dataset<Row> dataset, boolean writeStepResultIntoFile) {
        dataset
                //TODO: comment why
                .repartition(dataset.col(SparkImporterVariables.VAR_PROCESS_INSTANCE_ID))
                .write()
                .mode(SaveMode.Append)
                .save("file:///Users/mim/Desktop/spark_sink");

        return dataset;
    }
}
