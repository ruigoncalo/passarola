package pt.passarola.model;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class Beer {

    public static final String BEER_ID_IPA = "ipa";
    public static final String BEER_ID_DOS = "dos";
    public static final String BEER_ID_ARA = "ara";
    public static final String BEER_ID_ALCATEIA = "alcateia";
    public static final String BEER_ID_HONEY = "honey";

    private final String id;
    private final String name;
    private final String style;
    private final String abv;
    private final String ingredients;
    private final String description;

    public Beer(String id, String name, String style, String abv, String ingredients, String description) {
        this.id = id;
        this.name = name;
        this.style = style;
        this.abv = abv;
        this.ingredients = ingredients;
        this.description = description;
    }

    public String getId() {
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
