package grawlix.freesound.Resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luismierez on 8/5/14.
 */
public class Sound {

    private Integer id;
    private String url;
    private String name;
    private List<String> tags = new ArrayList<String>();
    private String description;
    private String geotag;
    private String created;
    private String license;
    private String type;
    private Integer channels;
    private Integer filesize;
    private Integer bitrate;
    private Integer bitdepth;
    private Double duration;
    private Double samplerate;
    private String username;
    private String pack;
    private String download;
    private String bookmark;
    private Previews previews;
    private Images images;
    private Integer numDownloads;
    private Double avgRating;
    private Integer numRatings;
    private String rate;
    private String comments;
    private Integer numComments;
    private String comment;
    private String similarSounds;
    private String analysis;
    private String analysisFrames;
    private String analysisStats;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeotag() {
        return geotag;
    }

    public void setGeotag(String geotag) {
        this.geotag = geotag;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getChannels() {
        return channels;
    }

    public void setChannels(Integer channels) {
        this.channels = channels;
    }

    public Integer getFilesize() {
        return filesize;
    }

    public void setFilesize(Integer filesize) {
        this.filesize = filesize;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public Integer getBitdepth() {
        return bitdepth;
    }

    public void setBitdepth(Integer bitdepth) {
        this.bitdepth = bitdepth;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getSamplerate() {
        return samplerate;
    }

    public void setSamplerate(Double samplerate) {
        this.samplerate = samplerate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public Previews getPreviews() {
        return previews;
    }

    public void setPreviews(Previews previews) {
        this.previews = previews;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public Integer getNumDownloads() {
        return numDownloads;
    }

    public void setNumDownloads(Integer numDownloads) {
        this.numDownloads = numDownloads;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Integer getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(Integer numRatings) {
        this.numRatings = numRatings;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getNumComments() {
        return numComments;
    }

    public void setNumComments(Integer numComments) {
        this.numComments = numComments;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSimilarSounds() {
        return similarSounds;
    }

    public void setSimilarSounds(String similarSounds) {
        this.similarSounds = similarSounds;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getAnalysisFrames() {
        return analysisFrames;
    }

    public void setAnalysisFrames(String analysisFrames) {
        this.analysisFrames = analysisFrames;
    }

    public String getAnalysisStats() {
        return analysisStats;
    }

    public void setAnalysisStats(String analysisStats) {
        this.analysisStats = analysisStats;
    }


}
