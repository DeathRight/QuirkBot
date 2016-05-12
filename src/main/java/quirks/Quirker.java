package quirks;

import java.io.File;
import java.util.List;

import de.btobastian.javacord.entities.User;

import org.python.util.PythonInterpreter;
import org.python.core.*;

public class Quirker
{
    String[] quirkNames;

    public Quirker(String quirks)
    {
        File q = new File(quirks);

        if (!q.exists())
            q.mkdir();
        else if (!q.isDirectory())
        {
            System.out.println("Error: quirks directory already exists, and is actually a file. Shutting down");
            System.exit(1);
        } else
            importQuirks(q);
    }

    public int getQuirkId(String name)
    {
        for (int i = 0; i < quirkNames.length; i++)
        {
            if (name.equalsIgnoreCase(quirkNames[i]))
            {
                return i;
            }
        }
        return -1;
    }

    public String quirk(String text, List<User> mentions, int quirkID)
    {
        String quirk = quirkNames[quirkID];
        return "";
    }

    public boolean isQuirk(String name)
    {
        for (String s : quirkNames)
        {
            if (s.equals(name))
                return true;
        }
        return false;
    }

    private void importQuirks(File dir)
    {
        File[] quirkFiles = dir.listFiles();
        quirkNames = new String[quirkFiles.length];
        for (int i = 0; i < quirkNames.length; i++)
        {
            String name = quirkFiles[i].getName();
            quirkNames[i] = name.substring(name.indexOf(0, '.'));
        }
    }

    public String[] getQuirks()
    {
        return quirkNames.clone();
    }

}
