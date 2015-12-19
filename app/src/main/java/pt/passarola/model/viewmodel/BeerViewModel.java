package pt.passarola.model.viewmodel;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class BeerViewModel {

    private final String id;
    private final String name;
    private final String style;
    private final String abv;
    private final String ingredients;
    private final String description;
    private final int drawable;

    private BeerViewModel(Builder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.style = builder.style;
        this.abv = builder.abv;
        this.ingredients = builder.ingredients;
        this.description = builder.description;
        this.drawable = builder.drawable;
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

    public int getDrawable() {
        return drawable;
    }

    public static class Builder {
        private String id;
        private String name;
        private String style;
        private String abv;
        private String ingredients;
        private String description;
        private int drawable;

        public Builder id(String id){
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

        public Builder drawable(int drawable){
            this.drawable = drawable;
            return this;
        }

        public BeerViewModel build(){
            return new BeerViewModel(this);
        }
    }
}
