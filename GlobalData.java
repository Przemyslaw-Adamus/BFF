package com.example.bff30;

//package com.example.bff30;
//
//import android.os.AsyncTask;
//import java.util.ArrayList;
//import java.util.List;
//
public class GlobalData{

    private static int IDUserOnline;

    private  static final String[] listNames = {
            "Adamus Przemysław",
            "Gdowska Paulina",
            "Gurgul Gabriela",
            "Hynek Karol",
            "Kozak Julia",
            "Kubala Weronika",
            "Kubis Justyna",
            "Kurek Kamil",
            "Kusior Miłosz",
            "Opioła Grzegorz",
            "Opioła Joanna"}; // to powinno być pobrane z bazy
    private static int[] idUsers = {4,5,6,7,12,13,14,15,16,17,18};
    private static float EURtoPLN = 4f;
    private static float HUFtoPLN = 0.013f;
    private static float HRKtoPLN = 0.57f;



//    List<CostHeader> costHeaderList = new ArrayList<>();
//    List<User> userList = new ArrayList<>();
//    List<CostRow> costRows = new ArrayList<>();
//    List<Settlement> settlementList = new ArrayList<>();
//
//    GlobalData(){
//        new LoadCostsHeader().execute("");
//        new LoadCostsRow().execute("");
//        new LoadUsers().execute("");
//        new LoadSettlements().execute("");
//    }
//
//    public boolean addCostHeader(){
//        new SaveCostHeader().execute("");
//        return true;
//    }
//
//    public boolean addCostRow(){
//        new SaveCostRow().execute("");
//        return true;
//    }
//
//    public boolean addCostHeader(){
//        new SaveCostHeader().execute("");
//        return true;
//    }
//
//    public boolean addCostHeader(){
//        new SaveCostsHeader().execute("");
//        return true;
//    }

    public static int getIDUserOnline() {
        return IDUserOnline;
    }

    public static void setIDUserOnline(int IDUserOnline) {
        GlobalData.IDUserOnline = IDUserOnline;
    }

    public static String[] getListNames() {
        return listNames;
    }

    public static float getEURtoPLN() {
        return EURtoPLN;
    }

    public static void setEURtoPLN(float EURtoPLN) {
        GlobalData.EURtoPLN = EURtoPLN;
    }

    public static float getHUFtoPLN() {
        return HUFtoPLN;
    }

    public static void setHUFtoPLN(float HUFtoPLN) {
        GlobalData.HUFtoPLN = HUFtoPLN;
    }

    public static float getHRKtoPLN() {
        return HRKtoPLN;
    }

    public static void setHRKtoPLN(float HRKtoPLN) {
        GlobalData.HRKtoPLN = HRKtoPLN;
    }


    public static int[] getIdUsers() {
        return idUsers;
    }

    public static void setIdUsers(int[] idUsers) {
        GlobalData.idUsers = idUsers;
    }
}
