/*
 * Copyright (c) Lobachevsky University, 2017. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal with the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 * Developed by: Komarov Nikolay.
 */
package com.astrocytes.client.data;

import com.astrocytes.client.App;
import com.astrocytes.client.ImageHelper;
import com.astrocytes.client.resources.ClientConstants;

import java.io.File;
import java.io.IOException;

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
