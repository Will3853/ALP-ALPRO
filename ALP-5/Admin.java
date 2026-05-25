
import java.util.ArrayList;

public class Admin extends User{
    private ArrayList<Laporan> laporan;

    public Admin(String nama, String password) {
        super(nama, password);
    }
    
    public boolean login(String nama, String password) {
        if (this.nama.equals(nama) && this.password.equals(password)) {
            System.out.println("Berhasil Login, Selamat Datang " + nama);
            return true;
        }
        return false;
    }
    
    public void changeStatus (User status) {
        if (status instanceof Petani){
            Petani petani = (Petani) status;
            petani.setStatus(false);
        } else if (status instanceof Pembeli){
            Pembeli petani = (Pembeli) status;
            petani.setStatus(false);
        } else if (status instanceof Admin){
            System.out.println("Cannot ban an Admin");
        }
    }
}
