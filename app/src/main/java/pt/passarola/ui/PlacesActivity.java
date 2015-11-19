package pt.passarola.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.squareup.otto.Subscribe;
import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.model.events.PlaceViewModelEvent;
import pt.passarola.services.BusProvider;
import pt.passarola.ui.recyclerview.PlacesAdapter;
import pt.passarola.utils.dagger.DaggerableAppCompatActivity;

/**
 * Created by ruigoncalo on 23/10/15.
 */
public class PlacesActivity extends DaggerableAppCompatActivity {

    @Inject PlacesPresenter presenter;
    @Inject BusProvider busProvider;

    @Bind(R.id.recycler_view) RecyclerView recyclerView;

    private PlacesAdapter adapter;
    private boolean hasItems;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.layout_places);
        ButterKnife.bind(this);
        setupAdapter();
        setupRecyclerView();
    }

    private void setupAdapter(){
        adapter = new PlacesAdapter(this);
    }

    private void setupRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
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
        adapter.setItemList(event.getPlaceViewModelList());
        hasItems = true;
    }
}
