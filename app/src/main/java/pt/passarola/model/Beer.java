package pt.passarola.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import pt.passarola.utils.Utils;


/**
 * Created by ruigoncalo on 22/10/15.
 */
public class Beer implements Comparable<Beer> {

    private String id;
    private String brewer;
    private String name;
    @SerializedName("short_name")
    private String shortName;
    @SerializedName("bjcp_style")
    private String style;
    private String ibu;
    private String abv;
    @SerializedName("brewed_at")
    private String brewedAt;
    @SerializedName("collab_with")
    private String collabWith;
    private String barcode;
    @SerializedName("label_pic")
    private String labelPic;
    @SerializedName("label_pic_small")
    private String labelPicSmall;
    @SerializedName("untappd_id")
    private String untappdId;
    @SerializedName("untappd_url")
    private String untappdUrl;
    @SerializedName("ratebeer_id")
    private String ratebeerId;
    @SerializedName("ratebeer_url")
    private String ratebeerUrl;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String endDate;
    private String active;
    @SerializedName("series_id")
    private String seriesId;
    @SerializedName("series_name")
    private String seriesName;
    private String ingredients;
    @SerializedName("medium_description")
    private String mediumDescription;

    public String getId() {
        return id;
    }

    public String getBrewer() {
        return brewer;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getStyle() {
        return style;
    }

    public String getIbu() {
        return ibu;
    }

    public String getAbv() {
        return abv;
    }

    public String getBrewedAt() {
        return brewedAt;
    }

    public String getCollabWith() {
        return collabWith;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getLabelPic() {
        return labelPic;
    }

    public String getLabelPicSmall() {
        return labelPicSmall;
    }

    public String getUntappdId() {
        return untappdId;
    }

    public String getUntappdUrl() {
        return untappdUrl;
    }

    public String getRatebeerId() {
        return ratebeerId;
    }

    public String getRatebeerUrl() {
        return ratebeerUrl;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getActive() {
        return active;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getMediumDescription() {
        return mediumDescription;
    }

    public boolean isValid(){
        return id != null && name != null &&
                style != null && labelPicSmall != null &&
                seriesId != null && startDate != null;
    }

    @Override
    public int compareTo(@NonNull Beer another) {
        if (this.isValid() && another.isValid()) {
            int seriesIdL = Integer.valueOf(this.seriesId);
            int seriesIdR = Integer.valueOf(another.seriesId);
            if(seriesIdL > seriesIdR) {
                return 1;
            } else if(seriesIdL < seriesIdR){
                return -1;
            } else {
               return compareDates(this.getStartDate(), another.getStartDate());
            }
        } else {
            return 0;
        }
    }

    private int compareDates(String dateA, String dateB){
        Date dateFormattedA = Utils.getDateFromString(dateA);
        Date dateFormattedB = Utils.getDateFromString(dateB);

        if(dateFormattedA != null && dateFormattedB != null){
            if(dateFormattedA.after(dateFormattedB)){
                return 1;
            } else {
                return -1;
            }
        } else {
            return 0;
        }
    }
}
