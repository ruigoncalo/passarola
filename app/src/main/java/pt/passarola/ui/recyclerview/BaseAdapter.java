package pt.passarola.ui.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruigoncalo on 19/11/15.
 */
public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<T> itemList;

    public BaseAdapter(Context context){
        this.context = context;
        itemList = new ArrayList<>();
    }

    public Context getContext(){
        return context;
    }

    public void setItemList(List<T> itemList){
        if(this.itemList.isEmpty()) {
            this.itemList = itemList;
            notifyDataSetChanged();
        } else {
            throw new IllegalStateException("ItemList is populated");
        }
    }

    public List<T> getItemList(){
        return itemList;
    }

    public abstract VH onCreateViewHolder(ViewGroup viewGroup, int viewType);
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public T getItem(int position){
        return itemList.get(position);
    }
}
