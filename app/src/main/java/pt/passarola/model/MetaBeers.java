package pt.passarola.model;

import java.util.List;

/**
 * Created by ruigoncalo on 09/01/16.
 */
public class MetaBeers {

    private String message;
    private int results;
    private List<Beer> data;

    public String getMessage() {
        return message;
    }

    public int getResults() {
        return results;
    }

    public List<Beer> getData() {
        return data;
    }
}
