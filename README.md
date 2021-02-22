# InvitationSystem
讓 Minecraft 伺服器實現邀請制的管理插件。  
A Minecraft plugin for invitation only server.

# Command
## invit (on/off)
如果打開，插件只會讓在白名單上的玩家登入。  
如果關閉，插件會讓任何不在黑名單上的玩家登入。  
If on, the plugin will only allow the player who is on whitelist login.  
If off, the plugin will allow any player who is not in blacklist login.
## invit set (who/all) (How many invitation)
設定該玩家的邀請配額。  
Set (who/all) invitation quota to (How many invitation).
## invit gencode
產生邀請碼，並使你的邀請配額減 1。  
Get invitation code, quota is reduced by 1.
## invit input (invitation code)
輸入邀請碼。  
如果你並沒有在黑名單與白名單上，插件會要求你輸入邀請碼。  
如果成功，你會被加入至白名單中。  
如果失敗，將會被請出伺服器。
Input (invitation code), when the player first time into server.  
If success, the player will add to the whitelist.
If fail, the player will be kicked by plugin.
## invit block (who) [days]
將該玩家加入到黑名單中。  
如果沒有設定天數，該玩家視為永久剔除，該玩家推薦人會被加入黑名單中 3 天。  
Add (who) to blacklist
If no input [days], (who)'s referrer will also be added to blacklist for 3 days.
## invit unblock [who]
將該玩家從黑名單中移除。  
Remove (who) from blacklist.  
## invit info (who)
顯示該玩家是否在黑白名單，邀請配額，推薦人與推薦玩家。  
Show the invitation quota and it's referrer of (who)
