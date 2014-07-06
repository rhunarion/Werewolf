package jp.ddo.jinroumc.werewolf.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class VillageManual {
	public static ItemStack getManual(){
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) book.getItemMeta();
		meta.setTitle("Minecraft 人狼サーバーへようこそ");
		meta.addPage("< 遊び方 >\n" +
				"村ワールドのスポーン地点両脇にコマンドが実行できる掲示板があります。\n" +
				"人狼ゲームが「空き状態」の場合は「人狼ゲームの準備を開始する」、\n" +
				"人狼ゲームが「募集中」場合は「ゲームに参加する」と書かれた" +
				"看板をクリックしましょう。");
		meta.addPage("< 困ったときは 1/2 >\n" +
				"まずは公式Wikiを確認してください。\n" +
				"http://jinroumc.wiki.fc2.com\n" +
				"(または「jinrou-mc」で検索)\n\n" +
				"最新情報や詳しい遊び方を確認できます。");
		meta.addPage("< 困ったときは 2/2 >\n" +
				"/helpとコマンドすると、今使えるコマンドのヘルプを見ることができます。\n" +
				"動けなくなった場合は、/homeまたは/lobbyコマンドを使ってスポーン地点へ移動してみてください。\n" +
				"それでも解決しない場合は再ログインしてみてください。");
		book.setItemMeta(meta);
		return book;
	}
}
