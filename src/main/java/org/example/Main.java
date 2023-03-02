package org.example;

import java.io.*;

import org.apache.commons.net.ftp.*;

public class Main {
    static FTPClient cliente = new FTPClient();

    public static void main(String[] args) {

        try {
            String servFTP = "127.0.0.1";
            conectar(servFTP, "sierro", "1234");
            mostrarContenido();
//            cambiarDirectorio("/pin");
            subirFichero("Prueba.txt", "src/main/java/org/example/prueba.txt");
            descargarFichero("Prueba.txt", "src/main/java/org/example/pruebaDescarga.txt");
            mostrarContenido();
            desconectar();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void conectar(String server, String user, String password) throws IOException {
        if (!cliente.isConnected()) {
            cliente.connect(server);
            cliente.enterLocalPassiveMode();

            boolean login = cliente.login(user, password);
            if (login) {
                System.out.println("Login exitoso");
            } else {
                System.out.println("Contraseña o usuario incorrecto");
            }
        }
    }

    public static void mostrarContenido() throws IOException {
        if (cliente.isConnected()) {
            System.out.println("Directorio actual: " + cliente.printWorkingDirectory());
            FTPFile[] files = cliente.listFiles();
            if (files.length <= 0) {
                System.out.println("No hay ficheros en el directorio actual");
            } else {
                System.out.println("Ficheros en el directorio actual:");
                for (FTPFile file : files) {
                    System.out.println("\t" + file + " " + file.getType());
                }
            }
        } else {
            System.out.println("No estas conectado");
        }
    }

    public static void cambiarDirectorio(String directorio) throws IOException {
        if (cliente.isConnected()) {
            boolean cambio = cliente.changeWorkingDirectory(directorio);
            if (cambio) {
                System.out.println("Cambio realizado a: " + cliente.printWorkingDirectory());
            } else {
                System.out.println("Error en el cambio");
            }
        } else {
            System.out.println("No estas conectado");
        }
    }

    public static void descargarFichero(String fichero, String rutaDescarga) throws IOException {
        if (cliente.isConnected()) {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(rutaDescarga));
            if (cliente.retrieveFile(fichero, out)) {
                System.out.println("Fichero descargado con exito");
            } else {
                System.out.println("Error en la descarga");
            }
            out.close();
        } else {
            System.out.println("No estas conectado");
        }
    }

    public static void subirFichero(String fichero, String ficheroASubir) throws IOException {
        if (cliente.isConnected()) {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(ficheroASubir));
            if (cliente.storeFile(fichero, in)) {
                System.out.println("Fichero subido con exito");
            } else {
                System.out.println("Error al subir el fichero");
            }
        } else {
            System.out.println("No estas conectado");
        }
    }

    public static void desconectar() throws IOException {
        if (cliente.isConnected()) {
            boolean logout = cliente.logout();
            if (logout) {
                System.out.println("Logout exitoso");
            } else {
                System.out.println("Error en el logout");
            }
            cliente.disconnect();
            System.out.println("Conexión finalizada.");
        } else {
            System.out.println("No estas conectado");
        }
    }

}