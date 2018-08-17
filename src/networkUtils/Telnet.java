package networkUtils;
import cupones.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.apache.commons.net.telnet.TelnetClient;

public class Telnet
{
  private TelnetClient telnet;
  private InputStream in;
  private PrintStream out;
  private String ip;
  private int puerto;
  
  public Telnet(String direccionIP)
  {
    this.ip = direccionIP;
    this.puerto = 23;
    conectar();
  }
  
  public Telnet(String ip, int puerto)
  {
    this.ip = ip;
    this.puerto = puerto;
    
    conectar();
  }
  
  private boolean conectar()
  {
    boolean conecto = false;
    
    
      System.out.println("CONECTANDO, POR FAVOR ESPERE..");
      System.out.println("");
    try
    {
      this.telnet = new TelnetClient();
      this.telnet.connect(getIp(), getPuerto());
      
      this.in = this.telnet.getInputStream();
      if (this.in == null)
      {
        System.out.println("ERROR EN CANAL DE ENTRADA.");
        conecto = false;
      }
      else
      {
        //System.out.println("CANAL DE ENTRADA ESTABLECIDO.");
        conecto = true;
      }
      this.out = new PrintStream(this.telnet.getOutputStream());
      if (this.out == null)
      {
        System.out.println("ERROR EN CANAL DE SALIDA.");
        conecto = false;
      }
      else
      {
        //System.out.println("CANAL DE SALIDA ESTABLECIDO.");
        conecto = true;
      }
    }
    catch (Exception e)
    {
      conecto = false;
      System.out.println("ERROR: No me pude conectar a" + getIp() + ":" + getPuerto());
    }
    return conecto;
  }
  
  public String leerHasta(String hasta, boolean conSalida)
  {
    String salida = "";
    String acumulador = "";
    char c = ' ';
    
    int longitudDeHasta = hasta.length();
    if (hasta.trim() != "")
    {
      char simboloFinal = hasta.charAt(longitudDeHasta - 1);
      try
      {
        for (;;)
        {
          c = (char)this.in.read();
          acumulador = acumulador + "" + c;
          
          if(conSalida)
          {
            System.out.print("" + c);
          }
          
          salida = salida + "" + c;
          if (c != 0) {
            if ((c == simboloFinal) && 
            
              (acumulador.endsWith(hasta.trim()))) {
              break;
            }
          }
        }
      }
      catch (IOException ex)
      {
        System.out.println("Error:Al intentar leerHasta : " + hasta);
      }
    }
    else
    {
      try
      {
        for (;;)
        {
          c = (char)this.in.read();
          acumulador = acumulador + "" + c;
          System.out.print("" + c);
          salida = salida + "" + c;
          if (c == 0) {
            break;
          }
        }
      }
      catch (IOException ex)
      {
        System.out.println("Error:Al intentar leerHasta : " + hasta);
      }
    }
    return salida;
  }
  
  public void leer()
  {
    String acumulador = "";
    char c = ' ';
    try
    {
      for (;;)
      {
        c = (char)this.in.read();
        acumulador = acumulador + "" + c;
        System.out.print("" + c);
      }
    }
    catch (IOException ex)
    {
      System.out.println("Error:Al intentar leer()");
    }
  }
  
  public String escribir(String que)
  {
    String salida = que;
    try
    {
      getOut().println(que);
      getOut().flush();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return salida;
  }
  
  public void pedirIngreso()
  {
    Scanner s = new Scanner(System.in);
    escribir(s.nextLine());
  }
  
  public void enviarEnter()
  {
    try
    {
      int asciiEnter = 13;
      getOut().print((char)asciiEnter);
      getOut().flush();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public TelnetClient getTelnet()
  {
    return this.telnet;
  }
  
  public void setTelnet(TelnetClient telnet)
  {
    this.telnet = telnet;
  }
  
  public InputStream getIn()
  {
    return this.in;
  }
  
  public void setIn(InputStream in)
  {
    this.in = in;
  }
  
  public PrintStream getOut()
  {
    return this.out;
  }
  
  public void setOut(PrintStream out)
  {
    this.out = out;
  }
  
  public String getIp()
  {
    return this.ip;
  }
  
  public void setIp(String ip)
  {
    this.ip = ip;
  }
  
  public int getPuerto()
  {
    return this.puerto;
  }
  
  public void setPuerto(int puerto)
  {
    this.puerto = puerto;
  }
}
