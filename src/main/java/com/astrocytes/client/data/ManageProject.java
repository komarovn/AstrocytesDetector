package com.astrocytes.client.data;

import java.io.File;
import java.io.IOException;

/**
 * Created by Nikolay Komarov on 19.03.2017.
 */
public class ManageProject {

    public static void saveProject(File projectDir) {
        try {
            File settings = new File(projectDir, "settings.xml");
            settings.createNewFile();
            File parameters = new File(projectDir, "parameters.xml");
            parameters.createNewFile();
            AppParameters.saveParameters(parameters);
            AppParameters.saveSettings(settings);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadProject(File projectDir) {
        AppParameters.destroyParameters();
        AppParameters.destroySettings();
        File settings = new File(projectDir, "settings.xml");
        File parameters = new File(projectDir, "parameters.xml");
        AppParameters.loadParameters(parameters);
        AppParameters.loadSettings(settings);
    }

}
