package com.brandycamacho.Spotify_Streamer.view;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.brandycamacho.Spotify_Streamer.R;
import com.brandycamacho.Spotify_Streamer.controller.Artist;
import com.brandycamacho.Spotify_Streamer.controller.ArtistTopTracksAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingRightInAnimationAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by brandycamacho on 6/10/15.
 */
public class ResultsTopTenFragment extends Fragment {

    private final String TAG = ArtistResultsFragment.class.getSimpleName();
    public static ArtistTopTracksAdapter<Artist> mArtistTopTenAdapter;
    SwingRightInAnimationAdapter animAdapter;
    public static ArrayList<Artist> mArtistTopTrackList = new ArrayList<>();
    String artistId = "";
    ProgressDialog progressDialog;
    View v;
    ListView lv_results_artist;


    // Configuration of menu options
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        Log.v(TAG, "OnCreate");

        // Using bundle to pass data between fragment activities
        Bundle mGetArtistBundle = this.getArguments();
        artistId = mGetArtistBundle.getString("artistId");


        if (saveInstanceState == null) {
            mArtistTopTenAdapter = new ArtistTopTracksAdapter<>(getActivity(), R.layout.list_view_artist_top_tracks, mArtistTopTrackList);
            FetchTopTenTracks fa = new FetchTopTenTracks();
            fa.execute();
        } else {
            saveInstanceState.getSerializable("topTenAdapter");
        }
        animAdapter = new SwingRightInAnimationAdapter(mArtistTopTenAdapter);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("topTenAdapter", mArtistTopTenAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // apply view by using inflater to inflate view with fragment data
        v = inflater.inflate(R.layout.fragment_activity_results_artist, container, false);

        lv_results_artist = (ListView) v.findViewById(R.id.lv_results_artist);
        lv_results_artist.setAdapter(animAdapter);
        animAdapter.setAbsListView(lv_results_artist);

        lv_results_artist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // We will use Spotify ID to implement a details fragment activity that will provide details along with ability to listen to song.

                Toast.makeText(getActivity(),"Spotify Song ID = "+ mArtistTopTenAdapter.getItem((int)id).getId(),Toast.LENGTH_SHORT).show();
            }
        });

        FrameLayout.LayoutParams lv_params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        lv_results_artist.setLayoutParams(lv_params);

        /** Wrapping list adapter with nHaarman animationAdapter Library
         retrieved from: http://nhaarman.github.io/ListViewAnimations/#appearance-animations */
        return v;
    }


    /** There was issues using spotify callback which resulted in using AsyncTask
     Error --> java.lang.IndexOutOfBoundsException: Invalid index 10, size is 10
     After trial and error I noticed first few times calling activity were a success.
     Yet, after a few more executions I would start to receive IndexOutOfBounds error.
     I think it has to do with time to execute within callback which is why I elected to use
     AsyncTask as I can control pre/background/post events. Using AsyncTask solved my issue.
     Also, its worth noting that I think this is related to the use of fragments versus starting
     a new activity. Also, I am using Android Studio Canary Channel which might be causing this issue.
     */
    public class FetchTopTenTracks extends AsyncTask<Void, Void, Void>{
        SpotifyApi api;
        SpotifyService spotify;
        Map<String, Object> map = new HashMap<>();
        Tracks artistTrack;
        Artist testTrack;
        String trackTitle, album, url, id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                mArtistTopTrackList.clear();
                // instantiate new progress dialog
                progressDialog = new ProgressDialog(getActivity());
                // spinner (wheel) style dialog
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                // better yet - use a string resource getString(R.string.your_message)
                progressDialog.setMessage("Loading data");
                // display dialog
                progressDialog.show();

                api = new SpotifyApi();
                spotify = api.getService();
                map.put("country", getResources().getConfiguration().locale.getCountry());
            } catch (Exception e){

            }

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                artistTrack = spotify.getArtistTopTrack(artistId, map);
            }catch (Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (artistTrack != null) {
                //Loop through tracks and add to list
                for (int i = 0; i < artistTrack.tracks.size(); i++) {
                    Track data = artistTrack.tracks.get(i);
                    trackTitle = data.name;
                    id = data.id;
                    album = data.album.name;

                    if (data.album.images.size() != 0) {
                        url = data.album.images.get(0).url;
                    } else {
                        url = null;
                    }
                    testTrack = new Artist(trackTitle, id, url, album);
                    mArtistTopTrackList.add(testTrack);
                }
            } else {
                mArtistTopTenAdapter.add(new Artist("ERROR", null, null, "Verify internet connectivity"));
            }
            // notify our adapter that the data in the list has changed
            mArtistTopTenAdapter.notifyDataSetChanged();
            // stop progress dialog
            progressDialog.dismiss();
        }
    }


}
