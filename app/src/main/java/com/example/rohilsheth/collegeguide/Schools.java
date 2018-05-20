package com.example.rohilsheth.collegeguide;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by rohilsheth on 5/19/18.
 */

public class Schools implements Serializable{
    double SATScorelow, SATScorehigh, ACTScorelow,ACTScorehigh;
    String name;
    public Schools(String name,double SATScorelow,double SATScorehigh,double ACTScorelow,double ACTScorehigh){
        this.name = name;
        this.ACTScorelow = ACTScorelow;
        this.ACTScorehigh = ACTScorehigh;
        this.SATScorehigh = SATScorehigh;
        this.SATScorelow = SATScorelow;
    }

    public void setACTScorehigh(double ACTScorehigh) {
        this.ACTScorehigh = ACTScorehigh;
    }

    public void setACTScorelow(double ACTScorelow) {
        this.ACTScorelow = ACTScorelow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
