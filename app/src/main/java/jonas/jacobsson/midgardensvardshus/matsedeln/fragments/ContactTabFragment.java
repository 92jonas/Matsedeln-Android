package jonas.jacobsson.midgardensvardshus.matsedeln.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jonas.jacobsson.midgardensvardshus.matsedeln.R;
import jonas.jacobsson.midgardensvardshus.matsedeln.constants.ResturantConstants;

/**
 * Created by Jonas on 2016-11-11.
 */

public class ContactTabFragment extends Fragment {


    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_text)
    EditText etText;
    private Animation anim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_contact, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        anim = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.btn_send)
    void onSendClick() {
        boolean validName = validText(etName);
        boolean validText = validText(etText);
        String errorMsg = "";
        if (!validName) {
            showError(etName);
            errorMsg += "namn";
        }
        if (!validText) {
            showError(etText);
            if (!errorMsg.isEmpty()) {
                errorMsg += " och ";
            }
            errorMsg += "förslag";
        }
        if (validName && validText) {
            sendEmail();
        } else {
            Toast.makeText(getContext(), "Fyll i " + errorMsg, Toast.LENGTH_SHORT).show();
        }
    }

    private void showError(final EditText et) {
        et.startAnimation(anim);
    }

    private boolean validText(EditText et) {
        return !et.getText().toString().isEmpty();
    }

    private void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{ResturantConstants.EMAIL});
        i.putExtra(Intent.EXTRA_SUBJECT, "APP: Matförslag");
        i.putExtra(Intent.EXTRA_TEXT, getUserMessage());
        try {
            startActivity(Intent.createChooser(i, "Välj en E-post app:"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public String getUserMessage() {
        return etName.getText().toString() + " har ett förslag:\n\n" + etText.getText().toString() + "\n\n Mvh, Midgårdens matsedel";
    }
}
