package pt.passarola.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.passarola.R;
import pt.passarola.model.PlaceViewModel;

/**
 * Created by ruigoncalo on 23/10/15.
 */
public class PlacesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<PlaceViewModel> placeViewModelList;
    private OnItemClickListener onItemClickListener;

    public PlacesAdapter(Context context, OnItemClickListener onItemClickListener){
        this.context = context;
        this.placeViewModelList = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View editView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_place, parent, false);
        viewHolder = new PlaceViewHolder(editView, onItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final PlaceViewModel placeViewModel = getItem(position);
        ((Composer) holder).compose(context, placeViewModel, position);
    }

    public interface Composer {
        void compose(final Context context, final PlaceViewModel placeViewModel, int position);
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder implements Composer, View.OnClickListener {

        @Bind(R.id.text_name) TextView nameTextView;
        @Bind(R.id.text_address) TextView addressTextView;
        @Bind(R.id.text_council) TextView councilTextView;
        @Bind(R.id.text_country) TextView countryTextView;
        @Bind(R.id.text_telephone) TextView telephoneTextView;

        private OnItemClickListener onItemClickListener;

        public PlaceViewHolder(View view, OnItemClickListener onItemClickListener) {
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
        public void compose(final Context context, final PlaceViewModel placeViewModel, final int position) {

            //name
            nameTextView.setText(placeViewModel.getName());

            //address
            addressTextView.setText(placeViewModel.getFullAddress());

            //council
            councilTextView.setText(placeViewModel.getCouncil());

            //country
            countryTextView.setText(placeViewModel.getCountry());

            //telephone
            telephoneTextView.setText(placeViewModel.getTelephone());
        }
    }

    @Override
    public int getItemCount() {
        return placeViewModelList.size();
    }

    public PlaceViewModel getItem(int position) {
        return placeViewModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(PlaceViewModel placeViewModel){
        placeViewModelList.add(placeViewModel);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }
}
