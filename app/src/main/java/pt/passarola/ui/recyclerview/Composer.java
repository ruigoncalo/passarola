package pt.passarola.ui.recyclerview;

/**
 * Created by ruigoncalo on 19/11/15.
 */
public interface Composer<T> {
    void compose(T item, int position);
}
