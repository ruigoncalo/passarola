package pt.passarola.ui;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pt.passarola.R;
import pt.passarola.model.Beer;
import pt.passarola.model.events.BeerViewModelEvent;
import pt.passarola.services.BeerProvider;
import pt.passarola.services.BusProvider;
import pt.passarola.ui.viewmodel.BeerViewModel;
import pt.passarola.utils.Callback;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class BeersPresenter {

    @Inject BeerProvider beerProvider;
    @Inject BusProvider busProvider;

    @Inject
    public BeersPresenter(){

    }

    public void start(){
        busProvider.register(this);
    }

    public void stop(){
        busProvider.unregister(this);
    }

    public void getItems(){
        beerProvider.getBeers(new Callback<List<Beer>>() {
            @Override
            public void onSuccess(List<Beer> beers) {
                List<BeerViewModel> viewModels = generateViewModels(beers);
                busProvider.post(new BeerViewModelEvent(viewModels));
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private List<BeerViewModel> generateViewModels(List<Beer> beers){
        List<BeerViewModel> result = new ArrayList<>();
        for(Beer beer : beers){
            BeerViewModel viewModel = createBeerViewModel(beer);
            if(viewModel != null){
                result.add(viewModel);
            }
        }

        return result;
    }

    private BeerViewModel createBeerViewModel(Beer beer){
        return new BeerViewModel.Builder()
                .id(beer.getId())
                .name(beer.getName())
                .style(beer.getStyle())
                .abv(beer.getAbv())
                .ingredients(beer.getIngredients())
                .description(beer.getDescription())
                .drawable(getBeerDrawable(beer.getId()))
                .build();
    }

    private int getBeerDrawable(String id){
        int resource;
        switch (id){
            case Beer.BEER_ID_IPA:
                resource = R.drawable.label_ipa_simple;
                break;

            case Beer.BEER_ID_DOS:
                resource = R.drawable.label_dos_simple;
                break;

            case Beer.BEER_ID_ARA:
                resource = R.drawable.label_ara_simple;
                break;

            case Beer.BEER_ID_ALCATEIA:
                resource = R.drawable.label_alc_simple;
                break;

            case Beer.BEER_ID_HONEY:
                resource = R.drawable.label_hih_simple;
                break;

            default: // TODO: get drawable error
                resource = R.drawable.label_ipa_simple;
                break;
        }

        return resource;
    }
}
