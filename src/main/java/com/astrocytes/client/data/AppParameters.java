package com.astrocytes.client.data;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nikolay Komarov on 05.03.2017.
 */
public class AppParameters {

    /**
     * settings   - storage of system setting (e.g. window size)
     * parameters - storage of project's parameters (e.g. values of thresholds)
     */
    private HashMap<String, Object> settings;
    private HashMap<String, Object> parameters;
    private static AppParameters singleton;

    private AppParameters() {
        settings = new HashMap<>();
        parameters = new HashMap<>();
    }

    static {
        singleton = new AppParameters();
    }

    public static void setParameter(String key, Object value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        singleton.parameters.put(key, value);
    }

    public static Object getParameter(String key) {
        return singleton.parameters.get(key);
    }

    public static void setSetting(String key, Object value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        singleton.settings.put(key, value);
    }

    public static Object getSetting(String key) {
        return singleton.settings.get(key);
    }

    public static void destroyParameters() {
        singleton.parameters.clear();
    }

    public static void destroySettings() {
        singleton.settings.clear();
    }

    public static void saveParameters(File file) {
        saveXML(file, singleton.parameters, "project-params", "parameter");
    }

    public static void saveSettings(File file) {
        saveXML(file, singleton.settings, "app-settings", "setting");
    }

    public static void loadParameters(File file) {
        loadXML(file, singleton.parameters, "project-params", "parameter");
    }

    public static void loadSettings(File file) {
        loadXML(file, singleton.settings, "app-settings", "setting");
    }

    private static void saveXML(File file, HashMap<String, Object> data, String rootName, String paramName) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
            Document document = docBuilder.newDocument();

            Element rootElement = document.createElement(rootName);
            document.appendChild(rootElement);

            if (!data.isEmpty()) {
                for (Map.Entry<String, Object> parameter : data.entrySet()) {
                    String key = parameter.getKey();
                    Object value = parameter.getValue();

                    Element parElement = document.createElement(paramName);
                    Attr attribute =document.createAttribute("name");
                    attribute.setValue(key);
                    parElement.setAttributeNode(attribute);
                    parElement.appendChild(document.createTextNode(value.toString()));
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

    private static void loadXML(File file, HashMap<String, Object> data, String rootName, String paramName) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
            Document document = docBuilder.parse(file);
            document.getDocumentElement().normalize();

            NodeList root = document.getElementsByTagName(paramName);
            for (int i = 0; i < root.getLength(); i++) {
                Node node = root.item(i);
                data.put(node.getAttributes().item(0).getNodeValue(), node.getTextContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
