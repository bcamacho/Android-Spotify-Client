package com.brandycamacho.Spotify_Streamer.controller;

import java.io.Serializable;

/**
 * Created by brandycamacho on 6/12/15.
 * This class is used to control Artist Data which can be stored into a ListArray
 */
public class Artist implements Serializable {
    private final String name;
    private final String album;
    private final String trackTitle;
    private final String album_art;
    private final String id;

    public String getAlbum() {
        return album;
    }
    public String getAlbum_art(){
        return album_art;
    }
    public String getTrackTitle() {
        return trackTitle;
    }
    public String getName() {
        return name;
    }
    public String getId(){return id;}



    public Artist(String trackTitle, String id, String album_art, String album){
        this.name = null;
        this.album = album;
        this.album_art = album_art;
        this.trackTitle = trackTitle;
        this.id = id;

    }
    public Artist(String name, String id, String album_art){
        this.name = name;
        this.id = id;
        this.album_art = album_art;
        this.trackTitle = null;
        this.album = null;

    }

}

