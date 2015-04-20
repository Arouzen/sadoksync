package com.sadoksync.sadoksync;

import java.util.ArrayList;

/**
 *
 * @author Arouz
 */
public class PublicPlaylist {

    ArrayList<Pair> playlist;

    public PublicPlaylist() {
        playlist = new ArrayList<Pair>();
    }

    public void addToPlaylist(String name, String path, String length, String type) {
        playlist.add(new Pair("Sadok", new Media(name, path, length, type)));
    }

    public ArrayList<Pair> getMediaList() {
        return playlist;
    }

    public String getNowPlaying() {
        Pair pair = playlist.get(0);
       
        Media Value = pair.value();
        return Value.getPath();
    }
    
    public void removeFirstInQueue() {
        playlist.remove(0);
    }

    public boolean isEmpty() {
        return playlist.isEmpty();
    }

    public static class Pair {

        private final String key;
        private final Media value;

        public Pair(String key, Media value) {
            this.key = key;
            this.value = value;
        }

        public String key() {
            return key;
        }

        public Media value() {
            return value;
        }
    }
}
