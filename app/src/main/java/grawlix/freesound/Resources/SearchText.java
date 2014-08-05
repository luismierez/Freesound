package grawlix.freesound.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luismierez on 8/5/14.
 */
public class SearchText {

    private Integer count;
    private String next;
    private List<Result> results = new ArrayList<Result>();
    private Object previous;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public Object getPrevious() {
        return previous;
    }

    public void setPrevious(Object previous) {
        this.previous = previous;
    }
}
