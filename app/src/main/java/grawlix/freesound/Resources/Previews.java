package grawlix.freesound.Resources;

import com.google.gson.annotations.SerializedName;

/**
 * Created by luismierez on 8/5/14.
 */
public class Previews {

    @SerializedName("preview-lq-ogg")
    private String previewLqOgg;
    @SerializedName("preview-lq-mp3")
    private String previewLqMp3;
    @SerializedName("preview-hq-ogg")
    private String previewHqOgg;
    @SerializedName("preview-hq-mp3")
    private String previewHqMp3;

    public String getPreviewLqOgg() {
        return previewLqOgg;
    }

    public void setPreviewLqOgg(String previewLqOgg) {
        this.previewLqOgg = previewLqOgg;
    }

    public String getPreviewLqMp3() {
        return previewLqMp3;
    }

    public void setPreviewLqMp3(String previewLqMp3) {
        this.previewLqMp3 = previewLqMp3;
    }

    public String getPreviewHqOgg() {
        return previewHqOgg;
    }

    public void setPreviewHqOgg(String previewHqOgg) {
        this.previewHqOgg = previewHqOgg;
    }

    public String getPreviewHqMp3() {
        return previewHqMp3;
    }

    public void setPreviewHqMp3(String previewHqMp3) {
        this.previewHqMp3 = previewHqMp3;
    }

}
