package challenge;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class ChallengeBackpack {

	public static Inventory backpack = Bukkit.createInventory(null, 27, "Backpack");
	
	public static Inventory updateBackpack() {
		
		return backpack;
	}
	
}
