package nl.crowndov.displaydirect.distribution.chb;

import nl.crowndov.displaydirect.distribution.Configuration;
import nl.connekt.bison.chb.Export;
import nl.crowndov.displaydirect.distribution.stats.MetricStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class PassengerStopAssignmentLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerStopAssignmentLoader.class);

    public static Map<String, String> load() {
        Map<String, String> mapping = new HashMap<>();
        File chbFile = getFile();
        if (chbFile == null) {
            return null;
        }

        try (FileInputStream fis = new FileInputStream(chbFile);
             GZIPInputStream input = new GZIPInputStream(fis)) {
            Export export = (Export) JAXBContext.newInstance(Export.class).createUnmarshaller().unmarshal(input);
            if (export != null) {
                int count = handleExport(mapping, export);
                MetricStore.getInstance().storeMetric("chb.mappings", count);
                LOGGER.info("Got export, with {} entries", count);
            }
        } catch (JAXBException | IOException e) {
            LOGGER.info("Got exception reading CHB file", e);
        }
        return mapping;
    }

    private static File getFile() {
        File path = new File(Configuration.getChbPath());
        File[] fs = path.listFiles();
        if (fs == null || fs.length == 0) {
            return null;
        }
        File chbFile = null;
        for (File f : fs) {
            if (chbFile == null && f.getName().startsWith("PassengerStopAssignmentExportCHB") && f.getName().endsWith("xml.gz")) {
                chbFile = f;
            }
        }
        if (chbFile != null) {
            LOGGER.info("File to match {}", chbFile.getName());
        } else {
            return null;
        }
        return chbFile;
    }

    private static int handleExport(Map<String, String> mapping, Export export) {
        int i[] = {0};
        export.getQuays().getQuay().forEach(q -> {
            q.getUserstopcodes().getUserstopcodedata().forEach(usc -> {
                mapping.put(usc.getDataownercode()+"|"+usc.getUserstopcode(), q.getQuaycode());

                i[0] += 1;
            });
        });
        return i[0];
    }

}
