# MobileIA File
Libreria creada para la facil interacción con los servicios de Archivos de MobileIA. Con pocas lineas de codigo, podras subir archivos desde tus aplicaciones Android.

1. Agregar libreria al proyecto:
```gradle
compile 'com.mobileia:file:0.0.1'
```
2. Registrar nueva App en [MobileIA Lab](http://lab.mobileia.com).
3. Configurar el APP_ID:
```android
Mobileia.getInstance().setAppId(APP_ID);
```
3. Para subir un Archivo:
```android
MobileiaFile.getInstance().upload(file, new RestGenerator.OnFileUpload() {
    @Override
    public void onSuccess(com.mobileia.file.entity.File file) {
        System.out.println("MobileIA Success file: " + file.name);
        System.out.println("MobileIA Success file: " + file.path);
        System.out.println("MobileIA Success file: " + file.type);
    }

    @Override
    public void onError() {

    }
});
```