package pt.passarola.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.model.MapItems;
import pt.passarola.services.tracker.TrackerManager;
import pt.passarola.ui.recyclerview.PlacesAdapter;
import pt.passarola.services.dagger.DaggerableAppCompatActivity;
import pt.passarola.utils.Utils;

/**
 * Created by ruigoncalo on 23/10/15.
 */
public class PlacesActivity extends DaggerableAppCompatActivity
        implements PlacesPresenterCallback, SocialPlacesListener {

    @Inject PlacesPresenter presenter;
    @Inject TrackerManager trackerManager;

    @Bind(R.id.recycler_view) RecyclerView recyclerView;

    private PlacesAdapter adapter;
    private boolean hasItems;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.layout_places);
        ButterKnife.bind(this);
        setupToolbar();
        setupAdapter();
        setupRecyclerView();
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

    private void setupToolbar() {
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.places);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setSubtitle(int total){
        if(getSupportActionBar() != null){
            String totalText = String.valueOf(total);
            getSupportActionBar().setSubtitle(getString(R.string.total_places, totalText));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
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
            presenter.getPlaces();
        }
    }

    @Override
    public void onPlacesSuccessEvent(MapItems items) {
        setSubtitle(items.getPlaces().size());
        adapter.setItemList(items.getPlaces());
        hasItems = true;
    }

    @Override
    public void onPlacesErrorEvent(Exception e) {

    }

    @Override
    public void isLoading(boolean loading) {

    }


    @Override
    public void onFacebookClick(String link) {
        Utils.openLink(this, link);

        //Tracking Event
        trackerManager.trackEvent(TrackerManager.EVENT_CLICK_ALL_PLACES_FACEBOOK, "Facebook Url", link);
    }

    @Override
    public void onZomatoClick(String link) {
        Utils.openLink(this, link);

        //Tracking Event
        trackerManager.trackEvent(TrackerManager.EVENT_CLICK_ALL_PLACES_ZOMATO, "Zomato Url", link);
    }

    @Override
    public void onTripadvisorClick(String link) {
        Utils.openLink(this, link);

        //Tracking Event
        trackerManager.trackEvent(TrackerManager.EVENT_CLICK_ALL_PLACES_TRIPADVISOR, "Tripadvisor Url", link);
    }

    @Override
    public void onPhoneClick(String phone) {

    }
}
