def quirk(t):
    punc = ['.', ',', '/', '\\', '"', '\'', ':', ';', '(', ')', '!', '?']
    ans = ""
    for i in range(len(t)):
        if t[i] in punc:
            continue
        if t[i].lower() == 'o':
            ans += '0'
        else:
            ans += t[i].lower()
    return ans
