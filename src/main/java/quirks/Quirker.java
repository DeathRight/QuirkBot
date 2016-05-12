package quirks;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import de.btobastian.javacord.entities.User;

import org.python.util.PythonInterpreter;
import org.python.core.*;

public class Quirker
{
    File[] quirks;

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
        for (int i = 0; i < quirks.length; i++)
        {
            if (name.equalsIgnoreCase(quirks[i].getName().substring(0, quirks[i].getName().lastIndexOf('.'))))
            {
                return i;
            }
        }
        return -1;
    }

    public String quirk(String text, List<User> mentions, int quirkID)
    {
        File quirk = quirks[quirkID];
        return "";
    }

    public boolean isQuirk(String name)
    {
        for (File f : quirks)
        {
            if (f.getName().substring(0, f.getName().lastIndexOf('.')).equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    private void importQuirks(File dir)
    {
        ArrayList<File> quirkFiles = new ArrayList<File>();
        for (File f : dir.listFiles())
        {
            if (!f.isDirectory())
                quirkFiles.add(f);
        }
        quirks = quirkFiles.toArray(quirks);
    }

    public String[] getQuirks()
    {
        String[] ans = new String[quirks.length];
        for (int i = 0; i < ans.length; i++)
            ans[i] = quirks[i].getName().substring(0, quirks[i].getName().lastIndexOf('.'));
        return ans;
    }

}
