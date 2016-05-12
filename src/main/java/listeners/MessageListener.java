package listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;

import quirks.Quirker;

import org.python.util.PythonInterpreter;
import org.python.core.*;

public class MessageListener implements MessageCreateListener
{
    private Quirker q;
    public Map<String, Integer> chanQuirks;

    public boolean silent;

    public File[] commands;

    public String prefix;
    public String silentPrefix;

    public PythonInterpreter interp;

    public MessageListener(Quirker qu, Map<String, Integer> map, String cdir, String pref, String spref)
    {
        q = qu;
        chanQuirks = map;
        silent = false;
        prefix = pref;
        silentPrefix = spref;

        File c = new File(cdir);
        if (!c.exists())
            c.mkdir();
        else if (!c.isDirectory())
        {
            System.out.println("Error: commands directory already exists, and is actually a file. Shutting down");
            System.exit(2);
        } else
            importCommands(c);

        interp = new PythonInterpreter();
    }

    public void onMessageCreate(DiscordAPI api, Message m)
    {
        if (!m.getAuthor().isYourself())
            return;
        String s = m.getContent();
        Channel chan = m.getChannelReceiver();
        String[] params = s.split(" ");
        if (params[0].equalsIgnoreCase(prefix))
        {
            if (params.length == 1)
                m.reply("If you are reading this, it means you successfully installed the Quirkinator v0.0.1-SNAPSHOT\nType \"~q help\" for a list of commands");

            else if (q.isQuirk(compile(params)))
            {
                String quirk = compile(params);
                chanQuirks.put(chan.getId(), q.getQuirkId(quirk));
                if (!silent)
                    m.reply("Setting channel quirk to: " + quirk);
            } else
            {
                run(getCommandId(params[1]), m, combine(params));
            }

            if (silent)
            {
                m.delete();
            }

        } else if (params[0].equalsIgnoreCase(silentPrefix))
        {
            if (params.length == 1)
            {
                silent = !silent;
            } else if (q.isQuirk(compile(params)))
            {
                String quirk = compile(params);
                chanQuirks.put(chan.getId(), q.getQuirkId(quirk));
            } else
                run(getCommandId(params[1]), m, combine(params));
            m.delete();

        } else if (chanQuirks.containsKey(chan.getId()))
        {
            List<User> mentions = m.getMentions();
            s = q.quirk(s, mentions, chanQuirks.get(chan.getId()));
            m.edit(s);
        }
    }

    private String compile(String[] params)
    {

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < params.length; i++)
        {
            sb.append(params[i]);
            sb.append(' ');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private String[] combine(String[] params)
    {
        String[] ans = new String[params.length - 2];
        for (int i = 2; i < params.length; i++)
            ans[i - 2] = params[i];
        return ans;
    }

    private void importCommands(File dir)
    {
        ArrayList<File> commandFiles = new ArrayList<File>();
        for (File f : dir.listFiles())
            if (!f.isDirectory())
                commandFiles.add(f);
        commands = commandFiles.toArray(commands);

    }

    private int getCommandId(String name)
    {
        for (int i = 0; i < commands.length; i++)
        {
            if (name.equalsIgnoreCase(commands[i].getName().substring(0, commands[i].getName().lastIndexOf('.'))))
            {
                return i;
            }
        }
        return -1;
    }

    private void run(int id, Message m, String[] args)
    {
        if (id == -1)
            return;
        String filename = commands[id].getName().substring(0, commands[id].getName().lastIndexOf('.'));
        interp.exec("import " + filename);
        interp.set("messenger", getCommands());
        interp.set("quirker", q);
        interp.set("out", "");
        interp.set("args", args);
        interp.exec(filename + ".run(messenger, quirker, args, out");
        String out = interp.get("out").asString();
        if (!out.isEmpty())
            m.reply(out);
    }

    public String[] getCommands()
    {
        String[] ans = new String[commands.length];
        for (int i = 0; i < ans.length; i++)
            ans[i] = commands[i].getName().substring(0, commands[i].getName().lastIndexOf('.'));
        return ans;
    }

}
