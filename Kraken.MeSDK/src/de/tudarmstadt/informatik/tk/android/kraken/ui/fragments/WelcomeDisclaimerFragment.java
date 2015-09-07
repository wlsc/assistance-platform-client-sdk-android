package de.tudarmstadt.informatik.tk.android.kraken.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.tudarmstadt.informatik.tk.kraken.sdk.R;

/**
 * @author Karsten Planz
 */
@Deprecated
public class WelcomeDisclaimerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome_disclaimer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = (TextView) getView().findViewById(R.id.tutorial_disclaimer_text);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}