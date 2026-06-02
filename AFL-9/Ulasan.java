public class Ulasan {
    private String nama;
    private String komen;
    private int rating;

    public Ulasan(String nama, String komen, int rating) {
        this.nama = nama;
        this.komen = komen;
        this.rating = rating;
    }
    public String getNama () {
        return nama;
    }
    public String getKomen () {
        return komen;
    }
    public int getRating(){
        return rating;
    }
}