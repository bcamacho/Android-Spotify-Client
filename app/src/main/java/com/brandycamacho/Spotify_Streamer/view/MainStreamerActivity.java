package com.brandycamacho.Spotify_Streamer.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.brandycamacho.Spotify_Streamer.R;

/**
 * Created by brandycamacho on 6/10/15.
 */


public class MainStreamerActivity extends Activity {
    String TAG = this.toString();
    RelativeLayout rl1;
    FrameLayout fl_search_artist;
    FrameLayout fl_artist_results;
    FragmentManager fm;
    FragmentTransaction ft;
    Fragment frag1;
    Fragment frag2;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streamer);

        Log.v(TAG, "Created");

        // Attaching 2 frame layouts, search and results to view.

        fl_search_artist = (FrameLayout) findViewById(R.id.search_frame_layout);
        fl_artist_results = (FrameLayout) findViewById(R.id.results_frame_layout);

        if (savedInstanceState == null) {

            //get fragment manager
            fm = getFragmentManager();

            // get fragment transition
            ft = fm.beginTransaction();

            // next we need to execute
            // get fragment class
            frag1 = new ArtistSearchFragment();
            frag2 = new ArtistResultsFragment();

            //set fragment frame to fragment class
            ft.replace(R.id.search_frame_layout, frag1, "Search"); // Search is a tag
            ft.replace(R.id.results_frame_layout, frag2, "Results"); // Results is a tag
            ft.commit();

            this.overridePendingTransition(R.anim.anim_slide_from_left,
                    R.anim.anim_slide_from_left);
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "Saved");

        super.onSaveInstanceState(outState);
        outState.putString("saved", "true");
    }
}