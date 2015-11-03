package pt.passarola.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;
import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.model.events.PlaceViewModelEvent;
import pt.passarola.services.BusProvider;
import pt.passarola.utils.dagger.DaggerableFragment;

/**
 * Created by ruigoncalo on 23/10/15.
 */
public class PlacesFragment extends DaggerableFragment {

    @Inject PlacesPresenter presenter;
    @Inject BusProvider busProvider;

    @Bind(R.id.recycler_view) RecyclerView recyclerView;

    private PlacesAdapter adapter;
    private boolean hasItems;

    public static PlacesFragment newInstance() {
        return new PlacesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_places, container, false);
        ButterKnife.bind(this, view);
        setupAdapter();
        setupRecyclerView();

        TabLayout tabs = (TabLayout) getActivity().findViewById(R.id.tabs);
        tabs.setVisibility(View.GONE);
        return view;
    }

    private void setupAdapter(){
        adapter = new PlacesAdapter(getActivity(), null); // TODO: add listener
    }

    private void setupRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void requestItems(){
        if(!hasItems){
            presenter.getItems();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        busProvider.register(this);
        presenter.start();
        requestItems();
    }

    @Override
    public void onPause(){
        busProvider.unregister(this);
        presenter.stop();
        super.onPause();
    }

    // receive event by bus
    @Subscribe
    public void onPlaceViewModelEvent(PlaceViewModelEvent event){
        hasItems = true;
        adapter.addItem(event.getPlaceViewModel());
    }
}
