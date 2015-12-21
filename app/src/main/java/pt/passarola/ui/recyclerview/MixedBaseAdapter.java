package pt.passarola.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pt.passarola.model.viewmodel.MixedViewModel;

/**
 * Created by ruigoncalo on 20/12/15.
 */
public abstract class MixedBaseAdapter<VHA extends RecyclerView.ViewHolder, VHB extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MixedViewModel> itemList;
    private OnBaseItemClickListener onBaseItemAClickListener;
    private OnBaseItemClickListener onBaseItemBClickListener;

    public MixedBaseAdapter(){
        itemList = new ArrayList<>();
    }

    public void registerClickListeners(OnBaseItemClickListener onBaseItemAClickListener,
                                       OnBaseItemClickListener onBaseItemBClickListener){
        this.onBaseItemAClickListener = onBaseItemAClickListener;
        this.onBaseItemBClickListener = onBaseItemBClickListener;
    }

    public void unregisterClickListeners(){
        this.onBaseItemAClickListener = null;
        this.onBaseItemAClickListener = null;
    }

    public void setItemList(List<MixedViewModel> itemList){
        if(this.itemList.isEmpty()) {
            this.itemList = itemList;
            notifyDataSetChanged();
        } else {
            throw new IllegalStateException("ItemList is populated");
        }
    }

    public void clearList(){
        this.itemList.clear();
    }

    public List<MixedViewModel> getItemList(){
        return itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType){
            case MixedViewModel.TYPE_A:
                return onCreateViewHolderA(viewGroup, viewType, onBaseItemAClickListener);

            case MixedViewModel.TYPE_B:
                return onCreateViewHolderB(viewGroup, viewType, onBaseItemBClickListener);

            default:
                throw new IllegalStateException("Incorrect ViewType found");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MixedViewModel mixedViewModel = getItem(position);
        switch (mixedViewModel.getType()){
            case MixedViewModel.TYPE_A:
                onBindViewHolderA(holder, position);
                break;

            case MixedViewModel.TYPE_B:
                onBindViewHolderB(holder, position);
                break;

            default:
                throw new IllegalStateException("Incorrect ViewType found");
        }
    }

    public abstract VHA onCreateViewHolderA(ViewGroup viewGroup, int viewType,
                                            OnBaseItemClickListener onBaseItemClickListener);

    public abstract VHB onCreateViewHolderB(ViewGroup viewGroup, int viewType,
                                            OnBaseItemClickListener onBaseItemClickListener);

    public abstract void onBindViewHolderA(RecyclerView.ViewHolder holder, int position);

    public abstract void onBindViewHolderB(RecyclerView.ViewHolder holder, int position);


    @Override
    public int getItemViewType(int position) {
        MixedViewModel mixedViewModel = getItem(position);
        return mixedViewModel.getType();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public MixedViewModel getItem(int position){
        return itemList.get(position);
    }
}
