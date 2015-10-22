package pt.passarola.utils.dagger;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public interface Daggerable {
    void inject(Object object);
    void inject(Object object, Object... modules);
}
