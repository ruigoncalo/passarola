package pt.passarola.model;

import java.util.List;

/**
 * Created by ruigoncalo on 22/10/15.
 */
public class MetaPlaces {

        private String message;
        private int results;
        private List<Place> data;

        public String getMessage() {
            return message;
        }

        public int getResults() {
            return results;
        }

        public List<Place> getData() {
            return data;
        }
}
