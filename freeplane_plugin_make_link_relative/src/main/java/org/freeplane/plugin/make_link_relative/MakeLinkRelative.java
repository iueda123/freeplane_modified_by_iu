package org.freeplane.plugin.make_link_relative;

import org.freeplane.core.ui.AMultipleNodeAction;
import org.freeplane.core.util.TextUtils;
import org.freeplane.features.link.NodeLinks;
import org.freeplane.features.link.mindmapmode.MLinkController;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MakeLinkRelative extends AMultipleNodeAction {

	public static final int LINK_ABSOLUTE = 0;
	public static final int LINK_RELATIVE_TO_MINDMAP = 1;


	public MakeLinkRelative() {
		super("MakeLinkRelative");
	}

	@Override
	protected void actionPerformed(ActionEvent e, NodeModel node) {
		System.out.println("リンクアドレスを相対化する");
		/* **** リンクPathのURIを取り出す **** */
		URI linkURI = NodeLinks.getLink(node).getUri();//リンクPathのURIを取り出す
		if (linkURI == null) {//リンクが設定されていない場合は何もしない
			return;
		}
		System.out.println("linkURI.toString():" + linkURI.toString());
		if (!linkURI.toString().startsWith("file:/")) {//絶対化リンクが設定されていない場合は何もしない
			System.out.println("file:/ で始まらない。");
			return;
		}


		/* **** 現在開いているファイルパスを取り出す **** */
		final MapModel map = Controller.getCurrentController().getMap();
		String currentMMFilePathString;
		if (map.getFile() != null) {
			//fileNamePath
			currentMMFilePathString = map.getFile().toString();
		} else {
			currentMMFilePathString = TextUtils.getText("FileProperties_NeverSaved");
		}

		/* **** 現在のMMファイルの親フォルダへのパスを取り出す  **** */
		Path parentPath = Paths.get(currentMMFilePathString).getParent();

		/* **** linkAsStringを絶対パスから相対パスにする **** */
		URI RelativizedLinkUri = parentPath.toUri().relativize(linkURI).normalize();
		System.out.println("RelativizedLinkUri: " + RelativizedLinkUri + "    line 68 @R_ConvertLinkToRelativeAction.java");
		System.out.println("RelativizedLinkUri.toString(): " + RelativizedLinkUri.toString() + "    line 69 @R_ConvertLinkToRelativeAction.java");
		//TODO: ここでは実際にはRelativizedLinkUriは相対化されてないから変数名がおかしい


		/* **** リンクを書き換える **** */
		final MLinkController linkController = (MLinkController) MLinkController.getController();

		if (RelativizedLinkUri.toString().contains("#")) {//同一ファイル内へのノードリンクの場合は#ID_123456789のみの表記にする
			String relativizedlinkPathStringWithoutID = RelativizedLinkUri.toString().substring(0, RelativizedLinkUri.toString().indexOf("#"));
			Path currentMMFilePath = Paths.get(currentMMFilePathString);
			System.out.println("currentMMFilePath.getFileName(): " + currentMMFilePath.getFileName());
			System.out.println("relativizedlinkPathStringWithoutID: " + relativizedlinkPathStringWithoutID);
			if (currentMMFilePath.getFileName().toString().equals(relativizedlinkPathStringWithoutID.toString())) {
				String relativizedlinkPathStringOnlyID = RelativizedLinkUri.toString().substring(RelativizedLinkUri.toString().indexOf("#"));
				System.out.println("relativizedlinkPathStringOnlyID: " + relativizedlinkPathStringOnlyID);

				linkController.setLink(node, relativizedlinkPathStringOnlyID, LINK_RELATIVE_TO_MINDMAP);//第３引数がtrueなら相対パス、falseなら絶対パスを設定する。
			} else {
				linkController.setLink(node, RelativizedLinkUri.toString(), LINK_RELATIVE_TO_MINDMAP);//第３引数がtrueなら相対パス、falseなら絶対パスを設定する。
			}
		} else if (RelativizedLinkUri.toString() != "") {//他ファイルへのリンクの場合
			linkController.setLink(node, RelativizedLinkUri.toString(), LINK_RELATIVE_TO_MINDMAP);//第３引数がtrueなら相対パス、falseなら絶対パスを設定する。
		} else {
			/* **** ダイアログボックスの表示 **** */
			JOptionPane.showConfirmDialog(null, "対象ノードはリンクが設定されていません。");
			System.out.println("対象ノードはリンクが設定されていません。");
		}

		/**
		 * http://　リンク等に対して行ってしまった場合に対処したいなぁ
		 *
		 */
		/**
		 * file:///とfile:/の混在に対処したいなぁ。
		 * file:///が正式？（http://docs.oracle.com/cd/E26537_01/tutorial/essential/io/pathOps.htmlにはfile:///が使われている。）
		 * 入力ボックスから直接アドレスを入力すると、file:/になる。
		 */
		/* **** 先頭の余分な「/」を消す**** */
		//String linkedFileUriString = linkedFilePath.toUri().getPath();
		//System.out.println("linkedFileUriString:" + linkedFileUriString);
		//linkedFileUriString = linkedFileUriString.substring(linkedFileUriString.indexOf("/") + 1);
		//System.out.println("linkedFileUriString:" + linkedFileUriString);
		//URI linkedFileUri = null;
		//try {
		//    linkedFileUri = new URI(linkedFileUriString);
		//} catch (URISyntaxException ex) {
		//    Logger.getLogger(A_ConvertLinkToAbsoluteAction.class.getName()).log(Level.SEVERE, null, ex);
		//}

		/**
		 * "file:///C:/Users/Issey/Documents/Dropbox/【医学Topic】/017_画像解析/画像解析.mm#ID_543683403"
		 * "file:///C:/Users/Issey/Documents/Dropbox/【医学Topic】/017_画像解析/画像解析.mm"
		 * "file:/C:/Users/Issey/Documents/Dropbox/【医学Topic】/017_画像解析/画像解析.mm"
		 * "#ID_543683403"
		 * "C:/Users/Issey/Documents/Dropbox/【医学Topic】/017_画像解析/画像解析.mm#ID_543683403"
		 * ←×
		 */
		// navigate to anchored map (source)
		String sourceID1 = "file:///C:/Users/Issey/Documents/Dropbox/【医学Topic】/017_画像解析/画像解析.mm#ID_543683403";
		String sourceID2 = "file:/C:/Users/Issey/Documents/Dropbox/【医学Topic】/X_■放射線■/読影_MRI.mm#Freemind_Link_10726378";

	}
}