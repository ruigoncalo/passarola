package pt.passarola.model.viewmodel;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import pt.passarola.model.Beer;

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
    private final String labelPic;
    private final String labelPicSmall;
    private final String rateBeerUrl;
    private final String untappdUrl;

    private BeerViewModel(Builder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.style = builder.style;
        this.abv = builder.abv;
        this.ingredients = builder.ingredients;
        this.description = builder.description;
        this.labelPic = builder.labelPic;
        this.labelPicSmall = builder.labelPicSmall;
        this.rateBeerUrl = builder.rateBeerUrl;
        this.untappdUrl = builder.untappdUrl;
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

    public String getLabelPic() {
        return labelPic;
    }

    public String getLabelPicSmall() {
        return labelPicSmall;
    }

    public String getRateBeerUrl() {
        return rateBeerUrl;
    }

    public String getUntappdUrl() {
        return untappdUrl;
    }

    public static class Builder {
        private String id;
        private String name;
        private String style;
        private String abv;
        private String ingredients;
        private String description;
        private String labelPic;
        private String labelPicSmall;
        private String rateBeerUrl;
        private String untappdUrl;

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

        public Builder labelPic(String labelPic){
            this.labelPic = labelPic;
            return this;
        }

        public Builder labelPicSmall(String labelPicSmall){
            this.labelPicSmall = labelPicSmall;
            return this;
        }

        public Builder rateBeerUrl(String rateBeerUrl){
            this.rateBeerUrl = rateBeerUrl;
            return this;
        }

        public Builder untappdUrl(String untappdUrl){
            this.untappdUrl = untappdUrl;
            return this;
        }

        public BeerViewModel build(){
            return new BeerViewModel(this);
        }
    }

    public static List<BeerViewModel> createViewModelList(List<Beer> beers){
        List<BeerViewModel> result = new ArrayList<>();
        for(Beer beer : beers){
            BeerViewModel viewModel = createBeerViewModel(beer);
            if(viewModel != null){
                result.add(viewModel);
            }
        }

        return result;
    }

    @Nullable
    public static BeerViewModel createBeerViewModel(Beer beer){
        if(beer != null) {
            return new Builder()
                    .id(beer.getId())
                    .name(beer.getName())
                    .style(beer.getStyle())
                    .abv(beer.getAbv())
                    .ingredients(beer.getIngredients())
                    .description(beer.getMediumDescription())
                    .labelPic(beer.getLabelPic())
                    .labelPicSmall(beer.getLabelPicSmall())
                    .rateBeerUrl(beer.getRatebeerUrl())
                    .untappdUrl(beer.getUntappdUrl())
                    .build();
        } else {
            return null;
        }
    }
}
