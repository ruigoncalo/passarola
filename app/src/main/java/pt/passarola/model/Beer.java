package pt.passarola.model;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class Beer {

    public static final int BEER_ID_IPA = 1;
    public static final int BEER_ID_DOS = 2;
    public static final int BEER_ID_ARA = 3;

    private final int id;
    private final String name;
    private final String style;
    private final String abv;
    private final String ingredients;
    private final String description;

    public Beer(int id, String name, String style, String abv, String ingredients, String description) {
        this.id = id;
        this.name = name;
        this.style = style;
        this.abv = abv;
        this.ingredients = ingredients;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStyle() {
        return style;
    }

    public String getAbv() {
        return abv;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getDescription() {
        return description;
    }
}
