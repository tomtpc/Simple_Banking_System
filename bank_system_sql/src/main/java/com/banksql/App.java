package com.banksql;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Hello world!
 *
 */
public class App 
{
    static ArrayList<Customer> cus_data = new ArrayList<>();
    static ArrayList<Wallet> wal_data = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    private static String connectionURL_Customer = "jdbc:mysql://localhost:3306/customerdb";
    private static String connectionURL_Wallet= "jdbc:mysql://localhost:3306/walletdb";    
    private static String username ="root";
    private static String password ="";
    
    public static void create_cus(){
        Customer temp = new Customer();
        System.out.println("Input Name: "); temp.setName(sc.nextLine());
        System.out.println("Input National ID: "); temp.setNationalID(sc.nextLine());
        System.out.println("Input Phone: "); temp.setPhoneNumber(sc.nextLine());
        System.out.println("Input Address: "); temp.setAddress(sc.nextLine());
        System.out.println("Input Date: "); temp.setIssueDate(sc.nextLine());
        System.out.println("Input Place: "); temp.setIssuePlace(sc.nextLine());
        System.out.println("Input Customer ID: "); temp.setCustomerID(Integer.parseInt(sc.nextLine()));
        cus_data.add(temp);
    }
    
    
    public static void create_wal(){
        if(cus_data.isEmpty()){
            System.out.println("Fill Customer data please.");
        }
        else{
            for(Customer a: cus_data){
                Wallet temp = new Wallet();
                System.out.println("Inputing data for Mr/Mrs: "+ a.getName());
                temp.setCustomerID(a.getCustomerID());
                System.out.println("No: "); temp.setWalletNo(sc.nextLine());
                System.out.println("Date: "); temp.setCreateDate(sc.nextLine());
                System.out.println("Amount: "); temp.setAmount(Float.parseFloat(sc.nextLine()));
                int temp_status ;
                do{
                    System.out.println("Status (1/0): ");
                    temp_status = Integer.parseInt(sc.nextLine());
                }while(temp_status < 0 || temp_status > 1 );     
                temp.setStatus(temp_status);
                wal_data.add(temp);
            }
        }
    }
    
    public static void create_file_cus() throws FileNotFoundException, IOException{
        FileOutputStream fos = new FileOutputStream("cus.dat");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(cus_data);
        System.out.println("Save Wallet Data Success.");
        fos.close();
        oos.close();
    }
    
    public static void create_file_wal() throws IOException, IOException{
        FileOutputStream fos = new FileOutputStream("wal.dat");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(wal_data);
        System.out.println("Save Customer Data Success.");
        fos.close();
        oos.close();        
    }
    
    public static void read_cus() throws FileNotFoundException, IOException, ClassNotFoundException{
        FileInputStream fis = new FileInputStream("cus.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);
        cus_data = (ArrayList<Customer>) ois.readObject();
        System.out.println("Read Customer Data Success.");
        ois.close();
    }
    
    public static void read_wal() throws FileNotFoundException, IOException, ClassNotFoundException{
        FileInputStream fis = new FileInputStream("wal.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);
        wal_data = (ArrayList<Wallet>) ois.readObject();
        System.out.println("Read Wallet Data Success.");
        ois.close();
    }
    
    public static void modi_cus(int CustomerID) throws IOException, SQLException{
        if(check_cus(CustomerID) != null){
            for(Customer a: cus_data){
                if(a.getCustomerID() == CustomerID){
                    System.out.println("Input Name: "); a.setName(sc.nextLine());
                    System.out.println("Input National ID: "); a.setNationalID(sc.nextLine());
                    System.out.println("Input Phone: "); a.setPhoneNumber(sc.nextLine());
                    System.out.println("Input Address: "); a.setAddress(sc.nextLine());
                    System.out.println("Input Date: "); a.setIssueDate(sc.nextLine());
                    System.out.println("Input Place: "); a.setIssuePlace(sc.nextLine());
                    System.out.println("No: "); check_cus(CustomerID).setWalletNo(sc.nextLine());
                    System.out.println("Date: "); check_cus(CustomerID).setCreateDate(sc.nextLine());
                    int temp_status ;
                    do{
                        System.out.println("Status (1/0): ");
                        temp_status = Integer.parseInt(sc.nextLine());
                    }while(temp_status < 0 || temp_status > 1 );     
                    check_cus(CustomerID).setStatus(temp_status);
                    save_SQL_customer();
                    save_SQL_wallet();
                }
            }
            create_file_cus();
            create_file_wal();
        }
    }
    
    public static Wallet check_cus(int CustomerID){
        for(Wallet a: wal_data){
            if(CustomerID == a.getCustomerID()){
                return a;
            }
        }
        return null;
    }
    
    public static boolean check_status(int CustomerID){
        if(check_cus(CustomerID) != null){
            return check_cus(CustomerID).getStatus() == 1; 
        }
        return  false;
    }
    
    public static String find(String PhoneNumber){
        String data;
        for(Customer a: cus_data){
            if(PhoneNumber.equalsIgnoreCase(a.getPhoneNumber())){
                data = a.toString();
                for(Wallet b: wal_data){
                    if(a.getCustomerID() == b.getCustomerID()){
                        data += b.toString();
                        return data;
                    }
                }
            }
        }
        return "Not Found";
    }
    
    public static String show(Customer a){
        return a.toString() + check_cus(a.getCustomerID()).toString();
    }
    
    public static void un_lockWallet(int CustomerID){
        if(check_cus(CustomerID) != null){
            for(Wallet a: wal_data){
                if(a.getCustomerID() == CustomerID){
                    if(check_cus(CustomerID).getStatus() == 1){
                        check_cus(CustomerID).setStatus(0);
                        System.out.println("Locked Wallet");
                    }
                    if(check_cus(CustomerID).getStatus() == 0){
                        check_cus(CustomerID).setStatus(1);
                        System.out.println("UnLocked Wallet");
                    }
                }
            }
        }
        System.out.println("Done");
    }
    

    public static String recharge(String PhoneNumber, float amount){

        for(Customer a: cus_data){
            if(PhoneNumber.equalsIgnoreCase(a.getPhoneNumber())){
                for(Wallet b: wal_data){
                    if(a.getCustomerID() == b.getCustomerID()){
                        if(b.getStatus() == 0){
                            return "Wallet is Locked.";
                        }else{
                            float temp = b.getAmount() + amount;
                            b.setAmount(temp);
                            System.out.println(a.toString()+b.toString());
                            return "Reacharge Success.";
                        }
                    }
                }
            }
        }
        return "Invaild Phone Number.";
    }
    
    public static String draw(String PhoneNumber, float amount){
        for(Customer a: cus_data){
            if(PhoneNumber.equalsIgnoreCase(a.getPhoneNumber())){
                for(Wallet b: wal_data){
                    if(a.getCustomerID() == b.getCustomerID()){
                        if(b.getStatus() == 0){
                            return "Wallet is Locked.";
                        }
                        if(b.getAmount() - amount < 0){
                                return "Invaild amount.";
                        }
                        else{
                            float temp = b.getAmount() - amount;
                            b.setAmount(temp);
                            System.out.println(a.toString()+b.toString());
                            return "Draw Success.";
                        }
                    }
                }
            }
        }
        return "Invaild Phone Number.";
    }    
    public static boolean check_wal_phone(String phone){
        for(Customer a: cus_data){
            if(phone.equalsIgnoreCase(a.getPhoneNumber())){
                for(Wallet b: wal_data){
                    if(a.getCustomerID() == b.getCustomerID()){
                        return (b.getStatus() == 1);
                    }
                }
            
            }
        }
        return false;
    }
    public static String tranfer(String phone1, String phone2, float amount){
        if(amount < 0 ){
            return "Invalid Amount";
        }
        if(check_wal_phone(phone1)){
            if(check_wal_phone(phone2)){
                System.out.println(draw(phone1, amount));
                System.out.println(recharge(phone2, amount));
                System.out.println("Transfer Success.");
            }else{
                return "Wallet of " +phone2 + " is Locked";
            }            
        }else{
            return "Wallet of " +phone1 + " is Locked";
        }
        return null;
    }
    public static void save_SQL_customer(){
        try{
            Connection conn = DriverManager.getConnection(connectionURL_Customer, username, password); 
            int count = 0;
            for(Customer a: cus_data){
                String to_sql = " INSERT INTO customer(CustomerID,Name,NationalID,IssueDate,IssuePlace,address,phoneNumber)"
                                +"VALUES(?,?,?,?,?,?,?)";
                PreparedStatement pstm = conn.prepareCall(to_sql);
                pstm.setInt(1, a.getCustomerID());
                pstm.setString(2, a.getName());
                pstm.setString(3, a.getNationalID());
                pstm.setString(4, a.getIssueDate());
                pstm.setString(5, a.getIssuePlace());
                pstm.setString(6, a.getAddress());
                pstm.setString(7, a.getPhoneNumber());
                int executeUpdate = pstm.executeUpdate();
                if(executeUpdate != 0 ){
                    System.out.println("Saved: "+ (++count));
                }
            }
            System.out.println("Saved to SQL customer database.");
            conn.close();
        }catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Fail to save.");
        }        
    }
    public static void save_SQL_wallet(){
        try{
            Connection conn = DriverManager.getConnection(connectionURL_Wallet, username, password); 
            int count = 0;
            for(Wallet a: wal_data){
                String to_sql = " INSERT INTO wallet(CustomerID,WalletNo,CreateDate,Amount,Status)"
                                +"VALUES(?,?,?,?,?)";
                PreparedStatement pstm = conn.prepareCall(to_sql);
                pstm.setInt(1, a.getCustomerID());
                pstm.setString(2, a.getWalletNo());
                pstm.setString(3, a.getCreateDate());
                pstm.setString(4, ""+a.getAmount());
                pstm.setString(5, ""+a.getStatus());
                int executeUpdate = pstm.executeUpdate();
                if(executeUpdate != 0 ){
                    System.out.println("Saved: "+ (++count));
                }
            }
            System.out.println("Saved to SQL wallet database.");
            conn.close();
        }catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Fail to save.");
        }        
    }
    public static void from_SQL_customer(){
        try{
            Connection conn = DriverManager.getConnection(connectionURL_Customer, username, password);
            java.sql.Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select *from customer");
            int count = 0;
            while(rs.next()){
                ++count;
                Customer temp = new Customer();
                temp.setCustomerID(Integer.parseInt(rs.getString(1)));
                temp.setName(rs.getString(2));
                temp.setNationalID(rs.getString(3));
                temp.setIssueDate(rs.getString(4));
                temp.setIssuePlace(rs.getString(5));
                temp.setAddress(rs.getString(6));
                temp.setPhoneNumber(rs.getString(7));
                cus_data.add(temp);
                System.out.println("Read "+ count );
            }
            System.out.println("Read from SQL database successfully.");
            conn.close();
        }catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Fail to save.");
        }        
    }    
    public static void from_SQL_wallet(){
        try{
            Connection conn = DriverManager.getConnection(connectionURL_Wallet, username, password);
            java.sql.Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select *from wallet");
            int count = 0;
            while(rs.next()){
                ++count;
                Wallet temp = new Wallet();
                temp.setCustomerID(Integer.parseInt(rs.getString(1)));
                temp.setWalletNo(rs.getString(2));
                temp.setCreateDate(rs.getString(3));
                temp.setAmount(Float.parseFloat(rs.getString(4)));
                temp.setStatus(Integer.parseInt(rs.getString(5)));
                wal_data.add(temp);
                System.out.println("Read "+ count );
            }
            System.out.println("Read from SQL database successfully.");
            conn.close();
        }catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Fail to save.");
        }        
    }
    public static void delelte_SQL_customer() throws SQLException, Exception{
        Connection conn = DriverManager.getConnection(connectionURL_Customer, username, password); 
        java.sql.Statement statement = conn.createStatement();
        statement.executeUpdate("TRUNCATE customerdb");
    }
    
    public static void read_SQL(){
        from_SQL_customer();;
        from_SQL_wallet();
        for(Customer a: cus_data){
            for(Wallet b: wal_data){
                if(a.getCustomerID() == b.getCustomerID()){
                    System.out.println(a.toString() + b.toString());
                }
            }
        }
    }
    public static Connection getConnection(String dbURL, String userName, String password) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(dbURL, userName, password);
            System.out.println("Connect successfully!");
        } catch (Exception ex) {
            System.out.println("Connect failure!");
            ex.printStackTrace();
        }
        return conn;
    }
    
    //update SQL data
    public static void update_sql(){
        
    }
    //menu
    public static void menu(){
        System.out.println("1.Create Customer.");
        System.out.println("2.Create Wallet");
        System.out.println("3.Save to File.");
        System.out.println("4.Read from File.");
        System.out.println("5.Change Customer's Info.");
        System.out.println("6.Find data by Phone Number.");
        System.out.println("7.Lock/Unlock Wallet.");
        System.out.println("8.Recharge.");
        System.out.println("9.Withdraw.");
        System.out.println("10.Tranfer.");
        System.out.println("11.Save to Customer_SQL.");
        System.out.println("12.Read from SQL.");
        System.out.println("13.Update SQL");
        System.out.println("14.Delete SQL");
        System.out.println("15.Payday.");
        System.out.println("0.Exit.");
        System.out.println("Enter:");
    }
    
    public static void switch_funtion(int a) throws IOException, FileNotFoundException, ClassNotFoundException, SQLException, Exception{
        switch(a){
            case 1:
                create_cus();
                break;
            case 2:
                create_wal();
                break;  
            case 3:
                create_file_cus();
                create_file_wal();
                break;
            case 4:
                read_cus();
                read_wal();
                break;
            case 5:
                int CustomerID;
                System.out.println("Input CustomerID: ");
                CustomerID = Integer.parseInt(sc.nextLine());
                modi_cus(CustomerID);
                break;
            case 6:
                System.out.println("Input phone number: ");
                String PhoneNumber = sc.nextLine();
                System.out.println(find(PhoneNumber));
                break;
            case 7:
                System.out.println("Input CustomerID: ");
                String custString = sc.nextLine();
                un_lockWallet(Integer.parseInt(custString));
                break;
            case 8:
                System.out.println("Input Phone number:");
                String phone = sc.nextLine();
                System.out.println("Input Amount:");
                String amount = sc.nextLine();
                System.out.println(recharge(phone, Float.parseFloat(amount)));
                break;
            case 9:
                System.out.println("Input Phone number:");
                String phoneString = sc.nextLine();
                System.out.println("Input Amount:");
                String amoString = sc.nextLine();
                System.out.println(draw(phoneString, Float.parseFloat(amoString)));
                break;
            case 10:
                System.out.println("Input 1st Phone number:");
                String phone1 = sc.nextLine();
                System.out.println("Input 2nd Phone number:");
                String phone2 = sc.nextLine();
                System.out.println("Input Amount:");
                String amountString = sc.nextLine();
                System.out.println(tranfer(phone1,phone2, Float.parseFloat(amountString)));
                break;
            case 11:
                if(cus_data.isEmpty() && wal_data.isEmpty()){
                    System.out.println("Data is empty.");
                }else{
                    save_SQL_customer();
                    save_SQL_wallet();
                }
                break;
            case 12:
                read_SQL();
                break;
            case 13:
                delelte_SQL_customer();
                break;
            case 15:
                System.out.println("Input 1st Phone number:");
                String phone1String = sc.nextLine();
                System.out.println("Input 2nd Phone number:");
                String phone2String = sc.nextLine();
                System.out.println("Input 3rd Phone number:");
                String phone3 = sc.nextLine();
                System.out.println("Input Amount:");
                String amountString2 = sc.nextLine();
                Server_Handle sh1 = new Server_Handle(phone2String,phone1String, Float.parseFloat(amountString2));
                Thread t1 = new Thread(sh1);
                Server_Handle sh2 = new Server_Handle(phone1String, phone3, Float.parseFloat(amountString2));
                Thread t2 = new Thread(sh2);
                t1.start();
                t2.start();
                
                break;
        }
    }
    
    public static void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException, SQLException, Exception {
        int a;
        do{
            System.out.println("\n\n\n");
            menu();
                a = Integer.parseInt(sc.nextLine());
            switch_funtion(a);
        }while(a != 0);
    }
}
