package com.github.mbauhardt.dmp;

import com.lexicalscope.jewel.cli.Option;

interface Options {
    @Option
    String getLeftFolder();

    @Option
    String getRightFolder();
}
