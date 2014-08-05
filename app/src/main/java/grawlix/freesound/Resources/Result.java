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
    private String license;
    private String username;

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

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
