import java.util.ArrayList;

public class Event {
    private String namaEvent;
    private int diskonPersen;
    private int maksDiskon;
    private boolean aktif;

    private ArrayList<Produk> produkEvent = new ArrayList<>();

    public Event(String namaEvent, int diskonPersen, int maksDiskon) {
        this.namaEvent = namaEvent;
        this.diskonPersen = diskonPersen;
        this.maksDiskon = maksDiskon;
        this.aktif = false;
    }

    public void daftarProduk(Produk p) {
        if (!produkEvent.contains(p)) {
            produkEvent.add(p);
            System.out.println(p.getNama() + " berhasil masuk event " + namaEvent);
        }
    }

    public void mulaiEvent() {
        aktif = true;

        for (Produk p : produkEvent) {
            p.setHargaDiskon(diskonPersen, maksDiskon);
        }
    }

    public void stopEvent() {
        aktif = false;

        for (Produk p : produkEvent) {
            p.setHargaDiskon(0, 0);
        }
    }

    public ArrayList<Produk> getProdukEvent() {
        return produkEvent;
    }

    public String getNamaEvent() {
        return namaEvent;
    }

    public boolean isAktif() {
        return aktif;
    }
    public int getDiskon() {
        return diskonPersen;
    }
    public int getMaksDiskon() {
        return maksDiskon;
    }
}