package com.abilitymap;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.abilitymap.MainActivity;

public class LocationDetailFragment extends Fragment {
    MainActivity activity;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.location_detail, container, false);
        View mapview = rootView.findViewById(R.id.return_to_map_cc);
        mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().remove(LocationDetailFragment.this).commit();
                Log.d("click event","fragment 제거");
            }
        });

        return rootView;
    }

}

