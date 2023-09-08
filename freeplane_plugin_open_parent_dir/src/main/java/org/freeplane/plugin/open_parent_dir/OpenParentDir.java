package org.freeplane.plugin.open_parent_dir;

import org.apache.commons.lang.SystemUtils;
import org.freeplane.core.ui.AMultipleNodeAction;
import org.freeplane.core.util.Hyperlink;
import org.freeplane.core.util.LogUtils;
import org.freeplane.core.util.TextUtils;
import org.freeplane.features.link.LinkController;
import org.freeplane.features.link.NodeLinks;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OpenParentDir extends AMultipleNodeAction {

	public OpenParentDir() {
		super("OpenParentDir");
	}

	@Override
	protected void actionPerformed(ActionEvent e, NodeModel node) {

		/* **** リンクを取り出す **** */
		String linkAsString = NodeLinks.getLinkAsString(node);//ノードからリンクを取り出す

		if (linkAsString != null) {

			//System.out.println("linkAsString: " + linkAsString);

			final MapModel map = Controller.getCurrentController().getMap();
			if (SystemUtils.IS_OS_LINUX) {

				/* **** 現在開いているファイルパスを取り出す **** */
				String currentMMFilePathString;
				if (map.getFile() != null) {
					//fileNamePath
					currentMMFilePathString = map.getFile().toString();

				} else {
					currentMMFilePathString = TextUtils.getText("FileProperties_NeverSaved");
				}

				/* **** 現在のMMファイルの親フォルダへのパスを取り出す  **** */
				Path parentPath = Paths.get(currentMMFilePathString).getParent();

				/* *** 起点ディレクトリパス、探索対象ファイルパスを指定して、リンク先ファイルが存在するフォルダを開く *** */
				openFileParent(parentPath.toString(), linkAsString);

			} else {
				linkAsString = linkAsString.replaceAll("%20", " ");//リンク先パスに半角スペースを意味する「%20」が含まれるとエラーが起こることを回避

				/* *** リンク先ファイルの親フォルダを取り出す *** */
				File parent_folder = new File(linkAsString).getParentFile();


				/* *** 親フォルダのパスを構築 *** */
				URI parent_folder_uri = parent_folder.toURI().normalize();
				//JOptionPane.showMessageDialog(null, parent_folder_uri.toString());


				/* *** 親フォルダを開く **** */
				loadLink(node, parent_folder_uri.toString());
				/*
				try {
					getURLManager().load(parent_folder_uri.toURL(), map);
				} catch (IOException ioException) {
					ioException.printStackTrace();
				} catch (XMLException xmlException) {
					xmlException.printStackTrace();
				}
				*/

			}

		} else {
			System.out.println("対象ノードはリンクが設定されていません。");
			JOptionPane.showMessageDialog(null, "対象ノードはリンクが設定されていません。");
		}

	}

	private void loadLink(NodeModel node, final String link) {
		try {
			LinkController.getController().loadURI(node, new Hyperlink(new URI(link)));
		} catch (Exception ex) {
			LogUtils.warn(ex);
		}
	}

	/*
	private UrlManager getURLManager() {
		ModeController modeController = Controller.getCurrentModeController();
		return (UrlManager) modeController.getExtension(UrlManager.class);
	}
	*/


	/**
	 * 指定したファイルがあるフォルダをnautilusで開く
	 * 第1引数：相対パスに対応するため、基準として考えるディレクトリのパス。
	 * 第2引数：探索対象のパス。
	 *
	 * @return
	 */
	private static void openFileParent(String base_directory_path, String linked_file_path) {

		URI open_target_uri = null;

		if (linked_file_path.startsWith("/")) {
			//絶対パスのとき
			open_target_uri = new File(linked_file_path).toURI().normalize();
		} else {
			//相対パスのとき
			if (!base_directory_path.endsWith("/")) {
				base_directory_path = base_directory_path + "/";
			}
			open_target_uri = new File(base_directory_path + linked_file_path).toURI().normalize();
		}

		//全角スペースに相当する「%25E3%2580%2580」を置換する
		String open_target_uri_str = open_target_uri.toString().replaceAll("%25E3%2580%2580", "　");
		//JOptionPane.showMessageDialog(null, open_target_uri_str, "open_target_uri_str", JOptionPane.PLAIN_MESSAGE);


		try {
			Process process = new ProcessBuilder("nautilus", "-s", open_target_uri_str).start();
			InputStreamReader isr = new InputStreamReader(process.getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			StringBuilder builder = new StringBuilder();
			int c;
			while ((c = reader.read()) != -1) {
				builder.append((char) c);
			}
			System.out.println("result:\n" + builder.toString());
			System.out.println("Command return code: " + process.waitFor());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}


}