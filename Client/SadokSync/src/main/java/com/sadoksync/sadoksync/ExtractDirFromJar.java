package com.sadoksync.sadoksync;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;

public class ExtractDirFromJar {

    public ExtractDirFromJar(String sourceDirectory, String writeDirectory) throws IOException {
        // Only proceed if the folder doesn't already exist :)
        if (!new File(writeDirectory).exists()) {
            new File(writeDirectory).mkdirs();
            extract(sourceDirectory, writeDirectory);
        }
    }

    private void extract(String sourceDirectory, String writeDirectory) throws IOException {
        final URL dirURL = getClass().getResource(sourceDirectory);
        final String path = sourceDirectory.substring(1);

        if ((dirURL != null) && dirURL.getProtocol().equals("jar")) {
            final JarURLConnection jarConnection = (JarURLConnection) dirURL.openConnection();
            System.out.println("jarConnection is " + jarConnection);

            final ZipFile jar = jarConnection.getJarFile();

            final Enumeration< ? extends ZipEntry> entries = jar.entries(); // gives ALL entries in jar

            while (entries.hasMoreElements()) {
                final ZipEntry entry = entries.nextElement();
                final String name = entry.getName();
                // System.out.println( name );
                if (!name.startsWith(path)) {
                    // entry in wrong subdir -- don't copy
                    continue;
                }
                final String entryTail = name.substring(path.length());

                final File f = new File(writeDirectory + File.separator + entryTail);
                if (entry.isDirectory()) {
                    // if its a directory, create it
                    final boolean bMade = f.mkdir();
                    System.out.println((bMade ? "  creating " : "  unable to create ") + name);
                } else {
                    System.out.println("  writing  " + name);
                    final InputStream is = jar.getInputStream(entry);
                    final OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
                    final byte buffer[] = new byte[4096];
                    int readCount;
                    // write contents of 'is' to 'os'
                    while ((readCount = is.read(buffer)) > 0) {
                        os.write(buffer, 0, readCount);
                    }
                    os.close();
                    is.close();
                }
            }

        } else if (dirURL == null) {
            throw new IllegalStateException("can't find " + sourceDirectory + " on the classpath");
        } else {
            // not a "jar" protocol URL
            throw new IllegalStateException("don't know how to handle extracting from " + dirURL);
        }
    }
}
