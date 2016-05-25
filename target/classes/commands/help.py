def run(m, q, arg):
    ans = "You are running QuirkBot version " + m.getVersion()
    ans += "\nAvailible commands are:\n"
    comms = m.getCommands()
    for i in range(len(comms)):
        ans += "\t" + comms[i] + "\n"
        
    ans += "Availible quirks are:"
    for i in range(len(q)):
        ans += "\n\t" + q[i]
        
    return ans
