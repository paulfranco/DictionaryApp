package co.paulfran.paulfranco.dictionaryapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.paulfran.paulfranco.dictionaryapp.R;
import co.paulfran.paulfranco.dictionaryapp.WordMeaningActivity;

public class FragmentExample extends Fragment {

    // Required Empty Contructor
    public FragmentExample() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_definition, container, false);

        Context context = getActivity();
        TextView text = (TextView) view.findViewById(R.id.textViewD);

        String example = ((WordMeaningActivity)context).example;

        text.setText(example);
        if (example == null) {
            text.setText("No example found");
        }


        return view;
    }




}
