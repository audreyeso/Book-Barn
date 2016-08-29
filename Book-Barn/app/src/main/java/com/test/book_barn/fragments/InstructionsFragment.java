package com.test.book_barn.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.book_barn.R;

/**
 * Created by audreyeso on 8/20/16.
 */
public class InstructionsFragment extends Fragment {

    public InstructionsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_instructions, container, false);


        return rootView;
    }
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

}

