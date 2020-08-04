package edu.asu.msse.mbatra3.mobileoffloading.Utlilities;

import android.content.Context;
import android.content.Intent;
import edu.asu.msse.mbatra3.mobileoffloading.OffloadCalculations;

public class Helper {

    public Intent navigateResultScreen(String row1, String row2, String row3, String row4,
                                       String estimation1, String estimation2, Context context ){
        Intent intent = new Intent(context, OffloadCalculations.class);
        intent.putExtra("row1", row1);
        intent.putExtra("row2", row2);
        intent.putExtra("row3", row3);
        intent.putExtra("row4", row4);
        intent.putExtra("estimation1", estimation1);
        intent.putExtra("estimation2", estimation2);
        return intent;
    }

    public int[][] convertStringToArray(String input) {
        input = input.trim().replaceAll("\\ ", "");
        input = input+",";
        int[][] outArr = new int[4][4];
        int loopI = 0, loopJ = 0;
        input = input.replaceAll("\\{","");
        input = input.substring(0,input.length()-2) + ",";
        String[] items = input.split("\\},");
        for(int i=0; i<items.length; i++) {
            String item = items[i] + ",";
            String[] itemRow = item.split(",");
            for(int j=0; j<itemRow.length; j++) {
                outArr[i][j] = Integer.parseInt(itemRow[j].trim() + "");
            }
        }
        return outArr;
    }

    public static int[][] getTranspose(int[][] A2) {
        int[][] returnArr = new int[4][4];
        for (int i = 0; i < A2.length; i++) {
            for (int j = 0; j < A2[0].length; j++) {
                returnArr[i][j] = A2[j][i];
            }
        }
        return returnArr;
    }

    public static String arrayToString(int[] A) {
        String arrayString = "{";
        for (int i = 0; i < A.length; i++) {
            arrayString = arrayString + A[i] + ",";
        }
        arrayString = arrayString.substring(0, arrayString.length() - 1);
        arrayString = arrayString + "}";
        return arrayString;
    }

    public static String arrayRCMult(String iV, String jV) {
        int sum = 0;
        for (int i = 0; i < iV.length(); i++) {
            if (iV.charAt(i) >= '0' && iV.charAt(i) <= '9') {
                sum = sum + (((int) (iV.charAt(i)) - 48) * ((int) (jV.charAt(i)) - 48));
            }
        }
        return sum + "";
    }

    public static String ar2String(int[] arr) {
        String out = "";
        for(int i = 0; i<arr.length; i++) {
            out = out + arr[i] + "\t\t";
        }
        return out;
    }
}
