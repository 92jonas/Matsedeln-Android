package jonas.jacobsson.midgardensvardshus.matsedeln.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jonas.jacobsson.midgardensvardshus.matsedeln.R;

/**
 * Created by Jonas on 2016-11-11.
 */

public class ContactTabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        return view;
    }
}
