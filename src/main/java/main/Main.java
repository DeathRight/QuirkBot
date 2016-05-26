package main;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.common.util.concurrent.FutureCallback;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;

import listeners.MessageListener;
import quirkers.Quirker;

public class Main
{

    private static final String DEFAULT_SETTINGS = "quirks\ncommands\n~q\n~sq";

    static Quirker q;

    static Map<String, Integer> chanMap;

    public static void main(String[] args) throws IOException
    {
        File config = new File("config.cfg");

        if (!config.exists())
        {
            config.createNewFile();
            PrintWriter out = new PrintWriter(config);
            out.write(DEFAULT_SETTINGS);
            out.close();
        }

        StringBuilder settings = new StringBuilder();

        BufferedReader in = new BufferedReader(new FileReader(config));

        while (in.ready())
        {
            settings.append(in.readLine());
            settings.append('\n');
        }

        in.close();

        settings.deleteCharAt(settings.length() - 1);

        final String[] data = settings.toString().split("\n");

        String email;
        char[] pwd;
        StringBuilder pwdBuild = new StringBuilder();

        Console c = System.console();

        System.out.print("Enter email: ");
        email = c.readLine();

        System.out.print("Enter password: ");
        pwd = c.readPassword();

        for (char ch : pwd)
        {
            pwdBuild.append(ch);
        }

        chanMap = new HashMap<String, Integer>();
        q = new Quirker(data[0]);

        final DiscordAPI api = Javacord.getApi(email, pwdBuild.toString());

        api.connect(new FutureCallback<DiscordAPI>()
        {

            public void onSuccess(DiscordAPI result)
            {
                // TODO Auto-generated method stub
                System.out.println("Connected! Type \"~q\" in any channel to get started");

                MessageListener m = new MessageListener(q, chanMap, data[1], data[2], data[3]);
                api.registerListener(m);
            }

            public void onFailure(Throwable t)
            {
                System.out.println("EVERYTHING IS HORRIBLE!!!\n" + t.getMessage());
            }

        });
    }

}
