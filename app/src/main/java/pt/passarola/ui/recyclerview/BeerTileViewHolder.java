package pt.passarola.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.model.viewmodel.BeerViewModel;

/**
 * Created by ruigoncalo on 20/12/15.
 */
public class BeerTileViewHolder extends RecyclerView.ViewHolder implements Composer<BeerViewModel>, View.OnClickListener {

    @Bind(R.id.image_beer) ImageView image;

    private OnBaseItemClickListener onItemClickListener;

    public BeerTileViewHolder(View view, OnBaseItemClickListener onItemClickListener) {
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
        Picasso.with(image.getContext()).load(beerViewModel.getLabelPicSmall()).into(image);
    }
}