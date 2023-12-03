package uas;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class User {

    protected String username;
    protected String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

class Admin extends User {

    public Admin(String username, String password) {
        super(username, password);
    }
// Menampilkan Informasi mengenai nasabah

    public void lihatInfoNasabah(List<Nasabah> nasabahList) {
        for (Nasabah nasabah : nasabahList) {
            System.out.println("Username: " + nasabah.username);
            System.out.println("Saldo: " + nasabah.getSaldo());
            System.out.println("--------------------");
        }
    }
// Melakukan fungsi transaksi ( simpan / tarik )

    public void lakukanTransaksi(Nasabah nasabah, int transactionType, double amount) {
        switch (transactionType) {
            case 1:
                // Simpan
                new Simpan().eksekusi(nasabah, amount);
                break;
            case 2:
                // Tarik
                new Tarik().eksekusi(nasabah, amount);
                break;
            default:
                System.out.println("Invalid transaction type");
        }
    }
}
// Menyimpan data mengenai Nasabah ( User ), rekening, username, password, riwayat transaksi

class Nasabah extends User {

    private Rekening rekening;
    private List<String> transactionHistory;

    public Nasabah(String username, String password, Rekening rekening) {
        super(username, password);
        this.rekening = rekening;
        this.transactionHistory = new ArrayList<>();
    }

    public Rekening getRekening() {
        return rekening;
    }

    public double getSaldo() {
        return rekening.getSaldo();
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }
// Menambahkan transaksi ke riwayat transaksi 

    public void addTransactionToHistory(String transaction) {
        transactionHistory.add(transaction);
    }
}

class Rekening {

    private double saldo;

    public Rekening(double saldo) {
        this.saldo = saldo;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
// Melakkukan fungsi transaksi

class Transaksi {

    public void eksekusi(Nasabah nasabah, double jumlah) {

    }
}
// Fungsi transaksi simpan

class Simpan extends Transaksi {

    @Override
    public void eksekusi(Nasabah nasabah, double jumlah) {
        nasabah.getRekening().setSaldo(nasabah.getRekening().getSaldo() + jumlah);
        System.out.println("Simpan uang sukses.");
    }
}
// Fungsi transaksi Tarik

class Tarik extends Transaksi {

    @Override
    public void eksekusi(Nasabah nasabah, double jumlah) {
        if (nasabah.getRekening().getSaldo() >= jumlah) {
            nasabah.getRekening().setSaldo(nasabah.getRekening().getSaldo() - jumlah);
            System.out.println("Tarik uang sukses.");
        } else {
            System.out.println("Saldo tidak mencukupi.");
        }
    }
}
// Fungsi transaksi transfer

class Transfer extends Transaksi {

    public void eksekusi(Nasabah pengirim, Nasabah penerima, double jumlah) {
        if (pengirim.getRekening().getSaldo() >= jumlah) {
            pengirim.getRekening().setSaldo(pengirim.getRekening().getSaldo() - jumlah);
            penerima.getRekening().setSaldo(penerima.getRekening().getSaldo() + jumlah);
            System.out.println("Transfer uang sukses.");
        } else {
            System.out.println("Saldo tidak mencukupi.");
        }
    }
}

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Inisialisasi data
        Rekening rekeningNasabah1 = new Rekening(1000);
        Nasabah nasabah1 = new Nasabah("user1", "pass1", rekeningNasabah1);

        Rekening rekeningNasabah2 = new Rekening(2000);
        Nasabah nasabah2 = new Nasabah("user2", "pass2", rekeningNasabah2);

        List<Nasabah> nasabahList = new ArrayList<>();
        nasabahList.add(nasabah1);
        nasabahList.add(nasabah2);

        Admin admin = new Admin("admin", "adminpass");

        // Login
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User currentUser = null;

        for (Nasabah nasabah : nasabahList) {
            if (nasabah.username.equals(username) && nasabah.password.equals(password)) {
                currentUser = nasabah;
                break;
            }
        }

        if (currentUser == null) {
            if (admin.username.equals(username) && admin.password.equals(password)) {
                currentUser = admin;
            } else {
                System.out.println("Login gagal. Username atau password salah.");
                return;
            }
        }

        while (true) {
            System.out.println("1. Transaksi");
            System.out.println("2. Lihat informasi rekening");
            System.out.println("3. Lihat riwayat transaksi");
            System.out.println("4. Ganti Akun");
            System.out.println("5. Log-out");

            // Fungsi nemambah nasabah baru
            if (currentUser instanceof Admin) {
                System.out.println("6. Tambahkan Nasabah baru");
            }

            // Fungsi untuk melakukan opsi yang hanya tersedia untuk admin
            if (currentUser instanceof Admin) {
                System.out.println("7. Transaksi Khusus Admin ");
            }

            System.out.print("Pilih Opsi: ");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    if (currentUser instanceof Nasabah) {
                        // Fungsi untuk transaksi Nasabah
                        Nasabah nasabah = (Nasabah) currentUser;
                        System.out.println("1. Simpan");
                        System.out.println("2. Tarik");
                        System.out.println("3. Transfer ke Nasabah lain");
                        System.out.print("Pilih Tipe Transaksi : ");
                        int transactionType = scanner.nextInt();

                        switch (transactionType) {
                            case 1:
                                System.out.print("Masukan nominal yang akan disimpan: ");
                                double simpanAmount = scanner.nextDouble();
                                new Simpan().eksekusi(nasabah, simpanAmount);
                                nasabah.addTransactionToHistory("Simpan: " + simpanAmount);
                                break;
                            case 2:
                                System.out.print("Masukan nominal yang akan ditarik: ");
                                double tarikAmount = scanner.nextDouble();
                                new Tarik().eksekusi(nasabah, tarikAmount);
                                nasabah.addTransactionToHistory("Tarik: " + tarikAmount);
                                break;
                            case 3:
                                System.out.print("Masukan nama tujuan Transfer: ");
                                String recipientUsername = scanner.next();
                                Nasabah recipient = null;
                                for (Nasabah n : nasabahList) {
                                    if (n.username.equals(recipientUsername)) {
                                        recipient = n;
                                        break;
                                    }
                                }
                                if (recipient != null) {
                                    System.out.print("Masukan nominal yang akan ditransfer: ");
                                    double transferAmount = scanner.nextDouble();
                                    new Transfer().eksekusi(nasabah, recipient, transferAmount);
                                    nasabah.addTransactionToHistory("Transfer: " + transferAmount + " to " + recipient.username);
                                } else {
                                    System.out.println("Penerima tidak ditemukan");
                                }
                                break;
                            default:
                                System.out.println("Tipe transaksi tidak ditemukan ");
                        }
                    } else if (currentUser instanceof Admin) {
                        
                        System.out.println("Admin tidak dapat melakukan fungsi tersebut");
                    }
                    break;

                case 2:
                    if (currentUser instanceof Nasabah) {
                        // Informasi mengenai Nasabah
                        Nasabah nasabah = (Nasabah) currentUser;
                        System.out.println("Nasabah : " + nasabah.username);
                        System.out.println("Saldo : " + nasabah.getSaldo());
                        System.out.println("--------------------");
                    } else if (currentUser instanceof Admin) {

                        admin.lihatInfoNasabah(nasabahList);
                    }
                    break;

                case 3:
                    if (currentUser instanceof Nasabah) {
                        // Fungsi untuk melihat riwayat transaksi
                        Nasabah nasabah = (Nasabah) currentUser;
                        System.out.println("Riwayat Transaksi Untuk " + nasabah.username + ":");
                        List<String> transactionHistory = nasabah.getTransactionHistory();
                        for (String transaction : transactionHistory) {
                            System.out.println(transaction);
                        }
                        System.out.println("--------------------");
                    } else {
                        System.out.println("Pilihan tidak tersedia ");
                    }
                    break;

                case 4:
                    // Funsi untuk ganti akun
                    System.out.print("Username: ");
                    String newUsername = scanner.next();
                    System.out.print("Password: ");
                    String newPassword = scanner.next();

                    User newCurrentUser = null;

                    for (Nasabah nasabah : nasabahList) {
                        if (nasabah.username.equals(newUsername) && nasabah.password.equals(newPassword)) {
                            newCurrentUser = nasabah;
                            break;
                        }
                    }

                    if (newCurrentUser == null) {
                        if (admin.username.equals(newUsername) && admin.password.equals(newPassword)) {
                            newCurrentUser = admin;
                        } else {
                            System.out.println("Login gagal. Username atau password salah.");
                            break;
                        }
                    }

                    currentUser = newCurrentUser;
                    System.out.println("Ganti ke Nasabah: " + currentUser.username);
                    break;

                case 5:
                    System.out.println("Program selesai!");
                    System.exit(0);
                    break;

                // Fungsi Untuk menambahkan Nasabah baru
                case 6:
                    if (currentUser instanceof Admin) {
                      
                        System.out.print(" Username untuk Nasabah baru: ");
                        String addUsername = scanner.next();
                        System.out.print(" Password untuk Nasabah baru: ");
                        String addPassword = scanner.next();
                        System.out.print("Saldo untuk Nasabah baru: ");
                        double initialSaldo = scanner.nextDouble();

                        Rekening newRekening = new Rekening(initialSaldo);
                        Nasabah newNasabah = new Nasabah(addUsername, addPassword, newRekening);
                        nasabahList.add(newNasabah);

                        System.out.println("Nasabah baru telah ditambahkan");
                    } else {
                        System.out.println("Pilihan tidak tersedia.");
                    }
                    break;

                // Transaksi khusus admin
                case 7:
                    if (currentUser instanceof Admin) {

                        System.out.print("Masukan nama nasabah  ");
                        String userUsername = scanner.next();
                        Nasabah user = null;
                        for (Nasabah n : nasabahList) {
                            if (n.username.equals(userUsername)) {
                                user = n;
                                break;
                            }
                        }
                        if (user != null) {

                            System.out.println("1. Simpan");
                            System.out.println("2. Tarik");
                            System.out.println("3. Transfer ke Nasabah lain");
                            System.out.print("Pilih Tipe Transaksi  ");
                            int transactionType = scanner.nextInt();

                            switch (transactionType) {
                                case 1:
                                    System.out.print("Masukan nominal yang akan disimpan: ");
                                    double simpanAmount = scanner.nextDouble();
                                    new Simpan().eksekusi(user, simpanAmount);
                                    user.addTransactionToHistory("Admin Simpan: " + simpanAmount);
                                    break;
                                case 2:
                                    System.out.print("Masukan nominal yang akan ditarik ");
                                    double tarikAmount = scanner.nextDouble();
                                    new Tarik().eksekusi(user, tarikAmount);
                                    user.addTransactionToHistory("Admin menarikarik: " + tarikAmount);
                                    break;
                                case 3:
                                    System.out.print("Masukan nama nasabah penerima: ");
                                    String recipientUsername = scanner.next();
                                    Nasabah recipient = null;
                                    for (Nasabah n : nasabahList) {
                                        if (n.username.equals(recipientUsername)) {
                                            recipient = n;
                                            break;
                                        }
                                    }
                                    if (recipient != null) {
                                        System.out.print("Masukan nominal yang akan ditransfer: ");
                                        double transferAmount = scanner.nextDouble();
                                        new Transfer().eksekusi(user, recipient, transferAmount);
                                        user.addTransactionToHistory(" Transfer Admin: " + transferAmount + " to " + recipient.username);
                                    } else {
                                        System.out.println("Penerima tidak ditemukan");
                                    }
                                    break;
                                default:
                                    System.out.println("Pilihan tidak tersedia");
                            }
                        } else {
                            System.out.println("Nasabah tidak ditemukan");
                        }
                    } else {
                        System.out.println("Pilihan tidak tersedia");
                    }
                    break;

                default:
                    System.out.println("Pilihan tidak tersedia");
            }
        }
    }
}
