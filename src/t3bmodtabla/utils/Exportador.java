/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t3bmodtabla.utils;


import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author daniel
 */
public class Exportador {

    private static String CVSEXT = ".cvs";
    private static String HTMLEXT = ".html";
    private static String TXTEXT = ".txt";
    private static String XLSEXT = ".xls";
    private static String PDFEXT = ".pdf";
    
    private static String CVSSEP = ",";
    private static String TXTSEP = "|";

    public Exportador() {

    }

    
    /**
     * Metodo para exportar a CVS un table model
     * @param cols
     * @param modelo
     * @param ruta
     * @param nombrearch
     * @return 
     */
    public boolean exportaCVS(ArrayList<String> cols, DefaultTableModel modelo, String ruta, String nombrearch) {
        boolean flag = false;
        StringBuffer sb = new StringBuffer();
        NumberFormat formato1 = new DecimalFormat("#,##0");
        NumberFormat formato2 = new DecimalFormat("#,##0.00");
        NumberFormat formato3 = new DecimalFormat("##0");
        try {
            String linea = "";
            for (String aux : cols) {
                linea += aux.split("_")[1]+CVSSEP;
            }
            sb.append(linea.substring(0, linea.lastIndexOf(CVSSEP)));
            sb.append("\n");
            linea = "";
            for (int i = 0; i < modelo.getRowCount(); i++) {
                linea = "";
                for (int j = 0; j < modelo.getColumnCount(); j++) {
                    String tipoDato = cols.get(j).split("_")[3];
                    Object aux = "";
                    if (tipoDato.equals("-")) {
                        aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):"");
                       linea += aux.toString().replaceAll(",", "")+CVSSEP;
                    } else {
                        if (tipoDato.equals("i")) {
                            aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                            linea += formato1.format(aux).replaceAll(",", "")+CVSSEP;
                        } else {
                            if (tipoDato.equals("d")) {
                                aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                                linea += formato2.format(aux).replaceAll(",", "")+CVSSEP;
                            } else {
                                if (tipoDato.equals("f")) {
                                    aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                                    linea += formato2.format(aux).replaceAll(",", "")+CVSSEP;
                                } else {
                                    if (tipoDato.equals("y")) {
                                        aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                                        linea += formato3.format(aux).replaceAll(",", "")+CVSSEP;
                                    } else {
                                        aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j).toString():"");
                                        linea += aux.toString().replaceAll(",", "")+CVSSEP;
                                    }
                                }
                            }
                        }
                    }
                }
                sb.append(linea.substring(0, linea.lastIndexOf(CVSSEP)));
                sb.append("\n");
            }
            System.out.println(sb);
            flag = escribeArchivo(ruta, nombrearch, CVSEXT, sb);
        } catch (Exception ex) {
            System.out.println("[" + new java.util.Date().toString() + "] Clase: " + this.getClass().toString() + " Metodo: exportaCVS Excepcion" + ex.toString());
        }
        return flag;
    }
    
    
    /**
     * Exporta a TXT
     * @param cols
     * @param modelo
     * @param ruta
     * @param nombrearch
     * @return 
     */
    public boolean exportaTXT(ArrayList<String> cols, DefaultTableModel modelo, String ruta, String nombrearch) {
        boolean flag = false;
        StringBuffer sb = new StringBuffer();
        NumberFormat formato1 = new DecimalFormat("#,##0");
        NumberFormat formato2 = new DecimalFormat("#,##0.00");
        NumberFormat formato3 = new DecimalFormat("##0");
        try {
            String linea = "";
            for (String aux : cols) {
                linea += aux.split("_")[1]+TXTSEP;
            }
            sb.append(linea.substring(0, linea.lastIndexOf(TXTSEP)));
            sb.append("\n");
            linea = "";
            for (int i = 0; i < modelo.getRowCount(); i++) {
                linea = "";
                for (int j = 0; j < modelo.getColumnCount(); j++) {
                    String tipoDato = cols.get(j).split("_")[3];
                    Object aux = "";
                    if (tipoDato.equals("-")) {
                        aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):"");
                       linea += aux+TXTSEP;
                    } else {
                        if (tipoDato.equals("i")) {
                            aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                            linea += formato1.format(aux)+TXTSEP;
                        } else {
                            if (tipoDato.equals("d")) {
                                aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                                linea += formato2.format(aux)+TXTSEP;
                            } else {
                                if (tipoDato.equals("f")) {
                                    aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                                    linea += formato2.format(aux)+TXTSEP;
                                } else {
                                    if (tipoDato.equals("y")) {
                                        aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                                        linea += formato3.format(aux)+TXTSEP;
                                    } else {
                                        aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j).toString():"");
                                        linea += aux+TXTSEP;
                                    }
                                }
                            }
                        }
                    }
                }
                sb.append(linea.substring(0, linea.lastIndexOf(TXTSEP)));
                sb.append("\n");
            }
            System.out.println(sb);
            flag = escribeArchivo(ruta, nombrearch, TXTEXT, sb);
        } catch (Exception ex) {
            System.out.println("[" + new java.util.Date().toString() + "] Clase: " + this.getClass().toString() + " Metodo: exportaTXT Excepcion" + ex.toString());
        }
        return flag;
    }
    
    /**
     * Metodo para exportar en HTML 
     * @param cols
     * @param modelo
     * @param titulo
     * @param ruta
     * @param nombrearch
     * @return 
     */
    public boolean exportaHTML(ArrayList<String> cols, DefaultTableModel modelo, String titulo, String ruta, String nombrearch) {
        boolean flag = false;
        StringBuffer sb = new StringBuffer();
        NumberFormat formato1 = new DecimalFormat("#,##0");
        NumberFormat formato2 = new DecimalFormat("#,##0.00");
        NumberFormat formato3 = new DecimalFormat("##0");
        String headerHTML = "<html><head><title>"+titulo+"</title><head><body>";
        try {
            sb.append(headerHTML);
            String linea = "";
            String head = "<tr>";
            String body = "";
            sb.append("<table style=\"border: thin solid #000000; height: auto; width: 90%; margin-left: 5%; padding: 0px; \">");
            for (String aux : cols) {
                head += "<th>"+aux.split("_")[1]+"</th>";
            }
            head += "</tr>";
            sb.append(head);
            for (int i = 0; i < modelo.getRowCount(); i++) {
                linea = "";
                body += "<tr>";
                for (int j = 0; j < modelo.getColumnCount(); j++) {
                    String tipoDato = cols.get(j).split("_")[3];
                    Object aux = "";
                    if (tipoDato.equals("-")) {
                        aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):"");
                       linea += "<td style=\"border-top: thin solid #000000; text-align: left\">"+aux+"</td>";
                    } else {
                        if (tipoDato.equals("i")) {
                            aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                            linea += "<td style=\"border-top: thin solid #000000; text-align: right\">"+formato1.format(aux)+"</td>";
                        } else {
                            if (tipoDato.equals("d")) {
                                aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                                linea += "<td style=\"border-top: thin solid #000000; text-align: right\">"+formato2.format(aux)+"</td>";
                            } else {
                                if (tipoDato.equals("f")) {
                                    aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                                    linea += "<td style=\"border-top: thin solid #000000; text-align: right\">"+formato2.format(aux)+"</td>";
                                } else {
                                    if (tipoDato.equals("y")) {
                                        aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                                        linea += "<td style=\"border-top: thin solid #000000; text-align: right\">"+formato3.format(aux)+"</td>";
                                    } else {
                                        aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j).toString():"");
                                        linea += "<td style=\"border-top: thin solid #000000; text-align: left\">"+aux+"</td>";
                                    }
                                }
                            }
                        }
                    }
                }
                body += linea+"</tr>";
                sb.append(body);
                body = "";
            }
            sb.append("</table></body></html>");
            System.out.println(sb);
            flag = escribeArchivo(ruta, nombrearch, HTMLEXT, sb);
        } catch (Exception ex) {
            System.out.println("[" + new java.util.Date().toString() + "] Clase: " + this.getClass().toString() + " Metodo: exportaTXT Excepcion" + ex.toString());
        }
        return flag;
    }
    
    /**
     * 
     * @param cols columnas para el reporte
     * @param modelo modelo de la tabla con el contenido
     * @param config configuracion para el export
     * @param ruta ruta de colocacion del archivo
     * @param nombrearch nombre del archivo
     * @return 
     */
    public boolean exportaHTML(ArrayList<String> cols, DefaultTableModel modelo, ExportConfig config, String ruta, String nombrearch) {
        boolean flag = false;
        StringBuffer sb = new StringBuffer();
        NumberFormat formato1 = new DecimalFormat("#,##0");
        NumberFormat formato2 = new DecimalFormat("#,##0.00");
        NumberFormat formato3 = new DecimalFormat("##0");
        String headerHTML = "<html><head><title>"+config.getTitulo()+"</title><head><body>";
        try {
            sb.append(headerHTML);
            sb.append("<h1>").append(config.getTitulo()).append("</h1>");
            sb.append("<h3>").append(config.getSubtitulo()).append("</h3>");
            sb.append("<p>").append(config.getDetalle()).append("</p>");
            sb.append("<p>").append(config.getUsuario()).append("</p>");
            String linea = "";
            String head = "<tr>";
            String body = "";
            sb.append("<table style=\"border: thin solid #000000; height: auto; width: 90%; margin-left: 5%; padding: 0px; \">");
            for (String aux : cols) {
                head += "<th>"+aux.split("_")[1]+"</th>";
            }
            head += "</tr>";
            sb.append(head);
            for (int i = 0; i < modelo.getRowCount(); i++) {
                linea = "";
                body += "<tr>";
                for (int j = 0; j < modelo.getColumnCount(); j++) {
                    String tipoDato = cols.get(j).split("_")[3];
                    Object aux = "";
                    if (tipoDato.equals("-")) {
                        aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):"");
                       linea += "<td style=\"border-top: thin solid #000000; text-align: left\">"+aux+"</td>";
                    } else {
                        if (tipoDato.equals("i")) {
                            aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                            linea += "<td style=\"border-top: thin solid #000000; text-align: right\">"+formato1.format(aux)+"</td>";
                        } else {
                            if (tipoDato.equals("d")) {
                                aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                                linea += "<td style=\"border-top: thin solid #000000; text-align: right\">"+formato2.format(aux)+"</td>";
                            } else {
                                if (tipoDato.equals("f")) {
                                    aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                                    linea += "<td style=\"border-top: thin solid #000000; text-align: right\">"+formato2.format(aux)+"</td>";
                                } else {
                                    if (tipoDato.equals("y")) {
                                        aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                                        linea += "<td style=\"border-top: thin solid #000000; text-align: right\">"+formato3.format(aux)+"</td>";
                                    } else {
                                        aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j).toString():"");
                                        linea += "<td style=\"border-top: thin solid #000000; text-align: left\">"+aux+"</td>";
                                    }
                                }
                            }
                        }
                    }
                }
                body += linea+"</tr>";
                sb.append(body);
                body = "";
            }
            sb.append("</table></body></html>");
            flag = escribeArchivo(ruta, nombrearch, HTMLEXT, sb);
        } catch (Exception ex) {
            System.out.println("[" + new java.util.Date().toString() + "] Clase: " + this.getClass().toString() + " Metodo: exportaTXT Excepcion" + ex.toString());
        }
        return flag;
    }
    
    
    /**
     * Metodo para exportar en PDF
     * @param cols
     * @param modelo
     * @param titulo
     * @param ruta
     * @param nombrearch
     * @return 
     */
    public boolean exportaPDF(ArrayList<String> cols, DefaultTableModel modelo, String titulo, String ruta, String nombrearch){
        boolean flag = false;
        String arch = ruta+File.separator+nombrearch+PDFEXT;
        NumberFormat formato1 = new DecimalFormat("#,##0");
        NumberFormat formato2 = new DecimalFormat("#,##0.00");
        NumberFormat formato3 = new DecimalFormat("##0");
        try{
            Document documento = null;
            if(cols.size() > 7){
                documento = new Document(PageSize.A4.rotate(), 25,25,25,25);
            }else{
                documento = new Document(PageSize.A4, 25,25,25,25);
            }
            PdfWriter.getInstance(documento, new FileOutputStream(arch));
            documento.open();
            Font fontTitulo = new Font(FontFamily.HELVETICA, 10, Font.BOLD, GrayColor.GRAYBLACK);
            Font fontCuerpo = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.GRAYBLACK);
            PdfPTable tabla = new PdfPTable(cols.size());
            tabla.setWidthPercentage(100);
            float[] tamanios = new float[cols.size()];
            for(int i = 0; i < cols.size(); i++){
                tamanios[i] = Float.parseFloat(cols.get(i).split("_")[2]);
            }
            tabla.setWidths(tamanios);
            
            for(int i = 0; i < cols.size(); i++){
                PdfPCell celda = new PdfPCell(new Phrase(cols.get(i).split("_")[1], fontTitulo));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(celda);
                celda = null;
            }
            
            for(int i = 0; i < modelo.getRowCount(); i++){
                for(int j = 0; j< modelo.getColumnCount(); j++){
                    PdfPCell celda = null;
                    String tipoDato = cols.get(j).split("_")[3];
                    Object aux = "";
                    if (tipoDato.equals("-")) {
                        aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):"");
                        celda = new PdfPCell(new Phrase(aux.toString(), fontCuerpo));
                        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    } else {
                        if (tipoDato.equals("i")) {
                            aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                            celda = new PdfPCell(new Phrase(formato1.format(aux), fontCuerpo));
                            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        } else {
                            if (tipoDato.equals("d")) {
                                aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                                celda = new PdfPCell(new Phrase(formato2.format(aux), fontCuerpo));
                                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            } else {
                                if (tipoDato.equals("f")) {
                                    aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                                    celda = new PdfPCell(new Phrase(formato2.format(aux), fontCuerpo));
                                    celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                } else {
                                    if (tipoDato.equals("y")) {
                                        aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                                        celda = new PdfPCell(new Phrase(formato3.format(aux), fontCuerpo));
                                        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                    } else {
                                        aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j).toString():"");
                                        celda = new PdfPCell(new Phrase(aux.toString(), fontCuerpo));
                                        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                                    }
                                }
                            }
                        }
                    }
                    
                    tabla.addCell(celda);
                    celda = null;
                }
            }
            
            documento.add(tabla);
            documento.close();
            
            flag = true;
            
        }catch(Exception ex){
            System.out.println("[" + new java.util.Date().toString() + "] Clase: " + this.getClass().toString() + " Metodo: exportaPDF Excepcion" + ex.toString());
        }
        return flag;
    }
    
    /**
     * Metodo para realizar el export a excel
     * @param cols
     * @param modelo
     * @param titulo
     * @param ruta
     * @param nombrearch
     * @return 
     */
    public boolean exportaEXCEL(ArrayList<String> cols, DefaultTableModel modelo, String titulo, String ruta, String nombrearch){
        boolean flag = false;
        Workbook wb = null;
        FileOutputStream fileOut = null;
        CreationHelper crtHlp = null;
        CellStyle cadstl = null;
        CellStyle numstl = null;
        CellStyle datstl = null;
        CellStyle fltstl = null;
        CellStyle nsfstl = null;
        try{
            wb = new HSSFWorkbook();
            
            crtHlp = wb.getCreationHelper();
            
            cadstl = wb.createCellStyle();
            cadstl.setAlignment(HorizontalAlignment.LEFT);
            
            numstl = wb.createCellStyle();
            numstl.setDataFormat(crtHlp.createDataFormat().getFormat("#,##0"));
            numstl.setAlignment(HorizontalAlignment.RIGHT);
            
            datstl = wb.createCellStyle();
            datstl.setDataFormat(crtHlp.createDataFormat().getFormat("yyyy-MM-dd"));
            datstl.setAlignment(HorizontalAlignment.CENTER);
            
            fltstl = wb.createCellStyle();
            fltstl.setDataFormat(crtHlp.createDataFormat().getFormat("#,##0.00"));
            fltstl.setAlignment(HorizontalAlignment.RIGHT);
            
            nsfstl = wb.createCellStyle();
            nsfstl.setAlignment(HorizontalAlignment.RIGHT);
            
            fileOut = new FileOutputStream(ruta+File.separator+nombrearch+XLSEXT);
            Sheet hoja = wb.createSheet(titulo);
            int idxfila = 0;
            int idxcelda = 0;
            Row filaTitulo = hoja.createRow(idxfila++);
            for(String aux: cols){
                Cell celda = filaTitulo.createCell(idxcelda++);
                celda.setCellValue(aux.split("_")[1]);
            }
            idxcelda = 0;
            for(int i = 0; i < modelo.getRowCount(); i++){
                Row fila = hoja.createRow(idxfila++);
                for(int j = 0; j < modelo.getColumnCount(); j++){
                    Cell celda = fila.createCell(idxcelda++);
                    String tipoDato = cols.get(j).split("_")[3];
                    Object aux = "";
                    if (tipoDato.equals("-")) {
                        aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):"");
                        celda.setCellValue(aux.toString());
                        celda.setCellStyle(cadstl);
                    } else {
                        if (tipoDato.equals("i")) {
                            aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                            celda.setCellValue((Integer)aux);
                            celda.setCellStyle(numstl);
                        } else {
                            if (tipoDato.equals("d")) {
                                aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                                celda.setCellValue((Double)aux);
                                celda.setCellStyle(fltstl);
                            } else {
                                if (tipoDato.equals("f")) {
                                    aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                                    celda.setCellValue((Float)aux);
                                    celda.setCellStyle(fltstl);
                                } else {
                                    if (tipoDato.equals("y")) {
                                        aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j):0);
                                        celda.setCellValue(aux.toString());
                                        celda.setCellStyle(nsfstl);
                                    } else {
                                        aux = (modelo.getValueAt(i, j)!= null?modelo.getValueAt(i, j).toString():"");
                                        celda.setCellValue(aux.toString());
                                        celda.setCellStyle(datstl);
                                    }
                                }
                            }
                        }
                    }
                }
                idxcelda = 0;
            }
            
            
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
            flag = true;
        }catch(Exception ex){
            System.out.println("[" + new java.util.Date().toString() + "] Clase: " + this.getClass().toString() + " Metodo: exportaEXCEL Excepcion" + ex.toString());
            flag = false;
        }
        return flag;
    }
    
    /**
     * Metodo para escribir el archivo con el contenido del export
     * @param ruta ruta donde se colocara
     * @param nombre nombre del archivo indicado por el usuario
     * @param ext extension del archivo
     * @param contenido contenido del archivo
     * @return true si lo genera corretamente false si hay una excepcion
     */
    private boolean escribeArchivo(String ruta, String nombre, String ext, StringBuffer contenido){
        boolean flag = false;
        BufferedWriter fw = null;
        PrintWriter pw = null;
        try{
            fw = new BufferedWriter(new FileWriter(ruta+File.separator+nombre+ext));
            fw.write(contenido.toString());
            fw.flush();
            fw.close();
            flag = new File(ruta+File.separator+nombre+"."+ext).exists();
        }catch(Exception ex){
            System.out.println("[" + new java.util.Date().toString() + "] Clase: " + this.getClass().toString() + " Metodo: escribeArchivo Excepcion" + ex.toString());
            flag = false;
        }
        return flag;
    }

}



