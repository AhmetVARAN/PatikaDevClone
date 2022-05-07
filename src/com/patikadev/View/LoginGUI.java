package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Operator;
import com.patikadev.Model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JPanel wrapper;
    private JPanel wtop;
    private JPanel wbottom;
    private JTextField fld_user_username;
    private JPasswordField fld_user_pass;
    private JButton btn_login;

    public LoginGUI(){
        add(wrapper);
        setSize(400,500);
        setLocation(Helper.screenCenterPoint("x",getSize()), Helper.screenCenterPoint("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);
        btn_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Helper.isFieldEmpty(fld_user_username) || Helper.isFieldEmpty(fld_user_pass)){
                    Helper.showMessage("fill");
                }else{
                    User u = User.getFetch(fld_user_username.getText(),fld_user_pass.getText());
                    if(u == null){
                        Helper.showMessage("User Not Found !");
                    }else{
                        Helper.showMessage("success");
                        switch (u.getType()){
                            case "operator":
                                OperatorGUI operatorGUI = new OperatorGUI((Operator) u);
                                break;
                            case "educator":
                                EducatorGUI educatorGUI = new EducatorGUI();
                                break;
                            case "student":
                                StudentGUI studentGUI = new StudentGUI();
                                break;
                        }
                        dispose();
                    }
                }
            }
        });
    }
    public static void main(String[] args) {
        Helper.setLayout();
        LoginGUI loginGUI = new LoginGUI();
    }
}
