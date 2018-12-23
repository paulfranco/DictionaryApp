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

public class FragmentSynonyms extends Fragment {

    // Required Empty Contructor
    public FragmentSynonyms() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_definition, container, false);


        Context context = getActivity();
        TextView text = (TextView) view.findViewById(R.id.textViewD);

        String synonyms = ((WordMeaningActivity)context).synonyms;

        if (synonyms != null) {
            synonyms = synonyms.replaceAll(",", ",\n");
            text.setText(synonyms);
        }
        if (synonyms == null) {
            text.setText(" No synonyms found");
        }

        return view;
    }




}
