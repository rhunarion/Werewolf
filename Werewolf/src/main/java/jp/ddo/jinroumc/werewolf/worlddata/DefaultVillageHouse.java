package jp.ddo.jinroumc.werewolf.worlddata;

import java.util.HashMap;

import jp.ddo.jinroumc.werewolf.util.C;

import org.bukkit.ChatColor;

public class DefaultVillageHouse {
	public static HashMap<ChatColor, DefVilHouse> house = new HashMap<ChatColor, DefVilHouse>();

	static{
		/*   //template
		house.put(C, new DefVilHouse(C,
				, , 62, 66, , ,		//plane
				, 63, , ,				//bed
				, 63, ,					//frontDoor
				, 63, ,					//rearDoor
				, 65, ,					//lever
				, 64, ,					//chest
				, 64, ,					//myroleSign
				, 64, ,					//namePlate
				, , ));					//voteSign
				
		 */
		/*
		house.put(C.white, new DefVilHouse(C.white,
				18, 23, 62, 66, -14, -9,		//plane
				22, 63, -10, 90,				//bed
				, 63, ,					//frontDoor
				, 63, ,					//rearDoor
				, 65, ,					//lever
				, 64, ,					//chest
				, 64, ,					//myroleSign
				, 64, ,					//namePlate
				, , ));					//voteSign
				
		house.put(C.gray, new DefVilHouse(C.gray,
				18, 23, 62, 66, -22, -17,		//plane
				22, 63, -18, 90,				//bed
				, 63, ,					//frontDoor
				, 63, ,					//rearDoor
				, 65, ,					//lever
				, 64, ,					//chest
				, 64, ,					//myroleSign
				, 64, ,					//namePlate
				, , ));					//voteSign
				
		house.put(C.d_gray, new DefVilHouse(C.d_gray,
				10, 15, 62, 66, -23, -18,		//plane
				11, 63, -22, 0,				//bed
				, 63, ,					//frontDoor
				, 63, ,					//rearDoor
				, 65, ,					//lever
				, 64, ,					//chest
				, 64, ,					//myroleSign
				, 64, ,					//namePlate
				, , ));					//voteSign
				
		house.put(C.black, new DefVilHouse(C.black,
				2, 7, 62, 66, -23, -18,		//plane
				3, 63, -22, 0,				//bed
				, 63, ,					//frontDoor
				, 63, ,					//rearDoor
				, 65, ,					//lever
				, 64, ,					//chest
				, 64, ,					//myroleSign
				, 64, ,					//namePlate
				, , ));					//voteSign
				
		house.put(C.d_red, new DefVilHouse(C.d_red,
				-6, -1, 62, 66, -23, -18,		//plane
				-2, 63, -22, 0,				//bed
				, 63, ,					//frontDoor
				, 63, ,					//rearDoor
				, 65, ,					//lever
				, 64, ,					//chest
				, 64, ,					//myroleSign
				, 64, ,					//namePlate
				, , ));					//voteSign
				
		house.put(C.red, new DefVilHouse(C.red,
				-14, -9, 62, 66, -23, -18,		//plane
				-10, 63, -22, 0,				//bed
				, 63, ,					//frontDoor
				, 63, ,					//rearDoor
				, 65, ,					//lever
				, 64, ,					//chest
				, 64, ,					//myroleSign
				, 64, ,					//namePlate
				, , ));					//voteSign
				
		house.put(C.gold, new DefVilHouse(C.gold,
				-22, -17, 62, 66, -20, -15,		//plane
				-21, 63, -16, -90,				//bed
				, 63, ,					//frontDoor
				, 63, ,					//rearDoor
				, 65, ,					//lever
				, 64, ,					//chest
				, 64, ,					//myroleSign
				, 64, ,					//namePlate
				, , ));					//voteSign
				
		house.put(C.yellow, new DefVilHouse(C.yellow,
				-22, -17, 62, 66, -12, -7,		//plane
				-21, 63, -8, -90,				//bed
				, 63, ,					//frontDoor
				, 63, ,					//rearDoor
				, 65, ,					//lever
				, 64, ,					//chest
				, 64, ,					//myroleSign
				, 64, ,					//namePlate
				, , ));					//voteSign
				
		house.put(C.green, new DefVilHouse(C.green,
				-22, -17, 62, 66, 7, 12,		//plane
				-21, 63, 8, -90,				//bed
				, 63, ,					//frontDoor
				, 63, ,					//rearDoor
				, 65, ,					//lever
				, 64, ,					//chest
				, 64, ,					//myroleSign
				, 64, ,					//namePlate
				, , ));					//voteSign
				
		house.put(C.d_green, new DefVilHouse(C.d_green,
				-22, -17, 62, 66, 15, 20,		//plane
				-21, 63, 16, -90,				//bed
				, 63, ,					//frontDoor
				, 63, ,					//rearDoor
				, 65, ,					//lever
				, 64, ,					//chest
				, 64, ,					//myroleSign
				, 64, ,					//namePlate
				, , ));					//voteSign
				
		house.put(C.d_aqua, new DefVilHouse(C.d_aqua,
				-14, -9, 62, 66, 18, 23,		//plane
				-10, 63, 22, 180,				//bed
				, 63, ,					//frontDoor
				, 63, ,					//rearDoor
				, 65, ,					//lever
				, 64, ,					//chest
				, 64, ,					//myroleSign
				, 64, ,					//namePlate
				, , ));					//voteSign
				
		house.put(C.d_blue, new DefVilHouse(C.d_blue,
				-6, -1, 62, 66, 18, 23,		//plane
				-2, 63, 22, 180,				//bed
				, 63, ,					//frontDoor
				, 63, ,					//rearDoor
				, 65, ,					//lever
				, 64, ,					//chest
				, 64, ,					//myroleSign
				, 64, ,					//namePlate
				, , ));					//voteSign
				
		house.put(C.aqua, new DefVilHouse(C.aqua,
				2, 7, 62, 66, 18, 23,		//plane
				3, 63, 22, 180,				//bed
				, 63, ,					//frontDoor
				, 63, ,					//rearDoor
				, 65, ,					//lever
				, 64, ,					//chest
				, 64, ,					//myroleSign
				, 64, ,					//namePlate
				, , ));					//voteSign
				
		house.put(C.blue, new DefVilHouse(C.blue,
				10, 15, 62, 66, 18, 23,		//plane
				11, 63, 22, 180,				//bed
				, 63, ,					//frontDoor
				, 63, ,					//rearDoor
				, 65, ,					//lever
				, 64, ,					//chest
				, 64, ,					//myroleSign
				, 64, ,					//namePlate
				, , ));					//voteSign
				
		house.put(C.d_purple, new DefVilHouse(C.d_purple,
				18, 23, 62, 66, 17, 22,		//plane
				22, 63, 18, 90,				//bed
				, 63, ,					//frontDoor
				, 63, ,					//rearDoor
				, 65, ,					//lever
				, 64, ,					//chest
				, 64, ,					//myroleSign
				, 64, ,					//namePlate
				, , ));					//voteSign
				
		house.put(C.l_purple, new DefVilHouse(C.l_purple,
				18, 23, 62, 66, 9, 14,		//plane
				22, 63, 10, 90,				//bed
				, 63, ,					//frontDoor
				, 63, ,					//rearDoor
				, 65, ,					//lever
				, 64, ,					//chest
				, 64, ,					//myroleSign
				, 64, ,					//namePlate
				, , ));					//voteSign
				




		 */
		
		
		
		
		
	}
}

class DefVilHouse{
	ChatColor color;
	
	int westPlane;
	int eastPlane;
	int bottomPlane;
	int topPlane;
	int northPlane;
	int southPlane;

	int bedX;
	int bedY;
	int bedZ;
	int bedPitch;
	
	int frontDoorX;
	int frontDoorY;
	int frontDoorZ;
	int rearDoorX;
	int rearDoorY;
	int rearDoorZ;
	int leverX;
	int leverY;
	int leverZ;
	int chestX;
	int chestY;
	int chestZ;
	int myroleSignX;
	int myroleSignY;
	int myroleSignZ;
	int namePlateX;
	int namePlateY;
	int namePlateZ;
	int voteSignX;
	int voteSignY;
	int voteSignZ;
	
	DefVilHouse(ChatColor color,
			
			int westPlane,
			int eastPlane,
			int bottomPlane,
			int topPlane,
			int northPlane,
			int southPlane,
			
			int bedX,
			int bedY,
			int bedZ,
			int bedPitch,
			int frontDoorX,
			int frontDoorY,
			int frontDoorZ,
			int rearDoorX,
			int rearDoorY,
			int rearDoorZ,
			int leverX,
			int leverY,
			int leverZ,
			int chestX,
			int chestY,
			int chestZ,
			int myroleSignX,
			int myroleSignY,
			int myroleSignZ,
			int namePlateX,
			int namePlateY,
			int namePlateZ,
			int voteSignX,
			int voteSignY,
			int voteSignZ){
		
		this.color = color;
		
		this.westPlane = westPlane;
		this.eastPlane = eastPlane;
		this.bottomPlane = bottomPlane;
		this.topPlane = topPlane;
		this.northPlane = northPlane;
		this.southPlane = southPlane;
		
		this.bedX = bedX;
		this.bedY = bedY;
		this.bedZ = bedZ;
		this.bedPitch = bedPitch;
		this.frontDoorX = frontDoorX;
		this.frontDoorY = frontDoorY;
		this.frontDoorZ = frontDoorZ;
		this.rearDoorX = rearDoorX;
		this.rearDoorY = rearDoorY;
		this.rearDoorZ = rearDoorZ;
		this.leverX = leverX;
		this.leverY = leverY;
		this.leverZ = leverZ;
		this.chestX = chestX;
		this.chestY = chestY;
		this.chestZ = chestZ;
		this.myroleSignX = myroleSignX;
		this.myroleSignY = myroleSignY;
		this.myroleSignZ = myroleSignZ;
		this.namePlateX = namePlateX;
		this.namePlateY = namePlateY;
		this.namePlateZ = namePlateZ;
		this.voteSignX = voteSignX;
		this.voteSignY = voteSignY;
		this.voteSignZ = voteSignZ;
	}
}

