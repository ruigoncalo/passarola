package pt.passarola.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ruigoncalo on 20/12/15.
 */
public class TransparentViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, Composer<Object> {

    private OnBaseItemClickListener onClickListener;

    public TransparentViewHolder(View itemView, OnBaseItemClickListener onClickListener) {
        super(itemView);
        this.onClickListener = onClickListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (onClickListener != null) {
            onClickListener.onBaseItemClick(getAdapterPosition(), view);
        }
    }

    @Override
    public void compose(Object item, int position) {

    }
}
