import com.google.gson.annotations.SerializedName;

public class EmployeeData {
    @SerializedName("lokasi")
    private String lokasi;

    @SerializedName("nama")
    private String nama;

    @SerializedName("nopek")
    private String nopek;

    public String getLokasi() {
        return lokasi;
    }

    public String getNama() {
        return nama;
    }

    public String getNopek() {
        return nopek;
    }
}
