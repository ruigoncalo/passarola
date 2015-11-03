package pt.passarola.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.utils.dagger.DaggerableFragment;

/**
 * Created by ruigoncalo on 03/11/15.
 */
public class EventsFragment extends DaggerableFragment {

    public static EventsFragment newInstance() {
        return new EventsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_events, container, false);
        ButterKnife.bind(this, view);

        TabLayout tabs = (TabLayout) getActivity().findViewById(R.id.tabs);
        tabs.setVisibility(View.GONE);
        return view;
    }

}
