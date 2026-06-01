public class ItemKeranjang {
    private Produk produk;
    private int kuantitas;
    private StatusPesanan status;
    private Pembeli pembeli;

    private int hargaAsli;
    private int hargaNego;
    private boolean isNego;
    private StatusNego statusNego;
    private boolean giliranPembeliNego;

    public ItemKeranjang(Produk produk, int kuantitas, Pembeli pembeli) {
        hargaAsli = produk.getHarga();
        this.produk = produk;
        this.kuantitas = kuantitas;  
        this.pembeli = pembeli; 
        status = StatusPesanan.PENDING;
        isNego = false;
        giliranPembeliNego = false;
    }
    public void displayProduk() {
        int total = hargaAsli * kuantitas;
        System.out.printf("%-20s %-15s %-12s Rp%,-8d %-8d Rp%,-8d\n",
            produk.getNama(),
            produk.getNamaPetani(),
            produk.getKategori(),
            hargaAsli,
            kuantitas,
            total);
    }

    public StatusNego getStatusNego() {
        return statusNego;
    }
    public int totalNego() {
        return hargaNego * kuantitas;
    }
    public void setStatusNego (StatusNego status) {
        this.statusNego = status;
    }
    public Pembeli getPembeli() {
        return pembeli;
    }
    public StatusPesanan getStatusPesanan() {
        return status;
    }
    public void setStatusPesanan(StatusPesanan statusPesanan){
        this.status = statusPesanan;
    }

    public int hitungTotal () {
        int total = hargaAsli * kuantitas;
        return total;
    }

    public int getKuantitas() {
        return kuantitas;
    }

    public Produk getProduk(){
        return produk;
    }
    public int getHargaNego() {
        return hargaNego;
    }

    public void setHargaNego(int hargaNego) {
        this.hargaNego = hargaNego;
    }

    public int getHargaAsli() {
        return hargaAsli;
    }
    public void setHargaAsli(int hargaNego) {
        this.hargaAsli = hargaNego;
    }
    public boolean getIsNego() {
        return isNego;
    }
    public void setIsNego(boolean isNego) {
        this.isNego = isNego;
    }

    public boolean getPembeliNego() {
        return giliranPembeliNego;
    }
    public void setPembeliNego(boolean isNego) {
        this.giliranPembeliNego = isNego;
    }
}
