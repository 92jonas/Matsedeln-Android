package jonas.jacobsson.midgardensvardshus.matsedeln;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;

public class ActivityContactFragment extends Fragment  {

    @Bind(R.id.textView5) TextView textView5;
    @Bind(R.id.textView2) TextView textView2;
    @Bind(R.id.editText) EditText editText;
    @Bind(R.id.textView3) TextView textView3;
    @Bind(R.id.editText2) EditText editText2;
    @Bind(R.id.textView4) TextView textView4;
    @Bind(R.id.editText3) EditText editText3;

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
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.button2) void onButton2Click() {
        //TODO implement
    }

    @OnLongClick(R.id.button2) boolean onButton2LongClick() {
        //TODO implement
        return true;
    }
}
