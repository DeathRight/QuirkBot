def run(*args):
    args[0].importCommands()
    args[1].importQuirks()
    return "Refreshed quirks and commands"
