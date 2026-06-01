import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Pembeli extends User{
    private ArrayList<ItemKeranjang> keranjang;
    private String noHP;
    private Stack<ItemKeranjang> history;
    private  Wallet wallet;
    private ArrayList<ItemKeranjang> negoList;
    private ArrayList<Suggestion> suggestionList = new ArrayList<>();
    public Pembeli(String nama, String password, String lokasi, String noHP) {
        super(nama, password);
        wallet = new Wallet();
        this.lokasi = lokasi;
        this.noHP = noHP;
        keranjang = new ArrayList<>();
        history = new Stack<>();
        negoList = new ArrayList<>();
    }  

    public void laporkan(User target, String alasan, ArrayList<Laporan> daftar) {
        Laporan laporan = new Laporan(this, target, alasan);
        daftar.add(laporan);
        System.out.println("Laporan berhasil dikirim!");
    }

    public int hitungTotalOngkir() {
        ArrayList<Petani> petaniSudahDihitung = new ArrayList<>();
        int totalOngkir = 0;

        for (ItemKeranjang item : keranjang) {
            Petani p = item.getProduk().getPetani();

            if (!petaniSudahDihitung.contains(p)) {

                String lokasiPenjual = p.getLokasi();
                String lokasiPembeli = item.getPembeli().getLokasi();

                totalOngkir += hitungOngkir(lokasiPenjual, lokasiPembeli);

                petaniSudahDihitung.add(p);
            }
        }

        return totalOngkir;
    }

    public int hitungOngkir(String lokasiPenjual, String lokasiPembeli) {
        if (lokasiPenjual.equalsIgnoreCase(lokasiPembeli)) {
            return 5000;
        } else {
            return 10000;
        }
    }

    public void ajukanSuggestion(Petani petani, String namaProduk, Kategori kategori, int harga, int qty) {
         if (wallet.getSaldo() < harga * qty) {
            System.out.println("Saldo tidak mencukupi!");
            return;
        }
        if (harga <= 0) {
            System.out.println("Harga baru tidak valid!");
            return;
        }
        Suggestion s = new Suggestion(namaProduk, kategori, harga, qty, this, petani);
        petani.addSuggestion(s);   
        suggestionList.add(s);    
        System.out.println("Suggestion berhasil dikirim!");
    }

   public void terimaSuggestion(int i) {
        if (i < 0 || i >= suggestionList.size()) {
            System.out.println("Index tidak valid!");
            return;
        }
        Suggestion s = suggestionList.get(i);
        if (!s.isGiliranPembeli()) {
            System.out.println("Menunggu petani!");
            return;
        }
        ItemKeranjang item = s.toItemKeranjang();
        if (wallet.getSaldo() < item.getKuantitas() * item.getHargaNego()) {
            System.out.println("Saldo tidak mencukupi!");
            return;
        }
        s.setStatus(StatusNego.DITERIMA);
    
        this.tambahProduk(item);
        s.getPetani().addPesanan(item);
        suggestionList.remove(i);
        s.getPetani().getSuggestionList().remove(s);
        System.out.println("Suggestion disetujui!");
    }
    public void tolakSuggestion(int i) {
        if (i < 0 || i >= suggestionList.size()) {
            System.out.println("Index tidak valid!");
            return;
        }
        Suggestion s = suggestionList.get(i);
        if (!s.isGiliranPembeli()) {
            System.out.println("Menunggu petani!");
            return;
        }
        s.setStatus(StatusNego.DITOLAK);
        ItemKeranjang item = s.toItemKeranjang();
        item.setStatusPesanan(StatusPesanan.CANCELLED);
        this.addToHistory(item);
        suggestionList.remove(i);
        s.getPetani().getSuggestionList().remove(s);
        System.out.println("Suggestion ditolak!");
    }

    public void counterSuggestion(int i) {
        if (i < 0 || i >= suggestionList.size()) {
            System.out.println("Index tidak valid!");
            return;
        }

        Suggestion s = suggestionList.get(i);
        if (!s.isGiliranPembeli()) {
            System.out.println("Menunggu petani!");
            return;
        }
            Scanner sc = new Scanner(System.in);
            System.out.print("Masukkan harga baru: ");
            int hargaBaru = sc.nextInt();
            if (hargaBaru <= 0) {
                System.out.println("Harga baru tidak valid!");
                return;
            }
            s.setHargaUsulan(hargaBaru);
            s.setGiliranPembeli(false);
            System.out.println("Counter berhasil dikirim!");
    }

    public void displaySuggestion() {
        if (suggestionList.isEmpty()) {
            System.out.println("Tidak ada suggestion");
            return;
        }

        System.out.printf("%-4s %-20s %-15s %-12s %-10s %-10s %-10s\n",
            "No", "Produk", "Petani", "Kategori", "Harga", "Qty", "Status");

        int no = 1;
        for (Suggestion s : suggestionList) {
            if (s.isGiliranPembeli()) {
                System.out.println(">> Dari Pembeli");
            } else {
                System.out.println(">> Dari Petani");
            }
            
            System.out.printf("%-4d %-20s %-15s %-12s Rp%,8d %-10d %-10s\n",
                no++,
                s.getNamaProduk(),
                s.getPetani().getNama(),
                s.getKategori(),
                s.getHargaUsulan(),
                s.getKuantitas(),
                s.getStatus());
            System.out.println("---------------------------------------------------------------------------------------------");

        }
    }
    public void ajukanNegosiasi(ItemKeranjang item, int hargaBaru) {
        if (item.getIsNego()) {
            System.out.println("Produk ini sudah di negosiasi");
            return;
        }
        if (wallet.getSaldo() < item.getKuantitas() * hargaBaru) {
            System.out.println("Saldo tidak mencukupi!");
            return;
        }
        if (hargaBaru <= 0) {
            System.out.println("Harga baru tidak valid!");
            return;
        }
        item.setStatusNego(StatusNego.PENDING);
        item.setHargaNego(hargaBaru);
        item.getProduk().getPetani().addNegoList(item);
        negoList.add(item);
        keranjang.remove(item);
    }


    public void terimaNego(int i) {
        if (i < 0 || i >= negoList.size()) {
        System.out.println("Index tidak valid!");
        return;
        }
        if (negoList.get(i).getPembeliNego()) { 
            ItemKeranjang item = negoList.get(i);

            wallet.bayar(item.getKuantitas() * item.getHargaNego());
            item.setHargaAsli(item.getHargaNego());
            item.setIsNego(true);
            item.setStatusNego(StatusNego.DITERIMA);
            keranjang.add(item);
            item.getProduk().getPetani().getNegoList().remove(item);
            System.out.println("Negosiasi berhasil diterima!");
        } else {
            System.out.println("Sedang menunggu putusan petani");
        }
    }

    public void tolakNego(int i) {
        if (i < 0 || i >= negoList.size()) {
            System.out.println("Index tidak valid!");
            return;
        }
        if (negoList.get(i).getPembeliNego()) {
            ItemKeranjang item = negoList.get(i);
            negoList.remove(i);
            System.out.println("Negosiasi berhasil ditolak!");
            item.setStatusNego(StatusNego.DITOLAK);
            item.setStatusPesanan(StatusPesanan.CANCELLED);
            item.getProduk().getPetani().getNegoList().remove(item);
            item.setIsNego(true);
            item.getPembeli().addToHistory(item);
            negoList.remove(i);   
        } else {
            System.out.println("Sedang menunggu putusan petani");
        }
    }

    public void counterNego(int i) {
        Scanner s = new Scanner(System.in);
        if (i < 0 || i >= negoList.size()) {
            System.out.println("Index tidak valid!");
            return;
        }
        if (negoList.get(i).getPembeliNego()) {
            ItemKeranjang item = negoList.get(i);
            if (wallet.getSaldo() < item.getKuantitas() * item.getHargaNego()) {
                System.out.println("Saldo tidak mencukupi!");
                return;
            }
            System.out.print("Harga baru: ");
            int hargaBaru = s.nextInt();
            if (hargaBaru <= 0) {
                System.out.println("Harga baru tidak valid!");
                return;
            }
            negoList.get(i).setPembeliNego(false);
            item.setStatusNego(StatusNego.PENDING);
            item.setHargaNego(hargaBaru);
        } else {
            System.out.println("Sedang menunggu putusan petani");

        }

    }

    public void addNegoList(ItemKeranjang item){
        negoList.add(item);
    }

    public int totalKeranjang() {
        int total = 0;
        for (ItemKeranjang produk : keranjang) {
            total += produk.hitungTotal();
        }
        return total;
    }

    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public boolean login(String nama, String password) {
        if (!status) {
            return false;
        }
        return this.nama.equals(nama) && this.password.equals(password);
    }

    public ArrayList<ItemKeranjang> getItemKeranjang (){
        return keranjang;
    }
    public void displayNego () {
        if (negoList.isEmpty()) {
            System.out.println("Daftar Negosiasi Kosong");
            return;
        }
        System.out.printf("%-4s %-20s %-15s %-12s %-12s %-12s %-10s %-12s\n",
    "No", "Produk", "Petani", "Kategori", "Harga Asli", "Harga Nego", "Kuantitas", "Total");
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
                o.getProduk().getNamaPetani(),
                o.getProduk().getKategori(),
                o.getHargaAsli(),
                o.getHargaNego(),
                o.getKuantitas(),
                o.totalNego());

            System.out.println("---------------------------------------------------------------------------------------------");
        }
        
    }

    public void checkout(ArrayList<User> userList) {
        if (keranjang.isEmpty()) {
            System.out.println("Keranjang kosong!");
            return;
        }
        if (wallet.getSaldo() < totalKeranjang()) {
            System.out.println("Saldo kurang");
            return;
        }
        wallet.bayar(totalKeranjang()); 
        for (User user2 : userList) {

        if (user2 instanceof Petani) {
            Petani petani = (Petani) user2;

            ArrayList<ItemKeranjang> itemsPetani = new ArrayList<>();

            for (ItemKeranjang item : keranjang) {
                if (item.getProduk().getNamaPetani().equals(petani.getNama())) {
                    itemsPetani.add(item);
                    history.push(item);
                }
            }

            if (!itemsPetani.isEmpty()) { 
                petani.terimaPesanan(itemsPetani); 
            }
        }
    }

        System.out.println("Checkout berhasil!");
        keranjang.clear();
        
    }
    public void showHistory() {
        if (history.isEmpty()) {
            System.out.println("Riwayat anda kosong");
            return;
        }

        Stack<ItemKeranjang> temp = new Stack<>();
        int no =1;
        while (!history.isEmpty()) {
            ItemKeranjang o = history.pop();
            int total = o.getHargaAsli() * o.getKuantitas();
                System.out.printf("%-4s %-20s %-15s %-12s Rp%,8d %-8d Rp%,8d %-8s\n",
                no,
                o.getProduk().getNama(),
                o.getProduk().getNamaPetani(),
                o.getProduk().getKategori(),
                o.getHargaAsli(),
                o.getKuantitas(),
                total,
                o.getStatusPesanan());
                no++; 
            temp.push(o);
        }

        while (!temp.isEmpty()) {
            history.push(temp.pop());
        }
    }

    public void showPurchased() {
        if (history.isEmpty()) {
            System.out.println("History kosong.");
            return;
        }

        Stack<ItemKeranjang> temp = new Stack<>();
        int no = 1;
        while (!history.isEmpty()) {
            ItemKeranjang o = history.pop();

            if (o.getStatusPesanan() == StatusPesanan.FINISHED) {
                int total = o.getHargaAsli() * o.getKuantitas();
                System.out.printf("%-4d %-20s %-15s %-12s Rp%,8d %-8d Rp%,8d %-8s\n",
                    no,
                    o.getProduk().getNama(),
                    o.getProduk().getNamaPetani(),
                    o.getProduk().getKategori(),
                    o.getHargaAsli(),
                    o.getKuantitas(),
                    total,
                    o.getStatusPesanan());
                no++;
            }

            temp.push(o);
        }

        while (!temp.isEmpty()) {
            history.push(temp.pop());
        }
    }

    public boolean isPurchasedEmpty() {
        boolean status = true;
        if (history.isEmpty()) {
            System.out.println("History kosong.");
            return true;
        }

        Stack<ItemKeranjang> temp = new Stack<>();

        while (!history.isEmpty()) {
            ItemKeranjang o = history.pop();

            if (o.getStatusPesanan() == StatusPesanan.FINISHED) {
                status = false;
            }

            temp.push(o);
        }

        while (!temp.isEmpty()) {
            history.push(temp.pop());
        }
        return status;
    }

    public boolean isHistoryEmpty() {
        return history.isEmpty();
    }

    public ArrayList<ItemKeranjang> purchased() {
        if (history.isEmpty()) {
            System.out.println("History kosong.");
            return null;
        }

        Stack<ItemKeranjang> temp = new Stack<>();
        ArrayList<ItemKeranjang> items = new ArrayList<>();
        while (!history.isEmpty()) {
            ItemKeranjang o = history.pop();
            if (o.getStatusPesanan() == StatusPesanan.FINISHED) {
                items.add(o);
            }

            temp.push(o);
        }

        while (!temp.isEmpty()) {
            history.push(temp.pop());
        }
        return items;
    }

    public boolean hasPurchased(Produk p) {
        Stack<ItemKeranjang> temp = new Stack<>();
        boolean found = false;

        while (!history.isEmpty()) {
            ItemKeranjang item = history.pop();

            if (item.getProduk().equals(p)) {
                found = true;
            }

            temp.push(item);
        }

        // restore stack
        while (!temp.isEmpty()) {
            history.push(temp.pop());
        }

        return found;
    }

    public void BeriUlasan(Produk p) {
        if (!hasPurchased(p)) {
            System.out.println("Harus beli dulu!");
            return;
        }

        Scanner sc = new Scanner(System.in);
        Scanner s = new Scanner(System.in);

        System.out.print("Rating: ");
        int rating = sc.nextInt();

        System.out.print("Komentar: ");
        String comment = s.nextLine();

        p.tambahUlasan(new Ulasan(nama, comment, rating));
        System.out.println("Review ditambahkan!");
    }

    public void tambahProduk(ItemKeranjang item) {
        keranjang.add(item);
    }

    public void addToHistory(ItemKeranjang item) {
        history.push(item);
    }

    public void displayProduk() {
        
    }

    public String getLokasi() {
        return lokasi;
    }


    public ArrayList<ItemKeranjang> getNegoList() {
        return negoList;
    }

    public void addHistory(ItemKeranjang item) {
        history.add(item);
    }
    public ArrayList<Suggestion> getSuggestionList() {
        return suggestionList;
    }

    public int hitungTotalKeranjang() {
        int total = 0;
        for (ItemKeranjang itemKeranjang : keranjang) {
            total =+ itemKeranjang.hitungTotal();
        }
        return total;
    }

    public void displayCheckout() {
            System.out.printf("%-4s %-20s %-15s %-12s %-8s %-10s %-8s\n",
            "No.", "Nama Produk", "Petani", "Kategori", "Harga", "Kuantitas", "Total");
        System.out.println("---------------------------------------------------------------------");
        if (getItemKeranjang().size() == 0) {
            System.out.println("Keranjang Pembeli Kosong");
            return;
        }
        int nomor = 1;
        for (ItemKeranjang item : getItemKeranjang()) {
            System.out.print(nomor + ".   ");
            item.displayProduk();
            nomor++;
        }
    }
}
