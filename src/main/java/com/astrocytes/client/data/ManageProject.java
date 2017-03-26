package com.astrocytes.client.data;

import com.astrocytes.client.App;
import com.astrocytes.client.ImageHelper;
import com.astrocytes.client.resources.ClientConstants;

import java.io.File;
import java.io.IOException;

/**
 * Created by Nikolay Komarov on 19.03.2017.
 */
public class ManageProject {
    private App mainApp;

    public ManageProject(App mainApp) {
        this.mainApp = mainApp;
    }

    public void saveProject(File projectDir) {
        try {
            File settings = new File(projectDir, ClientConstants.FILE_SETTINGS);
            settings.createNewFile();
            File parameters = new File(projectDir, ClientConstants.FILE_PARAMETERS);
            parameters.createNewFile();
            AppParameters.saveParameters(parameters);
            AppParameters.saveSettings(settings);
            File image = new File(projectDir, ClientConstants.FILE_IMAGE);
            image.createNewFile();
            ImageHelper.saveImage(ImageHelper.convertMatToBufferedImage(mainApp.getOperationsExecuter().getOperations().getSourceImage()), image);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadProject(File projectDir) {
        AppParameters.destroyParameters();
        AppParameters.destroySettings();
        File settings = new File(projectDir, ClientConstants.FILE_SETTINGS);
        File parameters = new File(projectDir, ClientConstants.FILE_PARAMETERS);
        AppParameters.loadParameters(parameters);
        AppParameters.loadSettings(settings);
        File image = new File(projectDir, ClientConstants.FILE_IMAGE);
        mainApp.getOperationsExecuter().getOperations().setSourceImage(ImageHelper.convertBufferedImageToMat(ImageHelper.loadImage(image)));
    }

}
