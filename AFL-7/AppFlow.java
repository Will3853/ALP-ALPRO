import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class AppFlow {
    private ArrayList<User> userList;
    private Scanner s = new Scanner(System.in);
    private Admin currentUserAdmin;
    private Petani currentUserPetani;
    private Pembeli currentUserPembeli;
    private Admin admin;
    public void start() {
        userList = new ArrayList<>();
        User user = new Pembeli("budi", "password123", "Bandung", "08123");
        Pembeli pembeli1 = (Pembeli) user;
        pembeli1.getWallet().topUp(10000000);
        
        userList.add(user);

        user = new Petani("andi", "password123", "Jakarta", "08234");
        Petani petani1 = (Petani) user;
        LocalDate date = LocalDate.of(2011, 12, 1);
        Produk produk = new Produk("Sawit", petani1.getNama(), Kategori.SAYUR, 10000, 200, date, petani1);
        petani1.tambahProduk(produk);
        date = LocalDate.of(2018, 9, 23);
        produk = new Produk("Appel", petani1.getNama(), Kategori.BUAH, 20000, 200, date, petani1);
        petani1.tambahProduk(produk);
        date = LocalDate.of(2023, 8, 20);
        produk = new Produk("Daging Sapi", petani1.getNama(), Kategori.DAGING, 50000, 200, date, petani1);
        petani1.tambahProduk(produk);
        userList.add(user);
//data dummy
        user = new Petani("mark", "password123", "Batulicin", "08234");
        petani1 = (Petani) user;
        date = LocalDate.of(2011, 12, 1);
        produk = new Produk("Sawit", petani1.getNama(), Kategori.SAYUR, 9000, 200, date, petani1);
        petani1.tambahProduk(produk);
        date = LocalDate.of(2018, 9, 23);
        produk = new Produk("Manga", petani1.getNama(), Kategori.BUAH, 23000, 200, date, petani1);
        petani1.tambahProduk(produk);
        date = LocalDate.of(2023, 8, 20);
        produk = new Produk("Daging Ayam", petani1.getNama(), Kategori.DAGING, 30000, 200, date, petani1);
        petani1.tambahProduk(produk);
        userList.add(user);
        ItemKeranjang item = new ItemKeranjang(produk, 100, pembeli1);
        item.setStatusPesanan(StatusPesanan.FINISHED);
        pembeli1.addToHistory(item);
        produk.tambahUlasan(new Ulasan(pembeli1.getNama(), "bagus", 5));
        admin = new Admin("Admin", "Admin123");
        userList.add(admin);

        int pilih;
        do {

            System.out.println("=== SISTEM MARKETPLACE ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Keluar");
            System.out.print("Pilih: ");
            pilih = s.nextInt();
            boolean register = true;
            s.nextLine();
            User registerUser = null;
            if (pilih == 1) {
                System.out.println("Register Sebagai:");
                System.out.println("1. Petani");
                System.out.println("2. Pembeli");
                System.out.println("3. Kembali");
                System.out.print("Pilih: ");
                int pilihRegister = s.nextInt();
                s.nextLine();
                if (pilihRegister == 1 || pilihRegister == 2) {
                    System.out.print("Nama: ");
                    String nama = s.nextLine();
                    for (User user1 : userList) {
                        if (user1.getNama().equals(nama)) {
                            register = false;
                            System.out.println("Nama tidak tersedia");
                        }
                    }

                    if (register) {
                        System.out.print("Password(8-12 Karakter): ");
                        String pass = s.nextLine();
                        if (pass.length() < 8 || pass.length() > 12) {
                            System.out.println("Password Harus 8-12 Karakter");
                        } else {
                            System.out.print("Lokasi: ");
                            String lokasi = s.nextLine();
                            if (lokasi.isEmpty()) {
                                System.out.println("Lokasi Tidak Boleh Kosong");
                            } else if (lokasi.matches(".*\\d+.*")) {
                                System.out.println("Lokasi Tidak Boleh berupa angka");

                            }
                            System.out.print("No. Handphone: ");
                            String noHP = s.nextLine();

                            if (nama.isBlank() || lokasi.isBlank() || noHP.isBlank()) {
                                System.out.println("Nama, Lokasi atau No. Handphone Tidak Boleh Kosong");
                            } else {
                                if (pilihRegister == 1) {
                                    registerUser = new Petani(nama, pass, lokasi, noHP);
                                    System.out.println("Berhasil Register Sebagai Petani");
                                    userList.add(registerUser);
                                } else if (pilihRegister == 2) {
                                    registerUser = new Pembeli(nama, pass, lokasi, noHP);
                                    System.out.println("Register Berhasil Sebagai Pembeli");
                                    userList.add(registerUser);
                                }
                            }
                        }
                    }

                } else {
                    System.out.println("Tidak ada dalam pilihan");
                }

            } else if (pilih == 2) {
                boolean foundUser = false;
                System.out.print("Nama: ");
                String nama = s.nextLine();

                System.out.print("Password: ");
                String pass = s.nextLine();

                for (User u : userList) {

                    if (u.login(nama, pass)) {
                        foundUser = true;
                        if (u instanceof Petani) {
                            Petani petani = (Petani) u;
                            if (!petani.getStatus()) {
                                System.out.println("Akun Anda telah diblokir.");
                                break;
                            }
                            currentUserPetani = (Petani) u;
                            menuPetani();
                            break;
                        } else if (u instanceof Pembeli) {
                            Pembeli pembeli = (Pembeli) u;
                            if (!pembeli.getStatus()) {
                                System.out.println("Akun Anda telah diblokir.");
                                break;
                            }
                            currentUserPembeli = (Pembeli) u;
                            menuPembeli();
                            break;
                        } else {
                            currentUserAdmin = (Admin) u;
                            menuAdmin();
                            break;
                        }
                    }
                }
                if (!foundUser) {
                    System.out.println("Nama Atau Password salah");
                }
            }

        } while (pilih != 3);

    }

    public void menuPembeli() {
        boolean loggedin = true;
        do {
            System.out.println("=== MENU PEMBELI ===");
            System.out.println("1. Setting");
            System.out.println("2. Marketplace");
            System.out.println("3. Saldo");
            System.out.println("4. Checkout");
            System.out.println("5. Lihat Riwayat");
            System.out.println("6. Beri Ulasan");
            System.out.println("7. Negosiasi");
            System.out.println("8. Suggest Produk");
            System.out.println("9. Lihat Suggestion");
            System.out.println("10.  Laporan");

            System.out.println("Pilihan: ");
            int pilihan = s.nextInt();
            switch (pilihan) {
                case 1:
                    System.out.println("=== SETTING ===");
                    System.out.println("1. Ganti Nama");
                    System.out.println("2. Ganti Password");
                    System.out.println("3. Log Out");
                    System.out.println("4. Kembali");
                    System.out.print("Pilihan: ");
                    int pilihanSetting = s.nextInt();
                    s.nextLine();
                    switch (pilihanSetting) {
                        case 1:
                            boolean gantiNama = true;
                            System.out.println("Nama Lama: " + currentUserPembeli.getNama());
                            System.out.print("Nama Baru: ");
                            String namaBaru = s.nextLine();
                            if (namaBaru.isBlank()) {

                            } else {
                                for (User user : userList) {
                                    if (user.getNama().equals(namaBaru)) {
                                        gantiNama = false;
                                        System.out.println("Nama Tidak Tersedia");
                                        break;
                                    }
                                }
                                if (gantiNama) {
                                    System.out.println("Nama Berhasil di Ganti");
                                    currentUserPembeli.setNama(namaBaru);
                                }
                            }
                            break;
                        case 2:
                            System.out.println("Masukkan Password Lama: ");
                            String passwordlama = s.nextLine();
                            if (passwordlama.equals(currentUserPembeli.getPassword())) {
                                System.out.print("Password Baru: ");
                                String passwordBaru = s.nextLine();
                                if (passwordBaru.isBlank()) {
                                    System.out.println("Password tidak boleh kosong");
                                } else {
                                    if (passwordBaru.length() > 7 && passwordBaru.length() < 13) {
                                        System.out.println("Nama Berhasil di Ganti");
                                        currentUserPembeli.setPassword(passwordBaru);
                                    } else {
                                        System.out.println("Password Harus 8-12 Karakter");
                                    }
                                }
                            } else {
                                System.out.println("Password Salah");
                            }

                            break;
                        case 3:
                            loggedin = false;
                            break;
                        case 4 : 
                            break;
                        default:
                            System.out.println("Tidak ada dalam pilihan");
                    }
                    break;
                case 2:
                    marketplaceMenu();
                    break;
                case 3:
                    saldo();
                    break;
                case 4: 
                    checkout();
                    break;
                case 5:
                    lihatRiwayat();
                    break;
                case 6:
                    ulasan();
                    break;
                case 7:
                    prosesNegosiasiPembeli();
                    break;
                case 8:
                    ajukanSuggestionPembeli();
                    break;
                case 9:
                    lihatSuggestionPembeli();
                    break;
                case 10:
                    User user = (Pembeli) currentUserPembeli;
                    menuLaporan(user);
                    break;
                default:
                    System.out.println("Tidak ada dalam pilihan");

            }
        } while (loggedin);

    }

    private void marketplaceMenu() {
    boolean diMarketplace = true;

    while (diMarketplace) {
        System.out.println("\n=== MARKETPLACE ===");
        System.out.println("1. Lihat Semua Katalog Produk");
        System.out.println("2. Cari Produk");
        System.out.println("3. Lihat Keranjang");
        System.out.println("4. Kembali ke Menu Utama");
        System.out.print("Pilihan: ");

        int pilihan = s.nextInt();
        s.nextLine();

        switch (pilihan) {
            case 1:
                tampilkanKatalogProduk();

                break;
            case 2:
                cariProduk();
                break;
            case 3:
                lihatKeranjang();
                break;
            case 4:
                diMarketplace = false;
                break;
            default:
                System.out.println("Pilihan tidak valid!");
        }
    }
}

private void tampilkanKatalogProduk() {
    System.out.println("\n=== KATALOG PRODUK ===");
    
    System.out.printf("%-4s %-20s %-15s %-12s %-16s %-12s %-8s %-10s\n",
            "No.", "Nama Produk", "Petani", "Kategori", "Expirtaion Date", "Lokasi", "Harga", "Stok");
    System.out.println("---------------------------------------------------------------------");

    int nomor = 1;
    boolean adaProduk = false;
    ArrayList <Produk> katalogProduk = new ArrayList<>();
    for (User user : userList) {
        if (user instanceof Petani) {
            Petani petani = (Petani) user;
            if (!petani.getStatus()) {
            } else {
                for (Produk produk : petani.getProdukList()) {
                katalogProduk.add(produk);
                System.out.printf("%-4d %-20s %-15s %-12s %-16s %-12s Rp%,-8d %-8d\n",
                        nomor++,
                        produk.getNama(),
                        produk.getNamaPetani(),
                        produk.getKategori(),
                        produk.getExpiredDate(),
                        petani.getLokasi(),
                        produk.getHarga(),
                        produk.getStok());
                adaProduk = true;
            }
            }
        }
    }

    if (!adaProduk) {
        System.out.println("Belum ada produk yang dijual.");
        return;
    }else if (adaProduk) {
        tambahProduk(katalogProduk);
        
    }
}

private void cariProduk() {
    System.out.print("\nMasukkan kata kunci (nama produk atau kategori): ");
    String keyword = s.nextLine().trim().toLowerCase();

    System.out.println("\n=== HASIL PENCARIAN ===");
    
    System.out.printf("%-4s %-20s %-15s %-12s %-8s %-10s\n",
            "No.", "Nama Produk", "Petani", "Kategori", "Harga", "Stok");
    System.out.println("---------------------------------------------------------------------");

    int nomor = 1;
    boolean ditemukan = false;
    ArrayList <Produk> katalogProduk = new ArrayList<>();
    for (User user : userList) {
        if (user instanceof Petani) {
            Petani petani = (Petani) user;
            for (Produk produk : petani.getProdukList()) {
                if (produk.getNama().toLowerCase().contains(keyword) ||
                    produk.getKategori().name().toLowerCase().contains(keyword) ||
                    petani.getNama().toLowerCase().contains(keyword)) {
                    katalogProduk.add(produk);
                    System.out.printf("%-4d %-20s %-15s %-12s Rp%,-8d %-8d\n",
                            nomor++,
                            produk.getNama(),
                            produk.getNamaPetani(),
                            produk.getKategori(),
                            produk.getHarga(),
                            produk.getStok());
                    ditemukan = true;
                }
            }
        }
    }

    if (!ditemukan) {
        System.out.println("Tidak ada produk yang cocok dengan kata kunci tersebut.");
        return;
    } else if (ditemukan) {
        tambahProduk(katalogProduk);
    }
}

    private void tambahProduk (ArrayList<Produk> katalogProduk) {
        int pilih;
        do { 
            System.out.println("1. Tambah Ke Keranjang");
            System.out.println("2. Lihat Review Produk");
            System.out.println("3. Kembali");
            System.out.print("Pilih: ");
            pilih = s.nextInt();
            switch (pilih) {
            case 1:
                System.out.println("Pilih Produk: ");
                int pilihProduk = s.nextInt() - 1;
                System.out.println("Kuantitas: ");
                int kuantitas = s.nextInt();
                if (kuantitas > katalogProduk.get(pilihProduk).getStok()) {
                    System.out.println("Gagal: Kuantitas Pembelian Melebihi Stok Produk");
                } else if (kuantitas <= 0) {
                    System.out.println("Gagal: Kuantitas Tidak Boleh 0");
                }else {
                    ItemKeranjang item = new ItemKeranjang(katalogProduk.get(pilihProduk), kuantitas, currentUserPembeli);
                    currentUserPembeli.tambahProduk(item);
                }
                break;
            case 2:
                System.out.println("Pilih Produk: ");
                pilihProduk = s.nextInt() - 1;
                System.out.printf("%-4s %-20s %-15s %-12s\n",
            "No.", "Nama", "Rating", "Komentar");
                System.out.println("---------------------------------------------------------------------");

                katalogProduk.get(pilihProduk).displayUlasan();
                break;
            case 3:
                break;
            default:
                throw new AssertionError();
            }
        } while (pilih != 3);
    }

    public void lihatKeranjang() {
        System.out.printf("%-4s %-20s %-15s %-12s %-8s %-10s %-8s\n",
            "No.", "Nama Produk", "Petani", "Kategori", "Harga", "Kuantitas", "Total");
        System.out.println("---------------------------------------------------------------------");
        int totalAkhir = 0;
        if (currentUserPembeli.getItemKeranjang().size() == 0) {
            System.out.println("Keranjang Pembeli Kosong");
            return;
        }
        int nomor = 1;
        for (ItemKeranjang item : currentUserPembeli.getItemKeranjang()) {
            System.out.print(nomor + ".   ");
            item.displayProduk();
            totalAkhir += item.hitungTotal();
            nomor++;
        }
        System.out.println("Total Keranjang: " + currentUserPembeli.hitungTotalKeranjang());
    }

    public void checkout () {
        int layanan = 2000;
        currentUserPembeli.displayCheckout();
        if (currentUserPembeli.getItemKeranjang().isEmpty()) {
            return;
        }
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Subtotal Produk: " + currentUserPembeli.hitungTotalKeranjang());
        System.out.println("Subtotal Pengiriman: " + currentUserPembeli.hitungTotalOngkir());
        System.out.println("Biaya Layanan: " + layanan);
        System.out.println("Ingin Checkout?");
        System.out.println("1. Checkout");
        System.out.println("2. Negosiasi");
        System.out.println("3. Kembali");
        int pilih = s.nextInt();
        switch (pilih) {
            case 1:
                currentUserPembeli.checkout(userList);
                break;
            case 2:
                ajukanNegosiasiPembeli();
                break;
            case 3: 
                break;
            default:
                System.out.println("Tidak ada di pilihan");
        }
    }

    public void saldo() {
        int pilih;
        do { 
            System.out.println("=== SALDO ===");
            System.out.println("1. Cek Saldo"); 
            System.out.println("2. Top Up");
            System.out.println("3. Kembali");
            System.out.println("Pilihan: ");
            pilih = s.nextInt();
            switch (pilih) {
                case 1:
                    currentUserPembeli.getWallet().cekSaldo();
                    break;
                case 2:
                    System.out.print("Jumlah Top Up: ");
                    int jumlah = s.nextInt();
                    if (jumlah < 0) {
                        System.out.println("Anda tidak dapat melakukan pengisian ulang dengan jumlah kurang dari 0");
                    } else {
                        currentUserPembeli.getWallet().topUp(jumlah);
                    }
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Tidak ada di pilihan");
            }
        } while (pilih != 3);
    }

    public void ulasan() {
        System.out.printf("%-4s %-20s %-15s %-12s %-8s %-10s %-8s\n",
            "No.", "Nama Produk", "Petani", "Kategori", "Harga", "Kuantitas", "Total", "Status");
        System.out.println("------------------------------------------------------------------------------------");
        if (currentUserPembeli.isHistoryEmpty() || currentUserPembeli.isPurchasedEmpty()) {
            System.out.println("Riwayat Pembelian Kosong");
            return;
        }
        currentUserPembeli.showPurchased();
        System.out.print("Pilih: ");
        int pilih = s.nextInt() - 1;
        ArrayList<ItemKeranjang> items = currentUserPembeli.purchased();
        currentUserPembeli.BeriUlasan(items.get(pilih).getProduk());
    }

    public void lihatRiwayat() {
        System.out.printf("%-4s %-20s %-15s %-12s %-8s %-10s %-8s\n",
            "No.", "Nama Produk", "Petani", "Kategori", "Harga", "Kuantitas", "Total", "Status");
        System.out.println("------------------------------------------------------------------------------------");
        currentUserPembeli.showHistory();
    }

    public void ajukanNegosiasiPembeli() {
        System.out.println("=== NEGOSIASI ===");
        lihatKeranjang();
        if (currentUserPembeli.getItemKeranjang().isEmpty()) {
            return;
        }
        System.out.println();
        System.out.print("Pilih: ");
        int pilih = s.nextInt() - 1;
        if (pilih < 0 || pilih > currentUserPembeli.getItemKeranjang().size()) {
            System.out.println("Tidak ada dalam pilihan");
            return;
        }
        System.out.print("Ajukan Harga Baru:");
        int hargaBaru = s.nextInt();
        currentUserPembeli.ajukanNegosiasi(currentUserPembeli.getItemKeranjang().get(pilih), hargaBaru);
    }

    public void lihatSuggestionPembeli() {
        System.out.println("=== SUGGESTION SAYA ===");

        currentUserPembeli.displaySuggestion();

        if (currentUserPembeli.getSuggestionList().isEmpty()) return;

        System.out.print("Pilih nomor: ");
        int pilih = s.nextInt() - 1;

        System.out.println("1. Terima");
        System.out.println("2. Tolak");
        System.out.println("3. Counter");
        System.out.println("4. Kembali");
        System.out.print("Pilih: ");
        int aksi = s.nextInt();

        switch (aksi) {
            case 1:
                currentUserPembeli.terimaSuggestion(pilih);
                break;
            case 2:
                currentUserPembeli.tolakSuggestion(pilih);
                break;
            case 3:
                currentUserPembeli.counterSuggestion(pilih);
                break;
            case 4:
                break;
            default:
                System.out.println("Pilihan tidak valid!");
        }
    }

    public void prosesNegosiasiPembeli() {
        if (currentUserPembeli.getNegoList().isEmpty()) {
            System.out.println("Daftar negosiasi kosong");
            return;
        }
        currentUserPembeli.displayNego();
        System.out.println();
        System.out.println("1. Terima");
        System.out.println("2. Tolak");
        System.out.println("3. Counter");
        System.out.println("4. Kembali");
        System.out.print("Pilih: ");
        int pilih = s.nextInt();
        switch (pilih) {
            case 1:
                System.out.print("Pilih Produk: ");
                int pilihanProduk = s.nextInt() - 1;
                if (pilihanProduk < 0 || pilihanProduk > currentUserPembeli.getNegoList().size()) {
                    System.out.println("Tidak ada dalam pilihan");
                    return;
                }
                currentUserPembeli.terimaNego(pilihanProduk);
                break;
            case 2:
                System.out.print("Pilih Produk: ");
                pilihanProduk = s.nextInt() - 1;
                if (pilihanProduk < 0 || pilihanProduk > currentUserPembeli.getNegoList().size()) {
                    System.out.println("Tidak ada dalam pilihan");
                    return;
                }
                currentUserPembeli.tolakNego(pilihanProduk);
                break;
            case 3: 
                System.out.print("Pilih Produk: ");
                pilihanProduk = s.nextInt() - 1;
                if (pilihanProduk < 0 || pilihanProduk > currentUserPembeli.getNegoList().size()) {
                    System.out.println("Tidak ada dalam pilihan");
                    return;
                }
                currentUserPembeli.counterNego(pilihanProduk);
                break;
            default:
                break;
        }
    }

    public void prosesNegosiasiPetani() {
        System.out.println("=== NEGOSIASI ===");
        
        if (currentUserPetani.getNegoList().isEmpty()) {
            System.out.println("Daftar negosiasi kosong");
            return;
        }
        currentUserPetani.displayNego();
        System.out.println();
        System.out.println("1. Terima");
        System.out.println("2. Tolak");
        System.out.println("3. Counter");
        System.out.println("4. Kembali");
        System.out.print("Pilih: ");
        int pilih = s.nextInt();
        System.out.println("Pilih Produk: ");
        int pilihanProduk = s.nextInt() - 1;
        if (pilihanProduk < 0 || pilihanProduk > currentUserPetani.getNegoList().size()) {
            System.out.println("Tidak ada dalam pilihan");
            return;
        }
        switch (pilih) {
            case 1:
                currentUserPetani.terimaNego(pilihanProduk);
                break;
            case 2:
                currentUserPetani.tolakNego(pilihanProduk);
                break;
            case 3: 
                currentUserPetani.counterNego(pilihanProduk);
                break;
            default:
                break;
        }
    }

    public void ajukanSuggestionPembeli() {
        System.out.println("=== AJUKAN SUGGESTION ===");

        ArrayList<Petani> daftarPetani = new ArrayList<>();
        int no = 1;

        for (User u : userList) {
            if (u instanceof Petani) {
                Petani p = (Petani) u;
                daftarPetani.add(p);
                System.out.println(no++ + ". " + p.getNama());
            }
        }

        if (daftarPetani.isEmpty()) {
            System.out.println("Tidak ada petani!");
            return;
        }

        System.out.print("Pilih Petani: ");
        int pilihPetani = s.nextInt() - 1;
        s.nextLine();

        if (pilihPetani < 0 || pilihPetani >= daftarPetani.size()) {
            System.out.println("Pilihan tidak valid!");
            return;
        }

        Petani petani = daftarPetani.get(pilihPetani);

        System.out.println("1. Sayur");
        System.out.println("2. Buah");
        System.out.println("3. Daging");
        System.out.print("Kategori: ");
        int pilihKategori = s.nextInt();
        s.nextLine();

        Kategori kategori;
        switch (pilihKategori) {
            case 1: kategori = Kategori.SAYUR; break;
            case 2: kategori = Kategori.BUAH; break;
            case 3: kategori = Kategori.DAGING; break;
            default:
                System.out.println("Kategori tidak valid!");
                return;
        }

        System.out.print("Nama Produk: ");
        String nama = s.nextLine();

        System.out.print("Harga Usulan: ");
        int harga = s.nextInt();

        System.out.print("Kuantitas: ");
        int qty = s.nextInt();

        currentUserPembeli.ajukanSuggestion(petani, nama, kategori, harga, qty);
    }
    public void menuPetani() {
        boolean loggedin = true;
        do {
            System.out.println("=== MENU PETANI ===");
            System.out.println("1. Setting");
            System.out.println("2. Kelola Produk");
            System.out.println("3. Lihat Pesanan");
            System.out.println("4. Lihat Negosiasi");
            System.out.println("5. Lihat Suggestion");
            System.out.println("6. Lihat hasil pejualan");
            System.out.println("7. Laporan");
            System.out.print("Pilihan: ");
            int pilihan = s.nextInt();
            switch (pilihan) {
                case 1:
                    boolean gantiNama = true;
                    System.out.println("=== SETTING ===");
                    System.out.println("1. Ganti Nama");
                    System.out.println("2. Ganti Password");
                    System.out.println("3. Log Out");
                    System.out.println("4. Kembali");
                    System.out.print("Pilihan: ");
                    int pilihanSetting = s.nextInt();
                    s.nextLine();
                    switch (pilihanSetting) {
                        case 1:
                            System.out.println("Nama Lama: " + currentUserPetani.getNama());
                            System.out.print("Nama Baru: ");
                            String namaBaru = s.nextLine();
                            if (namaBaru.isBlank()) {

                            } else {
                                for (User user : userList) {
                                    if (user.getNama().equals(namaBaru)) {
                                        gantiNama = false;
                                        System.out.println("Nama Tidak Tersedia");
                                        break;
                                    }
                                }
                                if (gantiNama) {
                                    System.out.println("Nama Berhasil di Ganti");
                                    currentUserPetani.setNama(namaBaru);
                                }
                            }
                            break;
                        case 2:
                            System.out.println("Masukkan Password Lama: ");
                            String passwordlama = s.nextLine();
                            if (passwordlama.equals(currentUserPetani.getPassword())) {
                                System.out.print("Password Baru: ");
                                String passwordBaru = s.nextLine();
                                if (passwordBaru.isBlank()) {
                                    System.out.println("Password tidak boleh kosong");
                                } else {
                                    if (passwordBaru.length() > 7 && passwordBaru.length() < 13) {
                                        System.out.println("Nama Berhasil di Ganti");
                                        currentUserPetani.setPassword(passwordBaru);
                                    } else {
                                        System.out.println("Password Harus 8-12 Karakter");
                                    }
                                }
                            } else {
                                System.out.println("Password Salah");
                            }
                            break;
                        case 3:
                            loggedin = false;
                            break;
                        case 4:
                            break;
                        default:
                            System.out.println("Tidak ada dalam pilihan");
                    }
                    break;
                case 2:
                    System.out.println("=== KELOLA PRODUK ===");
                    System.out.println("1. Tambah Produk");
                    System.out.println("2. Edit Produk");
                    System.out.println("3. Hapus Produk");
                    System.out.print("Pilihan: ");
                    int pilihanProduk = s.nextInt();
                    s.nextLine();
                    switch (pilihanProduk) {
                        case 1:
                            System.out.println("1. Sayur");
                            System.out.println("2. Buah");
                            System.out.println("3. Daging");
                            System.out.print("Pilihan: ");
                            int pilihanKategori = s.nextInt();
                            s.nextLine();
                            switch (pilihanKategori) {
                                case 1:
                                    System.out.println("=== TAMBAH PRODUK SAYUR ===");
                                    System.out.println("Nama Produk: ");
                                    String namaSayur = s.nextLine();
                                    System.out.println("Harga: " + "Rp. ");
                                    int hargaSayur = s.nextInt();
                                    s.nextLine();
                                    System.out.println("Stok: ");
                                    int stokSayur = s.nextInt();
                                    int expiredDay, expiredMonth, expiredYear;
                                    System.out.println("Tanggal Expired (DD MM YYYY): ");
                                    expiredDay = s.nextInt();
                                    expiredMonth = s.nextInt();
                                    expiredYear = s.nextInt();
                                    LocalDate expiredDate = LocalDate.of(expiredYear, expiredMonth, expiredDay);                                    
                                    s.nextLine();
                                    Produk produk = new Produk(namaSayur, currentUserPetani.getNama(), Kategori.SAYUR, hargaSayur, stokSayur, expiredDate, currentUserPetani);
                                    currentUserPetani.tambahProduk(produk);
                                    break;
                                case 2:
                                    System.out.println("=== TAMBAH PRODUK BUAH ===");
                                    System.out.println("Nama Produk: ");
                                    String namaBuah = s.nextLine();
                                    System.out.println("Harga: " + "Rp. ");
                                    int hargaBuah = s.nextInt();
                                    System.out.println("Stok: ");
                                    int stokBuah = s.nextInt();
                                    s.nextLine();
                                    int expiredDay1, expiredMonth1, expiredYear1;
                                    System.out.println("Tanggal Expired (DD MM YYYY): ");
                                    expiredDay1 = s.nextInt();
                                    expiredMonth1 = s.nextInt();
                                    expiredYear1 = s.nextInt();
                                    expiredDate = LocalDate.of(expiredYear1, expiredMonth1, expiredDay1);
                                    s.nextLine();
                                    produk = new Produk(namaBuah, currentUserPetani.getNama(), Kategori.BUAH, hargaBuah, stokBuah, expiredDate, currentUserPetani);
                                    currentUserPetani.tambahProduk(produk);
                                    break;
                                case 3:
                                    System.out.println("=== TAMBAH PRODUK DAGING ===");
                                    System.out.println("Nama Produk: ");
                                    String namaDaging = s.nextLine();
                                    System.out.println("Harga: " + "Rp. ");
                                    int hargaDaging = s.nextInt();
                                    System.out.println("Stok: ");
                                    int stokDaging = s.nextInt();
                                    s.nextLine();
                                    int expiredDay2, expiredMonth2, expiredYear2;
                                    System.out.println("Tanggal Expired (DD MM YYYY): ");
                                    expiredDay2 = s.nextInt();
                                    expiredMonth2 = s.nextInt();
                                    expiredYear2 = s.nextInt();
                                    expiredDate = LocalDate.of(expiredYear2, expiredMonth2, expiredDay2);
                                    s.nextLine();
                                    produk = new Produk(namaDaging, currentUserPetani.getNama(), Kategori.DAGING, hargaDaging, stokDaging, expiredDate, currentUserPetani);
                                    currentUserPetani.tambahProduk(produk);
                                    break;
                                default:
                                    System.out.println("Tidak ada dalam pilihan");
                            }
                            break;
                        case 2:
                            if (currentUserPetani.getProdukList().size() != 0) {
                                int nomor =1;
                                System.out.println("=== EDIT PRODUK ===");
                                System.out.println("Nama | Harga | Stok | Expired Date");
                                for (Produk produk : currentUserPetani.getProdukList()) {
                                    System.out.println(nomor + ". " + produk.getNama() + " - " + produk.getHarga() + " - " + produk.getStok() + " - " + produk.getExpiredDate());
                                }
                                System.out.print("Pilihan: ");
                                int pilihanpProduk = s.nextInt() - 1;
                                currentUserPetani.editProduk(currentUserPetani.getProdukList().get(pilihanpProduk));
                            } else {
                                System.out.println("Produk anda kosong");
                            }
                            
                            break;
                        case 3:
                            if (currentUserPetani.getProdukList().size() != 0) {
                                int nomor =1;
                                System.out.println("=== HAPUS PRODUK ===");
                                for (Produk produk : currentUserPetani.getProdukList()) {
                                    System.out.println(nomor + ". " + produk.getNama() + " - " + produk.getHarga() + " - " + produk.getStok() + " - " + produk.getExpiredDate());
                                }
                                System.out.print("Pilihan: ");
                                int pilihanHapus = s.nextInt() - 1;
                                currentUserPetani.hapusProduk(pilihanHapus);   
                            } else {
                                System.out.println("Produk anda kosong");
                            }

                            break;
                        default:
                            System.out.println("Tidak ada dalam pilihan");
                    }
                    break;
                case 3:
                    currentUserPetani.prosesPesanan();
                    break;
                case 4:
                    prosesNegosiasiPetani();
                    break;
                case 5:
                    prosesSuggestion();
                    break;
                case 6:
                    laporanPenjualan();
                    break;
                case 7:
                    User user = (Petani) currentUserPetani;
                    buatLaporanUI(user);
                    break;
                default:
                    System.out.println("Tidak ada dalam pilihan");
            }
        } while (loggedin);
    }
    public void prosesSuggestion() {
        System.out.println("=== DAFTAR SUGGESTION ===");

        currentUserPetani.displaySuggestion();

        if (currentUserPetani.getSuggestionList().isEmpty()) return;

        System.out.print("Pilih nomor: ");
        int pilih = s.nextInt() - 1;

        System.out.println("1. Terima");
        System.out.println("2. Tolak");
        System.out.println("3. Counter");
        System.out.println("4. Kembali");
        System.out.print("Pilih: ");
        int aksi = s.nextInt();

        switch (aksi) {
            case 1:
                currentUserPetani.terimaSuggestion(pilih);
                break;
            case 2:
                currentUserPetani.tolakSuggestion(pilih);
                break;
            case 3:
                currentUserPetani.counterSuggestion(pilih);
                break;
            case 4:
                break;
            default:
                System.out.println("Pilihan tidak valid!");
        }
    }
    public void laporanPenjualan() {
        int nomor = 1;
        System.out.println("=== LAPORAN HASIL PENJUALAN ===");
        System.out.printf("%-4s %-20s %-12s %-8s %-10s %-8s %-8s %-8s\n",
            "No.", "Nama Produk", "Kategori", "Harga", "Stok", "Expiration Date", "Total Penjualan", "Total Stok Terjual");
        System.out.println("--------------------------------------------------------------------------------------------------------------");
        for (Produk produk : currentUserPetani.getProdukList()) {
        System.out.printf("%-4d %-20s %-12s %-8d %-10d %-8s Rp%,10d Rp%,10d\n",
            nomor,
            produk.getNama(),
            produk.getKategori(),
            produk.getHarga(),
            produk.getStok(),
            produk.getExpiredDate(),
            produk.getTotalPenjualan(),
            produk.getTotalStokTerjual());
            nomor++;
        }
        System.out.println("Total Pendapatan: Rp. " + currentUserPetani.getTotalPendapatan());
    }    

    public void menuLaporan(User user) {
        Scanner sc = new Scanner(System.in);
        int pilih;

        do {
            System.out.println("\n=== MENU LAPORAN ===");
            System.out.println("1. Buat Laporan");
            System.out.println("2. Lihat Laporan Saya");
            System.out.println("3. Kembali");
            System.out.print("Pilih: ");
            pilih = sc.nextInt();
            sc.nextLine();

            switch (pilih) {
                case 1:
                    buatLaporanUI(user);
                    break;
                case 2:
                    lihatLaporanSaya(user);
                    break;
            }
        } while (pilih != 3);
    }
    public void buatLaporanUI(User user) {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n=== BUAT LAPORAN ===");

        System.out.print("Masukkan nama user/petani yang dilaporkan: ");
        String namaTarget = sc.nextLine();

        User target = cariUser(namaTarget);

        if (target == null) {
            System.out.println("User tidak ditemukan!");
            return;
        } else if (!target.getStatus()) {
            System.out.println("User sudah diblokir!");
            return;
        } else if (target == user) {
            System.out.println("Tidak dapat melaporkan diri!");
        }

        System.out.println("Pilih alasan:");
        System.out.println("1. Penipuan");
        System.out.println("2. Produk rusak");
        System.out.println("3. Harga tidak wajar");
        System.out.println("4. Lainnya");
        System.out.print("Pilih: ");
        int alasan = sc.nextInt();
        sc.nextLine();

        String alasanText = "";
        switch (alasan) {
            case 1: 
                alasanText = "Penipuan"; 
                break;
            case 2: 
                alasanText = "Produk rusak"; 
                break;
            case 3: 
                alasanText = "Harga tidak wajar"; 
                break;
            case 4:
                System.out.print("Tulis alasan: ");
                alasanText = sc.nextLine();
                break;
        }

        Laporan laporan = new Laporan(user, target, alasanText);
        admin.addLaporan(laporan);

        System.out.println("Laporan berhasil dikirim!");
    }
    public User cariUser(String nama) {
        for (User user : userList) {
            if (user.getNama().equals(nama)) {
                return user;
            }
        }
        return null;
    }

    public void lihatLaporanSaya(User user) {
        System.out.println("\n=== LAPORAN SAYA ===");

        System.out.printf("%-4s %-15s %-15s %-20s %-12s\n",
            "No", "Pelapor", "Terlapor", "Alasan", "Tanggal");

        int no = 1;

        for (Laporan l : admin.getLaporan()) {
            if (l.getPelapor().equals(user)) {
                System.out.printf("%-4d %-15s %-15s %-20s %-12s\n",
                    no++,
                    l.getPelapor().getNama(),
                    l.getTerlapor().getNama(),
                    l.getAlasan(),
                    l.getTanggal());
            }
        }

        if (no == 1) {
            System.out.println("Tidak ada laporan.");
        }
    }
    public void menuAdmin() {
        int pilih;
        do {
            System.out.println("=== MENU ADMIN ===");
            System.out.println("1. List Semua User");
            System.out.println("2. Lihat Laporan");
            System.out.println("3. Log Out");
            System.out.print("Pilihan: ");
            pilih = s.nextInt();
            switch (pilih) {
                case 1:
                    int i = 1;
                    for (User user : userList) {
                        System.out.println(i + ". " + user.getNama());
                        i++;
                    }
                    break;
                case 2:
                    prosesLaporan();
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Tidak ada dalam pilihan");
            }
        } while (pilih != 3);

    }
    public void prosesLaporan() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n=== DAFTAR LAPORAN ===");
        ArrayList <Laporan> daftarLaporan = currentUserAdmin.getLaporan(); 
        for (int i = 0; i < currentUserAdmin.getLaporan().size(); i++) {
            Laporan l = daftarLaporan.get(i);
            System.out.println((i + 1)+ ". " + l.getPelapor().getNama() +
                " -> " + l.getTerlapor().getNama() +
                " | " + l.getAlasan());
        }

        System.out.print("Pilih laporan: ");
        int idx = sc.nextInt() - 1;

        if (idx <= 0 || idx > daftarLaporan.size()) {
            System.out.println("Index tidak valid!");
            return;
        }

        Laporan l = daftarLaporan.get(idx);

        System.out.println("1. Terima (ban user)");
        System.out.println("2. Tolak");
        System.out.print("Pilih: ");
        int aksi = sc.nextInt();

        if (aksi == 1) {
            currentUserAdmin.changeStatus(l.getTerlapor());;
            System.out.println("User dibanned!");
            currentUserAdmin.hapusTerlapor(l.getTerlapor());

        } else {
            System.out.println("Laporan ditolak!");
            currentUserAdmin.hapusTerlapor(l.getTerlapor());
        }
    }
}