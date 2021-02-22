# invitation system
A Minecraft plugin for invitation only server

# Command
## invit (on/off)
If on, the plugin will NOT allow any player who does not has the invitation code.
If off, the plugin will allow any player who is not in blacklist login.
## invit set (who/all) (How many invitation)
Set (who/all) invitation quota to (How many invitation).
## invit gencode
Get invitation code, quota is reduced by 1.
## invit input (invitation code)
Input (invitation code), when new player first time into server.
If success, the player will add to the whitelist.
## invit block (who)
block (who) and it's referrer will be locked for 3 days.
## invit unblock [who]
unblock (who) and it's referrer will be unlocked if in lock.
## invit lock (who) (days)
Lock (who) for (days) days
## invit info (who)
Show the invitation quota and it's referrer of (who)
