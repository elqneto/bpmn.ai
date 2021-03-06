package de.viadee.bpmnai.core.runner.config;

import de.viadee.bpmnai.core.runner.SparkRunner;
import de.viadee.bpmnai.core.util.BpmnaiVariables;
import de.viadee.bpmnai.core.util.logging.BpmnaiLogger;
import org.apache.spark.sql.SaveMode;

import java.io.Serializable;

public class SparkRunnerConfig implements Serializable {
    private boolean initialConfigToBeWritten = false;
    private boolean minimalPipelineToBeBuild = false;
    private boolean writeStepResultsIntoFile = false;
    private int stepCounter = 1;

    private String workingDirectory = ".";
    private String logDirectory = ".";
    private String sourceFolder = ".";
    private String targetFolder = ".";
    private boolean devTypeCastCheckEnabled = false;
    private boolean devProcessStateColumnWorkaroundEnabled = false;
    private boolean revCountEnabled = false;
    private SaveMode saveMode = SaveMode.Append;
    private String dataLevel = BpmnaiVariables.DATA_LEVEL_PROCESS;
    private String outputFormat = BpmnaiVariables.OUTPUT_FORMAT_PARQUET;
    private String delimiter = "|";
    private String outputDelimiter = "|";
    private String processDefinitionFilter = "";
    private boolean batchMode = true;
    private String kafkaBroker = "";

    private String processFilterDefinitionId = null;

    private String pipelineMode = BpmnaiVariables.PIPELINE_MODE_LEARN;

    private SparkRunner.RUNNING_MODE runningMode = null;

    private boolean generateResultPreview = false;
    private int resultPreviewLineCount = 100;

    private boolean closeSparkSessionAfterRun = true;

    public enum ENVIRONMENT_VARIABLES {
        WORKING_DIRECTORY,
        LOG_DIRECTORY,
        FILE_SOURCE,
        FILE_DESTINATION,
        REVISION_COUNT,
        SAVE_MODE,
        DATA_LEVEL,
        OUTPUT_FORMAT,
        WRITE_STEP_RESULTS,
        DELIMITER,
        OUTPUT_DELIMITER,
        PROCESS_DEFINITION_FILTER,
        BATCH_MODE,
        KAFKA_BOOTSTRAP_SERVERS,
        JSON_PREVIEW,
        JSON_PREVIEW_LINES
    }

    public SparkRunnerConfig() {
        initializeWithEnvironmentVariables();
    }

    private void initializeWithEnvironmentVariables() {
        if(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.WORKING_DIRECTORY)) != null) {
            setWorkingDirectory(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.WORKING_DIRECTORY)));
        }
        if(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.LOG_DIRECTORY)) != null) {
            setLogDirectory(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.LOG_DIRECTORY)));
            BpmnaiLogger.getInstance().setLogDirectory(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.LOG_DIRECTORY)));
        }
        if(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.FILE_SOURCE)) != null) {
            setSourceFolder(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.FILE_SOURCE)));
        }
        if(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.FILE_DESTINATION)) != null) {
            setTargetFolder(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.FILE_DESTINATION)));
        }
        if(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.SAVE_MODE)) != null) {
            setSaveMode(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.SAVE_MODE)) == BpmnaiVariables.SAVE_MODE_APPEND ? SaveMode.Append : SaveMode.Overwrite);
        }
        if(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.DATA_LEVEL)) != null) {
            setDataLevel(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.DATA_LEVEL)));
        }
        if(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.OUTPUT_FORMAT)) != null) {
            setOutputFormat(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.OUTPUT_FORMAT)));
        }
        if(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.WRITE_STEP_RESULTS)) != null) {
            setWriteStepResultsIntoFile(true);
        }
        if(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.DELIMITER)) != null) {
            setDelimiter(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.DELIMITER)));
        }
        if(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.OUTPUT_DELIMITER)) != null) {
            setOutputDelimiter(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.OUTPUT_DELIMITER)));
        }
        if(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.PROCESS_DEFINITION_FILTER)) != null) {
            setProcessFilterDefinitionId(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.PROCESS_DEFINITION_FILTER)));
        }
        if(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.BATCH_MODE)) != null) {
            setBatchMode(true);
        }
        if(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.KAFKA_BOOTSTRAP_SERVERS)) != null) {
            setKafkaBroker(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.KAFKA_BOOTSTRAP_SERVERS)));
        }

        if(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.JSON_PREVIEW)) != null) {
            setGenerateResultPreview(true);
        }
        if(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.JSON_PREVIEW_LINES)) != null) {
            setResultPreviewLineCount(Integer.parseInt(System.getenv(String.valueOf(ENVIRONMENT_VARIABLES.JSON_PREVIEW_LINES))));
        }
    }

    public boolean isInitialConfigToBeWritten() {
        return initialConfigToBeWritten;
    }

    public void setInitialConfigToBeWritten(boolean initialConfigToBeWritten) {
        this.initialConfigToBeWritten = initialConfigToBeWritten;
    }

    public boolean isMinimalPipelineToBeBuild() {
        return minimalPipelineToBeBuild;
    }

    public void setMinimalPipelineToBeBuild(boolean minimalPipelineToBeBuild) {
        this.minimalPipelineToBeBuild = minimalPipelineToBeBuild;
    }

    public boolean isWriteStepResultsIntoFile() {
        return writeStepResultsIntoFile;
    }

    public void setWriteStepResultsIntoFile(boolean writeStepResultsIntoFile) {
        this.writeStepResultsIntoFile = writeStepResultsIntoFile;
    }

    public int getAndRaiseStepCounter() {
        return stepCounter++;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public String getLogDirectory() {
        return logDirectory;
    }

    public void setLogDirectory(String logDirectory) {
        this.logDirectory = logDirectory;
    }

    public String getSourceFolder() {
        return sourceFolder;
    }

    public void setSourceFolder(String sourceFolder) {
        this.sourceFolder = sourceFolder;
    }

    public String getTargetFolder() {
        return targetFolder;
    }

    public void setTargetFolder(String targetFolder) {
        this.targetFolder = targetFolder;
    }

    public boolean isDevTypeCastCheckEnabled() {
        return devTypeCastCheckEnabled;
    }

    public void setDevTypeCastCheckEnabled(boolean devTypeCastCheckEnabled) {
        this.devTypeCastCheckEnabled = devTypeCastCheckEnabled;
    }

    public boolean isDevProcessStateColumnWorkaroundEnabled() {
        return devProcessStateColumnWorkaroundEnabled;
    }

    public void setDevProcessStateColumnWorkaroundEnabled(boolean devProcessStateColumnWorkaroundEnabled) {
        this.devProcessStateColumnWorkaroundEnabled = devProcessStateColumnWorkaroundEnabled;
    }

    public boolean isRevCountEnabled() {
        return revCountEnabled;
    }

    public void setRevCountEnabled(boolean revCountEnabled) {
        this.revCountEnabled = revCountEnabled;
    }

    public SaveMode getSaveMode() {
        return saveMode;
    }

    public void setSaveMode(SaveMode saveMode) {
        this.saveMode = saveMode;
    }

    public String getDataLevel() {
        return dataLevel;
    }

    public void setDataLevel(String dataLevel) {
        this.dataLevel = dataLevel;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getOutputDelimiter() {
        return outputDelimiter;
    }

    public void setOutputDelimiter(String outputDelimiter) {
        this.outputDelimiter = outputDelimiter;
    }

    public String getProcessDefinitionFilter() {
        return processDefinitionFilter;
    }

    public void setProcessDefinitionFilter(String processDefinitionFilter) {
        this.processDefinitionFilter = processDefinitionFilter;
    }

    public String getProcessFilterDefinitionId() {
        return processFilterDefinitionId;
    }

    public void setProcessFilterDefinitionId(String processFilterDefinitionId) {
        this.processFilterDefinitionId = processFilterDefinitionId;
    }

    public String getPipelineMode() {
        return pipelineMode;
    }

    public void setPipelineMode(String pipelineMode) {
        this.pipelineMode = pipelineMode;
    }

    public SparkRunner.RUNNING_MODE getRunningMode() {
        return runningMode;
    }

    public void setRunningMode(SparkRunner.RUNNING_MODE runningMode) {
        this.runningMode = runningMode;
    }

    public boolean isBatchMode() {
        return batchMode;
    }

    public void setBatchMode(boolean batchMode) {
        this.batchMode = batchMode;
    }

    public String getKafkaBroker() {
        return kafkaBroker;
    }

    public void setKafkaBroker(String kafkaBroker) {
        this.kafkaBroker = kafkaBroker;
    }

    public boolean isGenerateResultPreview() {
        return generateResultPreview;
    }

    public void setGenerateResultPreview(boolean generateResultPreview) {
        this.generateResultPreview = generateResultPreview;
    }

    public int getResultPreviewLineCount() {
        return resultPreviewLineCount;
    }

    public void setResultPreviewLineCount(int resultPreviewLineCount) {
        this.resultPreviewLineCount = resultPreviewLineCount;
    }

    public boolean isCloseSparkSessionAfterRun() {
        return closeSparkSessionAfterRun;
    }

    public void setCloseSparkSessionAfterRun(boolean closeSparkSessionAfterRun) {
        this.closeSparkSessionAfterRun = closeSparkSessionAfterRun;
    }
}
