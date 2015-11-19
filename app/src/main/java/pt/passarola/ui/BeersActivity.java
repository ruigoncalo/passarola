package pt.passarola.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.model.events.BeerViewModelEvent;
import pt.passarola.services.BusProvider;
import pt.passarola.ui.recyclerview.BeersAdapter;
import pt.passarola.utils.dagger.DaggerableAppCompatActivity;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class BeersActivity extends DaggerableAppCompatActivity {

    @Inject BeersPresenter presenter;
    @Inject BusProvider busProvider;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;
    @Bind(R.id.fab) FloatingActionButton fab;

    private BeersAdapter adapter;
    private boolean hasItems;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.layout_beers);
        ButterKnife.bind(this);
        setupToolbar();
        setupFab();
        setupAdapter();
        setupRecyclerView();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(getString(R.string.app_name));
        }
    }

    private void setupFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void setupAdapter(){
        adapter = new BeersAdapter(this);
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
    public void onResume() {
        super.onResume();
        busProvider.register(this);
        presenter.start();
        requestItems();
    }

    @Override
    public void onPause() {
        busProvider.unregister(this);
        presenter.stop();
        super.onPause();
    }

    // receive event by bus
    @Subscribe
    public void onBeerViewModelUpdate(BeerViewModelEvent event){
        adapter.setItemList(event.getBeerViewModelList());
        hasItems = true;
    }
}
