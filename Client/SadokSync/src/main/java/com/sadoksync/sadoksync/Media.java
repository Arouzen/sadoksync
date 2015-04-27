package com.sadoksync.sadoksync;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
    public Media(File mediaFile, String type) {
        this.path = mediaFile.getAbsolutePath();

        // Create a media player
        MediaPlayerFactory factory = new MediaPlayerFactory();

        // Get the meta data and dump it out
        MediaMeta mediaMeta = factory.getMediaMeta(this.path, true);

        this.name = mediaMeta.getTitle();
        this.length = mediaMeta.getLength();
        this.type = type;

        // Orderly clean-up
        mediaMeta.release();
        factory.release();
    }

    public Media(String url, String type) {
        this.path = "http://www.youtube.com/watch?v=" + url;
        this.type = type;
        if (type.equals("youtube")) {
            initYoutube(url);
        } else if (type.equals("vine")) {
            // Vine stuff here
        } else if (type.equals("myspace")) {
            // Ayyy lmao
        }
    }

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

    private void initYoutube(String id) {
        JSONObject jsonRoot = readJsonFromUrl("http://gdata.youtube.com/feeds/api/videos/" + id + "?v=2&alt=json");
        JSONObject jsonMediaGroup = (JSONObject) ((JSONObject) jsonRoot.get("entry")).get("media$group");
       
        this.length = Long.valueOf((String) ((JSONObject) jsonMediaGroup.get("yt$duration")).get("seconds")) * 1000;
        this.name = (String) ((JSONObject) jsonMediaGroup.get("media$title")).get("$t");
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONParser JSONParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) JSONParser.parse(jsonText);
            return jsonObject;
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        // Error! Something went wrong, check stack trace
        return null;
    }
}
