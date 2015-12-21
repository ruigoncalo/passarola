package pt.passarola.services;

import java.util.ArrayList;
import java.util.List;

import pt.passarola.model.Beer;
import pt.passarola.model.events.BeersSuccessEvent;

/**
 * Created by ruigoncalo on 19/11/15.
 */
public class BeerProvider {

    private BusProvider busProvider;

    public BeerProvider(BusProvider busProvider) {
        this.busProvider = busProvider;
    }

    public void getBeers() {
        List<Beer> list = new ArrayList<>();
        list.add(getBeerIpa());
        list.add(getBeerDos());
        list.add(getBeerAra());
        list.add(getBeerBdi());
        list.add(getBeerAlcateia());
        list.add(getBeerHoney());

        busProvider.post(new BeersSuccessEvent(list));
    }

    private Beer getBeerIpa() {
        return new Beer(Beer.BEER_ID_IPA, "India Pale Ale", "India Pale Ale", "6,3%",
                "Pale and Crystal malts, American hops.",
                "Citrus and pine aroma from all American hops. Light refreshing body. " +
                        "Hop flavor and a solid bitterness linger in the finish. " +
                        "Bitterness refreshes the palate when paired with oily foods like Burgers, Pizza or Stirfry.");

    }

    private Beer getBeerDos() {
        return new Beer(Beer.BEER_ID_DOS, "Double Oatmeal Stout", "Imperial Stout", "8%",
                "Pale and Crystal malts, Flaked oats and Barley, Roasted malts, American hops.",
                "Roasty aroma, chocolate, malty, oat sweetness. A smooth body, slight alcohol warmth. " +
                        "All balanced by a healthy dose of bitterness from the hops. " +
                        "Indulge with oysters, chocolate desserts, or all by itself.");
    }

    private Beer getBeerAra() {
        return new Beer(Beer.BEER_ID_ARA, "Amber Rye Ale", "Specialty Grain", "4,7%",
                "Pilsner, Pale, Crystal, Rye and Roasted malts. German and American hops.",
                "Light carbonation produces a smooth rich body. " +
                        "Caramel malt sweetness balanced by the dry, spicy flavour of the rye malt. " +
                        "Great session beer. Pairs easily with any food. " +
                        "Excels with a big rich meaty lunch or an Arroz de Pato.");

    }

    private Beer getBeerBdi() {
        return new Beer(Beer.BEER_ID_BDI, "Blind Date IPA", "India Pale Ale", "6,5%",
                "Pale and Munich malts, American hops and Hop Oils.",
                "Big citrus and pine aroma. Hoppy flavour with a long and bitter finish. Very balanced.");

    }

    private Beer getBeerAlcateia() {
        return new Beer(Beer.BEER_ID_ALCATEIA, "Alcateia", "Grodziskie", "4,6%",
                "Oak Smoked Wheat Malt, Pils Malt. Saaz Hops.",
                "Hazy beer, with a delicate smokey aroma. Balanced acidity and highly refreshing.");
    }

    private Beer getBeerHoney() {
        return new Beer(Beer.BEER_ID_HONEY, "Honey I'm Home", "Saison", "7,5%",
                "Pale malt, Flaked Rye, Organic Honey. Summit and Mandarina Bavaria Hops. Belgian Yeast.",
                "Golden to dark orange color with a thick but smooth body. " +
                        "Taste is medium sweet and aromas of honey, spices and wood.");
    }
}
