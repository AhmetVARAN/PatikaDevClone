package com.patikadev.Helper;
import javax.swing.*;
import java.awt.*;

public class Helper {
    public static int screenCenterPoint(String axis, Dimension size){
        int point;
        switch (axis){
            case "x":
                point=(Toolkit.getDefaultToolkit().getScreenSize().width-size.width)/2;
                break;
            case "y":
                point=(Toolkit.getDefaultToolkit().getScreenSize().height-size.height)/2;
                break;
            default:
                point=0;
        }
        return point;
    }
    public static void setLayout(){
        for(UIManager.LookAndFeelInfo info:UIManager.getInstalledLookAndFeels()){
            if("Nimbus".equals(info.getName())){
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public static boolean isFieldEmpty(JTextField field){
        return field.getText().trim().isEmpty();
    }

    public static void showMessage(String str){
        optionPaneEN();
        String msg;
        String title;
        switch (str){
            case "fill":
                msg="Plase Fill In The Blanks";
                title="Error!";
                break;
            case "error":
                msg="Something Went Wrong";
                title="Error!";
                break;
            case "success":
                msg="Succesfully Completed";
                title="Result";
                break;
            default:
                msg=str;
                title="Message";
        }
        JOptionPane.showMessageDialog(null,msg,title,JOptionPane.INFORMATION_MESSAGE);
    }

    public static void optionPaneEN(){
        UIManager.put("OptionPane.okButtonText","Okay");
    }

}
