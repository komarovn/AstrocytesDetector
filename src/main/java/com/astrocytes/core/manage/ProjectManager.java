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
package com.astrocytes.core.manage;

import com.astrocytes.core.ImageHelper;
import com.astrocytes.core.CoreConstants;
import com.astrocytes.core.data.DataProvider;
import com.astrocytes.core.exception.LoadProjectException;
import com.astrocytes.core.exception.SaveProjectException;

import java.io.File;
import java.io.IOException;

public class ProjectManager {
    private ProjectBuilder projectBuilder = new ProjectBuilder();

    public void saveProject(File projectDir) throws IOException, SaveProjectException {
        File settings = new File(projectDir, CoreConstants.FILE_SETTINGS);
        settings.createNewFile();
        File parameters = new File(projectDir, CoreConstants.FILE_PARAMETERS);
        parameters.createNewFile();
        File rawData = new File(projectDir, CoreConstants.FILE_RAW_DATA);
        rawData.createNewFile();
        projectBuilder.saveParameters(parameters);
        projectBuilder.saveSettings(settings);
        projectBuilder.saveRaw(rawData);
        File image = new File(projectDir, CoreConstants.FILE_IMAGE);
        image.createNewFile();
        ImageHelper.saveImage(DataProvider.getWorkingImage(), image);
    }

    public void loadProject(File projectDir) throws LoadProjectException {
        DataProvider.destroyAllData();
        File settings = new File(projectDir, CoreConstants.FILE_SETTINGS);
        File parameters = new File(projectDir, CoreConstants.FILE_PARAMETERS);
        File rawData = new File(projectDir, CoreConstants.FILE_RAW_DATA);
        projectBuilder.loadParameters(parameters);
        projectBuilder.loadSettings(settings);
        projectBuilder.loadRaw(rawData);
        File image = new File(projectDir, CoreConstants.FILE_IMAGE);
        DataProvider.setWorkingImage(ImageHelper.loadImage(image));
    }

}
