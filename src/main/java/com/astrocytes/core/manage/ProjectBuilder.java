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

import com.astrocytes.core.CoreConstants;
import com.astrocytes.core.data.DataProvider;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class ProjectBuilder {
    private final String PROJECT_PARAMS_TAG = "project-params";
    private final String APP_SETTINGS_TAG = "app-settings";

    private DataProvider dataProvider = new DataProvider();

    public void saveParameters(File file) {
        saveXML(file, PROJECT_PARAMS_TAG, "parameter");
    }

    public void saveSettings(File file) {
        saveXML(file, APP_SETTINGS_TAG, "setting");
    }

    public void loadParameters(File file) {
        loadXML(file, PROJECT_PARAMS_TAG, "parameter");
    }

    public void loadSettings(File file) {
        loadXML(file, APP_SETTINGS_TAG, "setting");
    }

    private void saveXML(File file, String rootName, String paramName) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
            Document document = docBuilder.newDocument();

            Element rootElement = document.createElement(rootName);
            document.appendChild(rootElement);

            if (rootName.equals(PROJECT_PARAMS_TAG)) {
                if (dataProvider.getScale() != null) {
                    Element parElement = document.createElement(paramName);
                    Attr attribute = document.createAttribute("name");
                    attribute.setValue(CoreConstants.SCALE);
                    parElement.setAttributeNode(attribute);
                    parElement.appendChild(document.createTextNode(dataProvider.getScale().toString()));
                    rootElement.appendChild(parElement);
                }

                if (dataProvider.getCannyMinThreshold() != null) {
                    Element parElement = document.createElement(paramName);
                    Attr attribute = document.createAttribute("name");
                    attribute.setValue(CoreConstants.CANNY_MIN_THRESH);
                    parElement.setAttributeNode(attribute);
                    parElement.appendChild(document.createTextNode(dataProvider.getCannyMinThreshold().toString()));
                    rootElement.appendChild(parElement);
                }

                if (dataProvider.getCannyMaxThreshold() != null) {
                    Element parElement = document.createElement(paramName);
                    Attr attribute = document.createAttribute("name");
                    attribute.setValue(CoreConstants.CANNY_MAX_THRESH);
                    parElement.setAttributeNode(attribute);
                    parElement.appendChild(document.createTextNode(dataProvider.getCannyMaxThreshold().toString()));
                    rootElement.appendChild(parElement);
                }

                if (dataProvider.getRadiusMathMorphology() != null) {
                    Element parElement = document.createElement(paramName);
                    Attr attribute = document.createAttribute("name");
                    attribute.setValue(CoreConstants.RADIUS_DIL_ER);
                    parElement.setAttributeNode(attribute);
                    parElement.appendChild(document.createTextNode(dataProvider.getRadiusMathMorphology().toString()));
                    rootElement.appendChild(parElement);
                }

                if (dataProvider.getBoundingRectangleHeight() != null) {
                    Element parElement = document.createElement(paramName);
                    Attr attribute = document.createAttribute("name");
                    attribute.setValue(CoreConstants.BOUNDING_RECTANGLE_HEIGHT);
                    parElement.setAttributeNode(attribute);
                    parElement.appendChild(document.createTextNode(dataProvider.getBoundingRectangleHeight().toString()));
                    rootElement.appendChild(parElement);
                }

                if (dataProvider.getBoundingRectangleWidth() != null) {
                    Element parElement = document.createElement(paramName);
                    Attr attribute = document.createAttribute("name");
                    attribute.setValue(CoreConstants.BOUNDING_RECTANGLE_WIDTH);
                    parElement.setAttributeNode(attribute);
                    parElement.appendChild(document.createTextNode(dataProvider.getBoundingRectangleWidth().toString()));
                    rootElement.appendChild(parElement);
                }

                if (dataProvider.getBoundingRectangleCenterX() != null) {
                    Element parElement = document.createElement(paramName);
                    Attr attribute = document.createAttribute("name");
                    attribute.setValue(CoreConstants.BOUNDING_RECTANGLE_CENTER_X);
                    parElement.setAttributeNode(attribute);
                    parElement.appendChild(document.createTextNode(dataProvider.getBoundingRectangleCenterX().toString()));
                    rootElement.appendChild(parElement);
                }

                if (dataProvider.getBoundingRectangleCenterY() != null) {
                    Element parElement = document.createElement(paramName);
                    Attr attribute = document.createAttribute("name");
                    attribute.setValue(CoreConstants.BOUNDING_RECTANGLE_CENTER_Y);
                    parElement.setAttributeNode(attribute);
                    parElement.appendChild(document.createTextNode(dataProvider.getBoundingRectangleCenterY().toString()));
                    rootElement.appendChild(parElement);
                }
            }

            if (rootName.equals(APP_SETTINGS_TAG)) {
                if (dataProvider.getProjectDirectory() != null) {
                    Element parElement = document.createElement(paramName);
                    Attr attribute = document.createAttribute("name");
                    attribute.setValue(CoreConstants.PROJECT_DIR);
                    parElement.setAttributeNode(attribute);
                    parElement.appendChild(document.createTextNode(dataProvider.getProjectDirectory()));
                    rootElement.appendChild(parElement);
                }

                if (dataProvider.getProjectName() != null) {
                    Element parElement = document.createElement(paramName);
                    Attr attribute = document.createAttribute("name");
                    attribute.setValue(CoreConstants.PROJECT_NAME);
                    parElement.setAttributeNode(attribute);
                    parElement.appendChild(document.createTextNode(dataProvider.getProjectName()));
                    rootElement.appendChild(parElement);
                }

                if (dataProvider.getWindowWidth() != null) {
                    Element parElement = document.createElement(paramName);
                    Attr attribute = document.createAttribute("name");
                    attribute.setValue(CoreConstants.WINDOW_WIDTH);
                    parElement.setAttributeNode(attribute);
                    parElement.appendChild(document.createTextNode(dataProvider.getWindowWidth().toString()));
                    rootElement.appendChild(parElement);
                }

                if (dataProvider.getWindowHeight() != null) {
                    Element parElement = document.createElement(paramName);
                    Attr attribute = document.createAttribute("name");
                    attribute.setValue(CoreConstants.WINDOW_HEIGHT);
                    parElement.setAttributeNode(attribute);
                    parElement.appendChild(document.createTextNode(dataProvider.getWindowHeight().toString()));
                    rootElement.appendChild(parElement);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadXML(File file, String rootName, String paramName) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
            Document document = docBuilder.parse(file);
            document.getDocumentElement().normalize();

            NodeList root = document.getElementsByTagName(paramName);
            for (int i = 0; i < root.getLength(); i++) {
                Node node = root.item(i);
                //data.put(node.getAttributes().item(0).getNodeValue(), node.getTextContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
