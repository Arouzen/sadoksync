package com.sadoksync.sadoksync;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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

    @Override
    public boolean accept(File file) {
        boolean allowed = false;
        JSONParser JSONParser = new JSONParser();

        try {
            // Allow only extentions within the AllowedVLCExtensions.txt
            StringBuilder location = new StringBuilder(Client.class.getProtectionDomain().getCodeSource().getLocation().toString());
            location.delete(0, 6);
            File f = new File(location.toString() + "/VLCExtensions.json");
            JSONObject jsonObject = (JSONObject) JSONParser.parse(new FileReader(f));
            JSONObject jsonMediaObject = (JSONObject) jsonObject.get("media");
            JSONArray jsonVisualizeArray = (JSONArray) jsonMediaObject.get("visualize");
            JSONArray jsonVideoArray = (JSONArray) jsonMediaObject.get("video");

            for (Object type : jsonVisualizeArray) {
                if (file.getAbsolutePath().endsWith("." + type) || file.isDirectory()) {
                    allowed = true;
                    break;
                }
            }
            if (!allowed) {
                for (Object type : jsonVideoArray) {
                    if (file.getAbsolutePath().endsWith("." + type) || file.isDirectory()) {
                        allowed = true;
                        break;
                    }
                }
            }

        } catch (IOException | ParseException ex) {
            Logger.getLogger(FileFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allowed;
    }

    @Override
    public String getDescription() {
        return "Media file";
    }
}
