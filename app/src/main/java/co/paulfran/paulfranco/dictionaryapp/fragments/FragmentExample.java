package co.paulfran.paulfranco.dictionaryapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.paulfran.paulfranco.dictionaryapp.R;

public class FragmentExample extends Fragment {

    // Required Empty Contructor
    public FragmentExample() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_definition, container, false);
        return view;
    }




}
