package nl.crowndov.displaydirect.distribution.util;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.github.mustachejava.MustacheNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class TemplateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateUtil.class);

    public static String renderTemplate(String name, Map<String, Object> content) {
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache;
        try {
            mustache = mf.compile(name);
        } catch (MustacheNotFoundException ex) {
            LOGGER.error("Template {} doesn't exist: ", name, ex);
            return "";
        }

        Map<String, Object> mapCopy = cleanupMap(content);

        try (StringWriter strWriter = new StringWriter()) {
            try {
                mustache.execute(strWriter, mapCopy).flush();
            } catch (IOException e) {
                LOGGER.error("Failed to compile template");
            }
            return strWriter.toString();
        } catch (IOException ex) {
            return "";
        }
    }

    private static Map<String, Object> cleanupMap(Map<String, Object> content) {
        // Integers in schema are rendered as value.0 which is not what we want, render them to string in this step
        return content.entrySet().stream()
                .map(e -> {
                    if (e.getValue() instanceof Double) {
                        // This might have side-effects, but we can't really create an entry
                        e.setValue(e.getValue().toString().replace(".0", ""));
                    }
                    return e;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
