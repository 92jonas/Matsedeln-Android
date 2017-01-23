package jonas.jacobsson.midgardensvardshus.matsedeln.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import jonas.jacobsson.midgardensvardshus.matsedeln.R;

/**
 * Created by Jonas on 2016-11-11.
 */

public class ContactTabFragment extends Fragment {

    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.editText2)
    EditText editText2;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.editText3)
    EditText editText3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_contact, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.button2)
    void onButton2Click() {
        //TODO implement
    }

    @OnLongClick(R.id.button2)
    boolean onButton2LongClick() {
        //TODO implement
        return true;
    }
}
