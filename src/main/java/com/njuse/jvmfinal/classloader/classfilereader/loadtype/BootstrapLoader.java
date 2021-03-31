package com.njuse.jvmfinal.classloader.classfilereader.loadtype;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BootstrapLoader extends Loader {
    private static BootstrapLoader bootstrapLoader = new BootstrapLoader();

    public static BootstrapLoader getInstance() {
        return bootstrapLoader;
    }

    private BootstrapLoader() {
        setSuperLoader(null);
    }
}
