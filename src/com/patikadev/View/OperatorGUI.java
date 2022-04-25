package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Helper.Item;
import com.patikadev.Model.Course;
import com.patikadev.Model.Operator;
import com.patikadev.Model.Patika;
import com.patikadev.Model.User;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
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
    private JPanel pnl_patika_list;
    private JScrollPane scrl_patika_list;
    private JTable tbl_patika_list;
    private JPanel pnl_patika_add;
    private JTextField fld_patika_name;
    private JButton btn_patika_add;
    private JPanel pnl_course_list;
    private JScrollPane scrl_course_list;
    private JTable tbl_course_list;
    private JPanel pnl_course_add;
    private JTextField fld_course_name;
    private JTextField fld_course_lang;
    private JComboBox cmb_course_patika;
    private JComboBox cmb_course_educator;
    private JButton btn_course_add;

    private DefaultTableModel mdl_user_list;
    private Object[] row_user_list;
    private DefaultTableModel mdl_patika_list;
    private Object[] row_patika_list;
    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;

    private JPopupMenu patikaMenu;
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
        //ModelUserList Sonu

        //PatikaList
        //sağ tık menü oluşturma
        patikaMenu=new JPopupMenu();
        JMenuItem updateMenu=new JMenuItem("Update");
        JMenuItem deleteMenu=new JMenuItem("Delete");
        patikaMenu.add(updateMenu);
        patikaMenu.add(deleteMenu);
        //updatemenu için sağ tıklayınca update panelin açılması
        updateMenu.addActionListener(e -> {
            int select_id=Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(),0).toString());
            UpdatePatikaGUI updateGUI = new UpdatePatikaGUI(Patika.getFetch(select_id));
            updateGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadPatikaModel();
                    loadPatikaCombo();
                }
            });
        });

        deleteMenu.addActionListener(e -> {
            if(Helper.confirm("sure")){
                int select_id=Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(),0).toString());
                if (Patika.delete(select_id)){
                    Helper.showMessage("success");
                    loadPatikaModel();
                    loadPatikaCombo();
                }else {
                    Helper.showMessage("error");
                }
            }
        });
        mdl_patika_list=new DefaultTableModel();
        Object[] col_patika_list={"ID","Patika Adı"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);
        row_patika_list=new Object[col_patika_list.length];
        loadPatikaModel();

        tbl_patika_list.setModel(mdl_patika_list);
        tbl_patika_list.setComponentPopupMenu(patikaMenu);
        tbl_patika_list.getTableHeader().setReorderingAllowed(false);//row col yerlerinin değişmesini engeller
        tbl_patika_list.getColumnModel().getColumn(0).setMaxWidth(100);//ilk columnun genişliğini 100 yapma
        tbl_patika_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //sağ tıklanan yeri seçili hala getirir
                Point point=e.getPoint();
                int selected_row=tbl_patika_list.rowAtPoint(point);
                tbl_patika_list.setRowSelectionInterval(selected_row,selected_row);
            }
        });
        //PatikaList Sonu

        //CouserList
        mdl_course_list=new DefaultTableModel();
        Object[] col_courseList={"ID","Course Name","Programming Language","Patika","Educator"};
        mdl_course_list.setColumnIdentifiers(col_courseList);
        row_course_list=new Object[col_courseList.length];

        loadCourseList();

        tbl_course_list.setModel(mdl_course_list);
        tbl_course_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_course_list.getTableHeader().setReorderingAllowed(false);

        loadPatikaCombo();


        // CourseList Sonu


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
                if (Helper.confirm("sure")){
                    int user_id=Integer.parseInt(fld_user_id.getText());
                    if(User.delete(user_id)){
                        Helper.showMessage("success");
                        loadUserModel();
                    }else{
                        Helper.showMessage("error");
                    }
                }
            }
        });
        //operator menu search kısmı
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
        btn_patika_add.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_patika_name)){
                Helper.showMessage("fill");
            }else{
                if(Patika.add(fld_patika_name.getText())){
                    Helper.showMessage("success");
                    loadPatikaModel();
                    loadPatikaCombo();
                    fld_patika_name.setText(null);
                }else{
                    Helper.showMessage("error");
                }
            }
        });
    }

    private void loadCourseList() {
        DefaultTableModel clearModel= (DefaultTableModel) tbl_course_list.getModel();
        clearModel.setRowCount(0);
        int i=0;
        for (Course obj:Course.getList()){
            i=0;
            row_course_list[i++]=obj.getId();
            row_course_list[i++]=obj.getName();
            row_course_list[i++]=obj.getLang();
            row_course_list[i++]=obj.getPatika().getName();
            row_course_list[i++]=obj.getEducater().getName();
            mdl_course_list.addRow(row_course_list);
        }
    }

    private void loadPatikaModel() {
        DefaultTableModel clearModel= (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);
        int i=0;
        for(Patika obj:Patika.getList()){
            i=0;
            row_patika_list[i++]=obj.getId();
            row_patika_list[i++]=obj.getName();

            mdl_patika_list.addRow(row_patika_list);
        }
    }

    public void loadUserModel(){
        DefaultTableModel clearModel= (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);
        int i=0;
        for (User obj:User.getList()){
            i=0;
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

    public void loadPatikaCombo(){
        cmb_course_patika.removeAllItems();
        for (Patika obj:Patika.getList()){
            cmb_course_patika.addItem(new Item(obj.getId(),obj.getName()));
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
