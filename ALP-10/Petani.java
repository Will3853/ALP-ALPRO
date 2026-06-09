import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
public class Petani extends User {
    private String lokasi;
    private int totalPendapatan;
    private ArrayList<Produk> daftarProduk = new ArrayList<>();
    private Queue <ArrayList<ItemKeranjang>> pesanan = new ArrayDeque<>();
    private ArrayList<ItemKeranjang> negoList = new ArrayList<>();
    private ArrayList<Suggestion> suggestionList = new ArrayList<>();
    
    public Petani(String nama, String password, String lokasi, String noHP) {
        super(nama, password);
        this.lokasi = lokasi;
        this.totalPendapatan = 0;
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
            if (!s.isGiliranPembeli()) {
                System.out.println(">> Dari Pembeli");
            } else {
                System.out.println(">> Dari Petani");
            }
            System.out.printf("%-4d %-20s %-15s %-12s Rp%,8d %-10d\n",
                no++,
                s.getNamaProduk(),
                s.getPembeli().getNama(),
                s.getKategori(),
                s.getHargaUsulan(),
                s.getKuantitas());
            System.out.println("---------------------------------------------------------------------------------------------");

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

        ItemKeranjang item = new ItemKeranjang(p, s.getKuantitas(), s.getPembeli());
        item.setStatusPesanan(StatusPesanan.PENDING);
        s.getPembeli().tambahProduk(item);
        s.getPembeli().getSuggestionList().remove(s);
        suggestionList.remove(i);

        System.out.println("Suggestion diterima!");
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
        s.getPembeli().getSuggestionList().remove(s);
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
        System.out.print("Masukkan kuantitas baru: ");
        int kuantitasBaru = sc.nextInt();
        if (kuantitasBaru <= 0) {
            System.out.println("Kuantitas baru tidak valid!");
            return;
        }
        s.setKuantitas(kuantitasBaru);
        s.setHargaUsulan(harga);
        s.setGiliranPembeli(true);
        System.out.println("Counter berhasil!");
    }
    public void addPesanan(ItemKeranjang item) {
        Pembeli pembeli = item.getPembeli();

        for (ArrayList<ItemKeranjang> group : pesanan) {
            if (!group.isEmpty() && group.get(0).getPembeli().equals(pembeli)) {
                group.add(item);
                return;
            }
        }
        ArrayList<ItemKeranjang> newGroup = new ArrayList<>();
        newGroup.add(item);
        pesanan.add(newGroup);
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
            item.getProduk().getPetani().addPesanan(item);
            item.getPembeli().tambahProduk(item);
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
            System.out.print("Harga baru:");
            int hargaBaru = s.nextInt();

            if (hargaBaru <= 0) {
                System.out.println("Harga baru tidak valid!");
                return;
            }
            ItemKeranjang item = negoList.get(i);
            item.setStatusNego(StatusNego.PENDING);

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
            "Nama Produk", "Pembeli", "Lokasi", "Kategori", "Harga", "Kuantitas", "Total");

        System.out.println("---------------------------------------------------------------------------------------------");

        ArrayList<ItemKeranjang> itemKeranjang = pesanan.peek();
        for (ItemKeranjang item : itemKeranjang) {
            System.out.printf("%-20s %-15s %-15s %-12s Rp%,10d %-10d Rp%,10d\n",
            item.getProduk().getNama(),
            item.getPembeli().getNama(),
            item.getPembeli().getLokasi(),
            item.getProduk().getKategori(),
            item.getHargaAsli(),
            item.getKuantitas(),
            item.hitungTotal());
        }
        System.out.print("Pilih Produk: ");
        int produkNo = s.nextInt() - 1;
        if (produkNo < 0 || produkNo >= itemKeranjang.size()) {
            System.out.println("Tidak ada dalam pilihan.");
            return;
        }
        System.out.println("1. Selesaikan Pesanan");
        System.out.println("2. Batalkan Pesanan");
        System.out.println("3. Kembali");
        System.out.print("Pilih: ");
        int pilih = s.nextInt();
        if (pilih < 1 || pilih > 3) {
            System.out.println("Tidak ada dalam pilihan.");
        }
        ItemKeranjang current = itemKeranjang.get(produkNo);
        Produk produk = current.getProduk();
        switch (pilih) {
            case 1:
                boolean isFinished = false;
                if (produk.getStok() >= current.getKuantitas()) {
                    current.setStatusPesanan(StatusPesanan.FINISHED);
                    produk.getPetani().totalPendapatan += current.hitungTotal();
                    produk.addTotal(current.hitungTotal());
                    produk.addTotalStokTerjual(current.getKuantitas());
                    for (ItemKeranjang itemKeranjang2 : itemKeranjang) {
                        if (itemKeranjang2.getStatusPesanan() == StatusPesanan.PENDING) {
                            isFinished = false;
                            break;
                        } else {
                            isFinished = true;
                        }
                    }
                    if (isFinished) {
                        pesanan.remove();
                    }
                } else {
                    System.out.println("Stok tidak cukup!");
                }
                break;
            case 2:
                isFinished = false;
                current.setStatusPesanan(StatusPesanan.CANCELLED);
                int refund = hitungRefund(current, pesanan);
                current.getPembeli().getWallet().topUp(refund);
                pesanan.remove();
                System.out.println("Pesanan dibatalkan. Refund: Rp" + refund);
                                    for (ItemKeranjang itemKeranjang2 : itemKeranjang) {
                        if (itemKeranjang2.getStatusPesanan() == StatusPesanan.PENDING) {
                            isFinished = false;
                        } else {
                            isFinished = true;
                        }
                    }
                    if (isFinished) {
                        pesanan.remove();
                    }
                break;
            case 3:
                break;
            default:
                
                throw new AssertionError();
        }
    }
    public int hitungRefund(ItemKeranjang item, Queue<ArrayList<ItemKeranjang>> pesanan) {
        int refund = 0;
        int harga = item.getProduk().getHargaDiskon() * item.getKuantitas();

        refund += harga;

        Petani p = item.getProduk().getPetani();

        boolean masihAda = false;

        for (ArrayList<ItemKeranjang> a : pesanan) {
            for (ItemKeranjang i : a) {
                if (i != item &&
                    i.getProduk().getPetani().equals(p) &&
                    i.getStatusPesanan() != StatusPesanan.CANCELLED) {
                    masihAda = true;
                    break;
                }   
            }
        }

        if (!masihAda) {
            refund += item.getPembeli().hitungOngkir(item.getProduk().getPetani().getLokasi(), item.getPembeli().getLokasi());
        }
        return refund;
    }

    public ArrayList<Produk> getProdukList() {
        return daftarProduk;
    }
    public void terimaPesanan(ArrayList<ItemKeranjang> o) {
        for (ItemKeranjang t : o) {
            t.getProduk().getPetani().addPesanan(t);
        }
    }
    public void tambahProduk(Produk produk) {
        daftarProduk.add(produk);
    }
    
    public boolean login(String nama, String password) {
        if (!status) {
            return false;
        }
        return this.nama.equals(nama) && this.password.equals(password);
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
    
public void LapororanPenjualan(){
System.out.println("=== Laporan Hasil Penjualan ===");
    int totalPendapatan = 0;
    int totalProdukTerjual = 0;

    for (Produk item : daftarProduk) {
        int jumlah = item.getTotalStokTerjual();
        int subtotal = item.getTotalPenjualan();
        System.out.println("Produk: " + item.getNama());
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
    public String getNama() {
        return this.nama;
    }

    public String getLokasi() {
        return this.lokasi;
    }

    public int getTotalPendapatan() {
        return totalPendapatan;
    }
    
}


    

