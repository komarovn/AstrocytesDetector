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
import com.astrocytes.core.exception.LoadProjectException;
import com.astrocytes.core.exception.SaveProjectException;
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

    public void saveParameters(File file) throws SaveProjectException {
        saveXML(file, PROJECT_PARAMS_TAG, "parameter");
    }

    public void saveSettings(File file) throws SaveProjectException {
        saveXML(file, APP_SETTINGS_TAG, "setting");
    }

    public void loadParameters(File file) throws LoadProjectException {
        loadXML(file, PROJECT_PARAMS_TAG, "parameter");
    }

    public void loadSettings(File file) throws LoadProjectException {
        loadXML(file, APP_SETTINGS_TAG, "setting");
    }

    private void saveXML(File file, String rootName, String paramName) throws SaveProjectException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
            Document document = docBuilder.newDocument();

            Element rootElement = document.createElement(rootName);
            document.appendChild(rootElement);

            if (rootName.equals(PROJECT_PARAMS_TAG)) {
                if (DataProvider.getScale() != null) {
                    addElement(paramName, CoreConstants.SCALE,
                            DataProvider.getScale().toString(), rootElement, document);
                }

                if (DataProvider.getCannyMinThreshold() != null) {
                    addElement(paramName, CoreConstants.CANNY_MIN_THRESH,
                            DataProvider.getCannyMinThreshold().toString(), rootElement, document);
                }

                if (DataProvider.getCannyMaxThreshold() != null) {
                    addElement(paramName, CoreConstants.CANNY_MAX_THRESH,
                            DataProvider.getCannyMaxThreshold().toString(), rootElement, document);
                }

                if (DataProvider.getRadiusMathMorphology() != null) {
                    addElement(paramName, CoreConstants.RADIUS_DIL_ER,
                            DataProvider.getRadiusMathMorphology().toString(), rootElement, document);
                }

                if (DataProvider.getBoundingRectangleHeight() != null) {
                    addElement(paramName, CoreConstants.BOUNDING_RECTANGLE_HEIGHT,
                            DataProvider.getBoundingRectangleHeight().toString(), rootElement, document);
                }

                if (DataProvider.getBoundingRectangleWidth() != null) {
                    addElement(paramName, CoreConstants.BOUNDING_RECTANGLE_WIDTH,
                            DataProvider.getBoundingRectangleWidth().toString(), rootElement, document);
                }

                if (DataProvider.getBoundingRectangleCenterX() != null) {
                    addElement(paramName, CoreConstants.BOUNDING_RECTANGLE_CENTER_X,
                            DataProvider.getBoundingRectangleCenterX().toString(), rootElement, document);
                }

                if (DataProvider.getBoundingRectangleCenterY() != null) {
                    addElement(paramName, CoreConstants.BOUNDING_RECTANGLE_CENTER_Y,
                            DataProvider.getBoundingRectangleCenterY().toString(), rootElement, document);
                }
            }

            if (rootName.equals(APP_SETTINGS_TAG)) {
                if (DataProvider.getProjectDirectory() != null) {
                    addElement(paramName, CoreConstants.PROJECT_DIR,
                            DataProvider.getProjectDirectory(), rootElement, document);
                }

                if (DataProvider.getProjectName() != null) {
                    addElement(paramName, CoreConstants.PROJECT_NAME,
                            DataProvider.getProjectName(), rootElement, document);
                }

                if (DataProvider.getWindowWidth() != null) {
                    addElement(paramName, CoreConstants.WINDOW_WIDTH,
                            DataProvider.getWindowWidth().toString(), rootElement, document);
                }

                if (DataProvider.getWindowHeight() != null) {
                    addElement(paramName, CoreConstants.WINDOW_HEIGHT,
                            DataProvider.getWindowHeight().toString(), rootElement, document);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (Exception e) {
            throw new SaveProjectException(e);
        }
    }

    private void addElement(String paramName, String name, String value, Element root, Document document) {
        Element parElement = document.createElement(paramName);
        Attr attribute = document.createAttribute("name");
        attribute.setValue(name);
        parElement.setAttributeNode(attribute);
        parElement.appendChild(document.createTextNode(value));
        root.appendChild(parElement);
    }

    private void loadXML(File file, String rootName, String paramName) throws LoadProjectException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
            Document document = docBuilder.parse(file);
            document.getDocumentElement().normalize();

            NodeList root = document.getElementsByTagName(paramName);
            for (int i = 0; i < root.getLength(); i++) {
                Node node = root.item(i);
                switch (node.getAttributes().item(0).getNodeValue()) {
                    case CoreConstants.SCALE:
                        DataProvider.setScale(Integer.valueOf(node.getTextContent()));
                        break;
                    case CoreConstants.CANNY_MIN_THRESH:
                        DataProvider.setCannyMinThreshold(Integer.valueOf(node.getTextContent()));
                        break;
                    case CoreConstants.CANNY_MAX_THRESH:
                        DataProvider.setCannyMaxThreshold(Integer.valueOf(node.getTextContent()));
                        break;
                    case CoreConstants.RADIUS_DIL_ER:
                        DataProvider.setRadiusMathMorphology(Integer.valueOf(node.getTextContent()));
                        break;
                    case CoreConstants.BOUNDING_RECTANGLE_HEIGHT:
                        DataProvider.setBoundingRectangleHeight(Integer.valueOf(node.getTextContent()));
                        break;
                    case CoreConstants.BOUNDING_RECTANGLE_WIDTH:
                        DataProvider.setBoundingRectangleWidth(Integer.valueOf(node.getTextContent()));
                        break;
                    case CoreConstants.BOUNDING_RECTANGLE_CENTER_X:
                        DataProvider.setBoundingRectangleCenterX(Integer.valueOf(node.getTextContent()));
                        break;
                    case CoreConstants.BOUNDING_RECTANGLE_CENTER_Y:
                        DataProvider.setBoundingRectangleCenterY(Integer.valueOf(node.getTextContent()));
                        break;
                    case CoreConstants.PROJECT_DIR:
                        DataProvider.setProjectDirectory(node.getTextContent());
                        break;
                    case CoreConstants.PROJECT_NAME:
                        DataProvider.setProjectName(node.getTextContent());
                        break;
                    case CoreConstants.WINDOW_WIDTH:
                        DataProvider.setWindowWidth(Integer.valueOf(node.getTextContent()));
                        break;
                    case CoreConstants.WINDOW_HEIGHT:
                        DataProvider.setWindowHeight(Integer.valueOf(node.getTextContent()));
                        break;
                }
            }
        } catch (Exception e) {
            throw new LoadProjectException(e);
        }
    }
}
