package pt.passarola.ui.components;

import android.support.design.widget.TabLayout;

import pt.passarola.R;
import pt.passarola.utils.AnimatorManager;
import pt.passarola.utils.ScreenInspector;

/**
 * Created by ruigoncalo on 04/01/16.
 */
public class PassarolaTabLayoutManager {

    private static final String TAB_CLOSEST = "tab-closest";
    private static final String TAB_PLACES = "tab-places";
    private static final String TAB_BEERS = "tab-beers";
    public static final int TAB_CLOSEST_POSITION = 0;
    public static final int TAB_PLACES_POSITION = 1;
    public static final int TAB_BEERS_POSITION = 2;

    private final TabLayout tabLayout;
    private OnTabSelectedListener onTabSelectedListener;
    private int size;


    public PassarolaTabLayoutManager(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
        setupTabLayout();
        setupSize();
    }

    public void registerListener(OnTabSelectedListener onTabSelectedListener){
        this.onTabSelectedListener = onTabSelectedListener;
    }

    public void unregisterListener(){
        this.onTabSelectedListener = null;
    }

    private void setupTabLayout() {
        TabLayout.Tab closestPlacesTab = tabLayout.newTab();
        closestPlacesTab.setTag(TAB_CLOSEST);
        closestPlacesTab.setIcon(R.drawable.ic_closest);

        TabLayout.Tab allPlacesTab = tabLayout.newTab();
        allPlacesTab.setTag(TAB_PLACES);
        allPlacesTab.setIcon(R.drawable.ic_action_action_view_list);

        TabLayout.Tab beersTab = tabLayout.newTab();
        beersTab.setTag(TAB_BEERS);
        beersTab.setIcon(R.drawable.ic_beers);

        tabLayout.addTab(closestPlacesTab, TAB_CLOSEST_POSITION);
        tabLayout.addTab(allPlacesTab, TAB_PLACES_POSITION);
        tabLayout.addTab(beersTab, TAB_BEERS_POSITION);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(onTabSelectedListener != null){
                    onTabSelectedListener.onTabSelected(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });
    }

    private void setupSize(){
        size = ScreenInspector.getAppBarHeightPx(tabLayout.getContext());
    }

    public void show(boolean show) {
        if (show) {
            // sliding up
            AnimatorManager.slideInView(tabLayout, 0);
        } else {
            // sliding down
            AnimatorManager.slideOutView(tabLayout, size);
        }
    }

    public interface OnTabSelectedListener {
        void onTabSelected(int position);
    }

}
