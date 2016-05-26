def run(m, q, *args):
    ans = "You are running QuirkBot version " + m.getVersion()
    ans += "\nAvailible commands are:\n"
    comms = m.getCommands()
    for i in range(len(comms)):
        ans += "\t" + comms[i] + "\n"
        
    ans += "Availible quirks are:"
    qAry = q.getQuirks()
    for i in range(len(qAry)):
        ans += "\n\t" + qAry[i]
        
    return ans
