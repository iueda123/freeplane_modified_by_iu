package org.freeplane.plugin.helloaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
// Log4Jのような、Java標準以外、Freeplane標準以外のライブラリを使いたい場合、
// 「lib group: 'org.apache.logging.log4j', name: 'log4j-core', version: '2.20.0'」のようなlib宣言が、
// 改造中のサブプロジェクト（:freeplane_plugin_xxxx）側のbuild.gradle/dependenciesで必要であるとともに、
// メインプロジェクト（:freeplane）側のbuild.gradle/depandenciesで必要である。
// メインプロジェクト側にも記述しないとビルドは通っても、
// メインプログラムのfreeplaneを起動しようとした時に「java.lang.NoClassDefFoundError」が出て、
// 起動できないというトラブルに見舞われる。

import org.freeplane.core.ui.AFreeplaneAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class HelloAction extends AFreeplaneAction {

    private static final Logger logger = LogManager.getLogger("HelloWorld");

    public HelloAction() {
        super("HelloAction");
    }

    public void actionPerformed(final ActionEvent e) {

        JOptionPane.showMessageDialog(null, "Hello!");

        logger.fatal("Start"); // 20:03:46.985 [main] FATAL ろがー - Start

        logger.fatal("fatalです!");
        logger.error("errorです!");
        logger.warn("warnです!");
        logger.info("infoです!");
        logger.debug("debugです!");
        logger.trace("traceです!");

        logger.fatal("End"); // 20:03:46.985 [main] FATAL ろがー - End
    }
}