package com.banksql;


import static com.banksql.App.check_wal_phone;
import static com.banksql.App.draw;
import static com.banksql.App.recharge;

public class Server_Handle implements Runnable{
    private String name, othername;
    private float amount;

    Server_Handle(String name,String othername,float amount) {
        
        this.name = name;
        this.othername = othername;
        this.amount = amount;
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
    
    @Override
    public void run() {
        try {
            System.out.println("\n\nCreating transfer:");
            System.out.println("PhoneNumber: "+name + " to: "+ othername);
            System.out.println(tranfer(name, othername, amount));
            Thread.sleep(50);        
            
        } catch (InterruptedException ex) {
            System.out.println("Transfer between "+name+" and "+othername+" is interrupted.");
        }
        System.out.println("Exiting....");
    }

}
