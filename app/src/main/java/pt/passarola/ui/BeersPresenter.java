package pt.passarola.ui;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pt.passarola.model.Beer;
import pt.passarola.model.BeerViewModel;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class BeersPresenter {

    @Inject
    public BeersPresenter(){

    }

    public List<BeerViewModel> getBeerViewModelList(){
        List<BeerViewModel> beerViewModelList = new ArrayList<>();

        Beer ipa = new Beer(Beer.BEER_ID_IPA, "India Pale Ale", "India Pale Ale", "6,3%",
                "Pale and Crystal malts, American hops.",
                "Citrus and pine aroma from all American hops. Light refreshing body. " +
                        "Hop flavor and a solid bitterness linger in the finish. " +
                        "Bitterness refreshes the palate when paired with oily foods like Burgers, Pizza or Stirfry.");

        Beer dos = new Beer(Beer.BEER_ID_DOS, "Double Oatmeal Stout", "Imperial Stout", "8%",
                "Pale and Crystal malts, Flaked oats and Barley, Roasted malts, American hops.",
                "Roasty aroma, chocolate, malty, oat sweetness. A smooth body, slight alcohol warmth. " +
                        "All balanced by a healthy dose of bitterness from the hops. " +
                        "Indulge with oysters, chocolate desserts, or all by itself.");

        Beer ara = new Beer(Beer.BEER_ID_ARA, "Amber Rye Ale", "Specialty Grain", "4,7%",
                "Pilsner, Pale, Crystal, Rye and Roasted malts. German and American hops.",
                "Light carbonation produces a smooth rich body. " +
                        "Caramel malt sweetness balanced by the dry, spicy flavour of the rye malt. " +
                        "Great session beer. Pairs easily with any food. " +
                        "Excels with a big rich meaty lunch or an Arroz de Pato.");

        beerViewModelList.add(createBeerViewModel(ipa));
        beerViewModelList.add(createBeerViewModel(dos));
        beerViewModelList.add(createBeerViewModel(ara));

        return beerViewModelList;

    }

    private BeerViewModel createBeerViewModel(Beer beer){
        return new BeerViewModel.Builder()
                .id(beer.getId())
                .name(beer.getName())
                .style(beer.getStyle())
                .abv(beer.getAbv())
                .ingredients(beer.getIngredients())
                .description(beer.getDescription())
                .build();
    }
}
