package com.example.keshavdulal.a14_simple_drawing;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListFragment extends android.support.v4.app.Fragment {

    ArrayList<String> nameList;
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.file_list_fragment, container, false);
        list = (ListView) view.findViewById(R.id.listView);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       nameList = new ArrayList<>();
        getNames();

        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, nameList);
        // Set The Adapter
        list.setAdapter(arrayAdapter);
    }//End of onActivityCreated

    void getNames() {
        nameList.add("lorem ipsum");
        nameList.add("lorem ipsum");
        nameList.add("lorem ipsum");
        nameList.add("lorem ipsum");
        nameList.add("lorem ipsum");
        nameList.add("lorem ipsum");
        nameList.add("lorem ipsum");
        nameList.add("lorem ipsum");
    }
}