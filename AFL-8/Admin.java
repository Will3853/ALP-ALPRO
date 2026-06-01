
import java.util.ArrayList;

public class Admin extends User{
    private ArrayList<Laporan> laporan;

    public Admin(String nama, String password) {
        super(nama, password);
        laporan = new ArrayList<>();
    }
    
    public boolean login(String nama, String password) {
        if (this.nama.equals(nama) && this.password.equals(password)) {
            System.out.println("Berhasil Login, Selamat Datang " + nama);
            return true;
        }
        return false;
    }
    
    public void changeStatus (User status) {
        status.setStatus(false);
    }

    public ArrayList<Laporan> getLaporan() {
        return laporan;
    }

    public void addLaporan(Laporan laporan) {
        this.laporan.add(laporan);
    }

    public void hapusTerlapor(User terlapor) {
        for (int i = 0; i < laporan.size(); i++) {
            if (laporan.get(i).getTerlapor().equals(terlapor)) {
                laporan.remove(i);
                i--;
            }
        }
    }
}
