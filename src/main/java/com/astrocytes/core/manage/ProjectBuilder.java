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
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class ProjectBuilder {

    public void saveParameters(File file) throws SaveProjectException {
        try {
            Document document = createDocument(null);
            String paramName = "parameter";

            Element rootElement = document.createElement("project-params");
            document.appendChild(rootElement);

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

            saveDocument(document, file);
        } catch (Exception e) {
            throw new SaveProjectException(e);
        }
    }

    public void saveSettings(File file) throws SaveProjectException {
        try {
            Document document = createDocument(null);
            String paramName = "setting";

            Element rootElement = document.createElement("app-settings");
            document.appendChild(rootElement);

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

            saveDocument(document, file);
        } catch (Exception e) {
            throw new SaveProjectException(e);
        }
    }

    public void saveRaw(File file) throws SaveProjectException {
        try {
            Document document = createDocument(null);
            String paramName = "item";

            Element rootElement = document.createElement("raw-data");
            document.appendChild(rootElement);

            //TODO: insert data

            saveDocument(document, file);
        } catch (Exception e) {
            throw new SaveProjectException(e);
        }
    }

    public void loadParameters(File file) throws LoadProjectException {
        try {
            Document document = createDocument(file);
            NodeList params = document.getElementsByTagName("parameter");

            for (int i = 0; i < params.getLength(); i++) {
                Node node = params.item(i);

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
                }
            }
        } catch (Exception e) {
            throw new LoadProjectException(e);
        }
    }

    public void loadSettings(File file) throws LoadProjectException {
        try {
            Document document = createDocument(file);
            NodeList settings = document.getElementsByTagName("setting");

            for (int i = 0; i < settings.getLength(); i++) {
                Node node = settings.item(i);

                switch (node.getAttributes().item(0).getNodeValue()) {
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

    public void loadRaw(File file) throws LoadProjectException {
        try {
            Document document = createDocument(file);
            NodeList items = document.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                Node node = items.item(i);
            }
        } catch (Exception e) {
            throw new LoadProjectException(e);
        }
    }

    private Document createDocument(File file) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();

        if (file != null) {
            Document document = docBuilder.parse(file);
            document.getDocumentElement().normalize();

            return document;
        } else {
            return docBuilder.newDocument();
        }
    }

    private void saveDocument(Document document, File file) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    private void addElement(String paramName, String name, String value, Element root, Document document) {
        Element parElement = document.createElement(paramName);
        Attr attribute = document.createAttribute("name");
        attribute.setValue(name);
        parElement.setAttributeNode(attribute);
        parElement.appendChild(document.createTextNode(value));

        root.appendChild(parElement);
    }

}
