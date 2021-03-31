package com.njuse.jvmfinal.classloader.classfilereader.loadtype;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationLoader extends Loader {
    private static ApplicationLoader applicationLoader = new ApplicationLoader();

    public static ApplicationLoader getInstance() {
        return applicationLoader;
    }

    private ApplicationLoader() {
        setSuperLoader(ExtensionLoader.getInstance());
    }

}
