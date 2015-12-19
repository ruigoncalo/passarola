package pt.passarola.utils;

/**
 *
 * Created by ruigoncalo on 18/12/15.
 */
public abstract class Presenter<P> {

    private P presented;

    public void onStart(P presented){
        this.presented = presented;
    }

    public void onStop(){
        this.presented = null;
    }

    public P getPresented() {
        return presented;
    }
}
