package pt.passarola.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.model.Beer;
import pt.passarola.model.BeerViewModel;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class BeersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<BeerViewModel> beerViewModelList;
    private OnItemClickListener onItemClickListener;

    public BeersAdapter(Context context, List<BeerViewModel> beerViewModelList, OnItemClickListener onItemClickListener){
        this.context = context;
        this.beerViewModelList = beerViewModelList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View editView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_beer, parent, false);
        viewHolder = new BeerViewHolder(editView, onItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final BeerViewModel beerViewModel = getItem(position);
        ((Composer) holder).compose(context, beerViewModel, position);
    }

    public interface Composer {
        void compose(final Context context, final BeerViewModel beerViewModel, int position);
    }

    class BeerViewHolder extends RecyclerView.ViewHolder implements Composer, View.OnClickListener {

        @Bind(R.id.image_view) ImageView imageView;
        @Bind(R.id.text_title) TextView titleTextView;
        @Bind(R.id.text_style) TextView styleTextView;
        @Bind(R.id.text_abv) TextView abvTextView;
        @Bind(R.id.text_ingredients) TextView ingredientsTextView;
        @Bind(R.id.text_description) TextView descriptionsTextView;

        private OnItemClickListener onItemClickListener;

        public BeerViewHolder(View view, OnItemClickListener onItemClickListener) {
            super(view);
            this.onItemClickListener = onItemClickListener;
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position, view);
            }
        }

        @Override
        public void compose(final Context context, final BeerViewModel beerViewModel, final int position) {

            int imageResource = getDrawableFromBeerType(beerViewModel.getId());

            //image
            Picasso.with(context)
                    .load(imageResource)
                    .into(imageView);

            //title
            titleTextView.setText(beerViewModel.getName());

            //style
            styleTextView.setText(beerViewModel.getStyle());

            //abv
            abvTextView.setText(beerViewModel.getAbv());

            //ingredients
            ingredientsTextView.setText(beerViewModel.getIngredients());

            //description
            descriptionsTextView.setText(beerViewModel.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return beerViewModelList.size();
    }

    public BeerViewModel getItem(int position) {
        return beerViewModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // TODO: refactor
    private int getDrawableFromBeerType(int type){
        int result;
        switch (type){
            case Beer.BEER_ID_IPA:
                result = R.drawable.label_ipa_simple;
                break;

            case Beer.BEER_ID_DOS:
                result = R.drawable.label_dos_simple;
                break;

            case Beer.BEER_ID_ARA:
                result = R.drawable.label_ara_simple;
                break;

            default:
                result = 0; // TODO: add drawable placeholder
        }

        return result;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }
}
