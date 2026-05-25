
public abstract class User {
    protected String nama;
    protected String password;
    protected String lokasi;
    
    public User(String nama, String password) {
        this.nama = nama;
        this.password = password;
    }

    public abstract boolean login(String nama, String password);

    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
    
}