package networkUtils;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class ArchivosUtil
{
    public static List<String> leerArchivo(String ruta)
    {
        List<String> arrRaw = new ArrayList<String>();
        String salida = "";
        
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new java.io.FileReader(ruta));
            if(br != null)
            {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) 
                {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                    arrRaw.add(line);
                }
                //arrRaw.add(sb.toString());
                //salida = sb.toString();
                
                br.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return arrRaw;
    }
}
