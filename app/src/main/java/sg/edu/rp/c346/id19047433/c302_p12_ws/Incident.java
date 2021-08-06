package sg.edu.rp.c346.id19047433.c302_p12_ws;

import java.io.Serializable;
import java.util.Date;

public class Incident implements Serializable {
    private String type;
    private Double latitude;
    private Double longitude;
    private String message;
    private Date date;

    public Incident(String type, Double latitude, Double longitude, String message) {
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
    }

    public Incident(String type, Double latitude, Double longitude, String message, Date date) {
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
