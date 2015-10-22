package pt.passarola.model;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class BeerViewModel {

    private final int id;
    private final String name;
    private final String style;
    private final String abv;
    private final String ingredients;
    private final String description;

    private BeerViewModel(Builder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.style = builder.style;
        this.abv = builder.abv;
        this.ingredients = builder.ingredients;
        this.description = builder.description;
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

    public static class Builder {
        private int id;
        private String name;
        private String style;
        private String abv;
        private String ingredients;
        private String description;

        public Builder id(int id){
            this.id = id;
            return this;
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder style(String style){
            this.style = style;
            return this;
        }

        public Builder abv(String abv){
            this.abv = abv;
            return this;
        }

        public Builder ingredients(String ingredients){
            this.ingredients = ingredients;
            return this;
        }

        public Builder description(String description){
            this.description = description;
            return this;
        }

        public BeerViewModel build(){
            return new BeerViewModel(this);
        }
    }
}
