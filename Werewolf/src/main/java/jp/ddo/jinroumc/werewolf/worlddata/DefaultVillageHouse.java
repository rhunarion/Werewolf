package jp.ddo.jinroumc.werewolf.worlddata;

import java.util.HashMap;

import jp.ddo.jinroumc.werewolf.util.C;

import org.bukkit.ChatColor;

public class DefaultVillageHouse {
	private static HashMap<ChatColor, DefaultVillageHouseCore> houseMap = new HashMap<ChatColor, DefaultVillageHouseCore>();

	static{
		/*   //template
		house.put(C, new DefaultVillageHouseCore(C,
				, , 62, 66, , ,			//plane
				, 63, , ,					//bed
				, 63, ,					//frontDoor
				, 63, ,					//rearDoor
				, 65, ,					//lever
				, 64, ,					//chest
				, 64, ,					//myroleSign
				, 64, ,					//namePlate
				, , ,						//voteSign
				, 64, ));					//votePos
				
		 */

		houseMap.put(C.white, new DefaultVillageHouseCore(C.white,
				18, 23, 62, 66, -14, -9,		//plane
				22, 63, -10, 90,				//bed
				18, 63, -10,					//frontDoor
				23, 63, -13,					//rearDoor
				19, 65, -10,					//lever
				20, 64, -13,					//chest
				19, 64, -12,					//myroleSign
				17, 64, -9,					//namePlate
				3, 64, 0,						//voteSign
				5, 64, -1));					//votePos
				
		houseMap.put(C.gray, new DefaultVillageHouseCore(C.gray,
				18, 23, 62, 66, -22, -17,	//plane
				22, 63, -18, 90,				//bed
				18, 63, -18,					//frontDoor
				23, 63, -21,					//rearDoor
				19, 65, -18,					//lever
				20, 64, -21,					//chest
				19, 64, -20,					//myroleSign
				17, 64, -17,					//namePlate
				3, 64, -2,						//voteSign
				5, 64, -3));					//votePos
				
		houseMap.put(C.d_gray, new DefaultVillageHouseCore(C.d_gray,
				10, 15, 62, 66, -23, -18,	//plane
				11, 63, -22, 0,				//bed
				11, 63, -18,					//frontDoor
				14, 63, -23,					//rearDoor
				11, 65, -19,					//lever
				14, 64, -20,					//chest
				13, 64, -19,					//myroleSign
				10, 64, -17,					//namePlate
				4, 65, -4,						//voteSign
				3, 64, -5));					//votePos
				
		houseMap.put(C.black, new DefaultVillageHouseCore(C.black,
				2, 7, 62, 66, -23, -18,		//plane
				3, 63, -22, 0,				//bed
				3, 63, -18,					//frontDoor
				6, 63, -23,					//rearDoor
				3, 65, -19,					//lever
				6, 64, -20,					//chest
				5, 64, -19,					//myroleSign
				2, 64, -17,					//namePlate
				2, 64, -3,						//voteSign
				1, 64, -5));					//votePos
				
		houseMap.put(C.d_red, new DefaultVillageHouseCore(C.d_red,
				-6, -1, 62, 66, -23, -18,	//plane
				-2, 63, -22, 0,				//bed
				-2, 63, -18,					//frontDoor
				-5, 63, -23,					//rearDoor
				-2, 65, -19,					//lever
				-5, 64, -20,					//chest
				-4, 64, -19,					//myroleSign
				-1, 64, -17,					//namePlate
				0, 64, -3,						//voteSign
				-1, 64, -5));					//votePos
				
		houseMap.put(C.red, new DefaultVillageHouseCore(C.red,
				-14, -9, 62, 66, -23, -18,	//plane
				-10, 63, -22, 0,				//bed
				-10, 63, -18,					//frontDoor
				-13, 63, -23,					//rearDoor
				-10, 65, -19,					//lever
				-13, 64, -20,					//chest
				-12, 64, -19,					//myroleSign
				-9, 64, -17,					//namePlate
				-2, 64, -3,					//voteSign
				-3, 64, -5));					//votePos
				
		houseMap.put(C.gold, new DefaultVillageHouseCore(C.gold,
				-22, -17, 62, 66, -20, -15,	//plane
				-21, 63, -16, -90,			//bed
				-17, 63, -16,					//frontDoor
				-22, 63, -19,					//rearDoor
				-18, 65, -16,					//lever
				-19, 64, -19,					//chest
				-18, 64, -18,					//myroleSign
				-16, 64, -15,					//namePlate
				-4, 65, -4,					//voteSign
				-5, 64, -3));					//votePos
				
		houseMap.put(C.yellow, new DefaultVillageHouseCore(C.yellow,
				-22, -17, 62, 66, -12, -7,	//plane
				-21, 63, -8, -90,				//bed
				-17, 63, -8,					//frontDoor
				-22, 63, -11,					//rearDoor
				-18, 65, -8,					//lever
				-19, 64, -11,					//chest
				-18, 64, -10,					//myroleSign
				-16, 64, -7,					//namePlate
				-3, 64, -2,					//voteSign
				-5, 64, -1));					//votePos
				
		houseMap.put(C.green, new DefaultVillageHouseCore(C.green,
				-22, -17, 62, 66, 7, 12,		//plane
				-21, 63, 8, -90,				//bed
				-17, 63, 8,					//frontDoor
				-22, 63, 11,					//rearDoor
				-18, 65, 8,					//lever
				-19, 64, 11,					//chest
				-18, 64, 10,					//myroleSign
				-16, 64, 7,					//namePlate
				-3, 64, 0,						//voteSign
				-5, 64, 1));					//votePos
				
		houseMap.put(C.d_green, new DefaultVillageHouseCore(C.d_green,
				-22, -17, 62, 66, 15, 20,		//plane
				-21, 63, 16, -90,				//bed
				-17, 63, 16,					//frontDoor
				-22, 63, 19,					//rearDoor
				-18, 65, 16,					//lever
				-19, 64, 19,					//chest
				-18, 64, 18,					//myroleSign
				-16, 64, 15,					//namePlate
				-3, 64, 2,						//voteSign
				-5, 64, 3));					//votePos
				
		houseMap.put(C.d_aqua, new DefaultVillageHouseCore(C.d_aqua,
				-14, -9, 62, 66, 18, 23,		//plane
				-10, 63, 22, 180,				//bed
				-10, 63, 18,					//frontDoor
				-13, 63, 23,					//rearDoor
				-10, 65, 19,					//lever
				-13, 64, 20,					//chest
				-12, 64, 19,					//myroleSign
				-9, 64, 17,					//namePlate
				-4, 65, 4,						//voteSign
				-3, 64, 5));					//votePos
				
		houseMap.put(C.d_blue, new DefaultVillageHouseCore(C.d_blue,
				-6, -1, 62, 66, 18, 23,		//plane
				-2, 63, 22, 180,				//bed
				-2, 63, 18,					//frontDoor
				-5, 63, 23,					//rearDoor
				-2, 65, 19,					//lever
				-5, 64, 20,					//chest
				-4, 64, 19,					//myroleSign
				-1, 64, 17,					//namePlate
				-2, 64, 3,						//voteSign
				-1, 64, 5));					//votePos
				
		houseMap.put(C.aqua, new DefaultVillageHouseCore(C.aqua,
				2, 7, 62, 66, 18, 23,		//plane
				3, 63, 22, 180,				//bed
				3, 63, 18,						//frontDoor
				6, 63, 23,						//rearDoor
				3, 65, 19,						//lever
				6, 64, 20,						//chest
				5, 64, 19,						//myroleSign
				2, 64, 17,						//namePlate
				0, 64, 3,						//voteSign
				1, 64, 5));					//votePos
				
		houseMap.put(C.blue, new DefaultVillageHouseCore(C.blue,
				10, 15, 62, 66, 18, 23,		//plane
				11, 63, 22, 180,				//bed
				11, 63, 18,					//frontDoor
				14, 63, 23,					//rearDoor
				11, 65, 19,					//lever
				14, 64, 20,					//chest
				13, 64, 19,					//myroleSign
				10, 64, 17,					//namePlate
				2, 64, 3,						//voteSign
				3, 64, 5));					//votePos
				
		houseMap.put(C.d_purple, new DefaultVillageHouseCore(C.d_purple,
				18, 23, 62, 66, 17, 22,		//plane
				22, 63, 18, 90,				//bed
				18, 63, 18,					//frontDoor
				23, 63, 21,					//rearDoor
				19, 65, 18,					//lever
				20, 64, 21,					//chest
				19, 64, 20,					//myroleSign
				17, 64, 17,					//namePlate
				4, 65, 4,						//voteSign
				5, 64, 3));					//votePos
				
		houseMap.put(C.l_purple, new DefaultVillageHouseCore(C.l_purple,
				18, 23, 62, 66, 9, 14,		//plane
				22, 63, 10, 90,				//bed
				18, 63, 10,					//frontDoor
				23, 63, 13,					//rearDoor
				19, 65, 10,					//lever
				20, 64, 13,					//chest
				19, 64, 12,					//myroleSign
				17, 64, 9,						//namePlate
				3, 64, 2,						//voteSign
				5, 64, 1));					//votePos
	}
	
	public static HashMap<ChatColor, DefaultVillageHouseCore> getHouseMap(){
		return houseMap;
	}
	
}
