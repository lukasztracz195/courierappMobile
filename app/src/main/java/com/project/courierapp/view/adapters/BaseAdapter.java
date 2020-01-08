package com.project.courierapp.view.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.courierapp.model.dtos.response.Response;
import com.project.courierapp.view.holders.BaseHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.reactivex.disposables.CompositeDisposable;

public class BaseAdapter<T extends BaseHolder> extends RecyclerView.Adapter<T> implements Adapter, Serializable {

    protected List<? extends Response> responses = new ArrayList<>();

    protected Response response;

    protected Context context;

    protected View view;

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected Bundle savedInstanceState;

    public BaseAdapter(Context context) {
        this.context = context;
    }

    public BaseAdapter(Context context, Bundle savedInstanceState) {
        this.context = context;
        this.savedInstanceState = savedInstanceState;
        if (savedInstanceState != null) {
            savedInstanceState.putSerializable("RESPONSES", (Serializable) responses);
        }
    }

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull T holder, int position) {
    }

    @Override
    public int getItemCount() {
        return responses.size();
    }

    @Override
    public void downloadData() {

    }

    @Override
    public void updateData(Response response) {
        this.response = response;
    }

    @Override
    public void updateData(List<? extends Response> responses) {
        this.responses = responses;
        super.notifyDataSetChanged();
    }

    public void onSaveInstanceState(Bundle outState) {
        Optional<Bundle> bundleOptional = Optional.ofNullable(outState);
        if (bundleOptional.isPresent()) {
            responses = (List<? extends Response>) bundleOptional.get()
                    .getSerializable("RESPONSES");
        }
    }

}
