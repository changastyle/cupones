package cupones;

import java.io.FileReader;
import networkUtils.StringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import networkUtils.ArchivosUtil;
import networkUtils.FTPDownloader;
import networkUtils.Telnet;

public class Cupones
{
    public static List<String> terminales = new ArrayList<String>();
    
    public static int pedirDraw(int drawPorDefecto)
    {
        int DRAW = -1;
        System.out.print("INSERTE DRAW (" + drawPorDefecto +"), PARA SALIR (-1):");
        Scanner scanner = new Scanner(System.in);
        String drawAux = scanner.next();
        
        // 3 - CONVIERTE EL DRAW A NUMERO:
        if(drawAux != null)
        {
            if(drawAux.length() > 0)
            {
                if(StringUtils.isNumeric(drawAux))
                {
                    DRAW =  Integer.parseInt(drawAux);
                }
            }
        }
        return DRAW;
    }
    public static void main(String[] args)
    {
        terminales.add("110999");
        terminales.add("230023");
        terminales.add("150015");
        
        // 1 - CALCULA EL DRAW DE HOY:
        Date hoy = new Date();
        
        hoy.setHours(0);
        hoy.setMinutes(0);
        hoy.setSeconds(0);
        Date longTimeAgo = new Date("2008/05/22");

        long MILLSECS_PER_DAY = 86400000L;
        long DRAWLong = (hoy.getTime() - longTimeAgo.getTime()) / MILLSECS_PER_DAY;
        int DRAW = (int) DRAWLong;
        
        
        // 2 - MUESTRA EL DRAW DE HOY Y PREGUNTA QUE DRAW QUERES HACER:
        //DRAW = pedirDraw(DRAW);
        
        while(DRAW != -1)
        {
            Telnet telegrafo = new Telnet("192.168.88.11");
            telegrafo.leerHasta("login:" , false);
            telegrafo.escribir("l5_rdusr");

            telegrafo.leerHasta("Password:" , false);
            telegrafo.escribir("l5_rdusr");
        
            for(String terminalLoop: terminales)
            {
                telegrafo.leerHasta("Please Enter Your Choice :", false);
                telegrafo.escribir("6");

                telegrafo.leerHasta("Enter Selection :", false);
                telegrafo.escribir("4");

                telegrafo.leerHasta("Insert Game :" , false);
                telegrafo.escribir("4100");

                telegrafo.leerHasta("Insert Start Draw :" , false );
                telegrafo.escribir("" + DRAW);

                telegrafo.leerHasta("Insert Last Draw :" , false);
                telegrafo.escribir("" + DRAW);

                telegrafo.leerHasta("Insert Agency (xxxxxx:Specific / 0:All):", false);
                telegrafo.escribir(terminalLoop);

                telegrafo.leerHasta("Insert Terminal (xx:Specific / 0:All):" , false);
                telegrafo.escribir("0");

                telegrafo.leerHasta("Insert Coupon Status (1:All / 2:Played / 3:Cancelled) :", false);
                telegrafo.escribir("2");

                telegrafo.leerHasta("Print File ? [y/n] :", false);
                telegrafo.escribir("n");

                telegrafo.leerHasta("Enter Selection :", false);
                telegrafo.escribir("0");

                telegrafo.leerHasta("Press <Enter> to continue...", false);
                telegrafo.escribir("");

                String archivoDestino = "C:\\cupones\\draws\\" + DRAW + "-" + terminalLoop + ".txt";
                if(FTPDownloader.bajar("192.168.88.11", 21, "l5_rdusr", "l5_rdusr", "/f2/lotos/l5_arge/dat/out/CPN_4100_" + DRAW + "_" + DRAW + "_" + terminalLoop + "_0_PLAYED.OUT" , archivoDestino))
                {
                    System.out.println("|---------------------------------------------------------------------------------------|");
                    System.out.println("|                                LEYENDO " + DRAW + " -> " + terminalLoop + "                                 |");
                    System.out.println("|---------------------------------------------------------------------------------------|");
                    List<String> lineasArchivo = ArchivosUtil.leerArchivo(archivoDestino);
                    if(lineasArchivo != null)
                    {
                        if(lineasArchivo.size() > 0)
                        {
                            for(String lineaLoop : lineasArchivo)
                            {
                                if(lineaLoop != null)
                                {
                                    System.out.println(lineaLoop);
                                }
                            }
                        }
                    }
                }
            }
            
            
            // CIERRA LA CONEXION:
            try
            {
              telegrafo.getTelnet().disconnect();
            }
            catch (IOException ex)
            {
              ex.printStackTrace();
            }
            
            DRAW = pedirDraw((int)DRAWLong);
            
        }
        //System.out.println("FECHA HOY = " + hoy);
        //System.out.println("Fecha Long Time Ago = " + longTimeAgo);

        
        //System.out.println("DRAW = " + DRAW);

        
        
        
        
        
        
                
        
        
        
        
        /*
        telegrafo.leerHasta("Please Enter Your Choice :", true);
        telegrafo.escribir("14");

        telegrafo.leerHasta("Enter the filename you want to see:" , true );
        telegrafo.escribir("/f2/lotos/l5_arge/dat/out/CPN_4100_" + DRAW + "_" + DRAW + "_110999_0_PLAYED.OUT");

        telegrafo.leerHasta("CPN_4100_" + DRAW + "_" + DRAW + "_110999_0_PLAYED.OUT: END" , true);
        telegrafo.escribir("");

        telegrafo.leerHasta("$", true);
        telegrafo.escribir("\\1");
        telegrafo.leerHasta("$", true);
        telegrafo.escribir("\\1");
        telegrafo.leerHasta("$", true);
        /*
        telegrafo.leerHasta("Press <Enter> to continue...", true);
        telegrafo.pedirIngreso();

        telegrafo.leerHasta("Please Enter Your Choice :", true);
        telegrafo.escribir("99");*/
        
    }
    
}
