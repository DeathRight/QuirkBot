def run(m, q, arg, me):
    if len(arg) > 0 and arg[0] == "all":
        m.chanQuirks.clear()
        return "All quirks cleared"
    else:
        m.chanQuirks.remove(me.getChannelReceiver().getId())
        return "Cleared quirk for this channel"
