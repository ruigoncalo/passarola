package pt.passarola.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.ui.viewmodel.BeerViewModel;

/**
 * Created by ruigoncalo on 19/11/15.
 */
public class BeersViewHolder extends RecyclerView.ViewHolder implements Composer<BeerViewModel>, View.OnClickListener {

    @Bind(R.id.image_beer) ImageView image;
    @Bind(R.id.text_beer_style) TextView textStyle;
    @Bind(R.id.text_beer_abv) TextView textAbv;
    @Bind(R.id.text_beer_description) TextView textDescription;
    @Bind(R.id.text_beer_ingredients) TextView textIngredients;

    private BaseAdapter.OnBaseItemClickListener onItemClickListener;

    public BeersViewHolder(View view, BaseAdapter.OnBaseItemClickListener onItemClickListener) {
        super(view);
        this.onItemClickListener = onItemClickListener;
        ButterKnife.bind(this, view);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int position = getLayoutPosition();
        if (onItemClickListener != null) {
            onItemClickListener.onBaseItemClick(position, view);
        }
    }

    @Override
    public void compose(final BeerViewModel beerViewModel, final int position) {
        textStyle.setText(beerViewModel.getStyle());
        textAbv.setText(beerViewModel.getAbv());
        textDescription.setText(beerViewModel.getDescription());
        textIngredients.setText(beerViewModel.getIngredients());
        image.setImageResource(beerViewModel.getDrawable());
    }
}