def run(messenger, quirks, args):
    ans = "You are running QuirkBot version " + messenger.getVersion()
    ans += "\nAvailible commands are:\n"
    comms = messenger.getCommands()
    for i in range(comms.length):
        ans += "\t" + comms[i] + "\n"
        
    ans += "Availible quirks are:"
    for i in range(quirks.length):
        ans += "\n\t" + quirks[i]
        
    return ans
