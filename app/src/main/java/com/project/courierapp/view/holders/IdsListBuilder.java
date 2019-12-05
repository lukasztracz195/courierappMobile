package com.project.courierapp.view.holders;

import java.util.ArrayList;
import java.util.List;

public class IdsListBuilder {

    private List<Integer> ids = new ArrayList<>();


    public IdsListBuilder appendTextViewId(Integer textViewId){
        ids.add(textViewId);
        return  this;
    }

    public List<Integer> build(){
        return ids;
    }
}
