package com.astrocytes.client.data;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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

    protected static void saveParameters(File file) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
            Document document = docBuilder.newDocument();

            Element rootElement = document.createElement("project-params");
            document.appendChild(rootElement);

            if (!singleton.parameters.isEmpty()) {
                for (Map.Entry<String, Object> parameter : singleton.parameters.entrySet()) {
                    String key = parameter.getKey();
                    Object value = parameter.getValue();

                    Element parElement = document.createElement("parameter");
                    Attr attribute =document.createAttribute("name");
                    attribute.setValue(key);
                    parElement.setAttributeNode(attribute);
                    parElement.appendChild(document.createTextNode(value.toString()));
                    document.appendChild(parElement);
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

}
