package grawlix.freesound.Resources;


import com.google.gson.annotations.SerializedName;

/**
 * Created by luismierez on 8/5/14.
 */
public class Images {

    @SerializedName("waveform_l")
    private String waveformL;
    @SerializedName("waveform_m")
    private String waveformM;
    @SerializedName("spectral_m")
    private String spectralM;
    @SerializedName("spectral_l")
    private String spectralL;

    public String getWaveformL() {
        return waveformL;
    }

    public void setWaveformL(String waveformL) {
        this.waveformL = waveformL;
    }

    public String getWaveformM() {
        return waveformM;
    }

    public void setWaveformM(String waveformM) {
        this.waveformM = waveformM;
    }

    public String getSpectralM() {
        return spectralM;
    }

    public void setSpectralM(String spectralM) {
        this.spectralM = spectralM;
    }

    public String getSpectralL() {
        return spectralL;
    }

    public void setSpectralL(String spectralL) {
        this.spectralL = spectralL;
    }

}
