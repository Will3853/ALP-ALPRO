public class Wallet {
    private int saldo;

    public Wallet() {
        saldo = 0;
    }
    
    public void topUp(int amount) {
        if (amount > 0) {
            saldo += amount;
        }
    }

    public boolean bayar(int amount) {
        if (saldo < amount) {
            return false;
        } else {
            saldo -= amount;
            return true;
        }
    }

    public void cekSaldo() {
        System.out.println("Saldo: Rp." + saldo);
    }

    public int getSaldo() {
        return saldo;
    }
}
