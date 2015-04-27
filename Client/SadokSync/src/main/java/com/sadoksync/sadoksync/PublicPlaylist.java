package com.sadoksync.sadoksync;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Arouz
 */
public class PublicPlaylist implements Serializable {

    final Lock lock;
    final Condition ocupied;
    final Peer pr;
    ArrayList<Pair> playlist;

    public PublicPlaylist(Peer pr) {
        playlist = new ArrayList<Pair>();
        lock = new ReentrantLock();
        ocupied = lock.newCondition();
        this.pr = pr;
    }

    public PublicPlaylist(Peer pr, List li) {
        playlist = (ArrayList) li;
        lock = new ReentrantLock();
        ocupied = lock.newCondition();
        this.pr = pr;
    }

    /*
     Returns the Lock used by the playlist
     */
    public Lock getLock() {
        return lock;
    }

    /*
     Return the condition variable used by the playlist
     */
    public Condition getCV() {
        return ocupied;
    }

    /*
     Critical section locked with lock.
     Adds an item to the playlists.
     */
    public void addToPlaylist(String owner, File mediaFile) {
        Pair pair = new Pair(owner, new Media(mediaFile, "local file"));
        this.addToPlaylist(pair);
    }

    public void addToPlaylist(String owner, String url, String type) {
        Pair pair = new Pair(owner, new Media(url, type));
        this.addToPlaylist(pair);
    }

    public void addToPlaylist(Pair pair) {
        System.out.println("Adding " + pair.value().getName() + " owned by " + pair.key());

        lock.lock();
        try {
            if (pr.isHost()) {

                //if playlist was empty start stream
                //Add to playlist and then send the play list to all.
                if (this.isEmpty()) {
                    //add media to playlist
                    playlist.add(pair);
                    //pr.getClient().addtoPlaylist(msg.getPair());

                    //play next media
                    pr.getClient().startStream();
                } else {
                    playlist.add(pair);
                }

                pr.DeliverPlaylistToComunity();
            } else {
                Message msg = new Message();
                msg.setipAddr(pr.getMyIp());
                msg.setType("Playlist");
                msg.setText("add");
                msg.setPair(pair);
                pr.sendMsg(pr.getHost(), 4444, msg);
            }

            ocupied.signalAll();
        } finally {
            lock.unlock();
        }
    }
    /*
     Returnerar playlisten. Är inte syncronizerad. 
     Ett lock måste sättas kring denna användning.
     //locket måste sitta runt det som kallar getMediaList...
     */

    public ArrayList<Pair> getMediaList() {
        return playlist;
    }

    /*
     lås och läs vad som spelas nu.
     */
    public Media getFirstInList() {
        Pair pair;
        Media Value = null;
        lock.lock();
        try {
            if (!this.isEmpty()) {
                pair = playlist.get(0);
                Value = pair.value();
            }
            ocupied.signalAll();
        } finally {
            lock.unlock();
        }
        return Value;
    }

    public String getFirstInListOwner() {
        Pair pair;
        String key = null;
        lock.lock();
        try {
            pair = playlist.get(0);
            key = pair.key();
            ocupied.signalAll();
        } finally {
            lock.unlock();
        }
        return key;
    }

    /*
     Lock the plalist and remove the first pair
     */
    public void removeFirstInQueue() {
        lock.lock();
        try {
            playlist.remove(0);

            ocupied.signalAll();
        } finally {
            lock.unlock();
        }

    }

    public boolean isEmpty() {
        boolean ret;
        lock.lock();
        try {
            ret = playlist.isEmpty();

            ocupied.signalAll();
        } finally {
            lock.unlock();
        }
        return ret;
    }

    public static class Pair implements Serializable {

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
