/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sistematornel.ui;

import clientesistematornel.Client;
import clientesistematornel.Util;
import entitys.Accion;
import entitys.DetalleIndicador;
import entitys.Indicador;
import entitys.Proceso;
import entitys.UsuarioRegistro;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import static sistematornel.ui.Login.IP;
import utils.Colores;
import utils.IndicadorComparator;
import utils.Singleton;
import utils.Utils;

public class Matriz extends javax.swing.JFrame implements Printable {

    MyDefaultTableModel modelo;
    private Object[] columnas = new Object[]{
        "Nombre de proceso",
        "Dueño proceso",
        "No. Grafica",
        "Indicador",
        "Eficiencia/Eficiencia",
        "Target",
        "Enero",
        "Febrero",
        "Marzo",
        "Abril",
        "Mayo",
        "Junio",
        "Julio",
        "Agosto",
        "Septiembre",
        "Octubre",
        "Noviembre",
        "Diciembre",
        "Promedio",};

    ArrayList<Indicador> indicadores;

    private String selectedData = null;

    public Matriz() {
        initComponents();

        setExtendedState(MAXIMIZED_BOTH);

        UsuarioRegistro usuarioRegistro = Singleton.getInstance().getUsuarioRegistro();
        jLabel1.setText(usuarioRegistro.getNombre() + " " + usuarioRegistro.getApellidoPaterno() + " " + usuarioRegistro.getApellidoMaterno());

        String procesos = "";

        for (Proceso proceso : usuarioRegistro.getProcesos()) {
            procesos += proceso.getNombre() + ",";
        }

        jLabel2.setText(procesos);

        /*JTableHeader header = jTable1.getTableHeader();
         header.setBackground(Colores.VERDE);
         header.setForeground(Color.WHITE);*/
        jTable1.setRowHeight(50);
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel columnModel = jTable1.getColumnModel();

        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(400);
            columnModel.getColumn(i).setMinWidth(400);
            columnModel.getColumn(i).setMaxWidth(400);
            //columnModel.getColumn(i).setResizable(true);
            columnModel.getColumn(i).setWidth(400);

        }

        Accion accion = new Accion();
        accion.setAccion(1);

        Client c = new Client(IP, Util.PORT, 3, accion);//192.168.1.72

        c.setOnMatrizListener((ArrayList<Indicador> indicadoresNew) -> {
            indicadores = indicadoresNew;
            llenarTabla(filtrarIndicadoresPorRol(indicadoresNew,3));
            //llenarTabla(indicadoresNew);
            jTable1.setDefaultRenderer(Object.class, new RenderTabla(indicadoresNew));

            promedios();
        });

        Thread hilo = new Thread(c);
        hilo.start();

    }

    private void promedios() {
        int cols = modelo.getColumnCount() - 1;
        int rows = modelo.getRowCount();
        float valor = 0;
        int cantidadValoresValidos = 0;
        int accion = 0;
        for (int i = 0; i < rows; i++) {
            accion = validarTipoPromedio(i);
            for (int j = 6; j < cols; j++) {
                Object val = modelo.getValueAt(i, j);
                if (val != null) {
                    Float valF = Float.parseFloat((String) val);
                    if (valF > 0) {
                        valor += valF;
                        cantidadValoresValidos++;
                    }
                }
            }
            
            float promedio = 0;
            if(accion == 0){
                promedio = valor / cantidadValoresValidos;
            }else if(accion == 1){
                promedio = valor;
            }
            
            modelo.setValueAt(promedio, i, modelo.getColumnCount() - 1);
            valor = 0;
            cantidadValoresValidos = 0;

        }
    }

    private void promedio(int rowSelected) {
        int cols = modelo.getColumnCount() - 1;
        //int rows = modelo.getRowCount();
        float valor = 0;
        int cantidadValoresValidos = 0;
        int accion = 0;
        //for (int i = 0; i < rows; i++) {
        
        accion = validarTipoPromedio(rowSelected);
        for (int j = 6; j < cols; j++) {
            Object val = modelo.getValueAt(rowSelected, j);
            if (val != null) {
                Float valF = Float.parseFloat((String) val);
                if (valF > 0) {
                    valor += valF;
                    cantidadValoresValidos++;
                }

            }

        }

        float promedio = 0;
        if(accion == 0){
            promedio = valor / cantidadValoresValidos;
        }else if(accion == 1){
            promedio = valor;
        }
        

        modelo.setValueAt(promedio, rowSelected, modelo.getColumnCount() - 1);

    }
    
    private int validarTipoPromedio(int rowSelected){
      Indicador indicador = indicadores.get(rowSelected);
            if(indicador.getNumeroGrafica().equals("24.1")
                    || indicador.getNumeroGrafica().equals("24.2")
                    || indicador.getNumeroGrafica().equals("25.1")
                    || indicador.getNumeroGrafica().equals("28.1")){
                return 1;
            }
            else 
                return 0;
        
    }

    private void llenarTabla(ArrayList<Indicador> indicadores) {

       // Collections.sort(indicadores,new IndicadorComparator());
        //indicadores = filtrarIndicadoresPorRol(3);
        modelo = new MyDefaultTableModel(indicadores);
        modelo.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {

                int selectedRow = jTable1.getSelectedRow();
                int selectedColumn = jTable1.getSelectedColumn();

                if ((selectedColumn >= 6 && selectedColumn <= 17)
                        && (e.getColumn() >= 6 && e.getColumn() <= 17)) {

                    DetalleIndicador detalle = null;
                    //int valor = 0;

                    Indicador indicador = indicadores.get(selectedRow);
                    try {
                        detalle = indicador.getDetalleIndicadores().get(selectedColumn - 6);

                        //valor += Integer.parseInt(detalle.getPorcentaje());
                        //System.out.println("valor: " + valor);
                    } catch (IndexOutOfBoundsException ex) {
                        return;
                    }

                    selectedData = (String) modelo.getValueAt(selectedRow, selectedColumn);
                    System.out.println("selectedData: " + selectedData);
                    if (Utils.validarNumeros(selectedData)) {
                        if (detalle != null && detalle.getPorcentaje() != null) {

                            Accion accion = new Accion();
                            accion.setAccion(3);
                            detalle.setPorcentaje(selectedData);
                            accion.setObject(detalle);

                            Client c = new Client(IP, Util.PORT, 3, accion);//192.168.1.72

                            c.setOnStatusTextListener(new Client.OnStatusTextListener() {

                                @Override
                                public void onStatusText(boolean respuesta) {
                                    if (respuesta) {
                                        System.out.println("Se actualizo correctamente");
                                        System.out.println("selectedData: " + selectedData);
                                        promedio(jTable1.getSelectedRow());
                                    } else {
                                        JOptionPane.showMessageDialog(null, "No se actualizo correctamente el porcentaje");
                                    }
                                }
                            });

                            Thread hilo = new Thread(c);
                            hilo.start();

                        }
                    } else {
                        modelo.setValueAt("0", selectedRow, selectedColumn);
                        JOptionPane.showMessageDialog(null, "Debe agregar unicamente numeros");

                    }

                }

            }
        });
        jTable1.setModel(modelo);

        for (int i = 0; i < columnas.length; i++) {
            modelo.addColumn(columnas[i]);
        }

        for (Indicador indicador : indicadores) {

            Vector fila = new Vector();
            fila.add(indicador.getNombreProceso());
            fila.add(indicador.getDuenioProceso());
            fila.add(indicador.getNumeroGrafica());
            fila.add(indicador.getDescripcionIndicador());
            fila.add(indicador.getEficienciaEficacia());
            fila.add(indicador.getTarget());

            for (DetalleIndicador detalle : indicador.getDetalleIndicadores()) {
                fila.add(detalle.getPorcentaje());
            }

            modelo.addRow(fila);

        }

        

        //jTable1.setAutoCreateRowSorter(true);
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
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        jTable1.setCellSelectionEnabled(true);
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setText("jLabel1");

        jLabel2.setText("jLabel2");

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton1.setText("Imprimir");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton2.setText("Regresar al menu");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel4.setText("Matriz Indicadores");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setText("Compañia Hulera Tornel SA. de SV.");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setText("Sistema de Gestión de Calidad");

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sistematornel/ui/catalogo_jk_tornel.png"))); // NOI18N

        jButton3.setText("Buscar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 860, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(228, 228, 228)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        /* PrinterJob printerJob = PrinterJob.getPrinterJob();
        
        
         printerJob.setPrintable(this);

         if (printerJob.printDialog()) {
         try {
         printerJob.print();
         } catch (PrinterException ex) {
         System.out.println("Error: " + ex.toString());
         }
         }*/
        try {
            // fetch the printable
            Printable printable = jTable1.getPrintable(JTable.PrintMode.FIT_WIDTH, null, null);
            PrinterJob job = PrinterJob.getPrinterJob();// fetch a PrinterJob
            job.setPrintable(printable);// set the Printable on the PrinterJob
            // create an attribute set to store attributes
            //if (attr == null) {
            HashPrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
            attr.add(OrientationRequested.LANDSCAPE);
            try {
                //}
                // display a print dialog and record whether or not the user cancels it
//            boolean printAccepted = job.printDialog(attr);
//            if (printAccepted) {// if the user didn't cancel the dialog

                job.printDialog();
                job.print(attr);// do the printing
//            }
            } catch (PrinterException ex) {
                Logger.getLogger(Matriz.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            // possibly restore the original table state here
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Menu menu = new Menu();
        menu.setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        
        ArrayList<Indicador> indicadoresFiltro= filtrarIndicadores(jTextField1.getText().toString());
                llenarTabla(filtrarIndicadoresPorRol(indicadoresFiltro, 3));
                //llenarTabla(indicadoresFiltro);
        jTable1.setDefaultRenderer(Object.class, new RenderTabla(indicadoresFiltro));
        promedios();
    }//GEN-LAST:event_jButton3ActionPerformed

    private ArrayList<Indicador> filtrarIndicadores(String filtro) {

        ArrayList<Indicador> indicadoresFiltro = new ArrayList<>();
        for (Indicador indicador : indicadores) {
            if (indicador.getNombreProceso().toLowerCase().contains(filtro.toLowerCase())) {
                indicadoresFiltro.add(indicador);
            }
        }
        return indicadoresFiltro;
    }
    
    private ArrayList<Indicador> filtrarIndicadoresPorRol(ArrayList<Indicador> indicadores, int idRol){
    
        ArrayList<Indicador> indicadoresNew = new ArrayList<>();
        
        for(Indicador indicador : indicadores){
            if(indicador.getIdRol() == idRol){
                continue;
            }else{
                indicadoresNew.add(indicador);
            }
        }
        
        return indicadoresNew;
        
    }

    /**
     * @param args the command line arguments
     */
    /* public static void main(String args[]) {
     
     Util.changeLoogAndFeel();
           

       
     java.awt.EventQueue.invokeLater(new Runnable() {
     public void run() {
     new Matriz().setVisible(true);
     }
     });
     }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        // get the bounds of the component
        Dimension dim = this.getSize();
        double cHeight = dim.getHeight();
        double cWidth = dim.getWidth();

        // get the bounds of the printable area
        double pHeight = pageFormat.getImageableHeight();
        double pWidth = pageFormat.getImageableWidth();

        double pXStart = pageFormat.getImageableX();
        double pYStart = pageFormat.getImageableY();

        double xRatio = pWidth / cWidth;
        double yRatio = pHeight / cHeight;

        Graphics2D g2 = (Graphics2D) graphics;
        g2.translate(pXStart, pYStart);
        g2.scale(xRatio, yRatio);
        this.paint(g2);

        return Printable.PAGE_EXISTS;

    }
}
