package com.example.rohilsheth.collegeguide;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,SearchFragment.addClicked  {
    int rankID;
    HomeFragment hFrag;
    SearchFragment searchFrag;
    RankFragment rFrag;
    FitFragment fFrag;
    ArrayList<Schools> colleges;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colleges = new ArrayList<>();
        loadFragment(new HomeFragment());
        hFrag = new HomeFragment();
        searchFrag = new SearchFragment();
        rFrag = new RankFragment();
        fFrag = new FitFragment();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = hFrag;
                break;

            case R.id.navigation_rankings:
                fragment=rFrag;
                break;

            case R.id.navigation_search:
                fragment = searchFrag;
                break;

            case R.id.navigation_fit:
                fragment = fFrag;
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            fragment.getId();
            return true;
        }
        return false;
    }

    @Override
    public void sendArray(ArrayList<Schools> schools) {
        //colleges.addAll(schools);
        rFrag.updateArray(schools);
    }
}
