# InvitationSystem
讓 Minecraft 伺服器實現邀請制的管理插件。  
A Minecraft plugin for invitation only server.

# Command
## invit (on/off)
如果打開，插件只會讓在白名單上的玩家登入。  
當玩家沒有在黑名單與白名單上，插件將會請玩家輸入邀請碼。  
如果關閉，插件會讓任何不在黑名單上的玩家登入。  
If on, InvitationSystem will only allow the player is in whitelist login.  
When the player is both not in whitelist and blacklist, InvitationSystem will ask the player to input invitation code.  
If off, InvitationSystem will allow any player who is not in blacklist login.
## invit set (player/all) (How many invitation)
設定該玩家的邀請配額。  
Set (player/all) invitation quota to (How many invitation).
## invit gencode
產生邀請碼，並使玩家的邀請配額減 1。  
Get invitation code, quota is reduced by 1.
## invit input (invitation code)
輸入邀請碼。  
如果玩家並沒有在黑名單與白名單上，插件會要求玩家輸入邀請碼。  
如果成功，玩家會被加入至白名單中。  
如果失敗，將會被請出伺服器。  
Input (invitation code).  
When the player is both not in whitelist and blacklist, InvitationSystem will ask the player to input invitation code.  
If success, the player will add to the whitelist.
If fail, the player will be kicked by plugin.
## invit block (player) [days]
將該玩家加入到黑名單中。  
如果沒有設定天數，該玩家視為永久剔除，該玩家推薦人會被加入黑名單中 3 天。  
Add the player to blacklist
If no input [days], the referrer of the player will also be added to blacklist for 3 days.
## invit unblock (player)
將該玩家從黑名單中移除。  
Remove the player from blacklist.  
## invit info (player)
顯示該玩家是否在黑白名單，邀請配額，推薦人與推薦玩家。  
Show the invitation quota and the referrer of the player.  
