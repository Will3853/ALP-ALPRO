import java.time.LocalDate;

public class Laporan {
    private User pelapor;
    private User terlapor;
    private String alasan;
    private LocalDate tanggal;
    private LaporanStatus status;

    public Laporan(User pelapor, User terlapor, String alasan) {
        this.pelapor = pelapor;
        this.terlapor = terlapor;
        this.alasan = alasan;
        this.tanggal = LocalDate.now();
        this.status = LaporanStatus.PENDING;
    }

    public User getPelapor() { 
        return pelapor; 
    }
    public User getTerlapor() { 
        return terlapor; 
    }
    public String getAlasan() { 
        return alasan; 
    }
    public LocalDate getTanggal() { 
        return tanggal; 
    }
    public void setStatus(LaporanStatus status) {
        this.status = status;
    }

    public LaporanStatus getStatus() {
        return status;
    }
    
}