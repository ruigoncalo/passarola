package pt.passarola.utils;

/**
 * Created by ruigoncalo on 20/12/15.
 */
public class RandomJokesGenerator {

    public static String giveMeAJoke(int index){
        String joke;
        switch (index % 5){
            case 0:
                joke = "Don't be a vagina and put a Passarola into you.";
                break;

            case 1:
                joke = "Don't be a chicken and go drink a Passarola in here.";
                break;

            case 2:
                joke = "Don't be a sissy and do Passarola all the way.";
                break;

            case 3:
                joke = "Don't be a fag and go lick a Passarola.";
                break;

            case 4:
                joke = "Don't be a wuss and go grab a Passarola.";
                break;

            default:
                joke = "Passarola gives diarrhea.";
                break;
        }

        return joke;
    }
}
