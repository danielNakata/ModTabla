/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t3bmodtabla.utils;

import java.awt.Component;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author daniel
 */
public class RederCelda extends JLabel implements TableCellRenderer{
    
    private String tipoDato = "-";
    
    public RederCelda(){
        
    }
    
    public RederCelda(String tipoDato){
        this.tipoDato = tipoDato;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        try{
            if(isSelected){
                
            }
            
            if(hasFocus){
                
            }
            
            if(tipoDato.equals("-")){
                setHorizontalAlignment(SwingConstants.LEFT);
            }else{
                if(tipoDato.equals("i")){
                    setHorizontalAlignment(SwingConstants.RIGHT);
                }else{
                    if(tipoDato.equals("d")){
                        setHorizontalAlignment(SwingConstants.RIGHT);
                    }else{
                        if(tipoDato.equals("f")){
                            setHorizontalAlignment(SwingConstants.RIGHT);
                        }else{
                            if(tipoDato.equals("h")){
                                setHorizontalAlignment(SwingConstants.CENTER);
                            }else{
                                if(tipoDato.equals("y")){
                                    setHorizontalAlignment(SwingConstants.RIGHT);
                                }else{
                                    setHorizontalAlignment(SwingConstants.LEFT);
                                }
                            }
                        }
                    }
                }
            }
            setText(formateaDato(value));
            
            setToolTipText(formateaDato(value));
        }catch(Exception ex){
            System.out.println("["+new java.util.Date().toString()+"] Clase: "+this.getClass().toString() + " Metodo: getTableCellRendererComponent Excepcion" +ex.toString());
        }
        return this;
    }
    
    @Override
    public void validate() {}
    
    @Override
    public void revalidate() {}
    
    @Override
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {}
    
    @Override
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}
    
    
    /**
     * //(i para entero, s para cadena, d para double, f para fecha, y para numero sin formato)
     * @param value
     * @return 
     */
    private String formateaDato(Object value){
        String r = "";
        if(tipoDato.equals("-")){
            r = value != null?value.toString():"";
        }else{
            if(tipoDato.equals("i")){
                NumberFormat formato = new DecimalFormat("#,##0");
                r = formato.format(value);
            }else{
                if(tipoDato.equals("d")){
                    NumberFormat formato = new DecimalFormat("#,##0.00");
                    r = formato.format(value);
                }else{
                    if(tipoDato.equals("f")){
                        NumberFormat formato = new DecimalFormat("#,##0.00");
                        r = formato.format(value);
                    }else{
                        if(tipoDato.equals("y")){
                            NumberFormat formato = new DecimalFormat("##0");
                            r = formato.format(value);
                        }else{
                            r = value != null?value.toString():"";
                        }
                    }
                }
            }
        }        
        return r;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    
}
