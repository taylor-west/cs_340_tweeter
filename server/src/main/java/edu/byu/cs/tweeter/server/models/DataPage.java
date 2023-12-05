package edu.byu.cs.tweeter.server.models;

import java.util.ArrayList;
import java.util.List;

/**
 * A page of data returned by the database.
 *
 * @param <T> type of data objects being returned.
 */
public class DataPage<T> {
    private List<T> values = new ArrayList<>(); // page of values returned by the database
    private boolean hasMorePages; // Indicates whether there are more pages of data available to be retrieved

    public DataPage() {
        setValues(new ArrayList<T>());
        setHasMorePages(false);
    }

    public void setValues(List<T> values) {
        this.values = values;
    }
    public List<T> getValues() {
        return values;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }
    public boolean getHasMorePages() {
        return hasMorePages;
    }

    @Override
    public String toString() {
        String values_string = "[";
        for(T item : values){
            values_string += item.toString();
            values_string += ", ";
        }
        values_string += "]";

        return "DataPage{" +
                "hasMorePages=" + hasMorePages +
                ", values=" + values_string +
                '}';
    }
}