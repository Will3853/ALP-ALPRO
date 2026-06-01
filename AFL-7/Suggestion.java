import java.time.LocalDate;

public class Suggestion {
    private String namaProduk;
    private Kategori kategori;
    private int hargaUsulan;
    private int kuantitas;
    private Pembeli pembeli;
    private Petani petani;
    private StatusNego status;
    private boolean giliranPembeli;

    public Suggestion(String namaProduk, Kategori kategori, int hargaUsulan, int kuantitas, Pembeli pembeli, Petani petani) {
        this.namaProduk = namaProduk;
        this.kategori = kategori;
        this.hargaUsulan = hargaUsulan;
        this.kuantitas = kuantitas;
        this.pembeli = pembeli;
        this.petani = petani;
        this.status = StatusNego.PENDING;
        this.giliranPembeli = false;
    }

    public ItemKeranjang toItemKeranjang() {
        Produk p = new Produk( namaProduk, petani.getNama(), kategori, hargaUsulan, kuantitas, LocalDate.now().plusDays(7),petani);
        return new ItemKeranjang(p, kuantitas, pembeli);
    }

    public String getNamaProduk() { return namaProduk; }
    public Kategori getKategori() { return kategori; }
    public int getHargaUsulan() { return hargaUsulan; }
    public int getKuantitas() { return kuantitas; }
    public Pembeli getPembeli() { return pembeli; }
    public Petani getPetani() { return petani; }
    public boolean isGiliranPembeli() { return giliranPembeli; }
    public StatusNego getStatus() { return status; }

    public void setHargaUsulan(int harga) { this.hargaUsulan = harga; }
    public void setStatus(StatusNego status) { this.status = status; }
    public void setGiliranPembeli(boolean val) { this.giliranPembeli = val; }
    public void setKuantitas(int kuantitas) { this.kuantitas = kuantitas; }

}