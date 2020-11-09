package com.aprz.brouter.plugin.base;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author by liyunlei
 * <p>
 * write on 2020/11/9
 * <p>
 * Class desc:
 */
public interface IWeaver {

    public boolean isWeavableClass(String filePath) throws IOException;


    public byte[] weaveSingleClassToByteArray(InputStream inputStream) throws IOException;

}
