package pt.passarola.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;
import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.model.events.PlaceViewModelEvent;
import pt.passarola.services.BusProvider;
import pt.passarola.utils.dagger.DaggerableAppCompatActivity;

/**
 * Created by ruigoncalo on 23/10/15.
 */
public class PlacesActivity extends DaggerableAppCompatActivity {

    @Inject PlacesPresenter presenter;
    @Inject BusProvider busProvider;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;

    private PlacesAdapter adapter;
    private boolean hasItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        ButterKnife.bind(this);
        setupToolbar();
        setupAdapter();
        setupRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar(){
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.places);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void setupAdapter(){
        adapter = new PlacesAdapter(this, null); // TODO: add listener
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
    protected void onResume(){
        super.onResume();
        busProvider.register(this);
        presenter.start();
        requestItems();
    }

    @Override
    protected void onPause(){
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
