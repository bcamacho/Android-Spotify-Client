package com.brandycamacho.Spotify_Streamer.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.brandycamacho.Spotify_Streamer.R;
import com.brandycamacho.Spotify_Streamer.controller.Artist;
import com.brandycamacho.Spotify_Streamer.controller.ArtistNameAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingRightInAnimationAdapter;

import java.util.ArrayList;

/**
 * Created by brandycamacho on 6/10/15.
 */
public class ArtistResultsFragment extends Fragment {

    private final String TAG = ArtistResultsFragment.class.getSimpleName();
    public static ArtistNameAdapter<Artist> mArtistAdapter;
    public static ArrayList<Artist> mArtistList = new ArrayList<>();

    View v;

    // Configuration of menu options is not needed considering there are no options needed for end user. The only option needed for Spotify API is location which is automatically generated
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // apply view by using inflater to inflate view with fragment data
        v = inflater.inflate(R.layout.fragment_activity_results_artist, container, false);

        /** Wrapping adapter with nHaarman animationAdapter Library
        retrieved from: http://nhaarman.github.io/ListViewAnimations/#appearance-animations
         This allowed me to experiment with annimations for list view items*/

        mArtistAdapter = new ArtistNameAdapter<>(getActivity(), R.layout.list_view_artist, mArtistList);
        // If mArtistAdapter is empty prompt user to use EditText to search for artist and display results.
        if (mArtistAdapter.isEmpty()){
            // populating a single item with ID null to prevent onClick method from implementing
            mArtistAdapter.add(new Artist("Type artist name above to start search",null,null));
        }
        ListView lv_results_artist = (ListView) v.findViewById(R.id.lv_results_artist);
        SwingRightInAnimationAdapter animAdapter = new SwingRightInAnimationAdapter(mArtistAdapter);
        animAdapter.setAbsListView(lv_results_artist);
        lv_results_artist.setAdapter(animAdapter);

        lv_results_artist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 // If single item with ID null exist prevent onClick method from implementing
                 if (mArtistAdapter.getItem((int)id).getId() != null) {

                     // Use bundle to pass data to calling fragment
                     Bundle mArtistBundle = new Bundle();
                     mArtistBundle.putString("artistName", mArtistAdapter.getItem((int) id).getName());
                     mArtistBundle.putString("artistId", mArtistAdapter.getItem((int) id).getId());


                     //get fragment manager
                     FragmentManager fm = getFragmentManager();

                     // get fragment transition
                     FragmentTransaction ft = fm.beginTransaction()
                             .addToBackStack("result");

                     // next we need to execute
                     // get fragment class
                     Fragment frag1 = new TitleFragment();
                     frag1.setArguments(mArtistBundle);
                     Fragment frag2 = new ResultsTopTenFragment();
                     frag2.setArguments(mArtistBundle);


                     //set fragment frame to fragment class
                     ft.replace(R.id.search_frame_layout, frag1, "Search"); // Search is a tag
                     ft.replace(R.id.results_frame_layout, frag2, "Results"); // Results is a tag
                     ft.commit();
                 }
             }
         });

                FrameLayout.LayoutParams lv_params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
        lv_results_artist.setLayoutParams(lv_params);
        return v;
    }

    // DRY method to update list adapter
    public static void updateAdapter() {
        Handler updateUi = new Handler(Looper.getMainLooper());
        updateUi.post(new Runnable() {
            @Override
            public void run() {
            mArtistAdapter.notifyDataSetChanged();

            }
        });
    }

}
