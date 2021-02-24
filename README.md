# InvitationSystem
讓 Minecraft 伺服器實現邀請制的管理插件。  

# Command
## invit (on/off)
如果打開，插件只會讓在白名單上的玩家登入。  
當玩家沒有在黑名單與白名單上，插件將會請玩家輸入邀請碼。  
如果關閉，插件會讓任何不在黑名單上的玩家登入。  
## invit set (player/all) (How many invitation)
設定該玩家的邀請配額。  
## invit gencode
產生邀請碼，並使玩家的邀請配額減 1。  
## invit input (invitation code)
輸入邀請碼。  
如果玩家並沒有在黑名單與白名單上，插件會要求玩家輸入邀請碼。  
如果成功，玩家會被加入至白名單中。  
如果失敗，將會被請出伺服器。  
## invit block (player) [days]
將該玩家加入到黑名單中，發出的邀請碼作廢。  
如果沒有設定天數，該玩家視為永久剔除，該玩家的推薦人則也會被加入黑名單中 1 天。  
## invit unblock (player)
將該玩家從黑名單中移除。  
## invit info (player)
顯示該玩家是否在黑白名單，邀請配額，推薦人與推薦的玩家。  
