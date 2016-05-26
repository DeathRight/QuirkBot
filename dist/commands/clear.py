def run(m, q, arg, c):
    if len(arg) > 0 and arg[0] == "all":
        m.chanQuirks.clear()
        return "All quirks cleared"
    else:
        m.chanQuirks.remove(c)
        return "Cleared quirk for this channel"
