package com.test.book_barn.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.sql.R;

/**
 * Created by audreyeso on 8/22/16.
 */
public class ContactUsFragment extends android.support.v4.app.Fragment{

    public ContactUsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_us, container, false);

        return rootView;
    }
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}