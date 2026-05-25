import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
public class Petani extends User {
    private boolean status;
    private String noHP;
    private ArrayList<Produk> daftarProduk = new ArrayList<>();
    private Queue <ItemKeranjang> pesanan = new ArrayDeque<>();
    private ArrayList<ItemKeranjang> negoList = new ArrayList<>();
    private ArrayList<Suggestion> suggestionList = new ArrayList<>();

    public Petani(String nama, String password, String lokasi, String noHP) {
        super(nama, password);
        this.lokasi = lokasi;
        this.noHP = noHP;
        this.status = true; 
    }
    
    public void addNegoList(ItemKeranjang item){
        negoList.add(item);
    }
    public void addSuggestion(Suggestion s) {
        suggestionList.add(s);
    }

    public void displaySuggestion() {
        if (suggestionList.isEmpty()) {
            System.out.println("Tidak ada suggestion");
            return;
        }

        System.out.printf("%-4s %-20s %-15s %-12s %-10s %-10s\n",
            "No", "Produk", "Pembeli", "Kategori", "Harga", "Qty");

        int no = 1;
        for (Suggestion s : suggestionList) {
            System.out.printf("%-4d %-20s %-15s %-12s Rp%,8d %-10d\n",
                no++,
                s.getNamaProduk(),
                s.getPembeli().getNama(),
                s.getKategori(),
                s.getHargaUsulan(),
                s.getKuantitas());
        }
    }

    public void terimaSuggestion(int i) {
        if (i < 0 || i >= suggestionList.size()) {
            System.out.println("Index tidak valid!");
            return;
        }

        Suggestion s = suggestionList.get(i);
        if (s.isGiliranPembeli()) {
            System.out.println("Menunggu pembeli!");
            return;
        }
        s.setStatus(StatusNego.DITERIMA);

        Produk p = new Produk(
            s.getNamaProduk(),
            this.getNama(),
            s.getKategori(),
            s.getHargaUsulan(),
            s.getKuantitas(),
            java.time.LocalDate.now().plusDays(7),
            this
        );

        tambahProduk(p);

        ItemKeranjang item = new ItemKeranjang(p, s.getKuantitas(), s.getPembeli());

        pesanan.add(item);

        item.setStatusPesanan(StatusPesanan.PENDING);
        s.getPembeli().addToHistory(item);

        suggestionList.remove(i);

        System.out.println("Suggestion diterima → jadi pesanan!");
    }
    
    public void tolakSuggestion(int i) {
        if (i < 0 || i >= suggestionList.size()) {
            System.out.println("Index tidak valid!");
            return;
        }

        Suggestion s = suggestionList.get(i);
        s.setStatus(StatusNego.DITOLAK);
        ItemKeranjang item = s.toItemKeranjang();
        item.setStatusPesanan(StatusPesanan.CANCELLED);
        s.getPembeli().addToHistory(item);
        suggestionList.remove(i);
        System.out.println("Suggestion ditolak!");
    }

    public void counterSuggestion(int i) {
        if (i < 0 || i >= suggestionList.size()) {
            System.out.println("Index tidak valid!");
            return;
        }

        Suggestion s = suggestionList.get(i);
        if (s.isGiliranPembeli()) {
            System.out.println("Menunggu pembeli!");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Harga baru: ");
        int harga = sc.nextInt();
        s.setHargaUsulan(harga);
        s.setGiliranPembeli(true);
        System.out.println("Counter berhasil!");
    }
    public void addPesanan (ItemKeranjang item){
        pesanan.add(item);
    }
    
    public void displayNego () {
        if (negoList.isEmpty()) {
            System.out.println("Daftar Negosiasi Kosong");
            return;
        }
        System.out.printf("%-4s %-20s %-15s %-12s %-12s %-12s %-10s %-12s\n",
    "No", "Produk", "Pembeli", "Kategori", "Harga Asli", "Harga Nego", "Kuantitas", "Total");
        System.out.println("---------------------------------------------------------------------------------------------");

        int no = 1;

        for (ItemKeranjang o : negoList) {

            if (!o.getPembeliNego()) {
                System.out.println(">> Dari Pembeli");
            } else {
                System.out.println(">> Dari Petani");
            }

            System.out.printf("%-4d %-20s %-15s %-12s Rp%,10d Rp%,10d %-10d Rp%,10d\n",
                no++,
                o.getProduk().getNama(),
                o.getPembeli().getNama(),
                o.getProduk().getKategori(),
                o.getHargaAsli(),
                o.getHargaNego(),
                o.getKuantitas(),
                o.totalNego());

            System.out.println("---------------------------------------------------------------------------------------------");
        }
        
    }

    public void terimaNego(int i) {
    if (i < 0 || i >= negoList.size()) {
        System.out.println("Index tidak valid!");
        return;
    }   
        if (!negoList.get(i).getPembeliNego()) {
            ItemKeranjang item = negoList.get(i);
            item.setHargaAsli(item.getHargaNego());
            item.setIsNego(true);
            item.setStatusNego(StatusNego.DITERIMA);
            pesanan.add(item);
            item.getPembeli().addHistory(item);
            item.getPembeli().getNegoList().remove(item);
            negoList.remove(i);
            System.out.println("Negosiasi berhasil diterima!");
        } else {
            System.out.println("Sedang menunggu putusan pembeli");
        }
    }

    public void tolakNego(int i) {
        if (i < 0 || i >= negoList.size()) {
            System.out.println("Index tidak valid!");
            return;
        }
        if (!negoList.get(i).getPembeliNego()) {
            ItemKeranjang item = negoList.get(i);
            System.out.println("Negosiasi berhasil ditolak!");
            item.setStatusNego(StatusNego.DITOLAK);
            item.setStatusPesanan(StatusPesanan.CANCELLED);
            item.setIsNego(true);
            negoList.remove(i);
            item.getPembeli().getNegoList().remove(item);
            item.getPembeli().addToHistory(item);
        } else {
            System.out.println("Sedang menunggu putusan pembeli");
        }
            
    }

    public void counterNego(int i) {
        if (i < 0 || i >= negoList.size()) {
            System.out.println("Index tidak valid!");
            return;
         }
        if (!negoList.get(i).getPembeliNego()) {
            Scanner s = new Scanner(System.in);
            ItemKeranjang item = negoList.get(i);
            item.setStatusNego(StatusNego.PENDING);
            System.out.print("Harga baru:");
            int hargaBaru = s.nextInt();
            item.setHargaNego(hargaBaru);   
            item.setPembeliNego(true);
        } else {
            System.out.println("Sedang menunggu putusan pembeli");
        }
    }
    public ArrayList<ItemKeranjang> getNegoList() {
        return negoList;
    }

    public void prosesPesanan() {
        Scanner s = new Scanner(System.in);

        System.out.println("=== PROSES PESANAN ===");
        if (pesanan.isEmpty()) {
            System.out.println("Tidak ada pesanan");
            return;
        }        
        System.out.printf("%-20s %-15s %-15s %-12s %-12s %-10s %-12s\n",
            "Nama Produk", "Pembeli", "Lokasi", "Kategori", "Harga", "Qty", "Total");

        System.out.println("---------------------------------------------------------------------------------------------");

        ItemKeranjang item = pesanan.peek();

        System.out.printf("%-20s %-15s %-15s %-12s Rp%,10d %-10d Rp%,10d\n",
            item.getProduk().getNama(),
            item.getPembeli().getNama(),
            item.getPembeli().getLokasi(),
            item.getProduk().getKategori(),
            item.getHargaAsli(),
            item.getKuantitas(),
            item.hitungTotal());
        System.out.println("1. Selesaikan Pesanan");
        System.out.println("2. Batalkan Pesanan");
        System.out.println("3. Kembali");
        System.out.print("Pilih: ");
        int pilih = s.nextInt();
        ItemKeranjang current = pesanan.peek();
        Produk produk = current.getProduk();
        switch (pilih) {
            case 1:
                if (produk.getStok() >= current.getKuantitas()) {
                    current.setStatusPesanan(StatusPesanan.FINISHED);
                    produk.setStok(produk.getStok() + current.getKuantitas());
                    pesanan.remove();
                } else {
                    System.out.println("Stok tidak cukup!");
                }
                break;
            case 2:
                current.setStatusPesanan(StatusPesanan.CANCELLED);
                current.getPembeli().getWallet().topUp(current.hitungTotal());
                produk.setStok(produk.getStok() + current.getKuantitas());
                pesanan.remove();
                break;
            default:
                throw new AssertionError();
        }
    }

    public ArrayList<Produk> getProdukList() {
        return daftarProduk;
    }
    public void terimaPesanan(ArrayList<ItemKeranjang> o) {
        for (ItemKeranjang t : o) {
            pesanan.add(t);
        }
    }
    public void tambahProduk(Produk produk) {
        daftarProduk.add(produk);
    }
    
    public boolean login(String nama, String password) {
        return this.nama.equals(nama) && this.password.equals(password);
    }
    public boolean getStatus () {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void editProduk (Produk produk) {
        Scanner s = new Scanner(System.in);
        System.out.println("Nama Baru: ");
        String nama = s.nextLine();
        System.out.println("Harga: " + "Rp. ");
        int hargaDaging = s.nextInt();
        s.nextLine();
        System.out.println("Stok: ");
        int stokDaging = s.nextInt();
        s.nextLine();
        int expiredDay2, expiredMonth2, expiredYear2;
        System.out.println("Tanggal Expired (DD MM YYYY): ");
        expiredDay2 = s.nextInt();
        expiredMonth2 = s.nextInt();
        expiredYear2 = s.nextInt();
        LocalDate expiredDate = LocalDate.of(expiredYear2, expiredMonth2, expiredDay2);
        produk.setNama(nama);
        produk.setExpiredDate(expiredDate);
        produk.setStok(stokDaging);
        produk.setHarga(hargaDaging);
        
    }

    public void hapusProduk (int i) {
        daftarProduk.remove(i);
        System.out.println("Berhasil Di Hapus");
    }
    public ArrayList<Suggestion> getSuggestionList() {
        return suggestionList;
    }
}

public void LapororanPenjualan(){
System.out.println("=== Laporan Hasil Penjualan ===");
    int totalPendapatan = 0;
    int totalProdukTerjual = 0;

    for (ItemKeranjang item : pesanan) {
        Produk produk = item.getProduk();
        int jumlah = item.getKuantitas();
        int subtotal = item.hitungTotal();
    System.out.println("Produk: " + produk.getNama());
        System.out.println("Jumlah Terjual: " + jumlah);
        System.out.println("Subtotal: Rp " + subtotal);
        System.out.println("-----------------------------");

        totalPendapatan += subtotal;
        totalProdukTerjual += jumlah;
    }

    System.out.println("Total Produk Terjual: " + totalProdukTerjual);
    System.out.println("Total Pendapatan: Rp " + totalPendapatan);
    System.out.println("===============================");
}
public void SistemLapor

public void laporkanUser(User user, String alasan) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'laporkanUser'");
}
    System.out.println("=== Sistem Lapor ===")
