package pt.passarola.utils;

/**
 * Created by ruigoncalo on 19/11/15.
 */
public interface Callback<T> {
    void onSuccess(T t);
    void onFailure(Exception e);
    void isLoading(boolean loading);
}