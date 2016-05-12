package listeners;

import java.io.File;
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
    Quirker q;
    Map<String, Integer> chanQuirks;

    boolean silent;

    String[] commands;

    String prefix;
    String silentPrefix;

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

            else if (params[1].equalsIgnoreCase("help"))
            {
                m.reply("Current command/quirk prefix: \"~q\"\nSilent command prefix: \"~sq\"\nA silent command will not print anything, and will delete the command message\nType \"~sq\" by itself to toggle silent mode\nIn silent mode, all commands are treated as silent\nAvailible Commands:\n    help\n    clear\nAvailible Quirks:\n    aradia\n    tavros\n    sollux\n    karkat\n    nepeta\n    kanaya\n    terezi\n    vriska\n    equius\n    gamzee\n    eridan\n    feferi");
            } else if (params[1].equalsIgnoreCase("clear"))
            {
                chanQuirks.remove(chan.getId());
                if (!silent)
                    m.reply("Clearing quirk");
            } else
            {
                String quirk = compile(params);
                if (q.isQuirk(quirk))
                {
                    chanQuirks.put(chan.getId(), q.getQuirkId(quirk));
                    if (!silent)
                        m.reply("Setting channel quirk to: " + quirk);
                }
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
            } else if (params[1].equalsIgnoreCase("clear"))
            {
                chanQuirks.remove(chan.getId());
            } else
            {
                String quirk = compile(params);
                if (q.isQuirk(quirk))
                {
                    chanQuirks.put(chan.getId(), q.getQuirkId(quirk));
                }
            }
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

    public String[] getQuirks()
    {
        return q.getQuirks();
    }

    private void importCommands(File dir)
    {
        File[] commandFiles = dir.listFiles();
        commands = new String[commandFiles.length];
        for (int i = 0; i < commands.length; i++)
        {
            String name = commandFiles[i].getName();
            commands[i] = name.substring(name.indexOf(0, '.'));
        }
    }

    private int getCommandId(String name)
    {
        for (int i = 0; i < commands.length; i++)
        {
            if (name.equalsIgnoreCase(commands[i]))
            {
                return i;
            }
        }
        return -1;
    }

    private void run(int id)
    {
        String command = commands[id];
    }

}
