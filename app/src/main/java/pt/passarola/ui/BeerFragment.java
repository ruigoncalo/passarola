package pt.passarola.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.Constants;
import pt.passarola.R;
import pt.passarola.model.BeerViewModel;
import pt.passarola.utils.dagger.DaggerableFragment;

/**
 * Created by ruigoncalo on 31/10/15.
 */
public class BeerFragment extends DaggerableFragment {

    @Inject BeersPresenter presenter;

    @Bind(R.id.layout_beer) View layoutContainer;
    @Bind(R.id.image_view) ImageView imageView;
    @Bind(R.id.text_title) TextView titleTextView;
    @Bind(R.id.text_style) TextView styleTextView;
    @Bind(R.id.text_abv) TextView abvTextView;
    @Bind(R.id.text_ingredients) TextView ingredientsTextView;
    @Bind(R.id.text_description) TextView descriptionsTextView;


    public static BeerFragment newInstance(int type) {
        BeerFragment beerFragment = new BeerFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.EXTRA_BEER_TYPE, type);
        beerFragment.setArguments(args);
        return beerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beer, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments != null){
            int extraType = arguments.getInt(Constants.EXTRA_BEER_TYPE);
            setupType(extraType);
        }
    }

    private void setupType(int type){
        switch (type){
            case Constants.BEER_ARA_TYPE:
                showBeer(presenter.getBeerAra(), R.drawable.label_ara_simple);
                break;

            case Constants.BEER_DOS_TYPE:
                showBeer(presenter.getBeerDos(), R.drawable.label_dos_simple);
                break;

            default:
                showBeer(presenter.getBeerIpa(), R.drawable.label_ipa_simple);
        }
    }

    private void showBeer(BeerViewModel beerViewModel, int drawable){
        Picasso.with(getActivity()).load(drawable).into(imageView);
        titleTextView.setText(beerViewModel.getName());
        styleTextView.setText(beerViewModel.getStyle());
        abvTextView.setText(beerViewModel.getAbv());
        ingredientsTextView.setText(beerViewModel.getIngredients());
        descriptionsTextView.setText(beerViewModel.getDescription());
    }
}
