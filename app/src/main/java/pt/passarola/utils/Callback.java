package pt.passarola.utils;

/**
 * Created by ruigoncalo on 20/12/15.
 */
public interface Callback<T> {
    void onSuccess(T t);
    void onFailure(Exception e);
    void isLoadind(boolean loading);
}
