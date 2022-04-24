package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Operator;
import com.patikadev.Model.User;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
    private JTextField fld_user_id;
    private JButton btn_user_delete;
    private JTextField fld_sh_user_name;
    private JTextField fld_sh_user_username;
    private JComboBox cmb_sh_user_type;
    private JButton btn_user_sh;
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
        mdl_user_list=new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column==0)
                    return false;
                return super.isCellEditable(row, column);
            }
        };
        Object[] col_user_list={"ID","Name Surname","User Name","Password","User Type"};
        mdl_user_list.setColumnIdentifiers(col_user_list);

        row_user_list=new Object[col_user_list.length];

        loadUserModel();
        tbl_user_list.setModel(mdl_user_list);
        tbl_user_list.getTableHeader().setReorderingAllowed(false);

        //Tabloda tıklanılan verinin id bilgisini çekmek için
        tbl_user_list.getSelectionModel().addListSelectionListener(e -> {
            try{
                String select_user_id=tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),0).toString();
                fld_user_id.setText(select_user_id);
            }catch (Exception exception){

            }
        });

        //Tıklanan verinin içeriğini değiştirmek
        tbl_user_list.getModel().addTableModelListener(e -> {
            if(e.getType()==TableModelEvent.UPDATE){
                //tıklanan yerdeki verileri almak
                int user_id=Integer.parseInt(tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),0).toString());
                String user_name=tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),1).toString();
                String user_username=tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),2).toString();
                String user_pass=tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),3).toString();
                String user_type=tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),4).toString();

                if (User.update(user_id,user_name,user_username,user_pass,user_type)){
                    Helper.showMessage("success");

                }
                loadUserModel();
            }
        });

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
                    fld_user_username.setText(null);
                    fld_user_password.setText(null);
                    fld_user_name.setText(null);
                }
            }
        });
        btn_user_delete.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_user_id)){
                Helper.showMessage("fill");
            }else {
                int user_id=Integer.parseInt(fld_user_id.getText());
                if(User.delete(user_id)){
                    Helper.showMessage("success");
                    loadUserModel();
                }else{
                    Helper.showMessage("error");
                }
            }
        });
        btn_user_sh.addActionListener(e -> {
            String name=fld_sh_user_name.getText();
            String username=fld_sh_user_username.getText();
            String type=cmb_sh_user_type.getSelectedItem().toString();
            String query=User.searchQuery(name,username,type);
            ArrayList<User> searhingUser = User.searchUserList(query);
            loadUserModel(searhingUser);
        });
        btn_logout.addActionListener(e -> {
            dispose();
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
    public void loadUserModel(ArrayList<User> list){
        DefaultTableModel clearModel= (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        for (User obj:list){
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
