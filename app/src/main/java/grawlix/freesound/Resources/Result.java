package grawlix.freesound.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luismierez on 8/5/14.
 */
public class Result {

    private Integer id;
    private String name;
    private List<String> tags = new ArrayList<String>();
    private String username;
    private Previews previews;
    private Images images;
    private String geotag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Previews getPreviews() {
        return previews;
    }

    public void  setPreviews(Previews previews) {
        this.previews = previews;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public String getGeoTag() {
        return geotag;
    }

    public void setGeoTag(String geotag) {
        this.geotag = geotag;
    }

}
