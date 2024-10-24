package org.freeplane.plugin.make_link_absolute;

import org.freeplane.core.ui.AMultipleNodeAction;
import org.freeplane.core.util.TextUtils;
import org.freeplane.features.link.NodeLinks;
import org.freeplane.features.link.mindmapmode.MLinkController;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MakeLinkAbsolute extends AMultipleNodeAction {


    public MakeLinkAbsolute() {
        super("MakeLinkAbsolute");
    }

    public static final int LINK_ABSOLUTE = 0;
    public static final int LINK_RELATIVE_TO_MINDMAP = 1;

    @Override
    protected void actionPerformed(ActionEvent e, NodeModel node) {
        System.out.println("リンクアドレスを絶対化する");

        /* **** リンクPathのStringを取り出す **** */
        String linkPathString = NodeLinks.getLinkAsString(node);//リンクPathのStringを取り出す

        if (linkPathString == null) {
            //リンクが設定されていない場合は警告表示
            JOptionPane.showMessageDialog(null, "The node does not have a link.");
            return;
        }

        //インターネット系アドレスの場合
        if (linkPathString.startsWith("http") || linkPathString.startsWith("ftp:/")) {//webアドレスの場合何もしない。
            return;
        }

        if (linkPathString.startsWith("file:/")) {
            //「file:/」で始まるリンクが設定されている場合
            // （freeplaneの設定でハイパーリンクの扱いが”絶対リンク”となっている場合に作られたリンクの場合）

            //何もしない

            return;
        }else{
            //「file:/」で始まらないリンクが設定されている場合
            // （何らかの理由で標準的な絶対パスとは違う形式の絶対パスが設定されている場合）
        }

        System.out.println("linkAsString: " + linkPathString + "@A_ConvertLinkToAbsoluteAction.java line 59");

        //linkPathString = linkPathString.replaceAll("%20", " ");//リンク先パスに半角スペースを意味する「%20」が含まれるとエラーが起こることを回避
        //System.out.println("linkPathString: " + linkPathString);

        /* **** 現在開いているmmファイルのパスを取り出す **** */
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


        /**
         * 「#」が「%23」となってしまうバグ？回避のため、 また同一ファイル内ノードリンク「#ID_123456789」に対応するため、
         * ファイルパス部分とノードID部分に一旦分離し、再度結合する。
         */
        Path raw_link_path_without_node_id = null;
        String node_id_string = "";

        if (linkPathString.indexOf("#") == 0) {//同一ファイル内ノードリンク「#ID_123456789」の場合
            raw_link_path_without_node_id = Paths.get(currentMMFilePathString);
            node_id_string = linkPathString;
        } else if (linkPathString.contains("#")) {//相対リンクパス内にノードリンク「#ID_123456789」を含む場合
            raw_link_path_without_node_id = Paths.get(linkPathString.substring(0, linkPathString.indexOf("#")));
            node_id_string = linkPathString.substring(linkPathString.indexOf("#"));
        } else if (linkPathString != "") {//相対リンクパス内にノードリンクを含まない場合
            raw_link_path_without_node_id = Paths.get(linkPathString);
        } else {
            /* **** ダイアログボックスの表示 **** */
            JOptionPane.showConfirmDialog(null, "対象ノードはリンクが設定されていません。");
            System.out.println("対象ノードはリンクが設定されていません。");
        }
        System.out.println("raw_link_path_without_node_id: " + raw_link_path_without_node_id);
        System.out.println("node_id_string: " + node_id_string);


        /* **** 親フォルダへのパスにリンクパスを結合して、相対パスを絶対化する **** */
        Path absoluted_link_path = parentPath.resolve(raw_link_path_without_node_id).normalize();//resolveメソッドを使用すると、パスを結合できます。 部分パス（ルート要素を含まないパス）を渡すと、その部分パスが元のパスに追加されます。
        System.out.println("absoluted_link_path: " + absoluted_link_path + "line 106 @A_ConvertLinkToAbsoluteAction.java");

        // to URI
        URI AbsolutifiedLinkUri = absoluted_link_path.toUri();
        System.out.println("AbsolutedLinkUri: " + AbsolutifiedLinkUri + "    line 108 @A_ConvertLinkToAbsoluteAction.java");

        // 「file:///hoge/...」 ⇒ 「file:/hoge/...」
        System.out.println("AbsolutedLinkUri.toString(): " + AbsolutifiedLinkUri.toString() + "    line 108 @A_ConvertLinkToAbsoluteAction.java");
        String absolutified_link_path_str = AbsolutifiedLinkUri.toString().replaceFirst("file://", "file:");


        // やっかいな挙動対策。
        // 次に行うURL Decodeにおけるデコード対象の文字列（decode_target）内に「+」が含まれていると、
        // Decodeによって「+」→「 」となることに注意！
        //半角スペースは IllegalArgumentException の原因となるし、もともとの「+」という文字列情報が失われる。
        // これを回避するため、この時点で前もって「+」を「%2B」にしておく。
        AbsolutifiedLinkUri = URI.create(absolutified_link_path_str.replaceAll("\\+", "%2B"));


        /* **** URL Encode された文字列を 識別しやすいようにDecode **** */
        String decode_target = AbsolutifiedLinkUri + node_id_string;
        String decede_result = "";
        //System.out.println("line 111 @A_ConvertLinkToAbsoluteAction.java");
        try {
            decede_result = URLDecoder.decode(decode_target, "UTF-8");
            //System.out.println("デコード結果:" + decede_result);
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }
        //System.out.println("line 119 @A_ConvertLinkToAbsoluteAction.java");
        System.out.println("decede_result: " + decede_result + " line 132 @A_ConvertLinkToAbsoluteAction.java");


        /* **** リンクを書き換える **** */
        final MLinkController linkController = (MLinkController) MLinkController.getController();
        linkController.setLink(node, decede_result, LINK_ABSOLUTE);//第３引数が1なら相対パス、0なら絶対パスを設定する。;
        System.out.println("line 124 @A_ConvertLinkToAbsoluteAction.java");
    }


}
