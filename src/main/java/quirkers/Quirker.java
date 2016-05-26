package quirkers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import de.btobastian.javacord.entities.User;

import org.python.util.PythonInterpreter;

public class Quirker
{
    private File[] quirks;

    private PythonInterpreter interp;

    private String quirkDir;

    public Quirker(String quirks)
    {
        quirkDir = quirks;

        File q = new File(quirks);

        if (!q.exists())
        {
            q.mkdir();
            File it = new File(quirks + File.pathSeparator + "__init__.py");
            try
            {
                it.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        } else if (!q.isDirectory())
        {
            System.out.println("Error: quirks directory already exists, and is actually a file. Shutting down");
            System.exit(1);
        } else
            importQuirks(q);

        Properties p = new Properties();
        p.setProperty("python.import.site", "false");

        PythonInterpreter.initialize(System.getProperties(), p, new String[0]);

        interp = new PythonInterpreter();
        interp.setOut(System.out);
        interp.setErr(System.err);

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
        if (quirkID == -1)
            return text;
        String filename = quirk.getName();
        interp.exec("import " + quirkDir + "." + filename.substring(0, filename.lastIndexOf('.')));
        interp.set("text", text);
        interp.exec("ans = " + quirkDir + '.' + filename.substring(0, filename.lastIndexOf('.')) + ".quirk(text)");
        String out = interp.get("ans").asString();
        return out;
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
            if (!f.isDirectory() && !f.getName().equals("__init__.py"))
                quirkFiles.add(f);
        }
        quirks = new File[1];
        quirks = quirkFiles.toArray(quirks);
    }

    public void importQuirks()
    {
        importQuirks(new File(quirkDir));
    }

    public String[] getQuirks()
    {
        String[] ans = new String[quirks.length];
        for (int i = 0; i < ans.length; i++)
            ans[i] = quirks[i].getName().substring(0, quirks[i].getName().lastIndexOf('.'));
        return ans;
    }

    public void die()
    {
        interp.close();
    }

}
