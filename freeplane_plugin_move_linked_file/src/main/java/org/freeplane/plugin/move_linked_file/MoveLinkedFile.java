package org.freeplane.plugin.move_linked_file;

import org.freeplane.core.ui.AMultipleNodeAction;
import org.freeplane.core.util.HtmlUtils;
import org.freeplane.core.util.TextUtils;
import org.freeplane.features.link.LinkController;
import org.freeplane.features.link.NodeLinks;
import org.freeplane.features.link.mindmapmode.MLinkController;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.plugin.util.CharacterPickUpper_ver2;
import org.freeplane.plugin.util.FileCopyTools_v5;
import org.freeplane.plugin.util.UnicodeEscapeEncoderDecoder_ver4;
import org.freeplane.plugin.util.UnsuitableCharaReplacer_ver2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MoveLinkedFile extends AMultipleNodeAction {

	public MoveLinkedFile() {
		super("MoveLinkedFile");
	}

	@Override
	protected void actionPerformed(ActionEvent e, NodeModel node) {

		/* **** ノードテキストを取り出す **** */
		String nodeText = node.getText();

		nodeText = nodeText.replaceAll("<.+?>", "");
		//nodeText =nodeText.replaceAll("<.+?>", "");
		nodeText = nodeText.replaceAll("^\\s+", "");
		nodeText = nodeText.replaceAll("\\s+$", "");

		//System.out.println("nodeText: " + nodeText);
		if (UnicodeEscapeEncoderDecoder_ver4.isUnicodeEscaped(nodeText)) {
			/* **** 「&#12354;」形式ならデコード **** */
			nodeText = UnicodeEscapeEncoderDecoder_ver4.UnicodeEscape10Decode(nodeText);
			//System.out.println("デコード後: " + nodeText);
		}

		/* **** コアノードの文字列（nodeText）を元に新しいファイル名を作成する **** */
		String newFileName = "no name";
		try {
			newFileName = HtmlUtils.removeHtmlTagsFromString(nodeText);//HTMLを取り除く。org.​freeplane.​core.​util.HtmlUtilsよりメソッドを拝借。
			newFileName = UnsuitableCharaReplacer_ver2.replace(newFileName);
			newFileName = CharacterPickUpper_ver2.pickUp(newFileName, 50);//最初の50文字を取り出す。

		} catch (Exception ex) {
			Logger.getLogger(MoveLinkedFile.class.getName()).log(Level.SEVERE, null, ex);
		}


		/* **** リンクを取り出す **** */
		String linkAsString = NodeLinks.getLinkAsString(node);//ノードからリンクを取り出す
		System.out.println("linkAsString: " + linkAsString);

		/* **** Check whether the linked file exists. **** */
		JOptionPane.showMessageDialog(null, "" +
				"Check whether the linked file exist.\n" +
				"linkAsString: " + linkAsString

		);


		linkAsString = linkAsString.replaceAll("%20", " ");//リンク先パスに半角スペースを意味する「%20」が含まれるとエラーが起こることを回避
		if (linkAsString != "") {

			/* **** 現在開いているファイルパスを取り出す **** */
			final MapModel map = Controller.getCurrentController().getMap();
			String currentMMFilePath;
			if (map.getFile() != null) {
				//fileNamePath
				currentMMFilePath = map.getFile().toString();

			} else {
				currentMMFilePath = TextUtils.getText("FileProperties_NeverSaved");

			}

			/* **** 現在のMMファイルの親フォルダへのパスを取り出す  **** */
			Path parentPath = Paths.get(currentMMFilePath).getParent();


			/* **** コピー先のフォルダを設定 **** */
			Path destFolderPath = parentPath.resolve("./" + Paths.get(currentMMFilePath).getFileName() + ".files").normalize();
			//Path destFolderPath = parentPath.resolve("./").normalize();
			System.out.println("destFolderPath:" + destFolderPath);

			/* **** linkAsStringを相対パスから絶対パスにする **** */
			Path linkedFilePath = parentPath.resolve(linkAsString).normalize();

			/* **** ファイルを複製 **** */
			String srcFilePath = linkedFilePath.toString();
			String destFilePath = destFolderPath.toString() + "/" + newFileName;
			Boolean copyResult = FileCopyTools_v5.copy(srcFilePath, destFilePath);

			if (copyResult) {//コピーが成功した場合
				/* **** コピー確認ダイアログボックスの表示 **** */
				int answer = JOptionPane.showConfirmDialog(null, FileCopyTools_v5.getLogBuf(),"リンクを書き換え確認",JOptionPane.OK_CANCEL_OPTION);

				if (answer == 0) {//「はい」の場合

					/* **** リンクを書き換える **** */
					URI link = null;//new URI(linkAsString);
					try {
						link = LinkController.createHyperlink(destFilePath).getUri();
					} catch (URISyntaxException ex) {
						Logger.getLogger(MoveLinkedFile.class.getName()).log(Level.SEVERE, null, ex);
					}
					final MLinkController linkController = (MLinkController) MLinkController.getController();

					linkController.setLink(node, link, LinkController.LINK_RELATIVE_TO_MINDMAP);
					//String linkAsString = NodeLinks.getLinkAsString(node);

					/**
					 * 元ファイルを消すか否かのPopupDialogを表示させる
					 */
					int value = JOptionPane.showConfirmDialog(null, "元ファイル\n" + srcFilePath + "\nを削除しますか？", "元ファイル削除の確認", JOptionPane.YES_NO_OPTION);
					if (value == 0) {//元ファイル削除に対して「はい」の場合
						File srcFile = new File(srcFilePath);
						boolean result = srcFile.delete();
						if (result) {//削除に成功した場合
							JOptionPane.showMessageDialog(null, "元ファイル\n" + srcFilePath + "\nを削除しました。");

						} else {//削除に失敗した場合
							JOptionPane.showMessageDialog(null, "元ファイル\n" + srcFilePath + "\nを削除できませんでした。");
							result = false;
						}
					} else {//元ファイル削除に対して「いいえ」の場合

					}

				} else {//コピー命令取消の場合
					(new File(destFilePath)).delete();
				}
			} else {
				System.out.println("ファイルコピー失敗：copyResult=" + copyResult);
				System.out.println("原因："+ FileCopyTools_v5.getLogBuf());
				JOptionPane.showMessageDialog(null, FileCopyTools_v5.getLogBuf(),"ファイルコピー失敗", JOptionPane.PLAIN_MESSAGE);
			}

		} else {
			System.out.println("対象ノードはリンクが設定されていません。");
		}

	}

	public static void main(String[] args){

		System.out.println("Test for MoveLinkedFile");

		NodeModel demo_node = new NodeModel(null);

		URI link = null;//new URI(linkAsString);
		try {
			link = LinkController.createHyperlink("../../../Downloads/Covid-19の代表的CT所見_0420%E3%80%802.pptx").getUri();
		} catch (URISyntaxException ex) {
			Logger.getLogger(MoveLinkedFile.class.getName()).log(Level.SEVERE, null, ex);
		}
		final MLinkController linkController = (MLinkController) MLinkController.getController();

		linkController.setLink(demo_node, link, LinkController.LINK_RELATIVE_TO_MINDMAP);
		//String linkAsString = NodeLinks.getLinkAsString(node);


	}


}