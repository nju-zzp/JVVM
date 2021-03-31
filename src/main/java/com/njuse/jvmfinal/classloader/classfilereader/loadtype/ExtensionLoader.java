package com.njuse.jvmfinal.classloader.classfilereader.loadtype;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtensionLoader extends Loader {
    private static ExtensionLoader extensionLoader = new ExtensionLoader();

    public static ExtensionLoader getInstance() {
        return extensionLoader;
    }

    private ExtensionLoader() {
        setSuperLoader(BootstrapLoader.getInstance());
    }
}
