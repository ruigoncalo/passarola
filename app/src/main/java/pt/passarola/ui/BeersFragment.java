package pt.passarola.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.Constants;
import pt.passarola.R;
import pt.passarola.services.BusProvider;
import pt.passarola.utils.dagger.DaggerableFragment;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class BeersFragment extends DaggerableFragment {

    @Inject BusProvider busProvider;

    @Bind(R.id.viewpager) ViewPager viewPager;

    public static BeersFragment newInstance() {
        return new BeersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_beers, container, false);
        ButterKnife.bind(this, view);
        setupViewPager();
        setupTabs();
        return view;
    }

    private void setupViewPager() {
        Adapter adapter = new Adapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(BeerFragment.newInstance(Constants.BEER_IPA_TYPE), Constants.BEER_IPA_SHORT);
        adapter.addFragment(BeerFragment.newInstance(Constants.BEER_DOS_TYPE), Constants.BEER_DOS_SHORT);
        adapter.addFragment(BeerFragment.newInstance(Constants.BEER_ARA_TYPE), Constants.BEER_ARA_SHORT);
        viewPager.setAdapter(adapter);
    }

    private void setupTabs() {
        TabLayout tabs = (TabLayout) getActivity().findViewById(R.id.tabs);
        tabs.setVisibility(View.VISIBLE);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
        busProvider.register(this);
    }

    @Override
    public void onPause() {
        busProvider.unregister(this);
        super.onPause();
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }
}
