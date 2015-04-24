package com.sadoksync.sadoksync;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Arouz
 */
class FileFilter extends javax.swing.filechooser.FileFilter {

    StringBuilder location;

    public FileFilter() {
        location = new StringBuilder(Client.class.getProtectionDomain().getCodeSource().getLocation().toString());
        location.delete(0, 6);
    }

    public boolean acceptMediaFile(String mediaExtension, String type) throws ParseException, IOException {
        File f = new File(location.toString() + "/VLCExtensions.json");

        JSONParser JSONParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) JSONParser.parse(new FileReader(f));
        JSONObject jsonMediaObject = (JSONObject) jsonObject.get("media");
        JSONArray jsonVisualizeArray = (JSONArray) jsonMediaObject.get("visualize");
        JSONArray jsonVideoArray = (JSONArray) jsonMediaObject.get("video");

        if (type.equals("visualize") || type.equals("both")) {
            for (Object acceptedExtension : jsonVisualizeArray) {
                if (mediaExtension.toLowerCase().equals(acceptedExtension)) {
                    return true;
                }
            }
        }
        if (type.equals("video") || type.equals("both")) {
            for (Object acceptedExtension : jsonVideoArray) {
                if (mediaExtension.toLowerCase().equals(acceptedExtension)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean accept(File file) {
        try {
            if (file.isDirectory()) {
                return true;
            }
            if (acceptMediaFile(file.getAbsolutePath().split("\\.")[file.getAbsolutePath().split("\\.").length - 1], "both")) {
                return true;
            }
        } catch (IOException | ParseException ex) {
            Logger.getLogger(FileFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Media file";
    }
}
