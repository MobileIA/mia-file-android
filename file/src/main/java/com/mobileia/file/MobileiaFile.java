package com.mobileia.file;

import com.mobileia.file.rest.RestGenerator;

import java.io.File;

/**
 * Created by matiascamiletti on 28/7/17.
 */

public class MobileiaFile {
    /**
     * Almacena la unica instancia de la libreria
     */
    private static final MobileiaFile sOurInstance = new MobileiaFile();

    /**
     * Obtiene la instancia creada
     * @return
     */
    public static MobileiaFile getInstance() {
        return sOurInstance;
    }

    /**
     * Subir un archivo de imagen
     * @param file
     * @param callback
     */
    public void upload(File file, RestGenerator.OnFileUpload callback){
        RestGenerator.uploadImage(file, callback);
    }

    private MobileiaFile() {
    }
}
