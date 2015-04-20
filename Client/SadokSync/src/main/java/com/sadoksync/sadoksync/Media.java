package com.sadoksync.sadoksync;

/**
 *
 * @author Arouz
 */
class Media {

    String name;
    String path;
    String length;
    String type;

    public Media(String name, String path, String length, String type) {
        this.name = name;
        this.path = path;
        this.length = length;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getLength() {
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
