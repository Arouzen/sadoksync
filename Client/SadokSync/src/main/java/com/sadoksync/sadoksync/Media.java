package com.sadoksync.sadoksync;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;

/**
 *
 * @author Arouz
 */
class Media implements Serializable {

    String name;
    String path;
    long length;
    String type;

    // Handle local media files
    public Media(File mediaFile) {
        this.path = mediaFile.getAbsolutePath();
        
        // Create a media player
        MediaPlayerFactory factory = new MediaPlayerFactory();

        // Get the meta data and dump it out
        MediaMeta mediaMeta = factory.getMediaMeta(this.path, true);

        this.name = mediaMeta.getTitle();
        this.length = mediaMeta.getLength();
        this.type = "local file";

        // Orderly clean-up
        mediaMeta.release();
        factory.release();
    }

    // Handle youtube links
    /*public Media(String youtubeUrl) {
        
     }*/
    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public long getLength() {
        return length;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Path: %s, Length: %s, Type: %s", getName(), getPath(), getLength(), getType());
    }
}
