package com.awaj;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.awaj.R;

import java.io.File;
import java.util.ArrayList;

public class ListFragment extends android.support.v4.app.Fragment {
    ArrayList<String> nameList;
    ListView list;
    static int i=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.file_list_fragment, container, false);
       list = (ListView) view.findViewById(R.id.listView);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        i++;
        if(i>=2) {
            i = 0;
            return;
        }
       nameList = new ArrayList<>();
        //getNames();

        File folder = getActivity().getExternalFilesDir("Awaj");
        File file[] = folder.listFiles();
        for (int i=0; i < file.length; i++){
            nameList.add(file[i].getName());
//            nameList.add(String.valueOf(i));
        }

        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, nameList);
        // Set The Adapter
        list.setAdapter(arrayAdapter);
        list.setBackgroundColor(Color.parseColor("#888888"));
    }//End of onActivityCreated

//    void getNames() {
//        File folder = getActivity().getExternalFilesDir("Awaj");
//        File file[] = folder.listFiles();
//        for (int i=0; i < file.length; i++){
//            nameList.add(file[i].getName());
////            nameList.add(String.valueOf(i));
//        }
////        nameList.add("lorem ipsum");
////        nameList.add("lorem ipsum");
////        nameList.add("lorem ipsum");
////        nameList.add("lorem ipsum");
////        nameList.add("lorem ipsum");
////        nameList.add("lorem ipsum");
////        nameList.add("lorem ipsum");
//    }
}
/**End of ListFragment Class*/