package com.sadoksync.sadoksync;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arouz
 */
class FileFilter extends javax.swing.filechooser.FileFilter {

    @Override
    public boolean accept(File file) {
        FileReader fileReader = null;
        boolean allowed = false;
        try {
            // Allow only extentions within the AllowedVLCExtensions.txt
            StringBuilder location = new StringBuilder(Client.class.getProtectionDomain().getCodeSource().getLocation().toString());
            location.delete(0, 6);
            File f = new File(location.toString() + "/AllowedVLCExtensions.txt");
            fileReader = new FileReader(f);
            BufferedReader br = new BufferedReader(fileReader);
            String line = null;
            // if no more lines the readLine() returns null
            while ((line = br.readLine()) != null) {
                if (file.getAbsolutePath().endsWith("." + line) || file.isDirectory()) {
                    allowed = true;
                    break;
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("AllowedVLCExtensions.txt file not found");
        } catch (IOException ex) {
            System.out.println("AllowedVLCExtensions.txt file interrupted");
        } finally {
            try {
                fileReader.close();
            } catch (IOException ex) {
                Logger.getLogger(FileFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return allowed;
    }

    @Override
    public String getDescription() {
        return "Media file";
    }
}
