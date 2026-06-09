import java.time.LocalDate;
import java.util.ArrayList;

public class Produk {

    private String nama, ProdukList,  namaPetani;
    private Kategori kategori;
    private int hargaAsli;
    private int harga;
    private int stok;
    private LocalDate expiredDate;
    private ArrayList<Ulasan> ulasan;
    private Petani petani;
    private Event event;
    private int totalPenjualan;
    private int totalStokTerjual;
    public Produk(String nama, String namaPetani, Kategori kategori, int harga, int stok, LocalDate expiredDate, Petani petani) {
        this.nama = nama;
        this.namaPetani = namaPetani;
        this.kategori = kategori;
        this.harga = harga;
        this.hargaAsli = harga;
        this.stok = stok;                    
        this.expiredDate = expiredDate;
        this.ulasan = new ArrayList<>();
        this.petani = petani;
    }
    public void tambahUlasan(Ulasan ulasan) {
        this.ulasan.add(ulasan);
    }
    public String getNama() {
        return nama;
    }
    public String getNamaPetani() {
        return namaPetani;
    }
    public Kategori getKategori() {
        return kategori;
    }
    public int getHarga() {
        return harga;
    }
    public int getStok() {
        return stok;
    }
    public LocalDate getExpiredDate() {
        return expiredDate;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public void setHarga(int harga) {
        this.harga = harga;
    }
    public void setStok(int stok) {
        this.stok = stok;
    }
    public void setExpiredDate(LocalDate expiredDate) {
        this.expiredDate = expiredDate;
    }
    public boolean isExpired() {
        return LocalDate.now().isAfter(expiredDate);
    }

    public Petani getPetani (){
        return petani;
    }
    public String getstatus(){
        if (isExpired()) {
            return "Kadaluarsa";
        } else {
            return "Masih berlaku";
        }
    }

    public void addTotal(int totalPenjualan) {
        this.totalPenjualan += totalPenjualan;
    }

    public void addTotalStokTerjual(int totalStokTerjual) {
        this.totalStokTerjual += totalStokTerjual;
    }

    public int getTotalPenjualan() {
        return totalPenjualan;
    }

    public int getTotalStokTerjual() {
        return totalStokTerjual;
    }
    public void displayUlasan() {
        if (ulasan.isEmpty()) {
            System.out.println("Belum ada ulasan");
            return;
        }
        int nomor = 1;
        for (Ulasan ulasan2 : ulasan) {
            System.out.printf("%-4d %-20s %-15s %-12s\n",
                nomor++,
                ulasan2.getNama(),
                ulasan2.getRating() + "/5",
                ulasan2.getKomen());
        }
    }
    private int diskonPersen = 0;  // default 0%


public int getDiskonPersen() {
    return diskonPersen;
}

public boolean isDiskon() {
    return diskonPersen > 0;
}

public int getHargaDiskon() {
    if (diskonPersen == 0) return harga;
    return harga - (harga * diskonPersen / 100);
}
public void setHargaDiskon(int diskon, int maks) {
    if (diskon == 0) {
        this.harga = hargaAsli;
        return;
    }

    int potongan = hargaAsli * diskon / 100;

    if (potongan > maks) {
        potongan = maks;
    }

    this.harga = hargaAsli - potongan;
}
public Event getEvent() {
    return event;
}

public void setEvent(Event event) {
    this.event = event;
}

    public int getHargaAsli() {
        return hargaAsli;
    }

}
