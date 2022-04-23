package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Operator;
import com.patikadev.Model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OperatorGUI extends JFrame {
    private JPanel wrapper;
    private JTabbedPane tab_operator;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JButton btn_logout;
    private JPanel pnl_user_list;
    private JScrollPane scroll_user_list;
    private JTable tbl_user_list;
    private JPanel pnl_user_form;
    private JTextField fld_user_name;
    private JTextField fld_user_username;
    private JPasswordField fld_user_password;
    private JComboBox cmb_user_type;
    private JButton btn_user_add;
    private DefaultTableModel mdl_user_list;
    private Object[] row_user_list;

    private final Operator operator;

    public OperatorGUI(Operator operator){
        this.operator = operator;

        add(wrapper);
        setSize(1000,500);
        //this is for open the application at middle of screen
        int x=Helper.screenCenterPoint("x",getSize());
        int y=Helper.screenCenterPoint("y",getSize());
        setLocation(x,y);

        //close the application
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        lbl_welcome.setText("Welcome "+operator.getName());

        //ModelUserList
        mdl_user_list=new DefaultTableModel();
        Object[] col_user_list={"ID","Name Surname","User Name","Password","User Type"};
        mdl_user_list.setColumnIdentifiers(col_user_list);

        row_user_list=new Object[col_user_list.length];

        loadUserModel();
        tbl_user_list.setModel(mdl_user_list);
        tbl_user_list.getTableHeader().setReorderingAllowed(false);
        btn_user_add.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_user_name) || Helper.isFieldEmpty(fld_user_username) || Helper.isFieldEmpty(fld_user_password)){
                Helper.showMessage("fill");
            }else {
                String name=fld_user_name.getText();
                String username=fld_user_username.getText();
                String password= String.valueOf(fld_user_password.getPassword());
                String type=cmb_user_type.getSelectedItem().toString();
                if(User.add(name,username,password,type)){
                    Helper.showMessage("success");
                    loadUserModel();
                }else{
                    Helper.showMessage("error");
                }
            }
        });
    }
    public void loadUserModel(){
        DefaultTableModel clearModel= (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        for (User obj:User.getList()){
            int i=0;
            row_user_list[i++]=obj.getId();
            row_user_list[i++]=obj.getName();
            row_user_list[i++]=obj.getUsername();
            row_user_list[i++]=obj.getPass();
            row_user_list[i++]=obj.getType();

            mdl_user_list.addRow(row_user_list);
        }
    }
    public static void main(String[] args) {

        Helper.setLayout();

        Operator op=new Operator();
        op.setId(1);
        op.setName("Ahmet VARAN");
        op.setUsername("ahmet");
        op.setPass("1234");
        op.setType("operator");

        OperatorGUI opGUI=new OperatorGUI(op);
    }
}
