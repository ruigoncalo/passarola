package pt.passarola.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.model.viewmodel.BeerViewModel;
import pt.passarola.services.tracker.TrackerManager;
import pt.passarola.ui.recyclerview.BeersAdapter;
import pt.passarola.services.dagger.DaggerableAppCompatActivity;
import pt.passarola.utils.Utils;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class BeersActivity extends DaggerableAppCompatActivity
        implements BeersPresenterCallback, SocialBeerListener {

    @Inject BeersPresenter presenter;
    @Inject TrackerManager trackerManager;

    @Bind(R.id.recycler_view) RecyclerView recyclerView;

    private BeersAdapter adapter;
    private boolean hasItems;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.layout_beers);
        ButterKnife.bind(this);
        setupToolbar();
        setupAdapter();
        setupRecyclerView();
    }

    private void setupToolbar() {
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.beers);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupAdapter() {
        adapter = new BeersAdapter(this);
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void requestItems() {
        if (!hasItems) {
            presenter.getBeers();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart(this);
        adapter.registerListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        requestItems();
    }

    @Override
    protected void onStop() {
        presenter.onStop();
        adapter.unregisterListener();
        super.onStop();
    }

    @Override
    public void onBeersSuccessEvent(List<BeerViewModel> list) {
        adapter.setItemList(list);
        hasItems = true;
    }

    @Override
    public void onBeersErrorEvent(Exception e) {

    }

    @Override
    public void isLoading(boolean loading) {

    }

    @Override
    public void onRateBeerClick(String link) {
        Utils.openLink(this, link);

        //Tracking Event
        trackerManager.trackEvent(TrackerManager.EVENT_CLICK_BEER_RATEBEER, "Beer Url", link);
    }

    @Override
    public void onUntappdClick(String link) {
        Utils.openLink(this, link);

        //Tracking Event
        trackerManager.trackEvent(TrackerManager.EVENT_CLICK_BEER_UNTAPPD, "Beer Url", link);
    }
}
