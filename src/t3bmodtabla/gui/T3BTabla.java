/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t3bmodtabla.gui;

import dnn.nominae.modulobdconexion.db.Consulta;
import dnn.nominae.modulobdconexion.db.utils.Conexion;
import dnn.nominae.modulobdconexion.dto.CampoDTO;
import dnn.nominae.modulobdconexion.dto.ColumnaDTO;
import dnn.nominae.modulobdconexion.dto.QryRespDTO;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import t3bmodtabla.utils.Exportador;
import t3bmodtabla.utils.RederCelda;

/**
 *
 * @author daniel
 */
public class T3BTabla extends javax.swing.JPanel {
    
    private String ruta = System.getProperty("user.dir");
    private String nombrearch = "";
    
    private TableRowSorter<TableModel> modeloOrd = null;
    private ArrayList<String> cols = null;
    private String qry = "";
    private t3bmodtabla.utils.Config cnf;
    private HashMap<String, String> cnf2;
    private Conexion conex = null;
    private java.sql.Connection con = null;
    private DefaultTableModel modelo = null;
    private DefaultTableModel modeloFiltro = null;
    private boolean puedeExportar = true;
    private ArrayList<ColumnaDTO> otrasCols = null;

    /**
     * Constructor para la tabla
     * @param qry consulta que se ejecutara y mostrara en la tabla (SELECT o un SP que regrese un ResultSet)
     * @param cols Array list con el texto para las columnas de las tablas en el siguiente formato:
     *              nombre(alias)campo_textoColumna_tamannioColumna_tipocolumna
     *              ejemplo "usrnombre_Nombre_150_i" (i para entero, s para cadena, d para double, f para floats, h para fechas, y para numero sin formato)
     * @param cnf HashMap con los parametros de conexion a la base de datos
     *          las llaves deben ser:
     *              "dbHost" - host de la base de datos
     *              "dbUser" - usuario para la conexion 
     *              "dbPass" - Contraseña del usuario
     *              "dbPort" - Puerto de conexion
     *              "dbName" - Nombre de la base de datos o service name de la base
     *              "dbClassDriver" - Driver de la base de datos que se utilizara (Soportado actualmente MySQL y SyBASE)
     *              "dbUrl" - URL de conexion (primera parte, antes del host) de acuerdo al driver utilizado (Soportado actualmente MySQL y SyBASE)
     */    
    public T3BTabla(String qry, ArrayList<String> cols, HashMap<String, String> cnf, boolean puedeExportar){
        this.qry = qry;
        this.cols = cols;
        this.cnf2 = cnf;
        this.cnf = null;
        this.puedeExportar = puedeExportar;
        initComponents();
        cargaComboTipoExport();
        this.txtFiltro.setText("");
        realizaConsulta();
    }
    
    /**
     * Constructor para la tabla
     * @param qry consulta que se ejecutara y mostrara en la tabla (SELECT o un SP que regrese un ResultSet)
     * @param cols Array list con el texto para las columnas de las tablas en el siguiente formato:
     *              textoColumna_tamannioColumna
     *              nombre(alias)campo_textoColumna_tamannioColumna_tipocolumna
     *              ejemplo "usrnombre_Nombre_150_i" (i para entero, s para cadena, d para double, f para floats, h para fechas, y para numero sin formato)
     * @param cnf DTO con las variables de configuracion para el acceso a la base de datos
     *              "dbHost" - host de la base de datos
     *              "dbUser" - usuario para la conexion 
     *              "dbPass" - Contraseña del usuario
     *              "dbPort" - Puerto de conexion
     *              "dbName" - Nombre de la base de datos o service name de la base
     *              "dbClassDriver" - Driver de la base de datos que se utilizara (Soportado actualmente MySQL y SyBASE)
     *              "dbUrl" - URL de conexion (primera parte, antes del host) de acuerdo al driver utilizado (Soportado actualmente MySQL y SyBASE)
     */
    public T3BTabla(String qry, ArrayList<String> cols, t3bmodtabla.utils.Config cnf, boolean puedeExportar){
        this.qry = qry;
        this.cols = cols;
        this.cnf = cnf;
        this.cnf2 = null;
        this.puedeExportar = puedeExportar;
        initComponents();
        cargaComboTipoExport();
        this.txtFiltro.setText("");
        realizaConsulta();
    }
    
    private void cargaComboTipoExport(){
        this.cmbTipoExport.setEnabled(puedeExportar);
        this.btnExportar.setEnabled(puedeExportar);
        this.cmbTipoExport.removeAllItems();
        this.cmbTipoExport.addItem("XLS");
        this.cmbTipoExport.addItem("PDF");
        this.cmbTipoExport.addItem("HTML");
        this.cmbTipoExport.addItem("CVS");
        this.cmbTipoExport.addItem("TXT");
    }
    
    /**
     * Metodo para crear la conexion a la base de datos
     * @return 
     */
    private boolean creaConexion(){
        boolean regresa = false;
        try{
            conex = new Conexion();
            if(cnf != null){
                conex.creaConexion(cnf.getDbHost()
                        , cnf.getDbUser()
                        , cnf.getDbPass()
                        , cnf.getDbPort()
                        , cnf.getDbName()
                        , cnf.getDbClassDriver()
                        , cnf.getDbUrl());
                regresa = conex.getConexion() != null;
            }else{
                if(cnf2 != null){
                    conex.creaConexion(
                            cnf2.get("dbHost")
                            ,cnf2.get("dbUser")
                            ,cnf2.get("dbPass")
                            ,cnf2.get("dbPort")
                            ,cnf2.get("dbName")
                            ,cnf2.get("dbClassDriver")
                            ,cnf2.get("dbUrl")
                    );
                    regresa = conex.getConexion() != null;
                }else{
                    conex = null;
                    regresa = false;
                }
            }
        }catch(Exception ex){
            System.out.println("["+new java.util.Date().toString()+"] Clase: "+this.getClass().toString() + " Metodo: creaConexion Excepcion" +ex.toString());
            regresa = false;
        }
        return regresa;
    }
    
    /**
     * MEtodo para cargar las columnas, de acuerdo con los datos ingresados del usuario
     */
    private void cargaColumnas(){
        try{
            this.modelo = new DefaultTableModel(){
                @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
                
                @Override
                public Class getColumnClass(int col){
                    String tipo = cols.get(col).split("_")[3];
                    if(tipo.equals("i")){
                        return Integer.class;
                    }else{
                        if(tipo.equals("d")){
                            return Double.class;
                        }else{
                            if(tipo.equals("f")){
                                return Float.class;
                            }else{
                                if(tipo.equals("h")){
                                    return java.util.Date.class;
                                }else{
                                    return String.class;
                                }
                            }
                        }
                    }
                }
            };
            for(String str : this.cols){
                modelo.addColumn(str.split("_")[1]);
            }
            this.modeloFiltro = modelo;
            this.tblDatos.setModel(modeloFiltro);
            for(int i = 0; i < modeloFiltro.getColumnCount(); i++){
                this.tblDatos.getColumnModel().getColumn(i).setPreferredWidth(Integer.parseInt(this.cols.get(i).split("_")[2]));
                this.tblDatos.getColumnModel().getColumn(i).setCellRenderer(new RederCelda(this.cols.get(i).split("_")[3]));
            }
            modeloOrd = new TableRowSorter<TableModel>(modeloFiltro);
            modeloOrd.setRowFilter(RowFilter.regexFilter(""));
            this.tblDatos.setRowSorter(modeloOrd);            
        }catch(Exception ex){
            System.out.println("["+new java.util.Date().toString()+"] Clase: "+this.getClass().toString() + " Metodo: cargaColumnas Excepcion" +ex.toString());
        }
    }
    
    
    private void cargaColumnas(final ArrayList<ColumnaDTO> columnas){
        try{
            this.otrasCols = columnas;
            this.modelo = new DefaultTableModel(){
                @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
                
                @Override
                public Class getColumnClass(int col){
                    String tipo = columnas.get(col).getEtiqueta();
                    switch(columnas.get(col).getIdTipo()){
                        case java.sql.Types.INTEGER:
                            return Integer.class;
                            
                        case java.sql.Types.DOUBLE:
                            return Double.class;
                            
                        case java.sql.Types.FLOAT:
                            return Float.class;
                            
                        case java.sql.Types.DATE:
                            return java.util.Date.class;
                            
                        default:
                            return String.class;
                                    
                    }
                }
            };
            for(ColumnaDTO col : columnas){
                modelo.addColumn(col.getEtiqueta());
            }
            this.modeloFiltro = modelo;
            this.tblDatos.setModel(modeloFiltro);
            for(int i = 0; i < modeloFiltro.getColumnCount(); i++){
                this.tblDatos.getColumnModel().getColumn(i).setPreferredWidth(100);
                String tipocelda = "";
                switch(columnas.get(i).getIdTipo()){
                    case java.sql.Types.INTEGER:
                        tipocelda = "i"; break;

                    case java.sql.Types.DOUBLE:
                        tipocelda = "d"; break;

                    case java.sql.Types.FLOAT:
                        tipocelda = "f"; break;
                        
                    case java.sql.Types.DATE:
                        tipocelda = "h"; break;

                    default:
                        tipocelda = "s"; break;

                }
                this.tblDatos.getColumnModel().getColumn(i).setCellRenderer(new RederCelda(tipocelda));
            }
            modeloOrd = new TableRowSorter<TableModel>(modeloFiltro);
            modeloOrd.setRowFilter(RowFilter.regexFilter(""));
            this.tblDatos.setRowSorter(modeloOrd);              
        }catch(Exception ex){
            System.out.println("["+new java.util.Date().toString()+"] Clase: "+this.getClass().toString() + " Metodo: cargaColumnas Excepcion" +ex.toString());
        }
    }
    
    /**
     * Metodo para realizar la consulta
     */
    private void realizaConsulta(){
        try{
            this.creaConexion();
            this.con = this.conex.getConexion();
            Consulta dao = new Consulta();
            QryRespDTO dto = dao.consulta(con, qry);
            if(dto.getRes() == 1){
                if(this.cols != null){
                    this.cargaColumnas();
                }else{
                    this.cargaColumnas(dto.getColumnas());
                    this.cols = this.obtieneColumnasDesdeSQL(dto.getColumnas());
                }
                for(HashMap<String, CampoDTO> fila: dto.getDatosTabla()){
                    java.util.Vector fi = new java.util.Vector();
                    if(this.cols != null){
                        for(String str:this.cols){
                            fi.add(fila.get(str.split("_")[0]).getValor());
                        }
                    }else{
                        for(ColumnaDTO col : this.otrasCols){
                            fi.add(fila.get(col.getEtiqueta()).getValor());
                        }
                    }
                    modelo.addRow(fi);
                    fi = null;
                }
            }            
        }catch(Exception ex){
            System.out.println("["+new java.util.Date().toString()+"] Clase: "+this.getClass().toString() + " Metodo: realizaConsulta Excepcion" +ex.toString());
        }finally{
            try{
                if(this.con != null){
                    this.con.close();
                }
            }catch(Exception ex1){
                
            }
        }
    }

    /**
     * Obtiene el valor de una celda
     * @param fila
     * @param col
     * @return 
     */
    public Object getValueAt(int fila, int col){
        int colsel = 0;
        if(this.tblDatos.getRowSorter() != null){
            colsel = tblDatos.getRowSorter().convertRowIndexToModel(fila);
        }else{
            colsel = fila;
        }
        return this.modeloFiltro.getValueAt(colsel, col);
    }
    
    /**
     * Pone valor en una celda
     * @param value
     * @param fila
     * @param col 
     */
    public void setValueAt(Object value, int fila, int col){
        int colsel = 0;
        if(this.tblDatos.getRowSorter() != null){
            colsel = tblDatos.getRowSorter().convertRowIndexToModel(fila);
        }else{
            colsel = fila;
        }
        this.modeloFiltro.setValueAt(value, colsel, col);
        this.modelo.setValueAt(value, fila, col);
    }
    
    /**
     * Agregar una fila, obvio hasta el final
     * @param fila 
     */
    public void addRow(java.util.Vector fila){
        this.modelo.addRow(fila);
        this.modeloFiltro.addRow(fila);
    }
    
    /**
     * Quitar una fila en especifico
     * @param fila 
     */
    public void removeRow(int fila){
        int colsel = 0;
        if(this.tblDatos.getRowSorter() != null){
            colsel = tblDatos.getRowSorter().convertRowIndexToModel(fila);
        }else{
            colsel = fila;
        }
        this.modelo.removeRow(fila);
        this.modeloFiltro.removeRow(colsel);
    }
    
    /**
     * Quitar todas las filas
     */
    public void removeAllRows(){
        this.modelo.setRowCount(0);
        this.modeloFiltro.setRowCount(0);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblDatos = new javax.swing.JTable();
        btnExportar = new javax.swing.JButton();
        txtFiltro = new javax.swing.JTextField();
        cmbTipoExport = new javax.swing.JComboBox();

        tblDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblDatos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDatosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDatos);

        btnExportar.setText("Exportar");
        btnExportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarActionPerformed(evt);
            }
        });

        txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(txtFiltro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbTipoExport, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExportar))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExportar)
                    .addComponent(txtFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTipoExport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Metodo para el vento del key release para el filtro de la tabla
     * @param evt 
     */
    private void txtFiltroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyReleased
        try{
            if(this.txtFiltro.getText().length()>0){
                modeloOrd.setRowFilter(RowFilter.regexFilter(this.txtFiltro.getText()+"*"));
                this.tblDatos.setRowSorter(modeloOrd);  
            }else{
                modeloFiltro = modelo;
                this.tblDatos.setModel(modeloFiltro);
            }
        }catch(Exception ex){
            System.out.println("["+new java.util.Date().toString()+"] Clase: "+this.getClass().toString() + " Metodo: txtFiltroKeyReleased Excepcion" +ex.toString());
        }
    }//GEN-LAST:event_txtFiltroKeyReleased

    /**
     * Metodo para el mouseclicked
     * @param evt 
     */
    private void tblDatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDatosMouseClicked
        try{
            int colsel = 0;
            if(this.tblDatos.getRowSorter() != null){
                colsel = tblDatos.rowAtPoint(evt.getPoint());
                colsel = tblDatos.getRowSorter().convertRowIndexToModel(colsel);
            }else{
                colsel = tblDatos.rowAtPoint(evt.getPoint());
            }
            
            String salida = "[";
            for(int i =0; i < modeloFiltro.getColumnCount(); i++){
                salida += modeloFiltro.getValueAt(colsel, i)+"_";
            }
            //System.out.println(salida+"]");
        }catch(Exception ex){
            System.out.println("["+new java.util.Date().toString()+"] Clase: "+this.getClass().toString() + " Metodo: tblDatosMouseClicked Excepcion" +ex.toString());
        }
    }//GEN-LAST:event_tblDatosMouseClicked

    /**
     * Metodo para exportar los datos de la tabla
     * @param evt 
     */
    private void btnExportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarActionPerformed
        Exportador exp = new Exportador();
        
        if(this.cmbTipoExport.getSelectedItem().toString().equals("CVS")){
            if(exp.exportaCVS(this.cols, modeloFiltro, ruta, "exp_"+System.currentTimeMillis())){
                JOptionPane.showMessageDialog(null, "Datos Exportados!", "T3BTabla", JOptionPane.INFORMATION_MESSAGE);
            }
        }else{
            if(this.cmbTipoExport.getSelectedItem().toString().equals("TXT")){
                if(exp.exportaTXT(this.cols, modeloFiltro, ruta, "exp_"+System.currentTimeMillis())){
                    JOptionPane.showMessageDialog(null, "Datos Exportados!", "T3BTabla", JOptionPane.INFORMATION_MESSAGE);
                }
            }else{
                if(this.cmbTipoExport.getSelectedItem().toString().equals("HTML")){
                    if(exp.exportaHTML(this.cols, modeloFiltro, "titulo", ruta,  "exp_"+System.currentTimeMillis())){
                        JOptionPane.showMessageDialog(null, "Datos Exportados!", "T3BTabla", JOptionPane.INFORMATION_MESSAGE);
                    }
                }else{
                    if(this.cmbTipoExport.getSelectedItem().toString().equals("PDF")){
                        if(exp.exportaPDF(this.cols, modeloFiltro, "titulo", ruta,  "exp_"+System.currentTimeMillis())){
                            JOptionPane.showMessageDialog(null, "Datos Exportados!", "T3BTabla", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }else{
                        if(this.cmbTipoExport.getSelectedItem().toString().equals("XLS")){
                            if(exp.exportaEXCEL(this.cols, modeloFiltro, "titulo", ruta,  "exp_"+System.currentTimeMillis())){
                                JOptionPane.showMessageDialog(null, "Datos Exportados!", "T3BTabla", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_btnExportarActionPerformed

    
    /**
     * Obtiene las columnas desde las columnas SQL devueltas en caso que no se hayan especificado las columnas desde el inicio
     * @param lista
     * @return 
     */
    private ArrayList<String> obtieneColumnasDesdeSQL(ArrayList<ColumnaDTO> lista){
        ArrayList<String> salida = null;
        try{
            if(lista != null){
                salida = new ArrayList();
                for(ColumnaDTO dto: lista){
                    String aux = dto.getNombre()+"_"+dto.getEtiqueta()+"_"+(dto.getEtiqueta().length()*5)+"_";
                    switch(dto.getIdTipo()){
                        case java.sql.Types.INTEGER:
                            aux += "i"; break;

                        case java.sql.Types.DOUBLE:
                            aux += "d"; break;

                        case java.sql.Types.FLOAT:
                            aux += "f"; break;

                        case java.sql.Types.DATE:
                            aux += "h"; break;

                        default:
                            aux += "s"; break;
                    }
                    salida.add(aux);
                }
            }            
        }catch(Exception ex){
            System.out.println("["+new java.util.Date().toString()+"] Clase: "+this.getClass().toString() + " Metodo: obtieneColumnasDesdeSQL Excepcion" +ex.toString());
            salida = null;
        }        
        return salida;        
    }
    
    /**
     * 
     * @return 
     */
    public JTable getTabla(){
        return this.tblDatos;
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExportar;
    private javax.swing.JComboBox cmbTipoExport;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDatos;
    private javax.swing.JTextField txtFiltro;
    // End of variables declaration//GEN-END:variables
}
