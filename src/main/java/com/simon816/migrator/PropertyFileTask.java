package com.simon816.migrator;

import com.simon816.migrator.task.Task;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public abstract class PropertyFileTask implements Task {

    protected Path propertyFile;

    public PropertyFileTask(Path propertyFile) {
        this.propertyFile = propertyFile;
    }

    @Override
    public void run() throws Exception {
        Properties properties = new Properties();
        InputStream fileIn = Files.newInputStream(this.propertyFile, StandardOpenOption.READ);
        properties.load(fileIn);
        handleProperties(properties);
    }

    protected abstract void handleProperties(Properties properties) throws Exception;

}
