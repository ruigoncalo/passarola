package pt.passarola.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.nav_view) NavigationView navigationView;
    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.tabs) TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolbar();
        setupDrawerContent();
        setupFab();
        setupFragments();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setElevation(20f);
        }
    }

    private void setupDrawerContent() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    private void setupFragments(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);

        fragmentManager.beginTransaction()
                .add(R.id.container, MapsFragment.newInstance(), "init-fragment")
                .commit();
    }

    private void selectDrawerItem(MenuItem menuItem){
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_map:
                fragment = MapsFragment.newInstance();
                break;

            case R.id.nav_places:
                fragment = PlacesFragment.newInstance();
                break;

            case R.id.nav_beers:
                fragment = BeersFragment.newInstance();
                break;

            case R.id.nav_events:
                fragment = EventsFragment.newInstance();
                break;
        }

        if(fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment, fragment.getClass().getName())
                    .commit();
        }

        menuItem.setChecked(true);
        setToolbarTitle(menuItem);
        drawerLayout.closeDrawers();
    }

    private void setToolbarTitle(MenuItem item){
        if(item.getItemId() == R.id.nav_map){
            setTitle(getString(R.string.app_name));
        } else {
            setTitle(item.getTitle());
        }
    }

    @Override
    public void onBackStackChanged(){
        for(Fragment fragment : getSupportFragmentManager().getFragments()){
            Timber.d("Fragment = " + fragment.toString());
        }
    }

    private void setupFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
