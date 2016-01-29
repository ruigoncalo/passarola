package pt.passarola.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.model.viewmodel.BeerViewModel;
import pt.passarola.ui.SocialBeerListener;

/**
 * Created by ruigoncalo on 19/11/15.
 */
public class BeersViewHolder extends RecyclerView.ViewHolder implements Composer<BeerViewModel> {

    @Bind(R.id.image_beer) ImageView image;
    @Bind(R.id.text_beer_name) TextView textName;
    @Bind(R.id.text_beer_style) TextView textStyle;
    @Bind(R.id.text_beer_description) TextView textDescription;
    @Bind(R.id.text_beer_ingredients) TextView textIngredients;
    @Bind(R.id.button_ratebeer) View rateBeerButton;
    @Bind(R.id.button_untappd) View untappdButton;

    private SocialBeerListener listener;

    public BeersViewHolder(View view, SocialBeerListener listener) {
        super(view);
        this.listener = listener;
        ButterKnife.bind(this, view);
    }

    @Override
    public void compose(final BeerViewModel beerViewModel, final int position) {
        textName.setText(beerViewModel.getName());
        textStyle.setText(beerViewModel.getStyle());
        textDescription.setText(beerViewModel.getDescription());
        textIngredients.setText(beerViewModel.getIngredients());
        Picasso.with(image.getContext()).load(beerViewModel.getLabelPicSmall()).into(image);

        setClickListener(rateBeerButton, beerViewModel.getRateBeerUrl(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRateBeerClick(beerViewModel.getRateBeerUrl());
                }
            }
        });

        setClickListener(untappdButton, beerViewModel.getUntappdUrl(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onUntappdClick(beerViewModel.getUntappdUrl());
                }
            }
        });
    }

    private void setClickListener(View view, String link, View.OnClickListener listener){
        if(link == null || link.isEmpty()){
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            view.setOnClickListener(listener);
        }
    }
}