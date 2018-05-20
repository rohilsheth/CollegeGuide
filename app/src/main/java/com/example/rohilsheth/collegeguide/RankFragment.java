package com.example.rohilsheth.collegeguide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

/**
 * Created by rohilsheth on 4/27/18.
 */

public class RankFragment extends Fragment {
    ListView listView;
    ArrayList<Schools> colleges;
    public void updateArray(ArrayList<Schools> arrayList){
        colleges = arrayList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("arraykey",colleges);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rankFragmentView = inflater.inflate(R.layout.fragment_rankings, container, false);
        listView = rankFragmentView.findViewById(R.id.ListView);
        if(savedInstanceState!=null){
            colleges = (ArrayList<Schools>) savedInstanceState.getSerializable("arraykey");
        }
        final CustomAdapter CollegeAdapter = new CustomAdapter(getContext(),R.layout.listlayout,colleges);
        if(colleges!=null) {
            listView.setAdapter(CollegeAdapter);
        }
        return rankFragmentView;

    }
    public class CustomAdapter extends ArrayAdapter<Schools> {
        Context context;
        List list;

        public CustomAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Schools> objects) {
            super(context, resource, objects);
            this.context = context;
            list = objects;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View layoutView = layoutInflater.inflate(R.layout.listlayout,null);
            TextView name = (TextView) layoutView.findViewById(R.id.textView3);
            Button up = layoutView.findViewById(R.id.ID_up);
            Button down = layoutView.findViewById(R.id.ID_down);
            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Schools temp = colleges.get(position);
                        Schools temp2 = colleges.get(position-1);
                        colleges.set(position,temp2);
                        colleges.set(position-1,temp);
                        notifyDataSetChanged();
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }

                }
            });
            down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Schools temp = colleges.get(position);
                        Schools temp2 = colleges.get(position + 1);
                        colleges.set(position, temp2);
                        colleges.set(position + 1, temp);
                        notifyDataSetChanged();
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
            });
            name.setText(colleges.get(position).getName());
            return layoutView;
        }
    }
}
